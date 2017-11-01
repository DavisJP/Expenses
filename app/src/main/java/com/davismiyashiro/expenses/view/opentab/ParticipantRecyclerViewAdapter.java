package com.davismiyashiro.expenses.view.opentab;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davismiyashiro.expenses.R;
import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.view.opentab.ParticipantFragment.OnParticipantListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display an item and makes a call to the
 * specified {@link OnParticipantListFragmentInteractionListener}.
 */
public class ParticipantRecyclerViewAdapter extends RecyclerView.Adapter<ParticipantRecyclerViewAdapter.ViewHolder> {

    private List<Participant> mParticipants;
    private final OnParticipantListFragmentInteractionListener mListener;
    private Context mContext;

    public ParticipantRecyclerViewAdapter(List<Participant> participants, OnParticipantListFragmentInteractionListener listener, Context context) {
        mParticipants = participants;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_participant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mParticipants.get(position);
        holder.mIdView.setText(mParticipants.get(position).getName());
        holder.mContentView.setText(mParticipants.get(position).getEmail());

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onParticipantListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
        //return true to indicate that you have handled the event and it should stop here; return false if you have not handled it and/or the event should continue to any other on-click listeners.
//        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                return true;
//            }
//        });
    }

    public void replaceData (List<Participant> participants) {
        setParticipants(participants);
        notifyDataSetChanged();
    }

    private void setParticipants (List<Participant> participants) {
        mParticipants = participants;
    }

    @Override
    public int getItemCount() {
        return mParticipants.size();
    }

    public Participant getItem(int position) {
        return mParticipants.get(position);
    }

    public void removeItemAtPosition(int position) {
        mParticipants.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        Participant mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.list_item_title);
            mContentView = (TextView) view.findViewById(R.id.list_item_details);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onParticipantListFragmentInteraction(mItem);
                    }
                }
            });

            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setTitle("Delete Participant");
                    alertDialogBuilder.setMessage("Are you sure you want to delete this Participant ?");

                    alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            int position = getAdapterPosition();

                            mListener.onParticipantListFragmentLongClick(getItem(position));
                            removeItemAtPosition(position);
                            arg0.dismiss();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    return true;
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }
}
