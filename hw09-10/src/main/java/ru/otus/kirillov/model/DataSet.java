package ru.otus.kirillov.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

/** Базовый класс для всех моделей
 * Created by Александр on 20.01.2018.
 */
@MappedSuperclass
public abstract class DataSet {

    private static Random ID_GENERATOR =
            new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DataSet() {
        //Так как самописный ORM-фреймворк не умеет сетить
        //Только что сгенеренную дату, будем генерить id-шники так
        id = ID_GENERATOR.nextLong();
    }
}
