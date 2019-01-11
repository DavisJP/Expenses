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
package com.davismiyashiro.expenses.model

import android.content.Context
import android.support.v4.util.ArrayMap

import com.davismiyashiro.expenses.datatypes.Tab
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import kotlin.test.assertEquals

/**
 * Class that test if InMemoryTabsRepositoryTest caches and process localData requests
 */
class InMemoryTabsRepositoryTest {

    lateinit var mTabRepositoryImpl: RepositoryImpl

    private val mContext = mock<Context>()

    private val mLocalSource = mock<RepositoryDataSource>()

    private val mGetTabCallback = mock<Repository.GetTabCallback>()

    private val mLoadTabsCallback = mock<Repository.LoadTabsCallback>()

    private val mTabServiceCallback = mock<RepositoryDataSource.TabServiceCallback<ArrayMap <String, Tab>>>()

    private val mTabServiceCallbackArgumentCaptor = argumentCaptor<RepositoryDataSource.TabServiceCallback<ArrayMap <String, Tab>>>()

    private val tabArgumentCaptor = argumentCaptor<Tab>()

    @Before
    fun setupTabsRepository() {
        MockitoAnnotations.initMocks(this)

        // Class under test
        mTabRepositoryImpl = RepositoryImpl.getInstance(mLocalSource)
    }

    @After
    fun destroyRepositoryInstance() {
        mTabRepositoryImpl.destroyInstance()
    }

    /**
     * Convenience method that issues two calls to the tabs repository
     */
    private fun twoLoadCallsToRepository(callback: Repository.LoadTabsCallback) {
        // When tabs are requested from repository
        mTabRepositoryImpl.getTabs(callback) // First call to API

        // Use the Mockito Captor to capture the callback
        verify<RepositoryDataSource>(mLocalSource).getAllTabs(mTabServiceCallbackArgumentCaptor.capture())

        // Trigger callback so tabs are cached
        mTabServiceCallbackArgumentCaptor.firstValue.onLoaded(TABS)

        mTabRepositoryImpl.getTabs(callback) // Second call to API
    }

    @Test
    fun getTabs_repositoryCachesAfterFirstApiCall() {
        // Given a setup Captor to capture callbacks
        // When two calls are issued to the tabs repository
        twoLoadCallsToRepository(mLoadTabsCallback)

        // Then tabs should only be requested once from local DataSource
        verify<RepositoryDataSource>(mLocalSource).getAllTabs(any())
    }

    @Test
    fun invalidateCache_DoesNotCallTheDataSource() {
        // Given a setup Captor to capture callbacks
        twoLoadCallsToRepository(mLoadTabsCallback)

        // When data refresh is requested
        mTabRepositoryImpl.refreshData()
        mTabRepositoryImpl.getTabs(mLoadTabsCallback) // Third call to API

        // The tabs were requested twice from the local DataSource (Caching on first and third call)
        verify(mLocalSource, times(2)).getAllTabs(any())
    }

    @Test
    fun getTabs_requestsAllTabsFromServiceApi() {
        // When tabs are requested from the tabs repository
        mTabRepositoryImpl.getTabs(mLoadTabsCallback)

        // Then tabs are loaded from the local DataSource
        verify(mLocalSource).getAllTabs(any())
    }

    @Test
    fun saveTab_savesTabToLocalDataSourceAndCaches() {
        // Given a stub tab
        val tab = Tab("3", "third")

        // When a tab is saved to the tabs repository
        mTabRepositoryImpl.saveTab(tab)

        // Check if the tabs is saved to local db and cached
        verify(mLocalSource).saveTab(tab)
        assertThat(mTabRepositoryImpl.TAB_SERVICE_DATA?.size, `is`(1))
    }

    @Test
    fun saveTab_savesTabToLocalDataSourceAndCheckCache() {
        // Given a stub tab
        val tab = Tab("3", "third")

        // When a tab is saved to the tabs repository
        mTabRepositoryImpl.saveTab(tab)

        // The asked tab should be retrieved from the cache first
        mTabRepositoryImpl.getTab("3", mGetTabCallback)
        verify(mGetTabCallback).onTabLoaded(tabArgumentCaptor.capture())
        assertThat(tabArgumentCaptor.firstValue, `is`(tab))

        // So it doesn't have to check the db
        verify<RepositoryDataSource>(mLocalSource, times(0)).getTab(eq("3"), any())
    }

    @Test
    fun getTab_requestsSingleTabFromLocalDataSource() {
        // When a tab is requested from the tabs repository
        mTabRepositoryImpl.getTab("0", mGetTabCallback)

        // Then the tab is loaded from the LocalDataSource
        verify(mLocalSource).getTab(eq("0"), any())
    }

    @Test
    fun deleteTab_updatesTheCache() {
        val tab = Tab("3", "third")

        mTabRepositoryImpl.saveTab(tab)
        assertThat(mTabRepositoryImpl.TAB_SERVICE_DATA?.containsKey(tab.groupId), `is`(true))

        mTabRepositoryImpl.deleteTab(tab.groupId)

        assertThat(mTabRepositoryImpl.TAB_SERVICE_DATA?.values?.size, `is`(0))
        verify<RepositoryDataSource>(mLocalSource).deleteTab(tab.groupId)
    }

    companion object {

        private val TABS = ArrayMap<String, Tab>()

        init {
            TABS["1"] = Tab("1", "first")
            TABS["2"] = Tab("2", "second")
        }
    }
}
