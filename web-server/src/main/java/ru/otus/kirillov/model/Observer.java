package ru.otus.kirillov.model;

public interface Observer<RS> {

    void notify(RS response);
}
