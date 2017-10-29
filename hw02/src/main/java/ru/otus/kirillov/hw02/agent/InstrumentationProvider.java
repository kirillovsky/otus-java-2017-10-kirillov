package ru.otus.kirillov.hw02.agent;

import java.lang.instrument.Instrumentation;

/** Основные точки входа для Java-агента.
 * InstrumentationProvider#premain - точка входа для предварительно загружаемого агента
 * InstrumentationProvider#agentmain - точка входа для динамически загружаемого агента
 * Внимание! Работа с динамически загружаемым Java-агентом не тестировалась
 * Created by Александр on 24.10.2017.
 *
 */
public class InstrumentationProvider{

    private static Instrumentation instrumInstance;

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println(String.format("Invocation om premain with args %s", agentArgs));
        instrumInstance = inst;
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println(String.format("Invocation om agentmain with args %s", agentArgs));
        instrumInstance = inst;
    }

    public static Instrumentation getInstrumentation() {
        return instrumInstance;
    }
}
