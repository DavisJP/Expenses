package com.davismiyashiro.expenses.view.managetabs

import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.TabRepository
import com.davismiyashiro.expenses.model.TabRepository.LoadTabsCallback
import com.nhaarman.mockito_kotlin.mock

import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.MockitoAnnotations

import java.util.ArrayList
import java.util.Arrays

import org.mockito.Matchers.eq
import org.mockito.Mockito.verify

/**
 * Created by Davis on 01/09/2016.
 */
class ChooseTabsPresenterImplTest {

    private val mTabRepository: TabRepository = mock()

    private val mChooseTabsView = mock<ChooseTabsInterfaces.View>() //Another way

    private val mGetTabCallback: TabRepository.GetTabCallback = mock()

    @Captor
    private val mLoadTabsCallbackCaptor: ArgumentCaptor<LoadTabsCallback> = mock()

    @Captor
    private val mGetTabCallbackCaptor: ArgumentCaptor<TabRepository.GetTabCallback>? = null

    private lateinit var mChooseTabsPresenter: ChooseTabsPresenterImpl

    @Before
    @Throws(Exception::class)
    fun setUp() {
        //Kotlin doesn’t differentiate between checked and unchecked expressions. It doesn't have checked exceptions.
        //https://kotlinlang.org/docs/reference/exceptions.html

        MockitoAnnotations.initMocks(this)

        mChooseTabsPresenter = ChooseTabsPresenterImpl(mChooseTabsView, mTabRepository)
    }

    //    @After
    //    public void tearDown() throws Exception {
    //
    //    }

    @Test
    @Throws(Exception::class)
    fun testAddTab() {
        mChooseTabsPresenter.addTab()

        verify<ChooseTabsInterfaces.View>(mChooseTabsView).showAddTab()
    }

    @Test
    @Throws(Exception::class)
    fun testLoadTabs() {
        mChooseTabsPresenter.loadTabs(true)

        verify<TabRepository>(mTabRepository).getTabs(mLoadTabsCallbackCaptor.capture())

        mLoadTabsCallbackCaptor.value.onTabsLoaded(TABS)

        verify<ChooseTabsInterfaces.View>(mChooseTabsView).setProgressIndicator(false)
        verify<ChooseTabsInterfaces.View>(mChooseTabsView).showTabs(TABS)
    }

    @Test
    @Throws(Exception::class)
    fun testLoadNoTabs() {
        mChooseTabsPresenter.loadTabs(true)

        verify<TabRepository>(mTabRepository).getTabs(mLoadTabsCallbackCaptor.capture())

        mLoadTabsCallbackCaptor.value.onTabsLoaded(ArrayList())

        verify<ChooseTabsInterfaces.View>(mChooseTabsView).setProgressIndicator(false)
        verify<ChooseTabsInterfaces.View>(mChooseTabsView).showNoTabs()
    }

    @Test
    @Throws(Exception::class)
    fun testOpenTab() {
        val tab = Tab("1", "first")

        mChooseTabsPresenter.openTab("1")

        verify<TabRepository>(mTabRepository).getTab(eq(tab.groupId), mGetTabCallbackCaptor!!.capture())

        mGetTabCallbackCaptor.value.onTabLoaded(tab) // Trigger callback

        verify<ChooseTabsInterfaces.View>(mChooseTabsView).showTabDetailUi(tab)
    }

    companion object {

        private val TABS = ArrayList(
                Arrays.asList(Tab("1", "first"),
                        Tab("2", "second")
                )
        )
    }
}