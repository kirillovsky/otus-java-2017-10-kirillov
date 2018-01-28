package ru.otus.kirillov.service.hibernate;

import org.hibernate.SessionFactory;
import ru.otus.kirillov.dao.Dao;
import ru.otus.kirillov.service.AbstractDBService;
import ru.otus.kirillov.service.DBService;

import static ru.otus.kirillov.utils.ReflectionUtils.*;

/**
 * Реализация {@link DBService} с помощью Hibernate.
 * Особенностью реализации является то, что для создания
 * DAO, с которыми работает данная имплементация требуется {@link org.hibernate.SessionFactory}.
 * Он может быть проставлен через публичный конструктор либо через сеттер
 * Created by Александр on 20.01.2018.
 */
public class DBServiceHibernateImpl extends AbstractDBService {

    private final SessionFactory sessionFactory;

    public DBServiceHibernateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     *  И так. Задача создать DAO. И тут 2 варианта (в самом распространенном случае):
     *  1. У него есть конструктор с одним параметров SessionFactory
     *  2. У него есть дефолтный конструктор и поле SessionFactory
     *  3. Он должен иметь модификатор public
     * @param daoClass - класс DAO-шки
     * @return
     */
    @Override
    protected Dao<?> createDao(Class<?> daoClass) {
        Dao<?> dao = instantiateWithInjections(daoClass, sessionFactory);
        if(dao == null) {
            throw new RuntimeException(
                    String.format("Unable to create DAO %s: try to check contract " +
                            "for DBServiceHibernateImpl", daoClass));
        }
        return dao;
    }
}
