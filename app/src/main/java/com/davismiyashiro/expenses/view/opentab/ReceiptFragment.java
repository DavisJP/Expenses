package com.davismiyashiro.expenses.view.opentab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.davismiyashiro.expenses.R;
import com.davismiyashiro.expenses.datatypes.ReceiptItem;
import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.injection.App;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReceiptFragment.OnReceiptFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReceiptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceiptFragment extends Fragment implements ReceiptInterfaces.ReceiptView {

    private static final String RECEIPT_PARAM1 = "com.davismiyashiro.expenses.view.fragments.receiptfragment";
    private OnReceiptFragmentInteractionListener mListener;

    @Inject
    ReceiptInterfaces.UserActionsListener mPresenter;

    ExpandableListView expandableListView;
    CustomExpandableListAdapter expandableListAdapter;
    List<String> expandableListParticipantIds = new ArrayList<>();
    ArrayMap<String, List<ReceiptItem>> expandableMapReceiptItemList = new ArrayMap<> ();

    private Tab mTab;

    public ReceiptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tab Parameter 1.
     * @return A new instance of fragment ReceiptFragment.
     */
    public static ReceiptFragment newInstance(Tab tab ) {
        ReceiptFragment fragment = new ReceiptFragment();
        Bundle args = new Bundle();
        args.putParcelable(RECEIPT_PARAM1, tab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTab = getArguments().getParcelable(RECEIPT_PARAM1);
        }
        //expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListParticipantIds, expandableMapReceiptItemList);
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), new ArrayList<String>(), new ArrayMap<String, List<ReceiptItem>>());
    }

//    @Override
//    public void setPresenter(ParticipantInterfaces.UserActionsListener presenter) {
//        mPresenter = presenter;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_receipt, container, false);

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                ActivityHelper.showToast(getContext(),
//                        expandableListParticipantIds.get(groupPosition) + " List Expanded.");
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                ActivityHelper.showToast(getContext(),
//                        expandableListParticipantIds.get(groupPosition) + " List Collapsed.");

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //mListener.onReceiptFragmentInteraction(expandableMapReceiptItemList.get(expandableListParticipantIds.get(groupPosition)).get(childPosition));
                return false;
            }
        });
        expandableListView.setAdapter(expandableListAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");

        mPresenter.loadReceipItems(mTab);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setReceiptView(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((App)getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public void showReceiptItems(ArrayMap<String, List<ReceiptItem>> items) {
        expandableMapReceiptItemList = items;
        expandableListParticipantIds = new ArrayList<>(expandableMapReceiptItemList.keySet());

        expandableListAdapter.replaceData(items);
    }

    @Override
    public void refreshReceiptItemsAdapter () {
        expandableListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnReceiptFragmentInteractionListener) {
            mListener = (OnReceiptFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnReceiptFragmentInteractionListener");
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
    public interface OnReceiptFragmentInteractionListener {
        void onReceiptFragmentInteraction(ReceiptItem receiptItem);
    }
}
