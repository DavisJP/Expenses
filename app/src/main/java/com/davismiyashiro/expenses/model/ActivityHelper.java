package com.davismiyashiro.expenses.model;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public class ActivityHelper {

    public static final String TAG = "EXPENSES";

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text,
                Toast.LENGTH_SHORT).show();
    }

    public static void showSnack(View view, String text) {
                Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
    }
}
