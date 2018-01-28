package ru.otus.kirillov.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by Александр on 25.01.2018.
 */
@Entity
@Table(name = "phone")
public class PhoneDataSet extends DataSet {

    @Column(name = "phone")
    private String number;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String number) {
        this.number = number;
    }

    public static PhoneDataSet of(String number) {
        return new PhoneDataSet(number);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneDataSet)) return false;
        PhoneDataSet that = (PhoneDataSet) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getNumber(), that.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), number);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("number", getNumber())
                .toString();
    }
}
