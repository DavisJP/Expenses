package com.davismiyashiro.expenses.view.opentab;

import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Tab;

import java.util.List;

/**
 * @Author Davis Miyashiro
 */

public interface ExpenseInterfaces {
    interface ExpenseView {

        void showExpenses (List<Expense> expenses);
    }

    interface UserActionsListener {

        void setExpenseView (ExpenseView view);

        void loadExpenses (Tab tab);

        void removeExpense (Expense expense);

    }
}
