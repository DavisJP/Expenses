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
package com.davismiyashiro.expenses.model.localrepo

import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.runner.AndroidJUnit4
import android.support.v4.util.ArrayMap

import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.RepositoryDataSource

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Assert.fail

/**
 * Created by Davis on 10/10/2016.
 */
@RunWith(AndroidJUnit4::class)
class TabRepositoryDataSourceLocalTest {

    private var database: RepositoryDataSourceLocal? = null

    companion object {
        const val TAB_NAME = "JUnitTabName1"
        const val TAB_NAME_UPDATED = "JUnitTabName1Updated"

        const val PART_NAME = "JUnitPartName1"
        const val PART_EMAIL = "JUnitPartEmail1"
        const val PART_PHONE = "10000"
        const val PART_TABID = "JUnitPartTabId1"

        const val EXP_DESC = "JUnitExpDesc1"
        const val EXP_VALUE = 10.50
        const val EXP_TABID = "JUnitExpTabId1"
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        database = RepositoryDataSourceLocal.getInstance(getTargetContext())
    }

    @After
    @Throws(Exception::class)
    fun cleanUp() {
        database?.deleteAllTables()
    }

    @Test
    fun addTab_retrieveTab() {
        val tab = Tab(TAB_NAME)
        database?.saveTab(tab)

        database?.getTab(tab.groupId, object : RepositoryDataSource.TabServiceCallback<Tab> {
            override fun onLoaded(tabs: Tab) {
                assertThat(tabs, `is`(tab))
            }
        })
    }

    @Test
    fun shouldAddTabGetTabChangeNameAndDelete() {
        val tabTest = Tab(TAB_NAME)
        database?.saveTab(tabTest)
        database?.getAllTabs(object : RepositoryDataSource.TabServiceCallback<ArrayMap<String, Tab>> {
            override fun onLoaded(tabs: ArrayMap<String, Tab>) {
                assertNotNull(tabs)
                assertThat(tabs.size, `is`(1))

                for ((groupId, groupName) in tabs.values) {
                    if (groupName == TAB_NAME) {
                        assertTrue(groupName == TAB_NAME)
                        database?.getTab(groupId, object : RepositoryDataSource.TabServiceCallback<Tab> {
                            override fun onLoaded(tabs: Tab) {
                                val updatedRows = database!!.updateTabName(tabTest, TAB_NAME_UPDATED)
                                assertThat(updatedRows, `is`(1))
                            }
                        })

                        database?.getTab(tabTest.groupId, object : RepositoryDataSource.TabServiceCallback<Tab> {
                            override fun onLoaded(tabs: Tab) {
                                assertEquals(tabs.groupName, TAB_NAME_UPDATED)
                                database?.deleteTab(tabs.groupId)
                            }
                        })
                    } else {
                        assertNotEquals(groupName, TAB_NAME)
                    }
                }
            }
        })

        database?.getAllTabs(object : RepositoryDataSource.TabServiceCallback<ArrayMap<String, Tab>> {
            override fun onLoaded(tabs: ArrayMap<String, Tab>) {
                assertThat(tabs.size, `is`(0))
            }
        })
    }

    @Test
    fun shouldAddParticipantAndDelete() {
        database?.saveTab(Tab(PART_TABID, TAB_NAME))
        database?.saveParticipant(Participant(PART_NAME, PART_EMAIL, PART_PHONE, PART_TABID))
        database?.getAllParticipants(PART_TABID, object : RepositoryDataSource.ParticipantServiceCallback<ArrayMap<String, Participant>> {
            override fun onLoaded(participants: ArrayMap<String, Participant>) {
                assertNotNull(participants)
                assertThat(participants.values.size, `is`(1))

                for (part in participants.values) {
                    if (part.name == PART_NAME) {
                        assertTrue(part.name == PART_NAME)

                        database?.getParticipant(part.id, object : RepositoryDataSource.ParticipantServiceCallback<Participant> {
                            override fun onLoaded(participant: Participant) {
                                assertEquals(part.id, participant.id)
                                database?.deleteParticipant(participant)
                            }

                            override fun onDataNotAvailable() {
                                fail("Callback error")
                            }
                        })
                    } else {
                        assertNotEquals(part.name, PART_NAME)
                    }
                }
            }

            override fun onDataNotAvailable() {
                fail("Callback error")
            }
        })

        database?.getAllParticipants(PART_TABID, object : RepositoryDataSource.ParticipantServiceCallback<ArrayMap<String, Participant>> {
            override fun onLoaded(participants: ArrayMap<String, Participant>) {
                assertNotNull(participants)
                assertThat(participants.size, `is`(0))
            }

            override fun onDataNotAvailable() {
                fail("Callback error")
            }
        })
    }

    @Test
    fun shouldAddExpenseAndDelete() {
        database?.saveTab(Tab(EXP_TABID, TAB_NAME))
        database?.saveExpense(Expense(EXP_DESC, EXP_VALUE, EXP_TABID))
        database?.getAllExpenses(EXP_TABID, object : RepositoryDataSource.ExpenseServiceCallback<ArrayMap<String, Expense>> {
            override fun onLoaded(expenses: ArrayMap<String, Expense>) {
                assertNotNull(expenses)
                assertThat(expenses.size, `is`(1))

                for (exp in expenses.values) {
                    if (exp.description == EXP_DESC) {
                        assertTrue(exp.description == EXP_DESC)

                        database?.getExpense(exp.id, object : RepositoryDataSource.ExpenseServiceCallback<Expense> {
                            override fun onLoaded(expense: Expense) {
                                assertEquals(exp.id, expense.id)
                                database?.deleteExpense(expense)
                            }

                            override fun onDataNotAvailable() {
                                fail("Callback error")
                            }
                        })
                    } else {
                        assertNotEquals(exp.description, EXP_DESC)
                    }
                }
            }

            override fun onDataNotAvailable() {
                fail("Callback error")
            }
        })

        database?.getAllExpenses(EXP_TABID, object : RepositoryDataSource.ExpenseServiceCallback<ArrayMap<String, Expense>> {
            override fun onLoaded(expenses: ArrayMap<String, Expense>) {
                assertNotNull(expenses)
                assertThat(expenses.size, `is`(0))
            }

            override fun onDataNotAvailable() {
                fail("Callback error")
            }
        })
    }
}