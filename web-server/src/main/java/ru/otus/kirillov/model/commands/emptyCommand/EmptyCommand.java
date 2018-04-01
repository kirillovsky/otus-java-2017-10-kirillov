package ru.otus.kirillov.model.commands.emptyCommand;

import ru.otus.kirillov.model.commands.Command;
import ru.otus.kirillov.model.commands.ModelRequest;
import ru.otus.kirillov.model.commands.ModelResult;

/**
 * Пустая команда. Такой "хак" нужен для авторизации при отдаче
 * страниц
 */
public class EmptyCommand implements Command {

    @Override
    public boolean isApplicable(ModelRequest rq) {
        return rq instanceof EmptyModelRequest;
    }

    @Override
    public ModelResult execute(ModelRequest rq) {
        return EmptyModelResult.INSTANCE;
    }

    public static class EmptyModelRequest implements ModelRequest {
        public static final EmptyModelRequest INSTANCE = new EmptyModelRequest();
    }

    public static class EmptyModelResult implements ModelResult {
        public static final EmptyModelResult INSTANCE = new EmptyModelResult();
    }
}
