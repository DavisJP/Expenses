package com.davismiyashiro.expenses.view.opentab

import android.support.v4.util.ArrayMap
import com.davismiyashiro.expenses.datatypes.ReceiptItem

import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.Repository

/**
 * @Author Davis Miyashiro
 */

class ReceiptFragmentPresenter(internal var mRepository: Repository) : ReceiptInterfaces.UserActionsListener {

    internal lateinit var mReceiptView: ReceiptInterfaces.ReceiptView

    override fun setReceiptView(view: ReceiptInterfaces.ReceiptView) {
        mReceiptView = view
    }

    override fun loadReceipItems(tab: Tab) {
        if (tab != null) {
            mRepository.refreshData() // Fix to get Expenses list updated when switching tabs
            mRepository.getReceiptItemsByTabId(tab.groupId, object : Repository.LoadReceiptItemsCallback {
                override fun onReceiptItemsLoaded(items: ArrayMap<String, MutableList<ReceiptItem>>) {
                    mReceiptView.showReceiptItems(items)
                }
            })
        }
    }
}
