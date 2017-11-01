package com.davismiyashiro.expenses.view.opentab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davismiyashiro.expenses.R;
import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.injection.App;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExpenseFragment.OnExpenseFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment implements ExpenseInterfaces.ExpenseView{
    private static final String TAB_PARAM = "com.davismiyashiro.expenses.view.opentab.ExpenseFragment";
    private OnExpenseFragmentInteractionListener mListener;

    @Inject
    ExpenseInterfaces.UserActionsListener mPresenter;

    private ExpenseRecyclerViewAdapter mRecyclerAdapter;
    private Tab mTab;
    private RecyclerView recyclerView;
    // TODO: Set Layout of RecyclerView
    private int mColumnCount = 1;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tab Parameter 1.
     * @return A new instance of fragment ExpenseFragment.
     */
    public static ExpenseFragment newInstance(Tab tab) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putParcelable(TAB_PARAM, tab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");

        if (getArguments() != null) {
            mTab = getArguments().getParcelable(TAB_PARAM);
        }
        mRecyclerAdapter = new ExpenseRecyclerViewAdapter(new ArrayList<Expense>(), mListener, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_expense, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.exp_list);

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }

//        RecyclerView.ItemDecoration itemDecoration = new
//                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(itemDecoration);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mRecyclerAdapter);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((App)getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setExpenseView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");

        mPresenter.loadExpenses(mTab);
    }

    @Override
    public void showExpenses(List<Expense> expenses) {
        mRecyclerAdapter.replaceData(expenses);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Expense expense) {
        if (mListener != null) {
            mListener.onExpenseFragmentInteraction(expense);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.d("onAttach");

        if (context instanceof OnExpenseFragmentInteractionListener) {
            mListener = (OnExpenseFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnExpenseFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnExpenseFragmentInteractionListener {
        void onExpenseFragmentInteraction(Expense expense);
        void onExpenseFragmentLongClick(Expense expense);
    }
}
