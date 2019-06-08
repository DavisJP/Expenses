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
import com.davismiyashiro.expenses.datatypes.Participant

/**
 * [RecyclerView.Adapter] that can display an item and makes a call to the
 * specified [OnParticipantListFragmentInteractionListener].
 */
class ParticipantRecyclerViewAdapter(
    private var mParticipants: MutableList<Participant>,
    private val mListener: OnParticipantListFragmentInteractionListener?,
    private val mContext: Context
) : RecyclerView.Adapter<ParticipantRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_participant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mParticipants[position]
        holder.mIdView.text = mParticipants[position].name
        holder.mContentView.text = mParticipants[position].email
    }

    fun replaceData(participants: MutableList<Participant>) {
        setParticipants(participants)
        notifyDataSetChanged()
    }

    private fun setParticipants(participants: MutableList<Participant>) {
        mParticipants = participants
    }

    override fun getItemCount(): Int {
        return mParticipants.size
    }

    fun getItem(position: Int): Participant {
        return mParticipants[position]
    }

    fun removeItemAtPosition(position: Int) {
        mParticipants.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView
        val mContentView: TextView
        lateinit var mItem: Participant

        init {
            mIdView = mView.findViewById<View>(R.id.list_item_title) as TextView
            mContentView = mView.findViewById<View>(R.id.list_item_details) as TextView

            mView.setOnClickListener { v ->
                mListener?.onParticipantListFragmentInteraction(mItem)
            }

            mView.setOnLongClickListener { v ->
                val alertDialogBuilder = AlertDialog.Builder(mContext)
                alertDialogBuilder.setTitle("Delete Participant")
                alertDialogBuilder.setMessage("Are you sure you want to delete this Participant ?")

                alertDialogBuilder.setPositiveButton("YES") { arg0, arg1 ->
                    val position = adapterPosition

                    mListener?.onParticipantListFragmentLongClick(getItem(position))
                    removeItemAtPosition(position)
                    arg0.dismiss()
                }

                alertDialogBuilder.setNegativeButton("NO") { dialog, which -> dialog.cancel() }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()

                true
            }
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
    interface OnParticipantListFragmentInteractionListener {
        fun onParticipantListFragmentInteraction(item: Participant)

        fun onParticipantListFragmentLongClick(item: Participant)
    }
}
