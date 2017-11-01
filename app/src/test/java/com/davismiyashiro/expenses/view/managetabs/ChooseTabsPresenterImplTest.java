package com.davismiyashiro.expenses.view.managetabs;

import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.model.TabRepository;
import com.davismiyashiro.expenses.model.TabRepository.LoadTabsCallback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by Davis on 01/09/2016.
 */
public class ChooseTabsPresenterImplTest {

    private static List<Tab> TABS = new ArrayList<>(
            Arrays.asList(new Tab("1", "first"),
                    new Tab("2", "second")
            )
    );

    @Mock
    private TabRepository mTabRepository;

    @Mock
    private ChooseTabsInterfaces.View mChooseTabsView;

    @Mock
    private TabRepository.GetTabCallback mGetTabCallback;

    @Captor
    private ArgumentCaptor<LoadTabsCallback> mLoadTabsCallbackCaptor;

    @Captor
    private ArgumentCaptor<TabRepository.GetTabCallback> mGetTabCallbackCaptor;

    private ChooseTabsPresenterImpl mChooseTabsPresenter;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        mChooseTabsPresenter = new ChooseTabsPresenterImpl(mChooseTabsView, mTabRepository);
    }

//    @After
//    public void tearDown() throws Exception {
//
//    }

    @Test
    public void testAddTab() throws Exception {
        mChooseTabsPresenter.addTab();

        verify(mChooseTabsView).showAddTab();
    }

    @Test
    public void testLoadTabs() throws Exception {
        mChooseTabsPresenter.loadTabs(true);

        verify(mTabRepository).getTabs(mLoadTabsCallbackCaptor.capture());

        mLoadTabsCallbackCaptor.getValue().onTabsLoaded(TABS);

        verify(mChooseTabsView).setProgressIndicator(false);
        verify(mChooseTabsView).showTabs(TABS);
    }

    @Test
    public void testLoadNoTabs() throws Exception {
        mChooseTabsPresenter.loadTabs(true);

        verify(mTabRepository).getTabs(mLoadTabsCallbackCaptor.capture());

        mLoadTabsCallbackCaptor.getValue().onTabsLoaded(new ArrayList<Tab>());

        verify(mChooseTabsView).setProgressIndicator(false);
        verify(mChooseTabsView).showNoTabs();
    }

    @Test
    public void testOpenTab() throws Exception {
        Tab tab = new Tab("1", "first");

        mChooseTabsPresenter.openTab("1");

        verify(mTabRepository).getTab((eq(tab.getGroupId())), mGetTabCallbackCaptor.capture());

        mGetTabCallbackCaptor.getValue().onTabLoaded(tab); // Trigger callback

        verify(mChooseTabsView).showTabDetailUi(tab);
    }
}