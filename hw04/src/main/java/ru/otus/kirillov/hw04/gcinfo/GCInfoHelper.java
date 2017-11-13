package ru.otus.kirillov.hw04.gcinfo;

import com.sun.management.GarbageCollectionNotificationInfo;
import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.hw04.gcinfo.GCStatsInfo.MemoryUsageInfo;

import javax.management.Notification;
import javax.management.openmbean.CompositeData;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/** Вспомогательный класс, предназначенный для обработки сообщений нотификации от GC
 * {@link Notification}
 * Created by Александр on 12.11.2017.
 */
public final class GCInfoHelper {

    private static final LocalDateTime VM_START_TIME = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(ManagementFactory.getRuntimeMXBean().getStartTime()), ZoneId.systemDefault());

    private static Collector<Duration, ?, Duration> DURATION_COLLECTOR =
            Collectors.reducing(Duration.ZERO, (Duration d1, Duration d2) -> d1.plus(d2));

    public static long PRECISION_VALUE_DEFAULT = 1000L;
    public static long BYTES_IN_MEGABYTE = 1024L * 1024L;

    private GCInfoHelper() {
    }

    /**
     * Вспомогательный метод, позволяющий вытащить всю нужную нам информацию из
     * {@link Notification} и переложить ее в {@link GCStatsInfo}
     * @param notification - нотификация от GC
     * @return внутреннее представление информации о работе GC
     */
    public static GCStatsInfo cretaeGCStatsInfo(Notification notification) {
        Objects.requireNonNull(notification, "Notification must be not null");
        GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo
                .from((CompositeData) notification.getUserData());
        Map<String, MemoryUsage> membefore = info.getGcInfo().getMemoryUsageBeforeGc();
        return new GCStatsInfo()
                .withDuration(Duration.ofMillis(info.getGcInfo().getDuration()))
                .withStartTime(fromMilliseconds(info.getGcInfo().getStartTime()))
                .withEndTime(fromMilliseconds(info.getGcInfo().getEndTime()))
                .withGcName(info.getGcName())
                .withGcCause(info.getGcCause())
                .withGcAction(info.getGcAction())
                .putMemoryUsageInfo(
                        info.getGcInfo().getMemoryUsageAfterGc().entrySet().stream()
                                .map((Map.Entry<String, MemoryUsage> entry) -> {
                                    MemoryUsage before = membefore.get(entry.getKey());
                                    MemoryUsage current = entry.getValue();
                                    return Pair.of(entry.getKey(), new MemoryUsageInfo()
                                            .withBeforePercent(getPercentage(before.getUsed(), before.getCommitted()))
                                            .withPercent(getPercentage(current.getUsed(), before.getCommitted()))
                                            .withBeforeUsedBytes(before.getUsed())
                                            .withUsedBytes(current.getUsed())
                                    );
                                })
                                .collect(Collectors.toMap(Pair::getKey, Pair::getValue))
                );

    }

    public static GCStatsSummaryInfo createGCStatsSummaryInfo(List<GCStatsInfo> gcStatsInfoList) {
        Objects.requireNonNull(gcStatsInfoList, "gcStatsInfoList must be not null");
        return new GCStatsSummaryInfo()
                .withFirstGCStartTime(gcStatsInfoList.stream()
                        .map(GCStatsInfo::getStartTime)
                        .min(Comparator.naturalOrder()).orElse(null)
                ).withLastGCStartTime(gcStatsInfoList.stream()
                        .map(GCStatsInfo::getStartTime)
                        .max(Comparator.naturalOrder()).orElse(null)
                ).withGarbageCollectionsCount(gcStatsInfoList.size())
                .withSummaryDuration(gcStatsInfoList.stream()
                        .map(GCStatsInfo::getDuration)
                        .collect(DURATION_COLLECTOR)
                ).withGrouppedCauses(gcStatsInfoList.stream()
                        .collect(Collectors.groupingBy(GCStatsInfo::getGcCause, Collectors.counting()))
                ).withGrouppedDuration(gcStatsInfoList.stream()
                        .collect(Collectors.groupingBy(GCStatsInfo::getGcAction,
                                Collectors.mapping(GCStatsInfo::getDuration, DURATION_COLLECTOR)
                        ))
                ).withGrouppedActions(gcStatsInfoList.stream()
                        .collect(Collectors.groupingBy(GCStatsInfo::getGcAction, Collectors.counting()))
                ).withGcNamesCount(gcStatsInfoList.stream()
                        .collect(Collectors.groupingBy(GCStatsInfo::getGcName, Collectors.counting()))
                );
    }

    public static Long getPercentage(long value, long divider, long precision) {
        return divider != 0 ? (value * precision) / divider: null;
    }

    public static Long getPercentage(long value, long divider) {
        return getPercentage(value, divider, PRECISION_VALUE_DEFAULT);
    }

    public static LocalDateTime fromMilliseconds(long mills) {
        return VM_START_TIME.plus(Duration.ofMillis(mills));
    }
}
