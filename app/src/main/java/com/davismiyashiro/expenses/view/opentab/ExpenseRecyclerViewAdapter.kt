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

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.datatypes.Expense

/**
 * RecyclerView Adapter for ExpenseFragment
 */

class ExpenseRecyclerViewAdapter(
    private var mExpenses: MutableList<Expense>,
    private val mListener: OnExpenseFragmentInteractionListener?,
    private val mContext: Context
) : RecyclerView.Adapter<ExpenseRecyclerViewAdapter.ExpenseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(mExpenses[position])
    }

    fun replaceData(expenses: MutableList<Expense>) {
        setExpenses(expenses)
        notifyDataSetChanged()
    }

    private fun setExpenses(expenses: MutableList<Expense>) {
        mExpenses = expenses
    }

    override fun getItemCount(): Int {
        return mExpenses.size
    }

    fun getItem(position: Int): Expense {
        return mExpenses[position]
    }

    fun removeItemAtPosition(position: Int) {
        mExpenses.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mIdView: TextView = itemView.findViewById(R.id.list_item_expense_title)
        private val mContentView: TextView = itemView.findViewById(R.id.list_item_expense_participants)
        private lateinit var mExpense: Expense

        init {
            itemView.setOnClickListener {
                mListener?.onExpenseFragmentInteraction(mExpense)
            }
            itemView.setOnLongClickListener {
                val alertDialogBuilder = AlertDialog.Builder(mContext)
                alertDialogBuilder.setTitle("Delete Expense")
                alertDialogBuilder.setMessage("Are you sure you want to delete this Expense? This will also remove the receipts.")

                alertDialogBuilder.setPositiveButton("YES") { arg0, _ ->
                    val position = adapterPosition

                    mListener?.onExpenseFragmentLongClick(getItem(position))
                    removeItemAtPosition(position)
                    arg0.dismiss()
                }

                alertDialogBuilder.setNegativeButton("NO") { dialog, _ -> dialog.cancel() }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()

                true
            }
        }

        fun bind(expense: Expense) {
            mExpense = expense
            mIdView.text = mExpense.description
            mContentView.text = mExpense.value.toString()
        }

        override fun toString(): String {
            return super.toString() + " '" + mIdView.text + "'"
        }
    }

    /**
     * Best practices suggests that Fragments should have interfaces to be implemented by the Activities
     * that contain this Fragment. Although as functionality grows one presenter gets overwhelmed, so
     * in order to provide a cleaner separation of concerns, a presenter for each Fragment was used.
     *
     * More info on SharedPresenters/ViewModels:
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnExpenseFragmentInteractionListener {
        fun onExpenseFragmentInteraction(expense: Expense)
        fun onExpenseFragmentLongClick(expense: Expense)
    }
}
