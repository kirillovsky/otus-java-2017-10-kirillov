package ru.otus.kirillov.hw02.agent;

import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

import com.sun.tools.attach.VirtualMachine;

/** Динамический загрузчик Java-агента.
 *  Внимание!!! Не тестировался.
 * Created by Александр on 24.10.2017.
 */
public class InstrumentationProviderLoader {

    private static final String CLASS_NAME = InstrumentationProviderLoader.class.getSimpleName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    public static void loadAgent(String pathToJar) {
        LOGGER.fine(String.format("Try to dynamic load javaagent with path: %s", pathToJar));
        loadJavaAgent(pathToJar, getPid());
    }

    private static String getPid() {
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        LOGGER.fine(String.format("Runned VM - %s", nameOfRunningVM));
        return nameOfRunningVM.split("@")[0];
    }

    private static void loadJavaAgent(String pathToJar, String pid) {
        try {
            VirtualMachine vm = VirtualMachine.attach(pid);
            vm.loadAgent(pathToJar, "");
            vm.detach();
        } catch (Exception e) {
            LOGGER.throwing(CLASS_NAME, "loadJavaAgent", e);
            throw new RuntimeException(e);
        }
    }
}
