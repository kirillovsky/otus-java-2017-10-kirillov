package ru.otus.kirillov.model.commands.logout;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.otus.kirillov.model.commands.ModelResult;

public class LogOutModelResult implements ModelResult {
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
