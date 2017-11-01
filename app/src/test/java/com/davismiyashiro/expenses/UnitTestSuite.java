package com.davismiyashiro.expenses;

import com.davismiyashiro.expenses.view.managetabs.ChooseTabsPresenterImplTest;
import com.davismiyashiro.expenses.model.InMemoryTabsRepositoryTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Davis on 30/12/2015.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses ({
        InMemoryTabsRepositoryTest.class,
        ChooseTabsPresenterImplTest.class
})
public class UnitTestSuite {
}
