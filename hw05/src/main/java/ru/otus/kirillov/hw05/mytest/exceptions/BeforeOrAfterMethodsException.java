package ru.otus.kirillov.hw05.mytest.exceptions;

/** Ошибка в тесте при выполнении методов, помеченных
 * {@link ru.otus.kirillov.hw05.mytest.annotations.Before} или
 * {@link ru.otus.kirillov.hw05.mytest.annotations.After}
 * Created by Александр on 16.11.2017.
 */
public class BeforeOrAfterMethodsException extends RuntimeException {
    public BeforeOrAfterMethodsException(Throwable cause) {
        super(cause);
    }

    public BeforeOrAfterMethodsException(String message, Throwable cause) {
        super(message, cause);
    }
}
