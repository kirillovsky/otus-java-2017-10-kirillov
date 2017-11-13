package ru.otus.kirillov.hw04.gcinfo;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import static com.sun.management.GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION;

/**
 * Created by Александр on 12.11.2017.
 */
public class GCMonitor {
    private static GCStatsInfoHolder statsHolder = new GCStatsInfoHolder();

    public static void installGCMonitoring() {
        ManagementFactory.getGarbageCollectorMXBeans().stream()
                .map((gcbean) -> (NotificationEmitter) gcbean)
                .forEach((emitter) ->
                        emitter.addNotificationListener(
                                (notification, handback) -> saveGCNotificationInfo(notification),
                                null, null
                        )
                );
    }

    public static GCStatsSummaryInfo getSummary() {
        return GCInfoHelper.createGCStatsSummaryInfo(statsHolder.getStatsList());
    }

    public static List<GCStatsInfo> getDetailsInfo() {
        return new ArrayList<>(statsHolder.getStatsList());
    }

    private static void saveGCNotificationInfo(Notification notification) {
        if (GARBAGE_COLLECTION_NOTIFICATION.equals(notification.getType())) {
            statsHolder.addGCStats(GCInfoHelper.cretaeGCStatsInfo(notification));
        }
    }
}
