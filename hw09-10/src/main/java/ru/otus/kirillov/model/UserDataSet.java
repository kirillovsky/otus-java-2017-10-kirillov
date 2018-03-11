package ru.otus.kirillov.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Модель "Пользователь"
 * Created by Александр on 20.01.2018.
 */
@Entity
@Table(name = "user")
public class UserDataSet extends DataSet {

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private AddressDataSet addressDataSet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "phone_id")
    private List<PhoneDataSet> phoneDataSets;

    public UserDataSet() {
    }

    public UserDataSet(long id)  {
        setId(id);
    }

    public UserDataSet(String name, int age, AddressDataSet addressDataSet, List<PhoneDataSet> phoneDataSets) {
        this.name = name;
        this.age = age;
        this.addressDataSet = addressDataSet;
        this.phoneDataSets = phoneDataSets;
    }

    public UserDataSet(long id, String name, int age, AddressDataSet addressDataSet, List<PhoneDataSet> phoneDataSets) {
        this(name, age, addressDataSet, phoneDataSets);
        this.setId(id);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public AddressDataSet getAddressDataSet() {
        return addressDataSet;
    }

    public List<PhoneDataSet> getPhoneDataSets() {
        return phoneDataSets;
    }

    public UserDataSet withName(String name) {
        this.name = name;
        return this;
    }

    public UserDataSet withAge(int age) {
        this.age = age;
        return this;
    }

    public UserDataSet withAddressDataSet(AddressDataSet addressDataSet) {
        this.addressDataSet = addressDataSet;
        return this;
    }

    public UserDataSet withPhoneDataSets(List<PhoneDataSet> phoneDataSets) {
        this.phoneDataSets = phoneDataSets;
        return this;
    }

    public UserDataSet withNewPhoneDataSets(PhoneDataSet phoneDataSet) {
        this.phoneDataSets.add(phoneDataSet);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof UserDataSet)) return false;

        UserDataSet that = (UserDataSet) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .append(getAge(), that.getAge())
                .append(getName(), that.getName())
                .append(getAddressDataSet(), that.getAddressDataSet())
                .append(getPhoneDataSets().toArray(), that.getPhoneDataSets().toArray())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId())
                .append(name)
                .append(age)
                .append(addressDataSet)
                .append(phoneDataSets)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("name", name)
                .append("age", age)
                .append("addressDataSet", addressDataSet)
                .append("phoneDataSets", phoneDataSets)
                .toString();
    }
}
