package com.davismiyashiro.expenses.model

import android.content.Context
import android.support.v4.util.ArrayMap

import com.davismiyashiro.expenses.datatypes.Tab
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.mockito.Matchers.any
import org.mockito.Matchers.eq
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

/**
 * Class that test if InMemoryTabsRepositoryTest caches and process localData requests
 */
class InMemoryTabsRepositoryTest {

    private var mInMemoryTabRepository: InMemoryTabRepository? = null

    private val mContext = mock<Context>()

    private val mLocalSource = mock<TabRepositoryDataSource>()

    private val mGetTabCallback = mock<TabRepository.GetTabCallback>()

    private val mLoadTabsCallback = mock<TabRepository.LoadTabsCallback>()

    private val mTabServiceCallback =  mock<TabRepositoryDataSource.TabServiceCallback<ArrayMap <String, Tab>>>()

    private val mTabServiceCallbackArgumentCaptor = argumentCaptor<TabRepositoryDataSource.TabServiceCallback<ArrayMap <String, Tab>>>()

    private val tabArgumentCaptor = argumentCaptor<Tab>()

    @Before
    fun setupTabsRepository() {
        MockitoAnnotations.initMocks(this)

        // Class under test
        mInMemoryTabRepository = InMemoryTabRepository.getInstance(mLocalSource)
    }

    @After
    fun destroyRepositoryInstance() {
        mInMemoryTabRepository!!.destroyInstance()
    }

    /**
     * Convenience method that issues two calls to the tabs repository
     */
    private fun twoLoadCallsToRepository(callback: TabRepository.LoadTabsCallback?) {
        // When tabs are requested from repository
        mInMemoryTabRepository?.getTabs(callback) // First call to API

        // Use the Mockito Captor to capture the callback
        verify<TabRepositoryDataSource>(mLocalSource).getAllTabs(mTabServiceCallbackArgumentCaptor.capture())

        // Trigger callback so tabs are cached
        mTabServiceCallbackArgumentCaptor.firstValue.onLoaded(TABS)

        mInMemoryTabRepository?.getTabs(callback) // Second call to API
    }


    @Test
    fun getTabs_repositoryCachesAfterFirstApiCall() {
        // Given a setup Captor to capture callbacks
        // When two calls are issued to the tabs repository
        twoLoadCallsToRepository(mLoadTabsCallback)

        // Then tabs should only be requested once from local DataSource
        verify<TabRepositoryDataSource>(mLocalSource).getAllTabs(any<TabRepositoryDataSource.TabServiceCallback<ArrayMap <String, Tab>>>())
    }

    @Test
    fun invalidateCache_DoesNotCallTheDataSource() {
        // Given a setup Captor to capture callbacks
        twoLoadCallsToRepository(mLoadTabsCallback)

        // When data refresh is requested
        mInMemoryTabRepository!!.refreshData()
        mInMemoryTabRepository!!.getTabs(mLoadTabsCallback) // Third call to API

        // The tabs were requested twice from the local DataSource (Caching on first and third call)
        verify<TabRepositoryDataSource>(mLocalSource, times(2)).getAllTabs(any<TabRepositoryDataSource.TabServiceCallback<ArrayMap <String, Tab>>>())
    }

    @Test
    fun getTabs_requestsAllTabsFromServiceApi() {
        // When tabs are requested from the tabs repository
        mInMemoryTabRepository!!.getTabs(mLoadTabsCallback)

        // Then tabs are loaded from the local DataSource
        verify<TabRepositoryDataSource>(mLocalSource).getAllTabs(any<TabRepositoryDataSource.TabServiceCallback<ArrayMap <String, Tab>>>())
    }

    @Test
    fun saveTab_savesTabToLocalDataSourceAndCaches() {
        // Given a stub tab
        val tab = Tab("3", "third")

        // When a tab is saved to the tabs repository
        mInMemoryTabRepository!!.saveTab(tab)

        // Check if the tabs is saved to local db and cached
        verify<TabRepositoryDataSource>(mLocalSource).saveTab(tab)
        assertThat(mInMemoryTabRepository!!.TAB_SERVICE_DATA.size, `is`(1))
    }

    @Test
    fun saveTab_savesTabToLocalDataSourceAndCheckCache() {
        // Given a stub tab
        val tab = Tab("3", "third")

        // When a tab is saved to the tabs repository
        mInMemoryTabRepository!!.saveTab(tab)

        // The asked tab should be retrieved from the cache first
        mInMemoryTabRepository!!.getTab("3", mGetTabCallback)
        verify(mGetTabCallback).onTabLoaded(tabArgumentCaptor!!.capture())
        assertThat(tabArgumentCaptor.firstValue, `is`(tab))

        //So it doesn't have to check the db
        verify<TabRepositoryDataSource>(mLocalSource, times(0)).getTab(eq("3"), any<TabRepositoryDataSource.TabServiceCallback<Tab>>())
    }

    @Test
    fun getTab_requestsSingleTabFromLocalDataSource() {
        // When a tab is requested from the tabs repository
        mInMemoryTabRepository!!.getTab("0", mGetTabCallback)

        // Then the tab is loaded from the LocalDataSource
        verify<TabRepositoryDataSource>(mLocalSource).getTab(eq("0"), any<TabRepositoryDataSource.TabServiceCallback<Tab>>())
    }

    @Test
    fun deleteTab_updatesTheCache() {
        val tab = Tab("3", "third")

        mInMemoryTabRepository!!.saveTab(tab)
        assertThat(mInMemoryTabRepository!!.TAB_SERVICE_DATA.containsKey(tab.groupId), `is`(true))

        mInMemoryTabRepository!!.deleteTab(tab.groupId)

        assertThat(mInMemoryTabRepository!!.TAB_SERVICE_DATA.values.size, `is`(0))
        verify<TabRepositoryDataSource>(mLocalSource).deleteTab(tab.groupId)
    }

    companion object {

        private val TABS = ArrayMap<String, Tab>()

        init {
            TABS["1"] = Tab("1", "first")
            TABS["2"] = Tab("2", "second")
        }
    }
}
