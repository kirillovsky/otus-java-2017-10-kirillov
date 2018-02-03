package ru.otus.kirillov.service;

import org.junit.Assert;
import ru.otus.kirillov.model.AddressDataSet;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.model.PhoneDataSet;
import ru.otus.kirillov.model.UserDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Александр on 28.01.2018.
 */
public abstract class DBServiceTest {

    protected abstract DBService getDbService();

    public void clearDB() {
        deleteAll(UserDataSet.class);
        deleteAll(PhoneDataSet.class);
        deleteAll(AddressDataSet.class);
    }

    public void emptyUserReadTest() {
        Assert.assertTrue(getDbService().readAll(UserDataSet.class).isEmpty());
    }

    public void readOneSimpleUser() {
        UserDataSet user = getFullTestUser()
                .withAddressDataSet(null)
                .withPhoneDataSets(Collections.emptyList());

        user = getDbService().saveOrUpdate(user);
        Assert.assertEquals(user, getDbService().read(user.getId(), UserDataSet.class));
        Assert.assertTrue(getDbService().readAll(UserDataSet.class).size() == 1);
    }

    public void readOneFillUser() {
        UserDataSet user = getFullTestUser();
        user = getDbService().saveOrUpdate(user);

        Assert.assertEquals(user, getDbService().read(user.getId(), UserDataSet.class));
        Assert.assertTrue(getDbService().readAll(UserDataSet.class).size() == 1);
        Assert.assertTrue(getDbService().readAll(UserDataSet.class).contains(user));

        Assert.assertTrue(getDbService().readAll(AddressDataSet.class).size() == 1);
        Assert.assertTrue(getDbService().readAll(AddressDataSet.class).contains(user.getAddressDataSet()));

        Assert.assertTrue(getDbService().readAll(PhoneDataSet.class).size() == 2);
        Assert.assertTrue(getDbService().readAll(PhoneDataSet.class).containsAll(user.getPhoneDataSets()));
    }

    public void readOneFillUserSeparatelySave() {
        UserDataSet user = getFullTestUser();
        AddressDataSet address =
                getDbService().saveOrUpdate(user.getAddressDataSet());
        Assert.assertTrue(getDbService().readAll(AddressDataSet.class).size() == 1);
        Assert.assertTrue(getDbService().readAll(AddressDataSet.class).contains(address));

        PhoneDataSet phone1 = getDbService().saveOrUpdate(user.getPhoneDataSets().get(0));
        PhoneDataSet phone2 = getDbService().saveOrUpdate(user.getPhoneDataSets().get(1));
        Assert.assertTrue(getDbService().readAll(PhoneDataSet.class).size() == 2);
        Assert.assertTrue(getDbService().readAll(PhoneDataSet.class).containsAll(Arrays.asList(phone1, phone2)));

        user = getDbService().saveOrUpdate(user);

        Assert.assertEquals(user, getDbService().read(user.getId(), UserDataSet.class));
        Assert.assertTrue(getDbService().readAll(UserDataSet.class).size() == 1);
        Assert.assertTrue(getDbService().readAll(UserDataSet.class).contains(user));

        Assert.assertTrue(getDbService().readAll(AddressDataSet.class).size() == 1);
        Assert.assertTrue(getDbService().readAll(AddressDataSet.class).contains(user.getAddressDataSet()));

        Assert.assertTrue(getDbService().readAll(PhoneDataSet.class).size() == 2);
        Assert.assertTrue(getDbService().readAll(PhoneDataSet.class).containsAll(user.getPhoneDataSets()));
    }

    public void updateSimpleFields() {
        UserDataSet user = getFullTestUser();
        getDbService().saveOrUpdate(user);
        user.withAge(100500);
        user.withName("Alex");
        getDbService().saveOrUpdate(user);
        Assert.assertEquals(user, getDbService().read(user.getId(), UserDataSet.class));
    }

    public void updateDependentDataSets() {
        UserDataSet user = getFullTestUser();
        getDbService().saveOrUpdate(user);

        user.getAddressDataSet().setStreet("Lenin street");
        user.getPhoneDataSets().forEach(ph -> ph.setNumber("111111111111111"));
        getDbService().saveOrUpdate(user);

        Assert.assertEquals(user, getDbService().read(user.getId(), UserDataSet.class));
    }

    public void updateExtendDependentDataSets() {
        UserDataSet user = getFullTestUser();
        getDbService().saveOrUpdate(user);
        user.withPhoneDataSets(new ArrayList<>(user.getPhoneDataSets()))
                .withNewPhoneDataSets(createPhone("333333"))
                .withNewPhoneDataSets(createPhone("55555555"))
                .withNewPhoneDataSets(createPhone("77777777"));
        getDbService().saveOrUpdate(user);
        Assert.assertEquals(user, getDbService().read(user.getId(), UserDataSet.class));
    }

    public void updateReduceOneToOneFields() {
        UserDataSet user = getFullTestUser();
        user.withPhoneDataSets(new ArrayList<>(user.getPhoneDataSets()))
                .withNewPhoneDataSets(createPhone("333333"))
                .withNewPhoneDataSets(createPhone("55555555"))
                .withNewPhoneDataSets(createPhone("77777777"));
        getDbService().saveOrUpdate(user);
        user.withPhoneDataSets(Collections.singletonList(createPhone("000101010101")));
        getDbService().saveOrUpdate(user);
        Assert.assertEquals(user, getDbService().read(user.getId(), UserDataSet.class));
    }

    public void deleteSimpleDataSets() {
        AddressDataSet address = createAddress("9-may street");
        getDbService().saveOrUpdate(address);

        PhoneDataSet phone = createPhone("NUMBER");
        getDbService().saveOrUpdate(phone);

        Assert.assertTrue(getDbService().readAll(PhoneDataSet.class).contains(phone));
        Assert.assertTrue(getDbService().readAll(AddressDataSet.class).contains(address));

        getDbService().delete(phone);
        getDbService().delete(address);

        Assert.assertTrue(getDbService().readAll(PhoneDataSet.class).isEmpty());
        Assert.assertTrue(getDbService().readAll(AddressDataSet.class).isEmpty());
    }

    public void deleteUserWithoutDependentEntities() {
        UserDataSet user = getFullTestUser();
        getDbService().saveOrUpdate(user);

        getDbService().delete(user);
        Assert.assertTrue(getDbService().readAll(UserDataSet.class).isEmpty());
        Assert.assertTrue(getDbService().readAll(AddressDataSet.class).isEmpty());
        Assert.assertTrue(getDbService().readAll(PhoneDataSet.class).isEmpty());
    }

    protected UserDataSet getFullTestUser() {
        PhoneDataSet phone1 = getDbService().saveOrUpdate(createPhone("122421"));
        PhoneDataSet phone2 = getDbService().saveOrUpdate(createPhone("8932421"));

        AddressDataSet address =
                getDbService().saveOrUpdate(createAddress("Moscow, Pushkin's str., h. Kolotushkin's"));

        return createUser("Vasya Pupkin", 100, address, Arrays.asList(phone1, phone2));
    }

    protected <T extends DataSet> void deleteAll(Class<T> clazz) {
        getDbService().readAll(clazz).forEach(obj -> getDbService().delete(obj));
    }

    protected PhoneDataSet createPhone(String number) {
        return PhoneDataSet.of(number);
    }

    protected AddressDataSet createAddress(String street) {
        return AddressDataSet.of(street);
    }

    protected UserDataSet createUser(String name, int age, AddressDataSet address, List<PhoneDataSet> phones) {
        return new UserDataSet(name, age, address, phones);
    }


}
