package com.davismiyashiro.expenses.view.opentab

import android.support.v4.util.ArrayMap

import com.davismiyashiro.expenses.datatypes.ReceiptItem
import com.davismiyashiro.expenses.datatypes.Tab

/**
 * @Author Davis Miyashiro
 */

interface ReceiptInterfaces {
    interface ReceiptView {

        fun showReceiptItems(items: ArrayMap<String, MutableList<ReceiptItem>>)

        fun refreshReceiptItemsAdapter()
    }

    interface UserActionsListener {

        fun setReceiptView(view: ReceiptView)

        fun loadReceipItems(tab: Tab)
    }
}
