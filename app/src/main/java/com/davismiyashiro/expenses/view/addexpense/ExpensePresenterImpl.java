package com.davismiyashiro.expenses.view.addexpense;

import android.text.TextUtils;

import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.model.Repository;

public class ExpensePresenterImpl implements ExpenseView.UserActionsListener {
    private ExpenseView.View mView;
    Repository mRepo;

    public ExpensePresenterImpl (ExpenseView.View view, Repository model) {
        mView = view;
        mRepo = model;
    }

    @Override
    public void validateExpenseFields(String description, String value) {
        boolean error = false;
        if (TextUtils.isEmpty(description)){
            error = true;
            mView.setDescriptionError(true);
        } else {
            mView.setDescriptionError(false);
        }

        if (TextUtils.isEmpty(value)) {
            error = true;
            mView.setValueError(true);
        } else {
            mView.setValueError(false);
        }
        if (!error)
            mView.onSuccess();
    }

    @Override
    public void addExpense (Expense expense) {
        mRepo.saveExpense(expense);
    }

    @Override
    public void updateExpense(Expense expense) {
        mRepo.updateExpense(expense);
    }
}
