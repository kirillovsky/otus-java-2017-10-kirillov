package ru.otus.kirillov;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.otus.kirillov.atm.ATMImplTest;
import ru.otus.kirillov.atm.cells.CellRackTest;
import ru.otus.kirillov.atm.cells.CellTest;
import ru.otus.kirillov.atm.commands.BalanceCommandTest;
import ru.otus.kirillov.atm.commands.DepositCommandTest;
import ru.otus.kirillov.atm.commands.UndoToDefaultCommandTest;
import ru.otus.kirillov.atm.commands.WithdrawCommandTest;
import ru.otus.kirillov.atm.commands.invokers.CommandInvokerImplTest;
import ru.otus.kirillov.atmdepartment.NamedATMDepartmentTest;

/** Запуск всех тестов проекта.
 * Благодаря использованию абстракций и разделению приложения на слои,
 * появилась возможность тестировать их независимо с помощью мокирования
 * Created by Александр on 10.12.2017.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        //Бизнес-сценарий использования ATMDepartment и ATM
        BusinessScenarioTest.class,

        //Модульные тесты элементов системы
        //Тест для текущей реализации ATMDepartment
        NamedATMDepartmentTest.class,
        //Тест для текущей реализации ATM
        ATMImplTest.class,
        //Тест для запускалки комманд
        CommandInvokerImplTest.class,
        //Тест для каждой из комманд
        BalanceCommandTest.class,
        UndoToDefaultCommandTest.class,
        DepositCommandTest.class,
        WithdrawCommandTest.class,
        //Тест для системы управления ячейками
        CellRackTest.class,
        //Тесты для ячейки
        CellTest.class
})
public class AllTestRunner {
}
