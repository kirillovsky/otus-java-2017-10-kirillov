package ru.otus.kirillov.hw04;


import ru.otus.kirillov.hw04.gcinfo.GCMonitor;
import ru.otus.kirillov.hw04.memleak.LeakedThread;
import ru.otus.kirillov.hw04.report.ReportUtils;

import java.time.Duration;
import java.time.LocalTime;

/**
 * java -Xmx512m -Xms512m -XX:+UseSerialGC -jar target/HW04.jar
 * java -Xmx512m -Xms512m -XX:+UseParallelGC -jar target/HW04.jar
 * java -Xmx512m -Xms512m -XX:+UseConcMarkSweepGC -jar target/HW04.jar
 * java -Xmx512m -Xms512m -XX:+UseG1GC -jar target/HW04.jar
 */
public class Main {
    public static void main(String... args) throws Exception {
        GCMonitor.installGCMonitoring();
        LocalTime start = LocalTime.now();
        Thread t = new Thread(new LeakedThread(), "Leaked thread");
        t.start();
        t.join();
        System.out.println("Minutes: " + Duration.between(start, LocalTime.now()).toMinutes());
        System.out.println("Max list size: " + LeakedThread.getLastListSize());
        ReportUtils.printReport();
    }

}
