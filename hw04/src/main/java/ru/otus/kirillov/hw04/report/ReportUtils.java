package ru.otus.kirillov.hw04.report;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.hw04.gcinfo.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.otus.kirillov.hw04.gcinfo.GCInfoHelper.*;

/**
 * Created by Александр on 13.11.2017.
 */
public final class ReportUtils {

    private static final int REPORT_LENGTH = 90;

    private ReportUtils() {
    }

    public static void printRowSeparator() {
        System.out.println(StringUtils.repeat("-", REPORT_LENGTH));
    }

    public static void printDoubleRowSeparator() {
        System.out.println(StringUtils.repeat("=", REPORT_LENGTH));
    }

    public static void printLabelInCenter(String label) {
        System.out.println(String.format("%" + REPORT_LENGTH / 2 + "s", label));
    }

    public static void printBlockTitle(String title) {
        printDoubleRowSeparator();
        printLabelInCenter(title);
        printDoubleRowSeparator();
    }

    public static void printGCStatsSummaryInfo(GCStatsSummaryInfo summaryInfo) {
        Objects.requireNonNull(summaryInfo, "GCStatsSummaryInfo must be not null");
        System.out.println("Garbage collections counts: " + summaryInfo.getGarbageCollectionsCount());
        System.out.println("Garbage collections summary duration (sec.): " + summaryInfo.getSummaryDuration().getSeconds());
        System.out.println("First garbage collection start time: " + summaryInfo.getFirstGCStartTime());
        System.out.println("Last garbage collection start time: " + summaryInfo.getLastGCStartTime());
        System.out.println("Garbage collection count per action: " + summaryInfo.getGrouppedActions());
        System.out.println("Garbage collection count per cause: " + summaryInfo.getGrouppedCauses());
        System.out.println("Garbage collection count per names: " + summaryInfo.getGcNamesCount());
        System.out.println("Garbage collection durations (sec.) per action: "
                + summaryInfo.getGrouppedDuration().entrySet().stream()
                .collect(Collectors.mapping(
                        entry -> Pair.of(entry.getKey(), entry.getValue().getSeconds()),
                        Collectors.toMap(Pair::getKey, Pair::getValue)
                        )
                )
        );
        printDoubleRowSeparator();
    }

    public static void printGCStatsInfoList(List<GCStatsInfo> statsInfoList) {
        Objects.requireNonNull(statsInfoList, "statsInfoList must be not null");
        statsInfoList.forEach((statsInfo) -> {
            System.out.println("Garbage collection name: " + statsInfo.getGcName());
            System.out.println("Garbage collection action: " + statsInfo.getGcAction());
            System.out.println("Garbage collection cause: " + statsInfo.getGcCause());
            System.out.println("Garbage collection start time: " + statsInfo.getStartTime());
            System.out.println("Garbage collection end time: " + statsInfo.getEndTime());
            System.out.println("Garbage collection duration (nanosec.): " + statsInfo.getDuration().getNano());
            System.out.println("Memory usage: ");
            printRowSeparator();
            statsInfo.getMemoryUsageInfoMap().entrySet().stream()
                    .forEach((entry) -> {
                        System.out.print(entry.getKey() + ": ");
                        GCStatsInfo.MemoryUsageInfo memoryUsageInfo = entry.getValue();
                        System.out.println(String.format("%s.%s%%(%s MB)->%s.%s%%(%s MB)",
                                mapOrNull(memoryUsageInfo.getBeforePercent(), toPercentsIntegralPart),
                                mapOrNull(memoryUsageInfo.getBeforePercent(), toPercentsFractionalPart),
                                mapOrNull(memoryUsageInfo.getBeforeUsedBytes(), toMB),
                                mapOrNull(memoryUsageInfo.getPercent(), toPercentsIntegralPart),
                                mapOrNull(memoryUsageInfo.getPercent(), toPercentsFractionalPart),
                                mapOrNull(memoryUsageInfo.getUsedBytes(), toMB)
                        ));
                    });
            printDoubleRowSeparator();
        });
    }

    private static <T, R> R mapOrNull(T value, Function<T, R> mapFunction) {
        return Optional.ofNullable(value).map(mapFunction).orElse(null);
    }

    public static void printReport() {
        printBlockTitle("Summary Info");
        printGCStatsSummaryInfo(GCMonitor.getSummary());
        printBlockTitle("Details Info");
        printGCStatsInfoList(GCMonitor.getDetailsInfo());
        printDoubleRowSeparator();
    }

}
