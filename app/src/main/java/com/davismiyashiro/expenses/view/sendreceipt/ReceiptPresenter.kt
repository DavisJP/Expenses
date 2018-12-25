package com.davismiyashiro.expenses.view.sendreceipt

import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.Repository

/**
 * Created by Davis on 29/07/2016.
 */
class ReceiptPresenter(view: ReceiptInterfaces.View, internal var mRepo: Repository) : ReceiptInterfaces.UserActionsListener {

    override fun saveTab(tab: Tab) {
        mRepo.saveTab(tab)
    }
}
