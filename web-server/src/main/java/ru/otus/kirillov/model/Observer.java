package ru.otus.kirillov.model;

public interface Observer<RS> {

    void receive(RS response);
}
