package com.davismiyashiro.expenses.view.sendreceipt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.davismiyashiro.expenses.Injection;
import com.davismiyashiro.expenses.R;
import com.davismiyashiro.expenses.model.ActivityHelper;
import com.davismiyashiro.expenses.model.BaseCompatActivity;
import com.davismiyashiro.expenses.datatypes.Tab;

public class ReceiptActivity extends BaseCompatActivity implements ReceiptInterfaces.View{

    private static final String TAB_PARAM = "com.davismiyashiro.expenses.view.receipt.ReceiptActivity";
    private Tab mTab;
    private ReceiptInterfaces.UserActionsListener mActionsListener;
    private TextView receiptResume;

    public static Intent newInstance (Context context, Tab tab) {
        Intent intent = new Intent (context, ReceiptActivity.class);
        intent.putExtra(TAB_PARAM, tab);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        mTab = getIntent().getParcelableExtra(TAB_PARAM);

        receiptResume = (TextView) findViewById(R.id.multText);
        receiptResume.append("Resume Receipt");

        mActionsListener = new ReceiptPresenter(this, Injection.provideTabsRepository(getApplicationContext()));
    }

    public void sendTab(View view) {

        //ActivityHelper.showToast(this, "To be Implemented!");

        String mimeType = "text/plain";
        String title = "Share receipt for " + mTab.getGroupName();
        //String message = "To be Implemented!";

        ShareCompat.IntentBuilder.from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(receiptResume.getText().toString())
                .startChooser();

        //finish();
    }

    /**
     * Toast method to be executed in a thread
     *
     * @param toast param
     */
    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), toast,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showReceipt() {

    }
}