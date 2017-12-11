package ru.otus.kirillov;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import ru.otus.kirillov.atm.ATM;
import ru.otus.kirillov.atm.ATMImpl;
import ru.otus.kirillov.atm.cells.Cell;
import ru.otus.kirillov.atm.cells.CellManagement;
import ru.otus.kirillov.atm.cells.CellManagementSimpleFactory;
import ru.otus.kirillov.atm.cells.CellRack;
import ru.otus.kirillov.atm.commands.invokers.CommandInvoker;
import ru.otus.kirillov.atm.commands.invokers.CommandInvokerImpl;
import ru.otus.kirillov.atm.currency.Currency;
import ru.otus.kirillov.atm.money.Banknote;
import ru.otus.kirillov.atm.money.BillsPack;
import ru.otus.kirillov.atm.money.EURBanknote;
import ru.otus.kirillov.atm.money.RUBBanknote;
import ru.otus.kirillov.atm.utils.Commons;
import ru.otus.kirillov.atm.utils.ReportUtils;
import ru.otus.kirillov.atmdepartment.ATMDepartment;
import ru.otus.kirillov.atmdepartment.NamedATMDepartment;

import java.util.Arrays;
import java.util.Map;
import java.util.function.LongBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Простейший бизнес сценарий
 * (работаем с живыми настоящими запиленными структурами и алгоритмами,
 * без мокирования)
 * Сценарий:
 * 0. Создаем рублевый банкомат (все виды рублевых купюр по 100 штук каждой + 100 купюр по 10 евро).
 * Создаем евро банкомат (все виды евро купюр по 100 штук каждой + 100 купюр по 1000 рублей).
 * Объединяем банкоматы в департамент.
 * 1. Проверка баланса после инициализации с ожидаемым значением
 * 2. Снимаем с рублевого и евро банкомата (3330 в валюте банкомата). И проверяем все балансы
 * 3. Откат до первоначального состояния
 * 4. Кладем в рублевый банкомат 5500р. Снимаем с евро банкомата 6700 евро.
 * Проверяем все балансы
 * 5. Кладем в рублевый банкомат 1100р. Кладем в евро банкомат 5500 евро.
 * Проверяем все балансы
 * 6. Кладем в рублевый банкомат 550 евро. Кладем в евро банкомат 3000 рублей.
 * Проверяем все балансы
 * 7. Откат до первоначального состояния
 * 8. Кладем в рублевый банкомат 555 евро.
 * Падаем с ошибкой. Проверяем, что балансы не поменялись
 * 9. Снимаем из евро банкомата 78505 евро (столько в банкомате просто нет).
 * Падаем с ошибкой. Проверяем, что балансы не поменялись
 * 10. Кладем в евро банкомат 1300 рублей.
 * Падаем с ошибкой. Проверяем, что балансы не поменялись
 * Created by Александр on 11.12.2017.
 */
public class BusinessScenarioTest {

    /**
     * Ячейки с полным набором рублей и пачкой купюр 10 EUR
     */
    private CellManagement cellManagementRUB;

    /**
     * Ячейки с полном набором евро и пачкой купюр 1_000 RUB
     */
    private CellManagement cellManagementEUR;

    /**
     * Банкомат для ячейки {@link BusinessScenarioTest#cellManagementRUB}
     */
    private ATM rubATM;

    private final static Map<Currency, Long> rubATMInitBalance =
            Commons.ofMap(
                    Pair.of(Currency.RUB, IntStream.of(10, 50, 100, 200, 500, 1000, 2000, 5000)
                            .mapToLong(i -> i * 100L).sum()),
                    Pair.of(Currency.EURO, 10 * 100L)
            );

    /**
     * Банкомат для ячейки {@link BusinessScenarioTest#cellManagementEUR}
     */
    private ATM euroATM;

