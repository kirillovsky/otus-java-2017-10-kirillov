package ru.otus.kirillov.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.otus.kirillov.dao.Dao;
import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.utils.CommonUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Александр on 22.01.2018.
 */
public abstract class HibernateDao<T extends DataSet> implements Dao<T> {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public T saveOrUpdate(T object) {
        runInTransaction((Session session) -> session.saveOrUpdate(object));
        return object;
    }

    @Override
    public T read(long id) {
        return runInTransaction((Session session) -> session.load(getEntityType(), id));
    }

    @Override
    public List<T> readAll() {
        return runInTransaction(
                session -> {
                    CriteriaBuilder builder = session.getCriteriaBuilder();
                    CriteriaQuery<T> criteria = builder.createQuery(getEntityType());
                    criteria.select(criteria.from(getEntityType()));
                    return session.createQuery(criteria).getResultList();
                }
        );
    }

    @Override
    public void delete(T object) {
        runInTransaction((Session session) -> session.delete(object));
    }

    protected <T> T runInTransaction(Function<Session, T> handler) {
        try (Session session = sessionFactory.openSession()) {
            return runInNewTransaction(session, handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void runInTransaction(Consumer<Session> handler) {
        runInTransaction(CommonUtils.withoutResult(handler));
    }

    protected <T> T runInNewTransaction(Session session, Function<Session, T> handler) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            return handler.apply(session);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                tx = null;
            }
            throw new RuntimeException(e);
        } finally {
            if (tx != null) {
                tx.commit();
            }
        }
    }
}
