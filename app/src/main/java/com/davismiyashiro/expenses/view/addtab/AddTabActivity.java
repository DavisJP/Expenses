package com.davismiyashiro.expenses.view.addtab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.davismiyashiro.expenses.Injection;
import com.davismiyashiro.expenses.R;
import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.model.BaseCompatActivity;

import timber.log.Timber;

/**
 * Class that saves the name of the tab and pass along to TabActivity.
 * Testing TextInputLayout library
 */
public class AddTabActivity extends BaseCompatActivity implements AddTabInterfaces.View{

    public static String EDIT_TAB = "com.davismiyashiro.expenses.view.addtab";

    private FloatingActionButton fabAddTabDetails;
    private EditText mEditTextTabName;
    private TextInputLayout mInputLayoutName;
    private AddTabInterfaces.UserActionsListener mPresenter;

    private Tab mTab;

    public static Intent newIntent (Context context, Tab tab) {
        Intent intent = new Intent (context, AddTabActivity.class);
        intent.putExtra(EDIT_TAB, tab);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.add_tab_layout);
        super.onCreate(savedInstanceState);

        mInputLayoutName = (TextInputLayout) findViewById(R.id.edt_tab_layout);
        mEditTextTabName = (TextInputEditText) findViewById(R.id.edt_txt_name);

        mEditTextTabName.addTextChangedListener(new TabTextWatcher(mEditTextTabName));
        requestFocus(mEditTextTabName);

        fabAddTabDetails = (FloatingActionButton) findViewById(R.id.fab_add_tab);
        fabAddTabDetails.setOnClickListener(
                v -> {
                    if (isTabNew()) {
                        if (validateName()) {
                            mPresenter.addTab(mEditTextTabName.getText().toString());
                        }
                    } else {
                        mPresenter.updateTab(mEditTextTabName.getText().toString());
                    }
                }
        );

        mTab = getIntent().getParcelableExtra(EDIT_TAB);

        ActionBar ab = getSupportActionBar();
        if (isTabNew ()) {
            ab.setTitle(R.string.add_tab_action_bar);
        } else {
            ab.setTitle(R.string.edit_tab_action_bar);
        }

        mPresenter = new AddTabPresenterImpl(this, Injection.provideTabsRepository(getApplicationContext()), mTab);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");

        if (!isTabNew()){
            mPresenter.loadTab();
        }
    }

    private boolean isTabNew () {
        return mTab == null;
    }

    private boolean validateName() {
        if (mEditTextTabName.getText().toString().trim().isEmpty()) {
            showTabNameError();
            return false;
        } else {
            mInputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class TabTextWatcher implements TextWatcher {

        private View view;

        private TabTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edt_txt_name:
                    validateName();
                    break;
            }
        }
    }

    @Override
    public void showTabNameError() {
        mInputLayoutName.setError(getString(R.string.err_msg_empty_name));
        requestFocus(mEditTextTabName);
    }

    @Override
    public void setTabName(String name) {
        mEditTextTabName.setText(name);
    }

    @Override
    public void setTabValid() {
        finish();
    }

    @Override
    public void updatedTab(Tab tab) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EDIT_TAB, tab);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