    private final static Map<Currency, Long> euroATMInitBalance =
            Commons.ofMap(
                    Pair.of(Currency.EURO, IntStream.of(5, 10, 20, 50, 200, 500)
                            .mapToLong(i -> i * 100L).sum()),
                    Pair.of(Currency.RUB, 1_000 * 100L)
            );

    /**
     * Объединение двух банкоматов {@link BusinessScenarioTest#rubATM} и {@link BusinessScenarioTest#euroATM}
     */
    private ATMDepartment department;

    private final static Map<Currency, Long> initializeDepartmentBalance =
            Commons.ofMap(
                    Pair.of(Currency.RUB, rubATMInitBalance.get(Currency.RUB)
                            + euroATMInitBalance.get(Currency.RUB)),
                    Pair.of(Currency.EURO, euroATMInitBalance.get(Currency.EURO)
                            + rubATMInitBalance.get(Currency.EURO))
            );


    @Before
    public void initialize() {
        cellManagementRUB = createMultiCurrencyCellRack(Currency.RUB,
                Pair.of(EURBanknote.TEN_EURO, 100));
        cellManagementEUR = createMultiCurrencyCellRack(Currency.EURO,
                Pair.of(RUBBanknote.ONE_THOUSAND_RUBLES, 100));
        rubATM = createSimpleATM(cellManagementRUB);
        euroATM = createSimpleATM(cellManagementEUR);
        department = new NamedATMDepartment(Arrays.asList(rubATM, euroATM));
    }

