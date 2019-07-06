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
package com.davismiyashiro.expenses.view.opentab

import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.Repository
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

private val TAB = Tab("1", "first")
private val PARTICIPANTS = mutableListOf(
        Participant("name", "email", "number", "1"),
        Participant("name2", "email2", "number2", "1")
)

class ParticipantFragmentPresenterTest {

    private val repository: Repository = mock()
    private val loadParticipantsCallback = argumentCaptor<Repository.LoadParticipantsCallback>()
    private val view: ParticipantInterfaces.ParticipantView = mock()

    private lateinit var presenter: ParticipantFragmentPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = ParticipantFragmentPresenter(repository)
        presenter.setParticipantView(view)
    }

    @Test
    fun loadParticipants_withParticipant_showParticipants() {
        presenter.loadParticipants(TAB)

        verify(repository).getParticipants(eq(TAB.groupId), loadParticipantsCallback.capture())
        loadParticipantsCallback.firstValue.onParticipantsLoaded(eq(PARTICIPANTS))

        verify(view).showParticipants(PARTICIPANTS)
    }

    @Test
    fun loadParticipants_noParticipants_showEmptyParticipants() {
        presenter.loadParticipants(TAB)

        verify(repository).getParticipants(eq(TAB.groupId), loadParticipantsCallback.capture())
        loadParticipantsCallback.firstValue.onParticipantsLoaded(eq(mutableListOf()))

        verify(view).showParticipants(eq(mutableListOf()))
    }

    @Test
    fun removeParticipant_oneParticipnat_deleteParticipants() {
        presenter.removeParticipant(PARTICIPANTS[0])

        verify(repository).deleteParticipant(PARTICIPANTS[0])
    }
}