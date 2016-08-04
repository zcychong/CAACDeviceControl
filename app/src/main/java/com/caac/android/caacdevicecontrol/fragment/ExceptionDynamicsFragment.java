package com.caac.android.caacdevicecontrol.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.entity.DataService;
import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExceptionDynamicsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExceptionDynamicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExceptionDynamicsFragment extends Fragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int mCurrentCounter = 0;
    private static final int PAGE_SIZE = 6;
    private static final int TOTAL_COUNTER = 18;
    private int delayMillis = 2000;

    private LayoutInflater mInflater;
    private View notLoadingView;
    private RecyclerView rvList;
    private MainItemAdapter mainItemAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
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
        mInflater = inflater;
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.srl_main);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        rvList = (RecyclerView)view.findViewById(R.id.rv_list);

        mainItemAdapter = new MainItemAdapter(PAGE_SIZE);
        mainItemAdapter.openLoadAnimation();
        mCurrentCounter = mainItemAdapter.getData().size();
        mainItemAdapter.setOnLoadMoreListener(this);
        mainItemAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);

        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setAdapter(mainItemAdapter);

        return view;
    }


    @Override
    public void onLoadMoreRequested() {
        rvList.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    mainItemAdapter.notifyDataChangedAfterLoadMore(false);
                    if (notLoadingView == null) {
                        notLoadingView = mInflater.inflate(R.layout.not_loading, (ViewGroup) rvList.getParent(), false);
                    }
                    mainItemAdapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainItemAdapter.notifyDataChangedAfterLoadMore(DataService.createMainItemTestData(PAGE_SIZE), true);
                            mCurrentCounter = mainItemAdapter.getData().size();
                        }
                    }, delayMillis);
                }
            }

        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mainItemAdapter.setNewData(DataService.createMainItemTestData(PAGE_SIZE));
                mainItemAdapter.openLoadMore(PAGE_SIZE, true);
                mainItemAdapter.removeAllFooterView();
                mCurrentCounter = PAGE_SIZE;
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, delayMillis);
    }
}
