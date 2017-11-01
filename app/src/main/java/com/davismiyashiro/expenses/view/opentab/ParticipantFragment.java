package com.davismiyashiro.expenses.view.opentab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davismiyashiro.expenses.R;
import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.injection.App;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnParticipantListFragmentInteractionListener}
 * interface.
 */
public class ParticipantFragment extends Fragment implements ParticipantInterfaces.ParticipantView {

    private static final String TAB_PARAM = "com.davismiyashiro.expenses.view.opentab.ParticipantFragment";
    private OnParticipantListFragmentInteractionListener mListener;

    @Inject
    ParticipantInterfaces.UserActionsListener mPresenter;

    private Tab mTab;
    private RecyclerView recyclerView;
    private ParticipantRecyclerViewAdapter mRecyclerAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ParticipantFragment() {
    }

    public static ParticipantFragment newInstance(Tab tab) {
        ParticipantFragment fragment = new ParticipantFragment();
        Bundle args = new Bundle();
        args.putParcelable(TAB_PARAM, tab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.d("onAttach");
        if (context instanceof OnParticipantListFragmentInteractionListener) {
            mListener = (OnParticipantListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnParticipantListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");

        if (getArguments() != null) {
            mTab = getArguments().getParcelable(TAB_PARAM);
        }

        mRecyclerAdapter = new ParticipantRecyclerViewAdapter(new ArrayList<Participant>(), mListener, getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((App)getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("onCreateView");

        View view = inflater.inflate(R.layout.fragment_participant_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mRecyclerAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");

        mPresenter.loadParticipants(mTab);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setParticipantView(this);
    }

    @Override
    public void showParticipants(List<Participant> participants) {
        mRecyclerAdapter.replaceData(participants);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.d("onDetach");
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
    public interface OnParticipantListFragmentInteractionListener {
        void onParticipantListFragmentInteraction(Participant item);

        void onParticipantListFragmentLongClick(Participant item);
    }
}
