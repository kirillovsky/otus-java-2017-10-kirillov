package ru.otus.kirillov.hw04.gcinfo;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import static com.sun.management.GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION;

/** Монитор нотификаций от GC
 * Created by Александр on 12.11.2017.
 */
public class GCMonitor {
    private static GCStatsInfoHolder statsHolder = new GCStatsInfoHolder();

    /**
     * Добавить листенеров для гарбедж коллекторов
     */
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

    /**
     * Получение общей информации по сборкам мусора
     * @return суммарная информаци по сборкам мусора
     */
    public static GCStatsSummaryInfo getSummary() {
        return GCInfoHelper.createGCStatsSummaryInfo(statsHolder.getStatsList());
    }

    /**
     * Получение информации по каждой сборке мусора
     * @return Список информации по каждой произошедшей сборке мусора
     */
    public static List<GCStatsInfo> getDetailsInfo() {
        return new ArrayList<>(statsHolder.getStatsList());
    }

    private static void saveGCNotificationInfo(Notification notification) {
        if (GARBAGE_COLLECTION_NOTIFICATION.equals(notification.getType())) {
            statsHolder.addGCStats(GCInfoHelper.cretaeGCStatsInfo(notification));
        }
    }
}
