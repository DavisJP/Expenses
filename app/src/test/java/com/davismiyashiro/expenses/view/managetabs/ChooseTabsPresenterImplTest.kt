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
package com.davismiyashiro.expenses.view.managetabs

import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.Repository
import com.davismiyashiro.expenses.model.Repository.LoadTabsCallback
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

/**
 * Created by Davis on 01/09/2016.
 */
class ChooseTabsPresenterImplTest {

    private val mRepository: Repository = mock()
    private val mChooseTabsView = mock<ChooseTabsInterfaces.View>() // Another way
    private val mLoadTabsCallbackCaptor = argumentCaptor<LoadTabsCallback>()
    private val mGetCallbackCaptor = argumentCaptor<Repository.GetTabCallback>()

    private lateinit var mChooseTabsPresenter: ChooseTabsPresenterImpl

    @Before
    @Throws(Exception::class)
    fun setUp() {
        // Kotlin doesn’t differentiate between checked and unchecked expressions. It doesn't have checked exceptions.
        // https://kotlinlang.org/docs/reference/exceptions.html

        MockitoAnnotations.initMocks(this)

        mChooseTabsPresenter = ChooseTabsPresenterImpl(mChooseTabsView, mRepository)
    }

    @Test
    @Throws(Exception::class)
    fun testAddTab() {
        mChooseTabsPresenter.addTab()

        verify(mChooseTabsView).showAddTab()
    }

    @Test
    @Throws(Exception::class)
    fun testLoadTabs() {
        mChooseTabsPresenter.loadTabs(true)

        verify(mRepository).getTabs(mLoadTabsCallbackCaptor.capture())

        mLoadTabsCallbackCaptor.firstValue.onTabsLoaded(TABS)

        verify(mChooseTabsView).setProgressIndicator(false)
        verify(mChooseTabsView).showTabs(TABS)
    }

    @Test
    @Throws(Exception::class)
    fun testLoadNoTabs() {
        mChooseTabsPresenter.loadTabs(true)

        verify(mRepository).getTabs(mLoadTabsCallbackCaptor.capture())

        mLoadTabsCallbackCaptor.firstValue.onTabsLoaded(ArrayList())

        verify(mChooseTabsView).setProgressIndicator(false)
        verify(mChooseTabsView).showNoTabs()
    }

    @Test
    @Throws(Exception::class)
    fun testOpenTab() {
        val tab = Tab("1", "first")

        mChooseTabsPresenter.openTab("1")

        verify(mRepository).getTab(eq(tab.groupId), mGetCallbackCaptor.capture())

        mGetCallbackCaptor.firstValue.onTabLoaded(tab) // Trigger callback

        verify(mChooseTabsView).showTabDetailUi(tab)
    }

    companion object {

        private val TABS = ArrayList(
                Arrays.asList(Tab("1", "first"),
                        Tab("2", "second")
                )
        )
    }
}