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
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.injection.App
import com.davismiyashiro.expenses.view.addexpense.ExpenseActivity

import java.util.ArrayList

import javax.inject.Inject

import timber.log.Timber

class ExpenseFragment : Fragment(),
        ExpenseInterfaces.ExpenseView,
        ExpenseRecyclerViewAdapter.OnExpenseFragmentInteractionListener {

    @Inject
    lateinit var mPresenter: ExpenseInterfaces.UserActionsListener

    private lateinit var mRecyclerAdapter: ExpenseRecyclerViewAdapter
    private lateinit var mTab: Tab
    private lateinit var recyclerView: RecyclerView
    // TODO: Set Layout of RecyclerView
    private val mColumnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")

        if (arguments != null && arguments is Bundle) {
            mTab = (arguments as Bundle).getParcelable(TAB_PARAM)
        }
        mRecyclerAdapter = ExpenseRecyclerViewAdapter(ArrayList(), this, activity as Activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView")

        val rootView = inflater.inflate(R.layout.fragment_expense, container, false)

        recyclerView = rootView.findViewById<View>(R.id.exp_list) as RecyclerView

        if (mColumnCount <= 1) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            recyclerView.layoutManager = GridLayoutManager(context, mColumnCount)
        }

        recyclerView.adapter = mRecyclerAdapter
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity?.application as App).component.inject(this)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.setExpenseView(this)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")

        mPresenter.loadExpenses(mTab)
    }

    override fun showExpenses(expenses: MutableList<Expense>) {
        mRecyclerAdapter.replaceData(expenses)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Timber.d("onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("onDetach")
    }

    override fun onExpenseFragmentInteraction(expense: Expense) {
        activity?.applicationContext?.let {
            startActivity(ExpenseActivity.newIntent(it, mTab, expense))
        }
    }

    override fun onExpenseFragmentLongClick(expense: Expense) {
        mPresenter.removeExpense(expense)

        mRecyclerAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val TAB_PARAM = "com.davismiyashiro.expenses.view.opentab.ExpenseFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param tab Parameter 1.
         * @return A new instance of fragment ExpenseFragment.
         */
        fun newInstance(tab: Tab): ExpenseFragment {
            val fragment = ExpenseFragment()
            val args = Bundle()
            args.putParcelable(TAB_PARAM, tab)
            fragment.arguments = args
            return fragment
        }
    }
} // Required empty public constructor
