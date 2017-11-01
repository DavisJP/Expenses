package com.davismiyashiro.expenses.view.addexpense;

import com.davismiyashiro.expenses.datatypes.Expense;

public interface ExpenseView {
    interface View {
        void setExpenseDescription(String desc);
        void setExpenseValue(String value);
        void setDescriptionError(boolean value);
        void setValueError(boolean value);
        void onSuccess ();
        void chooseParticipants(Expense expense);
    }

    interface UserActionsListener {
        void validateExpenseFields (String description, String value);
        void addExpense (Expense expense);
        void updateExpense (Expense expense);
    }
}
