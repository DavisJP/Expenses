package com.davismiyashiro.expenses.model;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.davismiyashiro.expenses.datatypes.Tab;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Class that test if InMemoryTabsRepositoryTest caches and process localData requests
 */
public class InMemoryTabsRepositoryTest {

    private static ArrayMap<String, Tab> TABS = new ArrayMap<String, Tab>();
    static {
        TABS.put("1", new Tab("1", "first"));
        TABS.put("2", new Tab("2", "second"));
    }

    private InMemoryTabRepository mInMemoryTabRepository;


    @Mock
    private Context mContext;

    @Mock
    private TabRepositoryDataSource mLocalSource;

    @Mock
    private TabRepository.GetTabCallback mGetTabCallback;

    @Mock
    private TabRepository.LoadTabsCallback mLoadTabsCallback;

    @Mock
    private TabRepositoryDataSource.TabServiceCallback mTabServiceCallback;

    @Captor
    private ArgumentCaptor<TabRepositoryDataSource.TabServiceCallback> mTabServiceCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<Tab> tabArgumentCaptor;

    @Before
    public void setupTabsRepository() {
        MockitoAnnotations.initMocks(this);

        // Class under test
        mInMemoryTabRepository = InMemoryTabRepository.getInstance(mLocalSource);
    }

    @After
    public void destroyRepositoryInstance() {
        mInMemoryTabRepository.destroyInstance();
    }

    /**
     * Convenience method that issues two calls to the tabs repository
     */
    private void twoLoadCallsToRepository(TabRepository.LoadTabsCallback callback) {
        // When tabs are requested from repository
        mInMemoryTabRepository.getTabs(callback); // First call to API

        // Use the Mockito Captor to capture the callback
        verify(mLocalSource).getAllTabs(mTabServiceCallbackArgumentCaptor.capture());

        // Trigger callback so tabs are cached
        mTabServiceCallbackArgumentCaptor.getValue().onLoaded(TABS);

        mInMemoryTabRepository.getTabs(callback); // Second call to API
    }


    @Test
    public void getTabs_repositoryCachesAfterFirstApiCall() {
        // Given a setup Captor to capture callbacks
        // When two calls are issued to the tabs repository
        twoLoadCallsToRepository(mLoadTabsCallback);

        // Then tabs should only be requested once from local DataSource
        verify(mLocalSource).getAllTabs(any(TabRepositoryDataSource.TabServiceCallback.class));
    }

    @Test
    public void invalidateCache_DoesNotCallTheDataSource() {
        // Given a setup Captor to capture callbacks
        twoLoadCallsToRepository(mLoadTabsCallback);

        // When data refresh is requested
        mInMemoryTabRepository.refreshData();
        mInMemoryTabRepository.getTabs(mLoadTabsCallback); // Third call to API

        // The tabs were requested twice from the local DataSource (Caching on first and third call)
        verify(mLocalSource, times(2)).getAllTabs(any(TabRepositoryDataSource.TabServiceCallback.class));
    }

    @Test
    public void getTabs_requestsAllTabsFromServiceApi() {
        // When tabs are requested from the tabs repository
        mInMemoryTabRepository.getTabs(mLoadTabsCallback);

        // Then tabs are loaded from the local DataSource
        verify(mLocalSource).getAllTabs(any(TabRepositoryDataSource.TabServiceCallback.class));
    }

    @Test
    public void saveTab_savesTabToLocalDataSourceAndCaches() {
        // Given a stub tab
        Tab tab = new Tab("3", "third");

        // When a tab is saved to the tabs repository
        mInMemoryTabRepository.saveTab(tab);

        // Check if the tabs is saved to local db and cached
        verify(mLocalSource).saveTab(tab);
        assertThat(mInMemoryTabRepository.TAB_SERVICE_DATA.size(), is(1));
    }

    @Test
    public void saveTab_savesTabToLocalDataSourceAndCheckCache() {
        // Given a stub tab
        Tab tab = new Tab("3", "third");

        // When a tab is saved to the tabs repository
        mInMemoryTabRepository.saveTab(tab);

        // The asked tab should be retrieved from the cache first
        mInMemoryTabRepository.getTab("3", mGetTabCallback);
        verify(mGetTabCallback).onTabLoaded(tabArgumentCaptor.capture());
        assertThat(tabArgumentCaptor.getValue(), is(tab));

        //So it doesn't have to check the db
        verify(mLocalSource, times(0)).getTab(eq("3"), any(TabRepositoryDataSource.TabServiceCallback.class));
    }

    @Test
    public void getTab_requestsSingleTabFromLocalDataSource() {
        // When a tab is requested from the tabs repository
        mInMemoryTabRepository.getTab("0", mGetTabCallback);

        // Then the tab is loaded from the LocalDataSource
        verify(mLocalSource).getTab(eq("0"), any(TabRepositoryDataSource.TabServiceCallback.class));
    }

    @Test
    public void deleteTab_updatesTheCache () {
        Tab tab = new Tab("3", "third");

        mInMemoryTabRepository.saveTab(tab);
        assertThat(mInMemoryTabRepository.TAB_SERVICE_DATA.containsKey(tab.getGroupId()), is(true));

        mInMemoryTabRepository.deleteTab(tab.getGroupId());

        assertThat(mInMemoryTabRepository.TAB_SERVICE_DATA.values().size(), is(0));
        verify(mLocalSource).deleteTab(tab.getGroupId());
    }
}
