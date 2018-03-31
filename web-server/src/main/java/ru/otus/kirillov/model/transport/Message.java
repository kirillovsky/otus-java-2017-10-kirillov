package ru.otus.kirillov.model.transport;

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
}