    @Test
    public void runSimpleBusinessScenario() {
        Map<Currency, Long> tmpRubAtmBalance;
        Map<Currency, Long> tmpEuroAtmBalance;
        Map<Currency, Long> tmpDepartmentAtmBalance;

        ReportUtils.printBlockTitle("1. Проверка баланса после инициализации с ожидаемым значением");
        checkAllBalance(rubATMInitBalance, euroATMInitBalance, initializeDepartmentBalance);

        ReportUtils.printBlockTitle("2. Снимаем с рублевого и евро банкомата (3330 в валюте банкомата).\n" +
                "И проверяем все балансы");
        rubATM.withdraw(Currency.RUB, 3330L);
        euroATM.withdraw(Currency.EURO, 3330L);

        tmpRubAtmBalance = opsForBalanceMap(rubATMInitBalance, (a, b) -> a - b, Currency.RUB, 3330L);
        tmpEuroAtmBalance = opsForBalanceMap(euroATMInitBalance, (a, b) -> a - b, Currency.EURO, 3330L);
        tmpDepartmentAtmBalance = opsForBalanceMap(initializeDepartmentBalance, (a, b) -> a - b,
                Pair.of(Currency.RUB, 3330L), Pair.of(Currency.EURO, 3330L));

        checkAllBalance(tmpRubAtmBalance, tmpEuroAtmBalance, tmpDepartmentAtmBalance);

        ReportUtils.printBlockTitle("3. Откат до первоначального состояния");
        department.restoreAllToDefaultState();
        tmpRubAtmBalance = rubATMInitBalance;
        tmpEuroAtmBalance = euroATMInitBalance;
        tmpDepartmentAtmBalance = initializeDepartmentBalance;
        checkAllBalance(tmpRubAtmBalance, tmpEuroAtmBalance, tmpDepartmentAtmBalance);

        ReportUtils.printBlockTitle("4. Кладем в рублевый банкомат 5500р.\n" +
                "Снимаем с евро банкомата 6700 евро. Проверяем все балансы");
        rubATM.deposit(BillsPack.of(RUBBanknote.FIVE_THOUSAND_RUBLES, 1)
                .withNewBanknotes(RUBBanknote.FIVE_HUNDRED_RUBLES, 1)
        );
        euroATM.withdraw(Currency.EURO, 6700L);
        tmpRubAtmBalance = opsForBalanceMap(tmpRubAtmBalance, Long::sum, Currency.RUB, 5500L);
        tmpEuroAtmBalance = opsForBalanceMap(tmpEuroAtmBalance, (a, b) -> a - b, Currency.EURO, 6700L);
        tmpDepartmentAtmBalance = opsForBalanceMap(tmpDepartmentAtmBalance, Long::sum, Currency.RUB, 5500L);
        tmpDepartmentAtmBalance = opsForBalanceMap(tmpDepartmentAtmBalance, (a, b) -> a - b, Currency.EURO, 6700L);
        checkAllBalance(tmpRubAtmBalance, tmpEuroAtmBalance, tmpDepartmentAtmBalance);

        ReportUtils.printBlockTitle("5. Кладем в рублевый банкомат 1100р.\n" +
                "Кладем в евро банкомат 5500 евро. Проверяем все балансы");
        rubATM.deposit(BillsPack.of(RUBBanknote.ONE_THOUSAND_RUBLES, 1)
                .withNewBanknotes(RUBBanknote.ONE_HUNDRED_RUBLES, 1)
        );
        euroATM.deposit(BillsPack.of(EURBanknote.FIVE_HUNDRED_EURO, 10)
                .withNewBanknotes(EURBanknote.TWO_HUNDRED_EURO, 2)
                .withNewBanknotes(EURBanknote.FIFTY_EURO, 2)
        );
        tmpRubAtmBalance = opsForBalanceMap(tmpRubAtmBalance, Long::sum, Currency.RUB, 1100L);
        tmpEuroAtmBalance = opsForBalanceMap(tmpEuroAtmBalance, Long::sum, Currency.EURO, 5500L);
        tmpDepartmentAtmBalance = opsForBalanceMap(tmpDepartmentAtmBalance, Long::sum,
                Pair.of(Currency.RUB, 1100L), Pair.of(Currency.EURO, 5500L));
        checkAllBalance(tmpRubAtmBalance, tmpEuroAtmBalance, tmpDepartmentAtmBalance);

        ReportUtils.printBlockTitle("6. Кладем в рублевый банкомат 550 евро.\n" +
                "Кладем в евро банкомат 3000 рублей. Проверяем все балансы");
        rubATM.deposit(BillsPack.of(EURBanknote.TEN_EURO, 55));
        euroATM.deposit(BillsPack.of(RUBBanknote.ONE_THOUSAND_RUBLES, 3));

        tmpRubAtmBalance = opsForBalanceMap(tmpRubAtmBalance, Long::sum, Currency.EURO, 550L);
        tmpEuroAtmBalance = opsForBalanceMap(tmpEuroAtmBalance, Long::sum, Currency.RUB, 3000L);
        tmpDepartmentAtmBalance = opsForBalanceMap(tmpDepartmentAtmBalance, Long::sum,
                Pair.of(Currency.RUB, 3000L), Pair.of(Currency.EURO, 550L));
        checkAllBalance(tmpRubAtmBalance, tmpEuroAtmBalance, tmpDepartmentAtmBalance);

        ReportUtils.printBlockTitle("7. Откат до первоначального состояния");
        department.restoreAllToDefaultState();
        tmpRubAtmBalance = rubATMInitBalance;
        tmpEuroAtmBalance = euroATMInitBalance;
        tmpDepartmentAtmBalance = initializeDepartmentBalance;
        checkAllBalance(tmpRubAtmBalance, tmpEuroAtmBalance, tmpDepartmentAtmBalance);

        ReportUtils.printBlockTitle("8. Кладем в рублевый банкомат 555 евро.\n" +
                "Падаем с ошибкой. Проверяем, что балансы не поменялись");
        try {
            rubATM.deposit(BillsPack.of(EURBanknote.TEN_EURO, 55)
                    .withNewBanknotes(EURBanknote.FIVE_EURO, 1));
            assertTrue("Прошли, а должны были упасть. Шаг 8", 1 != 1);
        } catch (Exception e) {
            assertTrue("Не та ошибка. Шаг 8", e instanceof IllegalArgumentException);
            checkAllBalance(tmpRubAtmBalance, tmpEuroAtmBalance, tmpDepartmentAtmBalance);
        }

        ReportUtils.printBlockTitle("9. Снимаем из евро банкомата 78505 евро.\n" +
                "Падаем с ошибкой. Проверяем, что балансы не поменялись");
        try {
            euroATM.withdraw(Currency.EURO, 78505L);
            assertTrue("Прошли, а должны были упасть. Шаг 9", 1 != 1);
        } catch (Exception e) {
            assertTrue("Не та ошибка. Шаг 9", e instanceof IllegalArgumentException);
            checkAllBalance(tmpRubAtmBalance, tmpEuroAtmBalance, tmpDepartmentAtmBalance);
        }

        ReportUtils.printBlockTitle("10. Кладем в евро банкомат 1300 рублей.\n" +
                "Падаем с ошибкой. Проверяем, что балансы не поменялись");
        try {
            euroATM.deposit(BillsPack.of(RUBBanknote.ONE_THOUSAND_RUBLES, 1)
                    .withNewBanknotes(RUBBanknote.ONE_HUNDRED_RUBLES, 3));
            assertTrue("Прошли, а должны были упасть. Шаг 10", 1 != 1);
        } catch (Exception e) {
            assertTrue("Не та ошибка. Шаг 10", e instanceof IllegalArgumentException);
            checkAllBalance(tmpRubAtmBalance, tmpEuroAtmBalance, tmpDepartmentAtmBalance);
        }
        ReportUtils.printDoubleRowSeparator();
    }

