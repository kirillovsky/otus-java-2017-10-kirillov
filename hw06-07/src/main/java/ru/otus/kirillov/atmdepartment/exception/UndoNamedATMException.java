package ru.otus.kirillov.atmdepartment.exception;

import java.util.List;
import java.util.stream.Collectors;

/** Ошибка отката, какого-либо именованного банкомата
 * Created by Александр on 09.12.2017.
 */
public class UndoNamedATMException extends RuntimeException {

    public UndoNamedATMException(List<String> atmName) {
        super(atmName.stream()
                .collect(Collectors.joining("Failed ATMs:[", ",", "]"))
        );
    }
}
