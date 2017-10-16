package ru.otus.kirillov.hw01;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Александр on 15.10.2017.
 */

public class Main {

    public static void main(String... argv) {
        //Преобразовать входной массив в лист чисел
        List<Integer> lst = Lists.transform(Arrays.asList(argv),
                (str) -> NumberUtils.isNumber(str) ? Integer.valueOf(str): null);
        //Развернуть его
        lst = Lists.reverse(lst);
        System.out.println(lst);

        //Разделить на четные и все остальные (включая null)
        Map<Boolean, List<Integer>> mapEvenOrElse = lst.stream()
                .collect(Collectors.partitioningBy((n) -> n != null && (n % 2 == 0)));
        System.out.println(mapEvenOrElse);



    }
}
