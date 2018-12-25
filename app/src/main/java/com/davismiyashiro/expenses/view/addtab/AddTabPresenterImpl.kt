package com.davismiyashiro.expenses.view.addtab

import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.Repository

/**
 * Created by Davis on 23/01/2016.
 */
class AddTabPresenterImpl(private val mView: AddTabInterfaces.View, private val mModel: Repository, private var mTab: Tab?) : AddTabInterfaces.UserActionsListener {

    override fun addTab(name: String) {
        mTab = Tab(name)
        mModel.saveTab(mTab!!)

        mView.setTabValid()
    }

    override fun loadTab() {
        mModel.getTab(mTab?.groupId.toString(), object : Repository.GetTabCallback {
            override fun onTabLoaded(tab: Tab) {
                mTab = tab
                mView.setTabName(mTab!!.groupName)
            }
        })
    }

    override fun updateTab(name: String) {
        mTab = Tab(mTab?.groupId.toString(), name)
        mModel.updateTab(mTab!!)

        mView.updatedTab(mTab!!)
    }

    //    private boolean isTabNew () {
    //        return mTab == null;
    //    }
}
