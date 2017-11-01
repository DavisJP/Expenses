package com.davismiyashiro.expenses.model.localrepo;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.ArrayMap;

import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.model.TabRepositoryDataSource;
import com.davismiyashiro.expenses.model.localrepo.TabRepositoryDataSourceLocal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by Davis on 10/10/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TabRepositoryDataSourceLocalTest {

    public static final String TAB_NAME = "JUnitTabName1";
    public static final String TAB_NAME_UPDATED = "JUnitTabName1Updated";

    public static final String PART_NAME = "JUnitPartName1";
    public static final String PART_EMAIL = "JUnitPartEmail1";
    public static final String PART_PHONE = "10000";
    public static final String PART_TABID = "JUnitPartTabId1";

    public static final String EXP_DESC = "JUnitExpDesc1";
    public static final double EXP_VALUE = 10.50;
    public static final String EXP_TABID = "JUnitExpTabId1";

    private TabRepositoryDataSourceLocal database;

    @Before
    public void setUp() throws Exception {
        //Drop the database
        //getTargetContext().deleteDatabase(TabsDbHelper.DATABASE_NAME);
        database = TabRepositoryDataSourceLocal.getInstance(InstrumentationRegistry.getTargetContext());
        database.deleteAllTables();
    }

    @After
    public void cleanUp() throws Exception {
        database.deleteAllTables();
    }

    @Test
    public void addTab_retrieveTab () {
        final Tab tab = new Tab(TAB_NAME);
        database.saveTab(tab);

        database.getTab(tab.getGroupId(), new TabRepositoryDataSource.TabServiceCallback<Tab>() {
            @Override
            public void onLoaded(Tab tabs) {
                assertThat(tabs, is(tab));
            }
        });
    }

    @Test
    public void shouldAddTabGetTabChangeNameAndDelete() throws Exception {
        final Tab tabTest = new Tab (TAB_NAME);
        database.saveTab(tabTest);
        database.getAllTabs(new TabRepositoryDataSource.TabServiceCallback<ArrayMap<String, Tab>>() {
            @Override
            public void onLoaded(ArrayMap<String, Tab> tabs) {
                //TABS_DATA = tabs;
                assertNotNull(tabs);
                assertThat(tabs.size(), is(1));

                for (Tab tab : tabs.values()) {
                    if (tab.getGroupName().equals(TAB_NAME)) {
                        assertTrue(tab.getGroupName().equals(TAB_NAME));
                        database.getTab(tab.getGroupId(), new TabRepositoryDataSource.TabServiceCallback<Tab>() {
                            @Override
                            public void onLoaded(Tab tabs) {
                                int updatedRows = database.updateTabName(tabTest, TAB_NAME_UPDATED);
                                assertThat(updatedRows, is(1));
                            }
                        });

                        database.getTab(tabTest.getGroupId(), new TabRepositoryDataSource.TabServiceCallback<Tab>() {
                            @Override
                            public void onLoaded(Tab tabs) {
                                assertEquals(tabs.getGroupName(), TAB_NAME_UPDATED);
                                database.deleteTab(tabs.getGroupId());
                            }
                        });

                    } else {
                        assertNotEquals(tab.getGroupName(),TAB_NAME);
                    }
                }
            }
        });

        database.getAllTabs(new TabRepositoryDataSource.TabServiceCallback<ArrayMap<String, Tab>>() {
            @Override
            public void onLoaded(ArrayMap<String, Tab> tabs) {
                assertThat(tabs.size(), is(0));
            }
        });
    }

//    java.lang.AssertionError: Callback error
//    at org.junit.Assert.fail(Assert.java:88)
//    at com.davismiyashiro.expenses.model.localrepo.TabRepositoryDataSourceLocalTest$4.onDataNotAvailable(TabRepositoryDataSourceLocalTest.java:139)
//    at com.davismiyashiro.expenses.model.localrepo.TabRepositoryDataSourceLocal.getParticipant(TabRepositoryDataSourceLocal.java:151)
//    @Test
//    public void shouldAddParticipantGetByTabIdAndDelete() throws Exception {
//
//        TabRepositoryDataSource.ParticipantServiceCallback callback = mock(TabRepositoryDataSource.ParticipantServiceCallback.class);
//
//        final Participant participantTest = new Participant(PART_NAME, PART_EMAIL, PART_PHONE, PART_TABID);
//        database.saveParticipant(participantTest);
//
//        database.getParticipant(participantTest.getId(), new TabRepositoryDataSource.ParticipantServiceCallback<Participant>() {
//            @Override
//            public void onLoaded(Participant participant) {
//                assertNotNull(participant);
//                assertThat(participant, is(participantTest));
//                database.deleteParticipant(participant);
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                fail("Callback error");
//            }
//        });
//
//        database.getParticipant(participantTest.getId(), callback);
//        verify(callback).onDataNotAvailable();
//        verify(callback, never()).onLoaded(participantTest);
//
//    }


//    java.lang.AssertionError:
//    Expected: is <1>
//    but: was <0>
//    @Test
//    public void shouldAddParticipantAndDelete() throws Exception {
//        database.saveParticipant(new Participant(PART_NAME, PART_EMAIL, PART_PHONE, PART_TABID));
//        database.getAllParticipants(PART_TABID, new TabRepositoryDataSource.ParticipantServiceCallback<ArrayMap<String, Participant>>() {
//            @Override
//            public void onLoaded(ArrayMap<String, Participant> participants) {
//                assertNotNull(participants);
//                assertThat(participants.size(), is(1));
//
//                for (final Participant part : participants.values()) {
//                    if (part.getName().equals(PART_NAME)) {
//                        assertTrue(part.getName().equals(PART_NAME));
//
//                        database.getParticipant(part.getId(), new TabRepositoryDataSource.ParticipantServiceCallback<Participant>() {
//                            @Override
//                            public void onLoaded(Participant participant) {
//                                assertEquals(part.getId(),participant.getId());
//                                database.deleteParticipant(participant);
//                            }
//
//                            @Override
//                            public void onDataNotAvailable() {
//                                fail("Callback error");
//                            }
//                        });
//                    } else {
//                        assertNotEquals(part.getName(),PART_NAME);
//                    }
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                fail("Callback error");
//            }
//        });
//
//        database.getAllParticipants(PART_TABID, new TabRepositoryDataSource.ParticipantServiceCallback<ArrayMap<String, Participant>>() {
//            @Override
//            public void onLoaded(ArrayMap<String, Participant> participants) {
//                assertNotNull(participants);
//                assertThat(participants.size(), is(0));
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                fail("Callback error");
//            }
//        });
//    }


//    java.lang.AssertionError:
//    Expected: is <1>
//    but: was <0>
//    @Test
//    public void shouldAddExpenseAndDelete() throws Exception {
//        database.saveExpense(new Expense(EXP_DESC, EXP_VALUE, EXP_TABID));
//        database.getAllExpenses(EXP_TABID, new TabRepositoryDataSource.ExpenseServiceCallback<ArrayMap<String, Expense>>() {
//            @Override
//            public void onLoaded(ArrayMap<String, Expense> expenses) {
//                assertNotNull(expenses);
//                assertThat(expenses.size(), is(1));
//
//                for (final Expense exp : expenses.values()) {
//                    if (exp.getDescription().equals(EXP_DESC)) {
//                        assertTrue(exp.getDescription().equals(EXP_DESC));
//
//                        database.getExpense(exp.getId(), new TabRepositoryDataSource.ExpenseServiceCallback<Expense>() {
//                            @Override
//                            public void onLoaded(Expense expense) {
//                                assertEquals(exp.getId(), expense.getId());
//                                database.deleteExpense(expense);
//                            }
//
//                            @Override
//                            public void onDataNotAvailable() {
//                                fail("Callback error");
//                            }
//                        });
//                    } else {
//                        assertNotEquals(exp.getDescription(),EXP_DESC);
//                    }
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                fail("Callback error");
//            }
//        });
//
//        database.getAllExpenses(EXP_TABID, new TabRepositoryDataSource.ExpenseServiceCallback<ArrayMap<String, Expense>>() {
//            @Override
//            public void onLoaded(ArrayMap<String, Expense> expenses) {
//                assertNotNull(expenses);
//                assertThat(expenses.size(), is(0));
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                fail("Callback error");
//            }
//        });
//
//    }
}