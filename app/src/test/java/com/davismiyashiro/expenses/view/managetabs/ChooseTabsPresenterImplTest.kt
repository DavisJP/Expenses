package com.davismiyashiro.expenses.view.managetabs

import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.Repository
import com.davismiyashiro.expenses.model.Repository.LoadTabsCallback
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.*

import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

import java.util.ArrayList
import java.util.Arrays

import org.mockito.Mockito.verify

/**
 * Created by Davis on 01/09/2016.
 */
class ChooseTabsPresenterImplTest {

    private val mRepository: Repository = mock()

    private val mChooseTabsView = mock<ChooseTabsInterfaces.View>() //Another way

    private val mGetCallback: Repository.GetTabCallback = mock()

    private val mLoadTabsCallbackCaptor = argumentCaptor<LoadTabsCallback>()

    private val mGetCallbackCaptor = argumentCaptor<Repository.GetTabCallback>()

    private lateinit var mChooseTabsPresenter: ChooseTabsPresenterImpl

    @Before
    @Throws(Exception::class)
    fun setUp() {
        //Kotlin doesn’t differentiate between checked and unchecked expressions. It doesn't have checked exceptions.
        //https://kotlinlang.org/docs/reference/exceptions.html

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