package ru.otus.kirillov.atm.commands.invokers;

import ru.otus.kirillov.atm.commands.Command;
import ru.otus.kirillov.atm.commands.queries.Query;

/** Интерфейс обработчика комманд комманд для ATM
 * Created by Александр on 09.12.2017.
 */
public interface CommandInvoker {

    <RQ extends Query, RS> RS process(RQ request);

    <RQ extends Query, RS> void addCommand(Query.Type type, Command<RQ, RS> command);
}
