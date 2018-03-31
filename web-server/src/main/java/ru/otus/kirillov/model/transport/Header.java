package ru.otus.kirillov.model.transport;

import org.apache.commons.lang3.builder.ToStringBuilder;
import java.util.Objects;
import java.util.UUID;

/**
 * Считаем, что UUID уникален!
 */
public class Header {
    private final UUID opUid;

    public Header() {
        opUid = UUID.randomUUID();
    }

    public UUID getOpUid() {
        return opUid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Header)) return false;
        Header header = (Header) o;
        return Objects.equals(opUid, header.opUid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(opUid);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("opUid", opUid)
                .toString();
    }
}
