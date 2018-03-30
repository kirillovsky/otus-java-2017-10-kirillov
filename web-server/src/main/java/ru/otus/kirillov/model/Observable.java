package ru.otus.kirillov.transport;

public interface Observable<T> {

    void subscribe(Observer<T> observable);
}
