package com.davismiyashiro.expenses.view.addparticipant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.davismiyashiro.expenses.Injection;
import com.davismiyashiro.expenses.R;
import com.davismiyashiro.expenses.view.BaseCompatActivity;
import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.datatypes.Tab;

import timber.log.Timber;

public class ParticipantActivity extends BaseCompatActivity implements ParticipantInterfaces.View, View.OnClickListener {

    private static final String TAB_PARAM = "com.davismiyashiro.expenses.view.participant.Tab";
    private static final String PART_PARAM = "com.davismiyashiro.expenses.view.participant.Participant";
    private TextInputLayout mInputLayoutPartName;
    private TextInputLayout mInputLayoutPartEmail;
    private TextInputLayout mInputLayoutPartPhone;
    private EditText mEditTextName;
    private EditText mEditTextMail;
    private EditText mEditTextPhone;
    private Tab mTab;
    private Participant mParticipant;

    private ParticipantInterfaces.UserActionsListener mPresenter;

    public static Intent newInstance (Context context, Tab tab, Participant participant) {
        Intent intent = new Intent(context, ParticipantActivity.class);
        intent.putExtra(TAB_PARAM, tab);
        intent.putExtra(PART_PARAM, participant);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.participant);
        super.onCreate(savedInstanceState);

        mInputLayoutPartName = (TextInputLayout) findViewById(R.id.input_layout_participant_name);
        mEditTextName = (EditText) findViewById(R.id.edtParticipantName);
        requestFocus(mEditTextName);

        mInputLayoutPartEmail = (TextInputLayout) findViewById(R.id.input_layout_participant_email);
        mEditTextMail = (EditText) findViewById(R.id.edtParticipantEmail);

        mInputLayoutPartPhone = (TextInputLayout) findViewById(R.id.input_layout_participant_phone);
        mEditTextPhone = (EditText) findViewById(R.id.edtParticipantPhone);

        findViewById(R.id.fab_add_participant_details).setOnClickListener(this);

        mPresenter = new ParticipantPresenterImpl(this, Injection.INSTANCE.provideTabsRepository(getApplicationContext()));

        mTab = getIntent().getParcelableExtra(TAB_PARAM);
        mParticipant = getIntent().getParcelableExtra(PART_PARAM);

        ActionBar ab = getSupportActionBar();
        if (isParticipantNew()) {
            ab.setTitle(R.string.add_part_action_bar);
        } else {
            ab.setTitle(R.string.edit_part_action_bar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_participant, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");

        if (!isParticipantNew()){
            setParticipantName(mParticipant.getName());
            setParticipantEmail(mParticipant.getEmail());
            setParticipantPhone(mParticipant.getNumber());
        }
    }

    @Override
    public void onSuccess() {

        Participant participant;

        if (isParticipantNew()){
            participant = new Participant(mEditTextName.getText().toString(), mEditTextMail.getText().toString(), mEditTextPhone.getText().toString(), mTab.getGroupId());

            mPresenter.addParticipant(participant);
        } else {
            participant = Participant.Companion.retrieveParticipant(mParticipant.getId(), mEditTextName.getText().toString(), mEditTextMail.getText().toString(), mEditTextPhone.getText().toString(), mTab.getGroupId());

            mPresenter.updateParticipant(participant);
        }

        finish();
    }

    @Override
    public void setParticipantName(String name) {
        mEditTextName.setText(name);

    }

    @Override
    public void setParticipantEmail(String email) {
        mEditTextMail.setText(email);

    }

    @Override
    public void setParticipantPhone(String phone) {
        mEditTextPhone.setText(phone);
    }

    @Override
    public void showNameError(boolean value) {
        if (value) {
            mInputLayoutPartName.setError(getString(R.string.error_field_required));
            requestFocus(mEditTextName);
        }
        mInputLayoutPartName.setErrorEnabled(value);
    }

    @Override
    public void showEmailError(boolean value) {
        if (value) {
            mInputLayoutPartEmail.setError(getString(R.string.error_invalid_email));
            requestFocus(mEditTextMail);
        }
        mInputLayoutPartEmail.setErrorEnabled(value);
    }

    @Override
    public void showPhoneError(boolean value) {
        if (value) {
            mInputLayoutPartPhone.setError(getString(R.string.error_field_required));
            requestFocus(mEditTextPhone);
        } else {
            mInputLayoutPartPhone.setError(null);
            mInputLayoutPartPhone.setErrorEnabled(value);
        }
    }

    @Override
    public void onClick(View v) {
        mPresenter.validateInputFields(mEditTextName.getText().toString(), mEditTextMail.getText().toString(), mEditTextPhone.getText().toString());
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean isParticipantNew () {
        return mParticipant == null;
    }
}
