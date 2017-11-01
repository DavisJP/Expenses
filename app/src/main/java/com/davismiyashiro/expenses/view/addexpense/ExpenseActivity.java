package com.davismiyashiro.expenses.view.addexpense;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.davismiyashiro.expenses.Injection;
import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.model.BaseCompatActivity;
import com.davismiyashiro.expenses.R;
import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.view.splitter.SplitterActivity;

import timber.log.Timber;

public class ExpenseActivity extends BaseCompatActivity implements ExpenseView.View, View.OnClickListener {

    private static String TAB_REQUESTED = "com.davismiyashiro.expenses.view.expense";
    private static String EDIT_EXPENSE = "com.davismiyashiro.expenses.view.expense.edit";

    private TextInputLayout mInputLayoutDesc;
    private EditText mEditTextDescription;
    private TextInputLayout mInputLayoutValue;
    private EditText mEditTextValue;
    private FloatingActionButton mFabExpense;

    private ExpenseView.UserActionsListener mPresenter;

    private Tab mTab;
    private Expense mExpense;

    public static Intent newIntent (Context context, Tab tab, Expense expense) {
        Intent intent = new Intent(context, ExpenseActivity.class);
        intent.putExtra(TAB_REQUESTED, tab);
        intent.putExtra(EDIT_EXPENSE, expense);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.expenses);
        super.onCreate(savedInstanceState);

        mInputLayoutDesc = (TextInputLayout) findViewById(R.id.edt_exp_desc_layout);
        mEditTextDescription = (EditText) findViewById(R.id.edtDescriptionName);
        requestFocus(mEditTextDescription);

        mInputLayoutValue = (TextInputLayout) findViewById(R.id.edt_exp_value_layout);
        mEditTextValue = (EditText) findViewById(R.id.edtValue);

        mFabExpense = (FloatingActionButton) findViewById(R.id.fab_add_expense_details);
        mFabExpense.setOnClickListener(this);
        mFabExpense.show();
//        ObjectAnimator fadeAnimX = ObjectAnimator.ofFloat(mFabExpense, View.SCALE_X, 0, 1);
//        fadeAnimX.setDuration(500);
//        //fadeAnimX.start();
//
//        ObjectAnimator fadeAnimY = ObjectAnimator.ofFloat(mFabExpense, View.SCALE_Y, 0, 1);
//        fadeAnimY.setDuration(500);
//        //fadeAnimY.start();
//
//        AnimatorSet set1 = new AnimatorSet();
//        set1.playTogether(fadeAnimX, fadeAnimY);
//        set1.start();

        mPresenter = new ExpensePresenterImpl(this, Injection.provideTabsRepository(this));

        mTab = getIntent().getParcelableExtra(TAB_REQUESTED);
        mExpense = getIntent().getParcelableExtra(EDIT_EXPENSE);

        ActionBar ab = getSupportActionBar();
        if (isExpenseNew()) {
            ab.setTitle(R.string.add_expense_action_bar);
        } else {
            ab.setTitle(R.string.edit_expense_action_bar);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");

        if (!isExpenseNew()){
            setExpenseDescription(mExpense.getDescription());
            setExpenseValue(String.valueOf(mExpense.getValue()));
        }
    }

    @Override
    public void chooseParticipants(Expense expense) {
        Intent intent = SplitterActivity.newIntent(this, expense);
        startActivity(intent);
    }

    @Override
    public void setExpenseDescription(String desc) {
        mEditTextDescription.setText(desc);
    }

    @Override
    public void setExpenseValue(String value) {
        mEditTextValue.setText(value);
    }

    @Override
    public void setDescriptionError(boolean value) {
        //mEditTextDescription.setError(getString(R.string.error_field_required));
        if (value) {
            mInputLayoutDesc.setError(getString(R.string.error_field_required));
            requestFocus(mEditTextDescription);
        }
        mInputLayoutDesc.setErrorEnabled(value);
    }

    @Override
    public void setValueError(boolean value) {
        //mEditTextValue.setError(getString(R.string.error_field_required));
        if (value) {
            mInputLayoutValue.setError(getString(R.string.error_field_required));
            requestFocus(mEditTextValue);
        }
        mInputLayoutValue.setErrorEnabled(value);
    }

    @Override
    public void onSuccess() {
        String description = mEditTextDescription.getText().toString();
        String valueStr = mEditTextValue.getText().toString();
        double value = Double.parseDouble(valueStr);
        Expense expense;

        if (isExpenseNew()) {

            expense = new Expense(description, value, mTab.getGroupId());

            mPresenter.addExpense(expense);
        } else {
            expense = Expense.retrieveExpense(mExpense.getId(), description, value, mTab.getGroupId());

            mPresenter.updateExpense(expense);
        }
        chooseParticipants(expense);

        // ---closes the activity---
        mFabExpense.hide();
        finish();
    }

    @Override
    public void onClick(View v) {
        mPresenter.validateExpenseFields(mEditTextDescription.getText().toString(), mEditTextValue.getText().toString());
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean isExpenseNew () {
        return mExpense == null;
    }
}
