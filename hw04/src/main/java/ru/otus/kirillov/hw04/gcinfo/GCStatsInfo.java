package ru.otus.kirillov.hw04.gcinfo;

import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Набор данных по сборке мусора
 * Created by Александр on 12.11.2017.
 */
public class GCStatsInfo {

    /**
     * Длительность сборки
     */
    private Duration duration;

    /**
     * Время начала
     */
    private LocalDateTime startTime;

    /**
     * Время завершения
     */
    private LocalDateTime endTime;

    /**
     * Назание текущей части алгоритма сборки мусора
     */
    private String gcName;

    /**
     * Тип сборки - младшая/старшая сборка
     */
    private String gcAction;

    /**
     * Причина сборки мусора
     */
    private String gcCause;

    /**
     * Информация по работе сборщика мусора с областями памяти
     */
    private Map<String, MemoryUsageInfo> memoryUsageInfoMap = new HashMap<>();

    /**
     * Информация по изменениям занимамой памяти для области
     */
    public static class MemoryUsageInfo {

        /**
         * Предыдщий объем занимаемой памяти для области в процентах
         */
        private Long beforePercent;

        /**
         * Текущий объем занимаемой памяти для области в процентах
         */
        private Long percent;

        /**
         * Предыдущий объем занимаемой памяти для области в байтах
         */
        private Long beforeUsedBytes;

        /**
         * Текущий объем занимаемой памяти для области в байтах
         */
        private Long usedBytes;

        public Long getBeforePercent() {
            return beforePercent;
        }

        public MemoryUsageInfo withBeforePercent(Long beforePercent) {
            this.beforePercent = beforePercent;
            return this;
        }

        public Long getBeforeUsedBytes() {
            return beforeUsedBytes;
        }

        public MemoryUsageInfo withBeforeUsedBytes(Long beforeUsedBytes) {
            this.beforeUsedBytes = beforeUsedBytes;
            return this;
        }

        public Long getPercent() {
            return percent;
        }

        public MemoryUsageInfo withPercent(Long percent) {
            this.percent = percent;
            return this;
        }

        public Long getUsedBytes() {
            return usedBytes;
        }

        public MemoryUsageInfo withUsedBytes(Long usedBytes) {
            this.usedBytes = usedBytes;
            return this;
        }

        @Override
        public String toString() {
            return "MemoryUsageInfo{" +
                    "beforePercent=" + beforePercent +
                    ", percent=" + percent +
                    ", beforeUsedBytes=" + beforeUsedBytes +
                    ", usedBytes=" + usedBytes +
                    '}';
        }
    }

    public Duration getDuration() {
        return duration;
    }

    public GCStatsInfo withDuration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public GCStatsInfo withEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getGcAction() {
        return gcAction;
    }

    public GCStatsInfo withGcAction(String gcAction) {
        this.gcAction = gcAction;
        return this;
    }

    public String getGcCause() {
        return gcCause;
    }

    public GCStatsInfo withGcCause(String gcCause) {
        this.gcCause = gcCause;
        return this;
    }

    public String getGcName() {
        return gcName;
    }

    public GCStatsInfo withGcName(String gcName) {
        this.gcName = gcName;
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public GCStatsInfo withStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public Optional<MemoryUsageInfo> getMemoryUsageInfo(String memoryRegionName) {
        return Optional.ofNullable(memoryUsageInfoMap.getOrDefault(memoryRegionName, null));
    }

    public GCStatsInfo putMemoryUsageInfo(String memoryRegionName, MemoryUsageInfo memoryUsageInfo) {
        this.memoryUsageInfoMap.put(memoryRegionName, memoryUsageInfo);
        return this;
    }

    public GCStatsInfo putMemoryUsageInfo(Map<String, MemoryUsageInfo> memoryUsageInfoMap) {
        Objects.requireNonNull(memoryUsageInfoMap, "memoryUsageInfoMap must be not null");
        this.memoryUsageInfoMap.putAll(memoryUsageInfoMap);
        return this;
    }

    public Map<String, MemoryUsageInfo> getMemoryUsageInfoMap() {
        return memoryUsageInfoMap;
    }

    @Override
    public String toString() {
        return "GCStats{" +
                "duration=" + duration +
                "\n, startTime=" + startTime +
                "\n, endTime=" + endTime +
                "\n, gcName='" + gcName + '\'' +
                "\n, gcAction='" + gcAction + '\'' +
                "\n, gcCause='" + gcCause + '\'' +
                "\n, memoryUsageInfoMap=: " + memoryUsageInfoMap.entrySet().stream()
                        .map((entry) -> "memory region Type: " + entry.getKey() + ", memory usage info: " + entry.getValue())
                        .collect(Collectors.joining("\n")) +
                '}';
    }
}
