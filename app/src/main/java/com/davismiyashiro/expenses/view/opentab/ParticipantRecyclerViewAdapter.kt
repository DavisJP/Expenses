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
import com.davismiyashiro.expenses.view.opentab.ParticipantFragment.OnParticipantListFragmentInteractionListener

/**
 * [RecyclerView.Adapter] that can display an item and makes a call to the
 * specified [OnParticipantListFragmentInteractionListener].
 */
class ParticipantRecyclerViewAdapter(private var mParticipants: MutableList<Participant>,
                                     private val mListener: OnParticipantListFragmentInteractionListener?,
                                     private val mContext: Context) : RecyclerView.Adapter<ParticipantRecyclerViewAdapter.ViewHolder>() {

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

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
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

                    mListener!!.onParticipantListFragmentLongClick(getItem(position))
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
}
