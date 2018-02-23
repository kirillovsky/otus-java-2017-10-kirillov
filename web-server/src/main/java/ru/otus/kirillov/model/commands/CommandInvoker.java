package ru.otus.kirillov.model.commands;

import ru.otus.kirillov.model.service.SessionService;
import ru.otus.kirillov.model.service.UserService;
import ru.otus.kirillov.service.DBService;

public final class CommandInvoker {

    private final DBService dbService;

    private final UserService userService;

    private final SessionService sessionService;

    // TODO: 22.02.2018 В конструкторе слишком много параметров, подозреваю, что UserService и SessionService
    // TODO: 22.02.2018 надо объединить в AuthenticationService (что будет совсем логично)
    public CommandInvoker(DBService dbService, UserService userService, SessionService sessionService,
                          ) {
        this.dbService = dbService;
        this.userService = userService;
        this.sessionService = sessionService;
    }
}
