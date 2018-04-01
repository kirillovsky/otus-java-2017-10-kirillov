package ru.otus.kirillov.model.commands.common;

public class NotAuthModelResult extends ErroneousModelResult {

    public NotAuthModelResult(String additionalInfo) {
        super("Not authorised: " + additionalInfo);
    }
}
