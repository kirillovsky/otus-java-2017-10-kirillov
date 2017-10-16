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

    /**
     * Тестовый метод, для подключения библиотек Apache Common Lang 3 и Google Guava.
     * Преобразование входного массива осуществляется по следующему алгоритму:
     * 1. Входной массив строк преобразуется в лист интеджеров (строки не соответствующие числам мапятся в null)
     * 2. Меняется порядок массива интеджеров, полученного на шаге 1, на обратный
     * 3. Создается Map<Boolean, List<Integer>>. В которой: ключи - флаги того, четно ли число или нет;
     * значение - массивы чисел, соответствующие данному флагу.
     * Для null-элемента массива, ключ - false
     * @param argv - входной массив строк
     */
    public static void main(String... argv) {
        //Преобразовать входной массив в лист чисел
        List<Integer> lst = Lists.transform(Arrays.asList(argv),
                (str) -> NumberUtils.isNumber(str) ? Integer.valueOf(str): null);
        System.out.println(lst);
        //Развернуть его
        lst = Lists.reverse(lst);
        System.out.println(lst);

        //Разделить на четные и все остальные (включая null)
        Map<Boolean, List<Integer>> mapEvenOrElse = lst.stream()
                .collect(Collectors.partitioningBy((n) -> n != null && (n % 2 == 0)));
        System.out.println(mapEvenOrElse);



    }
}
