package ru.otus.kirillov.atm.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Александр on 11.12.2017.
 */
public class ReportUtils {

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
}
