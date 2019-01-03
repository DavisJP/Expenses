package com.davismiyashiro.expenses.view.opentab

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

import java.util.ArrayList

import javax.inject.Inject

import timber.log.Timber

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnParticipantListFragmentInteractionListener]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class ParticipantFragment : Fragment(), ParticipantInterfaces.ParticipantView {
    private var mListener: OnParticipantListFragmentInteractionListener? = null

    @Inject
    internal lateinit var mPresenter: ParticipantInterfaces.UserActionsListener

    private var mTab: Tab? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: ParticipantRecyclerViewAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Timber.d("onAttach")
        if (context is OnParticipantListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnParticipantListFragmentInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")

        if (arguments != null) {
            mTab = arguments.getParcelable(TAB_PARAM)
        }

        mRecyclerAdapter = ParticipantRecyclerViewAdapter(ArrayList(), mListener, activity)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity.application as App).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView")

        val view = inflater!!.inflate(R.layout.fragment_participant_list, container, false)

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

        mPresenter.loadParticipants(mTab!!)
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
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnParticipantListFragmentInteractionListener {
        fun onParticipantListFragmentInteraction(item: Participant)

        fun onParticipantListFragmentLongClick(item: Participant)
    }

    companion object {

        private val TAB_PARAM = "com.davismiyashiro.expenses.view.opentab.ParticipantFragment"

        fun newInstance(tab: Tab): ParticipantFragment {
            val fragment = ParticipantFragment()
            val args = Bundle()
            args.putParcelable(TAB_PARAM, tab)
            fragment.arguments = args
            return fragment
        }
    }
}
