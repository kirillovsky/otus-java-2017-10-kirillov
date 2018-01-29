package ru.otus.kirillov.service.myorm;

import ru.otus.kirillov.dao.Dao;
import ru.otus.kirillov.myorm.MyOrmService;
import ru.otus.kirillov.service.AbstractDBService;
import ru.otus.kirillov.utils.CommonUtils;

import java.io.Closeable;

import static ru.otus.kirillov.utils.ReflectionUtils.instantiateWithInjections;

/**
 * Created by Александр on 28.01.2018.
 */
public class DBServiceMyOrmImpl extends AbstractDBService {

    private final MyOrmService service;

    public DBServiceMyOrmImpl(MyOrmService service) {
        this.service = CommonUtils.retunIfNotNull(service);
    }

    /**
     *  И так. Задача создать DAO. И тут 2 варианта (в самом распространенном случае):
     *  1. У него есть конструктор с одним параметров Supplier<Connection>
     *  2. У него есть дефолтный конструктор и поле Supplier<Connection>
     *  3. Он должен иметь модификатор public
     * @param daoClass - класс DAO-шки
     * @return
     */
    @Override
    protected Dao<?> createDao(Class<?> daoClass) {
        Dao<?> dao = instantiateWithInjections(daoClass, service);
        if(dao == null) {
            throw new RuntimeException(
                    String.format("Unable to create DAO %s: try to check contract " +
                            "for DBServiceMyOrmImpl", daoClass));
        }
        return dao;
    }

    public void destroy() {
        service.destroy();
    }


}
