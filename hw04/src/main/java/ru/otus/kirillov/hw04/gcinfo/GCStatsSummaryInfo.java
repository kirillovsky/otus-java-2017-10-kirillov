package ru.otus.kirillov.hw04.gcinfo;

import org.apache.commons.lang3.tuple.Pair;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Александр on 12.11.2017.
 */
public class GCStatsSummaryInfo {

    private Map<String, Long> gcNamesCount;
    private int garbageCollectionsCount;
    private LocalDateTime firstGCStartTime;
    private LocalDateTime lastGCStartTime;
    private Duration summaryDuration;
    private Map<String, Duration> grouppedDuration;
    private Map<String, Long> grouppedCauses;
    private Map<String, Long> grouppedActions;

    public LocalDateTime getLastGCStartTime() {
        return lastGCStartTime;
    }

    public GCStatsSummaryInfo withLastGCStartTime(LocalDateTime endTime) {
        this.lastGCStartTime = endTime;
        return this;
    }

    public int getGarbageCollectionsCount() {
        return garbageCollectionsCount;
    }

    public GCStatsSummaryInfo withGarbageCollectionsCount(int garbageCollectionsCount) {
        this.garbageCollectionsCount = garbageCollectionsCount;
        return this;
    }

    public Map<String, Long> getGrouppedCauses() {
        return grouppedCauses;
    }

    public GCStatsSummaryInfo withGrouppedCauses(Map<String, Long> grouppedCauses) {
        this.grouppedCauses = grouppedCauses;
        return this;
    }

    public Map<String, Duration> getGrouppedDuration() {
        return grouppedDuration;
    }

    public GCStatsSummaryInfo withGrouppedDuration(Map<String, Duration> grouppedDuration) {
        this.grouppedDuration = grouppedDuration;
        return this;
    }

    public LocalDateTime getFirstGCStartTime() {
        return firstGCStartTime;
    }

    public GCStatsSummaryInfo withFirstGCStartTime(LocalDateTime startTime) {
        this.firstGCStartTime = startTime;
        return this;
    }

    public Duration getSummaryDuration() {
        return summaryDuration;
    }

    public GCStatsSummaryInfo withSummaryDuration(Duration summaryDuration) {
        this.summaryDuration = summaryDuration;
        return this;
    }

    public Map<String, Long> getGcNamesCount() {
        return gcNamesCount;
    }

    public GCStatsSummaryInfo withGcNamesCount(Map<String, Long> gcName) {
        this.gcNamesCount = gcName;
        return this;
    }

    public Map<String, Long> getGrouppedActions() {
        return grouppedActions;
    }

    public GCStatsSummaryInfo withGrouppedActions(Map<String, Long> grouppedActions) {
        this.grouppedActions = grouppedActions;
        return this;
    }

    @Override
    public String toString() {
        return "GCStatsSummaryInfo{" +
                "\n, gcNamesCount='" + gcNamesCount + '\'' +
                "\n, garbageCollectionsCount=" + garbageCollectionsCount +
                "\n, firstGCStartTime=" + firstGCStartTime +
                "\nlastGCStartTime=" + lastGCStartTime +
                "\n, summaryDuration=" + summaryDuration.getSeconds() + " seconds" +
                "\n, grouppedDuration="
                + grouppedDuration.entrySet().stream()
                .collect(Collectors.mapping(
                        (Map.Entry<String, Duration> entry) -> Pair.of(entry.getKey(), entry.getValue().getSeconds()),
                        Collectors.toMap(Pair::getKey, Pair::getValue))
                ) +
                "\n, grouppedCauses=" + grouppedCauses +
                "\n, grouppedActions=" + grouppedActions +
                '}';
    }

}
