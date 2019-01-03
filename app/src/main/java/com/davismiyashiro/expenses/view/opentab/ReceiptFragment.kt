package com.davismiyashiro.expenses.view.opentab

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView

import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.datatypes.ReceiptItem
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.injection.App

import java.util.ArrayList

import javax.inject.Inject

import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ReceiptFragment.OnReceiptFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ReceiptFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReceiptFragment : Fragment(), ReceiptInterfaces.ReceiptView {
    private var mListener: OnReceiptFragmentInteractionListener? = null

    @Inject
    internal lateinit var mPresenter: ReceiptInterfaces.UserActionsListener

    internal lateinit var expandableListView: ExpandableListView
    internal lateinit var expandableListAdapter: CustomExpandableListAdapter
    internal var expandableListParticipantIds: List<String> = ArrayList()
    internal var expandableMapReceiptItemList = ArrayMap<String, MutableList<ReceiptItem>>()

    private var mTab: Tab? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mTab = arguments.getParcelable(RECEIPT_PARAM1)
        }
        expandableListAdapter = CustomExpandableListAdapter(context, expandableListParticipantIds, expandableMapReceiptItemList)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_receipt, container, false)

        expandableListView = rootView.findViewById<View>(R.id.expandable_list_view) as ExpandableListView
        expandableListView.setOnGroupExpandListener { groupPosition ->

        }

        expandableListView.setOnGroupCollapseListener { groupPosition ->

        }

        expandableListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id -> false }

        expandableListView.setAdapter(expandableListAdapter)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")

        mPresenter.loadReceipItems(mTab!!)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.setReceiptView(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity.application as App).component.inject(this)
    }

    override fun showReceiptItems(items: ArrayMap<String, MutableList<ReceiptItem>>) {
        expandableMapReceiptItemList = items
        expandableListParticipantIds = ArrayList(expandableMapReceiptItemList.keys)

        expandableListAdapter.replaceData(items)
    }

    override fun refreshReceiptItemsAdapter() {
        expandableListAdapter.notifyDataSetChanged()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnReceiptFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnReceiptFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
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
    interface OnReceiptFragmentInteractionListener {
        fun onReceiptFragmentInteraction(receiptItem: ReceiptItem)
    }

    companion object {

        private val RECEIPT_PARAM1 = "com.davismiyashiro.expenses.view.fragments.receiptfragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param tab Parameter 1.
         * @return A new instance of fragment ReceiptFragment.
         */
        fun newInstance(tab: Tab): ReceiptFragment {
            val fragment = ReceiptFragment()
            val args = Bundle()
            args.putParcelable(RECEIPT_PARAM1, tab)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
