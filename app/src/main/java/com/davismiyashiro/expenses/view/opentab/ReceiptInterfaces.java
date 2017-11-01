package com.davismiyashiro.expenses.view.opentab;

import android.support.v4.util.ArrayMap;

import com.davismiyashiro.expenses.datatypes.ReceiptItem;
import com.davismiyashiro.expenses.datatypes.Tab;

import java.util.List;

/**
 * @Author Davis Miyashiro
 */

public interface ReceiptInterfaces {
    interface ReceiptView{

        void showReceiptItems (ArrayMap<String, List<ReceiptItem>> items);

        void refreshReceiptItemsAdapter ();
    }

    interface UserActionsListener {

        void setReceiptView (ReceiptView view);

        void loadReceipItems (Tab tab);
    }
}
