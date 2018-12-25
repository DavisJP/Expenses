package com.davismiyashiro.expenses.view.addtab

import com.davismiyashiro.expenses.datatypes.Tab

/**
 * Created by Davis on 26/08/2016.
 */
interface AddTabInterfaces {
    interface View {
        fun showTabNameError()
        fun setTabName(name: String)
        fun setTabValid()
        fun updatedTab(tab: Tab)
    }

    interface UserActionsListener {
        fun addTab(name: String)
        fun loadTab()
        fun updateTab(name: String)
    }
}
