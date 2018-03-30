package ru.otus.kirillov.model;

public interface Observable<T> {

    void subscribe(Observer<T> observable);
}