    private CellManagement createMultiCurrencyCellRack(Currency currency,
                                                       Pair<Banknote, Integer>... foreignBanknoteCount) {
        CellRack rack = (CellRack) CellManagementSimpleFactory.createSimpleForCurrency(currency);
        rack.addCells(Arrays.stream(foreignBanknoteCount)
                .map(p -> new Cell(p.getKey(), p.getValue()))
                .collect(Collectors.toList())
        );
        return rack;
    }

    private ATM createSimpleATM(CellManagement cellManagement) {
        CommandInvoker invoker = new CommandInvokerImpl(cellManagement);
        return new ATMImpl(invoker);
    }

    private void checkAllBalance(Map<Currency, Long> rubATMExpectedBalance,
                                 Map<Currency, Long> euroATMExpectedBalance,
                                 Map<Currency, Long> atmDepartmentExpectedBalance) {
        assertEquals(rubATMExpectedBalance, rubATM.getBalance());
        System.out.println("Actual balance for RUB ATM: " + rubATM.getBalance());
        assertEquals(euroATMExpectedBalance, euroATM.getBalance());
        System.out.println("Actual balance for EURO ATM: " + euroATM.getBalance());
        assertEquals(atmDepartmentExpectedBalance, department.getAllBalance());
        System.out.println("Actual balance for ATM Department: " + department.getAllBalance());
    }

    private Map<Currency, Long> opsForBalanceMap(Map<Currency, Long> old, LongBinaryOperator operator,
                                                 Currency currency, long rightOperand) {
        return old.entrySet().stream()
                .map(e -> e.getKey() == currency ?
                        Pair.of(e.getKey(), operator.applyAsLong(e.getValue(), rightOperand)) :
                        Pair.of(e.getKey(), e.getValue())
                ).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private Map<Currency, Long> opsForBalanceMap(Map<Currency, Long> old, LongBinaryOperator operator,
                                                 Pair<Currency, Long>... opsCurrency) {
        Map<Currency, Long> opsMap = Commons.ofMap(opsCurrency);
        return old.entrySet().stream()
                .map(e -> opsMap.containsKey(e.getKey()) ?
                        Pair.of(e.getKey(), operator.applyAsLong(e.getValue(), opsMap.get(e.getKey()))) :
                        Pair.of(e.getKey(), e.getValue())
                ).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }
}
