package com.davismiyashiro.expenses.view.sendreceipt

import com.davismiyashiro.expenses.datatypes.Tab

/**
 * Created by Davis on 29/07/2016.
 */
interface ReceiptInterfaces {

    interface View {
        fun showReceipt()
    }

    interface UserActionsListener {
        fun saveTab(tab: Tab)
    }
}
