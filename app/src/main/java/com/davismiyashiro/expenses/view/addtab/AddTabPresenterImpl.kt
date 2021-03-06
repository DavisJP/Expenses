/*
 * MIT License
 *
 * Copyright (c) 2019 Davis Miyashiro
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
