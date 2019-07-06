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

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.injection.App
import com.davismiyashiro.expenses.view.addparticipant.ParticipantActivity

import java.util.ArrayList

import javax.inject.Inject

import timber.log.Timber

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnParticipantListFragmentInteractionListener?]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class ParticipantFragment : Fragment(),
        ParticipantInterfaces.ParticipantView,
        ParticipantRecyclerViewAdapter.OnParticipantListFragmentInteractionListener {

    @Inject
    internal lateinit var mPresenter: ParticipantInterfaces.UserActionsListener

    private lateinit var mTab: Tab
    private lateinit var recyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: ParticipantRecyclerViewAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")

        if (arguments != null && arguments is Bundle) {
            mTab = (arguments as Bundle).getParcelable(TAB_PARAM)
        }

        mRecyclerAdapter = ParticipantRecyclerViewAdapter(ArrayList(), this, activity as Activity)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity?.application as App).component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView")

        val view = inflater.inflate(R.layout.fragment_participant_list, container, false)

        recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = mRecyclerAdapter
        return view
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")

        mPresenter.loadParticipants(mTab)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.setParticipantView(this)
    }

    override fun showParticipants(participants: MutableList<Participant>) {
        mRecyclerAdapter.replaceData(participants)
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("onDetach")
    }

    override fun onParticipantListFragmentInteraction(item: Participant) {
        activity?.applicationContext?.let {
            startActivity(ParticipantActivity.newInstance(it , mTab, item))
        }
    }

    override fun onParticipantListFragmentLongClick(item: Participant) {
        mPresenter.removeParticipant(item)
    }

    companion object {

        private const val TAB_PARAM = "com.davismiyashiro.expenses.view.opentab.ParticipantFragment"

        fun newInstance(tab: Tab): ParticipantFragment {
            val fragment = ParticipantFragment()
            val args = Bundle()
            args.putParcelable(TAB_PARAM, tab)
            fragment.arguments = args
            return fragment
        }
    }
}
