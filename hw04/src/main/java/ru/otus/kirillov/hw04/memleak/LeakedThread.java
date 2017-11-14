package ru.otus.kirillov.hw04.memleak;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Александр on 08.11.2017.
 */
public class LeakedThread implements Runnable{

    private List<String> list = new ArrayList<>();

    public static int getLastListSize() {
        return lastListSize;
    }

    private static int lastListSize;
    private static final int SKIP_LIST_STEP = 2;
    private static final int BATCH_LIST_SIZE = 100_000;
    private static final long SLEEP_MILLISECONDS = 2_000;

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(SLEEP_MILLISECONDS);
                fillLst();
                //System.out.println("Current List size: " + list.size());
                truncateLst();
                //System.out.println("Current List size: " + list.size());
            }

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void fillLst() {
        list.addAll(IntStream.rangeClosed(1, BATCH_LIST_SIZE)
                .mapToObj((i) -> LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "_" + i)
                .collect(Collectors.toList())
        );
        lastListSize = list.size();
    }

    private void truncateLst() {
        for(int i = list.size() - BATCH_LIST_SIZE; i < list.size(); i += SKIP_LIST_STEP) {
            list.remove(i);
        }
        lastListSize = list.size();
    }
}
