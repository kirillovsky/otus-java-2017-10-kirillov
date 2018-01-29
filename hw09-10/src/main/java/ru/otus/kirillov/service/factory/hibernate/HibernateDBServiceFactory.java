package ru.otus.kirillov.service.factory.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.sql.BigIntTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;
import ru.otus.kirillov.configuration.DBServiceConfig;
import ru.otus.kirillov.configuration.DBServiceConfig.DB;
import ru.otus.kirillov.dao.hibernate.AbstractHibernateDao;
import ru.otus.kirillov.model.AddressDataSet;
import ru.otus.kirillov.model.PhoneDataSet;
import ru.otus.kirillov.model.UserDataSet;
import ru.otus.kirillov.service.DBService;
import ru.otus.kirillov.service.DBServiceFactory;
import ru.otus.kirillov.service.factory.AbstractDBServiceFactory;
import ru.otus.kirillov.service.hibernate.DBServiceHibernateImpl;
import ru.otus.kirillov.utils.ReflectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Фабрика для создания {@link DBService} с реализацией Hibernate
 * Created by Александр on 24.01.2018.
 */
public class HibernateDBServiceFactory extends AbstractDBServiceFactory {

    //region DAO-классы для модели из задания
    public static class UserDataSetDao extends AbstractHibernateDao<UserDataSet> {}
    public static class AddressDataSetDao extends AbstractHibernateDao<AddressDataSet> {}
    public static class PhoneDataSetDao extends AbstractHibernateDao<PhoneDataSet> {}
    //endregion


    private static final List<Class<?>> DEFAULT_DAO_CLASSES =
            Arrays.asList(UserDataSetDao.class, AddressDataSetDao.class, PhoneDataSetDao.class);

    @Override
    protected List<Class<?>> getDefaultDaoClasses() {
        return DEFAULT_DAO_CLASSES;
    }

    private static final Map<DB, String> DB_TYPES_TO_DIALECT = new HashMap<>();

    static {
        DB_TYPES_TO_DIALECT.put(DB.H2, org.hibernate.dialect.H2Dialect.class.getName());
    }

    private static final Map<DB, String> DB_TYPE_TO_DRIVER = new HashMap<>();

    static {
        DB_TYPE_TO_DRIVER.put(DB.H2, org.h2.Driver.class.getName());
    }

    @Override
    public DBService createDBService(DBServiceConfig dbServiceConfig) {
        Configuration configuration = createConfiguration(dbServiceConfig);
        DBService dbService = new DBServiceHibernateImpl(createSessionFactory(configuration));
        registerDAO(dbService, dbServiceConfig);
        return dbService;
    }

    private Configuration createConfiguration(DBServiceConfig dbServiceConfig) {
        Configuration configuration = new Configuration();

        configuration.setProperty(Environment.DIALECT, DB_TYPES_TO_DIALECT.get(dbServiceConfig.getDbType()));
        configuration.setProperty(Environment.DRIVER, DB_TYPE_TO_DRIVER.get(dbServiceConfig.getDbType()));
        configuration.setProperty(Environment.URL, dbServiceConfig.getConnectionURL());
        configuration.setProperty(Environment.USER, dbServiceConfig.getUsername());
        configuration.setProperty(Environment.PASS, dbServiceConfig.getConnectionURL());
        configuration.setProperty(Environment.FORMAT_SQL, Boolean.TRUE.toString());
        configuration.setProperty(Environment.ENABLE_LAZY_LOAD_NO_TRANS, Boolean.TRUE.toString());
        configuration.setProperty(Environment.HBM2DDL_AUTO, "create");
        configuration.setProperty(Environment.SHOW_SQL, Boolean.TRUE.toString());

        getDaoClasses(dbServiceConfig.getUsersDaoClasses())
                .forEach(dao -> configuration.addAnnotatedClass(getDaoTypeArgument(dao)));

        return configuration;
    }

    private SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }


}
