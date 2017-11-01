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
import com.davismiyashiro.expenses.datatypes.Expense;

import java.util.List;

/**
 * RecyclerView Adapter for ExpenseFragment
 */

public class ExpenseRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseRecyclerViewAdapter.ExpenseViewHolder>{

    private List <Expense> mExpenses;
    private final ExpenseFragment.OnExpenseFragmentInteractionListener mListener;
    private Context mContext;

    public ExpenseRecyclerViewAdapter(List <Expense> items, ExpenseFragment.OnExpenseFragmentInteractionListener listener, Context context) {
        mExpenses = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {

        holder.bind (mExpenses.get(position));
    }

    public void replaceData (List<Expense> expenses) {
        setExpenses(expenses);
        notifyDataSetChanged();
    }

    private void setExpenses (List<Expense> expenses) {
        mExpenses = expenses;
    }

    @Override
    public int getItemCount() {
        return mExpenses.size();
    }

    public Expense getItem(int position) {
        return mExpenses.get(position);
    }

    public void removeItemAtPosition(int position) {
        mExpenses.remove(position);
        notifyItemRemoved(position);
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;
        private Expense mExpense;

        ExpenseViewHolder(View itemView) {
            super(itemView);
            mIdView = (TextView) itemView.findViewById(R.id.list_item_expense_title);
            mContentView = (TextView) itemView.findViewById(R.id.list_item_expense_participants);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onExpenseFragmentInteraction(mExpense);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setTitle("Delete Expense");
                    alertDialogBuilder.setMessage("Are you sure you want to delete this Expense? This will also remove the receipts.");

                    alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            int position = getAdapterPosition();

                            mListener.onExpenseFragmentLongClick(getItem(position));
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

        void bind (Expense expense){
            mExpense = expense;
            mIdView.setText(mExpense.getDescription());
            mContentView.setText(String.valueOf(mExpense.getValue()));
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }
}
