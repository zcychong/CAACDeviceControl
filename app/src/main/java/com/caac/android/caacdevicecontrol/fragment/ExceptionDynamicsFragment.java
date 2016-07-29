package com.caac.android.caacdevicecontrol.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caac.android.caacdevicecontrol.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExceptionDynamicsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExceptionDynamicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExceptionDynamicsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView rvList;

    public ExceptionDynamicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExceptionDynamicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExceptionDynamicsFragment newInstance(String param1, String param2) {
        ExceptionDynamicsFragment fragment = new ExceptionDynamicsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exception_dynamics, container, false);

        rvList = (RecyclerView)view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setAdapter(new MainItemAdapter(getActivity()));

        return view;
    }


}
