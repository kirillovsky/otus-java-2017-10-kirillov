package ru.otus.kirillov.model.commands.common;

public class NotAuthResult extends ErroneousResult {

    public NotAuthResult(String additionalInfo) {
        super("Not authorised: " + additionalInfo);
    }
}
