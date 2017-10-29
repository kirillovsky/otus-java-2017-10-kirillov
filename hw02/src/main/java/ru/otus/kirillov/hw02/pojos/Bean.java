package ru.otus.kirillov.hw02.pojos;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Александр on 27.10.2017.
 */
public class Bean implements Comparable<Bean> {

    private static Random RANDOM_VAL_GENERATOR = new Random(Calendar.getInstance().getTimeInMillis());

    private Long val = new Long(RANDOM_VAL_GENERATOR.nextLong());

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }

    @Override
    public int compareTo(Bean o) {
        return (int)(val.longValue() - o.val.longValue());
    }
}
