package com.davismiyashiro.expenses.view.opentab;

import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.model.TabRepository;
import com.davismiyashiro.expenses.view.opentab.ExpenseInterfaces;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Davis Miyashiro
 */

public class ExpenseFragmentPresenter implements ExpenseInterfaces.UserActionsListener {

    ExpenseInterfaces.ExpenseView mExpenseView;
    TabRepository mRepository;

    public ExpenseFragmentPresenter (TabRepository model) {
        mRepository = model;
    }

    @Override
    public void setExpenseView (ExpenseInterfaces.ExpenseView view) {
        mExpenseView = view;
    }

    @Override
    public void loadExpenses(Tab tab) {
        if (tab!= null) {
            mRepository.refreshData(); // Fix to get Expenses list updated when switching tabs
            mRepository.getExpenses(tab.getGroupId(), new TabRepository.LoadExpensesCallback() {
                @Override
                public void onExpensesLoaded(List<Expense> expenses) {
                    if (expenses == null) {
                        expenses = new ArrayList<>();
                    }
                    mExpenseView.showExpenses(expenses);
                }
            });
        }
    }

    @Override
    public void removeExpense(Expense expense) {
        mRepository.deleteExpense(expense);
        //mReceiptView.refreshReceiptItemsAdapter();
    }
}
