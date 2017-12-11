package ru.otus.kirillov.atm.cells;

import ru.otus.kirillov.atm.money.Banknote;
import ru.otus.kirillov.atm.utils.Commons;

import java.util.*;
import java.util.stream.Collectors;

/** Стойка с ячейками в банкомате.
 * Может содержать ячейки с разными валютами и банкнотами.
 * Данная сущность ничего не знает по поводу сумм и валют,
 * она знает только про типы купюр и умеет их выдавать,
 * принимать, возвращать кол-во купюр по их типам.
 * Ну и откатывать состояние ячеек в исходное.
 * @see CellManagement
 * Created by Александр on 04.12.2017.
 */
public class CellRack implements CellManagement{

    private List<Cell> cells;

    public CellRack() {
        cells = new ArrayList<>();
    }

    public CellRack(Collection<Cell> cells) {
        this();
        Commons.requiredNotNull(cells, "cells must be not null");
        this.cells.addAll(cells);
    }

    public CellRack(Cell... cells) {
        this(Arrays.asList(cells));
    }

    /**
     * Добавить ячейки в стойку
     * @param cells - коллекция ячеек
     */
    public void addCells(Collection<Cell> cells) {
        Commons.requiredNotNull(cells, "cells must be not null");
        this.cells.addAll(cells);
    }

    public void addCells(Cell... cells) {
        addCells(Arrays.asList(cells));
    }

    /**
     * Добавить ячейку в стойку
     * @param cell
     */
    public void addCell(Cell cell) {
        Commons.requiredNotNull(cell, "cell must be not null");
        cells.add(cell);
    }

    /**
     * Добавление новой ячейки в стойку
     * @param cell - ячейка
     * @return this
     */
    public CellRack withNextCell(Cell cell) {
        addCell(cell);
        return this;
    }

    /**
     * @see CellManagement#put(Banknote, int)
     */
    public void put(Banknote banknote, int count) {
        Commons.requiredTrue(getBanknoteCountByType().containsKey(banknote),
                String.format("cell rack has no banknote type - %s", banknote));
        Cell targetCell = getFirstCell(banknote, Comparator.comparingInt(Cell::getBanknoteCount));
        targetCell.put(count);
    }

    /**
     * @see CellManagement#get(Banknote, int)
     */
    public int get(Banknote banknote, int count) {
        Commons.requiredTrue(getBanknoteCountByType().containsKey(banknote),
                String.format("cell rack has no banknote type - %s", banknote));
        Commons.requiredTrue(getBanknoteCountByType().getOrDefault(banknote, 0) > count,
                String.format("Not enough banknotes for type %s in rack", banknote));

        //Ячейки с нужным типом банкнот, отсортированные в порядке убывания кол-ва
        //купюр в них
        List<Cell> filteredCells =  getCells(banknote,
                Comparator.comparing(Cell::getBanknoteCount, Comparator.reverseOrder()));

        int retainBanknotesCount = count;
        for(Cell cell: filteredCells) {
            int ejectedBanknotesCount = Math.min(cell.getBanknoteCount(), retainBanknotesCount);
            cell.get(ejectedBanknotesCount);
            retainBanknotesCount -= ejectedBanknotesCount;

            if(retainBanknotesCount == 0) {
                break;
            }
        }
        return count;
    }

    private Cell getFirstCell(Banknote banknote, Comparator<Cell> comp) {
        List<Cell> filteredCell = getCells(banknote, comp);

        if(filteredCell.isEmpty()) {
            throw new IllegalStateException("Empty cells");
        }

        return filteredCell.get(0);
    }

    private List<Cell> getCells(Banknote banknote, Comparator<Cell> comp) {
        return cells.stream()
                .filter(cell -> cell.getBanknoteType() == banknote)
                .sorted(comp)
                .collect(Collectors.toList());
    }

    /**
     * @see CellManagement#getBanknoteCountByType()
     */
    //TODO: реализация может быть затратной, было бы неплохо закешировать
    public Map<Banknote, Integer> getBanknoteCountByType() {
        return cells.stream()
                .collect(Collectors.groupingBy(Cell::getBanknoteType,
                        Collectors.summingInt(Cell::getBanknoteCount))
                );
    }

    /**
     * @see CellManagement#restoreToInitial()
     */
    public void restoreToInitial() {
        cells.forEach(Cell::restoreToInitial);
    }
}
