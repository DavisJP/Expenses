package com.davismiyashiro.expenses.view.opentab

import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.Repository
import com.nhaarman.mockitokotlin2.*
import org.junit.Test

import org.junit.Before
import org.mockito.MockitoAnnotations

private val TAB = Tab("1", "first")
private val PARTICIPANTS = mutableListOf(
        Participant("name","email", "number", "1"),
        Participant("name2","email2", "number2", "1")
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
        loadParticipantsCallback.firstValue.onParticipantsLoaded(eq(mutableListOf<Participant>()))

        verify(view).showParticipants(eq(mutableListOf<Participant>()))
    }

    @Test
    fun removeParticipant_oneParticipnat_deleteParticipants() {
        presenter.removeParticipant(PARTICIPANTS[0])

        verify(repository).deleteParticipant(PARTICIPANTS[0])
    }
}