package com.davismiyashiro.expenses.view.splitter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.davismiyashiro.expenses.Injection;
import com.davismiyashiro.expenses.R;
import com.davismiyashiro.expenses.model.BaseCompatActivity;
import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.model.ActivityHelper;
import com.davismiyashiro.expenses.datatypes.Tab;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class SplitterActivity extends BaseCompatActivity implements SplitterView.View, View.OnClickListener {

    private static String SPLITTER_EXPENSE_REQUESTED = "com.davismiyashiro.expenses.view.splitter";

    private Tab mTab;
    private SplitterView.UserActionsListener mPresenter;

    private ListView mListView;
    private Expense mExpense;
    List<Participant> mParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.splitter);
        super.onCreate(savedInstanceState);

        findViewById(R.id.fab_split_participants).setOnClickListener(this);

        mPresenter = new SplitterPresenterImpl(this, Injection.provideTabsRepository(this));

        mListView = (ListView) findViewById(R.id.list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setTextFilterEnabled(true);

        mExpense = getIntent().getParcelableExtra(SPLITTER_EXPENSE_REQUESTED);
    }

    public static Intent newIntent (Context context, Expense expense) {
        Intent newIntent = new Intent(context, SplitterActivity.class);
        newIntent.putExtra(SPLITTER_EXPENSE_REQUESTED, expense);
        return newIntent;
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");

        mParticipants = mPresenter.getParticipants(mExpense);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_checked, getParticipantsNames(mParticipants));
        mListView.setAdapter(adapter);

    }

    private List<String> getParticipantsNames (List <Participant> participants) {
        List <String> names = new ArrayList<>();
        for (Participant part: participants){
            names.add(part.getName());
        }
        return names;
    }

    @Override
    public void splitWithGroup() {
        ArrayList<Participant> participants = new ArrayList<>();//

        //TODO: Create a datatype for Payer UI
        for (int i = 0; i < mListView.getCount(); i++) {
            if (mListView.isItemChecked(i)) {
                participants.add(mParticipants.get(i));
            }
        }

        mPresenter.saveSplits (participants, mExpense);

        finish();
    }

    public void setParticipantSelectedError() {
        ActivityHelper.showToast(this, "Select at least 1 participant");
    }

    @Override
    public void onClick(View v) {
        mPresenter.validateParticipantsSelected(mListView.getCheckedItemCount());
    }
}