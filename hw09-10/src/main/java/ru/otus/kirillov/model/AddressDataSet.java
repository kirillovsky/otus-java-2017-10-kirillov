package ru.otus.kirillov.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by Александр on 25.01.2018.
 */
@Entity
@Table(name = "address")
public class AddressDataSet extends DataSet {

    @Column(name = "street")
    private String street;

    public static AddressDataSet of(String street) {
        return new AddressDataSet(street);
    }

    public static AddressDataSet of(long id, String street) {
        return new AddressDataSet(id, street);
    }

    public AddressDataSet() {
    }

    public AddressDataSet(String street) {
        this.street = street;
    }

    public AddressDataSet(long id, String street) {
        this(street);
        this.setId(id);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDataSet)) return false;
        AddressDataSet that = (AddressDataSet) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getStreet(), that.getStreet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), street);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("street", getStreet())
                .toString();
    }
}
