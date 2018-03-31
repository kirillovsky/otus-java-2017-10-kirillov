package ru.otus.kirillov.model.transport;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.otus.kirillov.utils.CommonUtils;

public abstract class Message {
    private final Header header;

    protected Message() {
        header = new Header();
    }

    protected Message(Header header) {
        this.header = CommonUtils.retunIfNotNull(header);
    }

    public Header getHeader() {
        return header;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("header", header)
                .toString();
    }
}
