package ru.otus.kirillov.hw04.gcinfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Александр on 12.11.2017.
 */
public class GCStatsInfoHolder {

    private List<GCStatsInfo> statsList = new ArrayList<>();

    public void addGCStats(GCStatsInfo stats) {
        statsList.add(stats);
    }

    public List<GCStatsInfo> getStatsList() {
        return new ArrayList<>(statsList);
    }
}
