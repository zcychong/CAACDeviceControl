package com.caac.android.caacdevicecontrol.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.entity.Dynamic;
import com.caac.android.caacdevicecontrol.entity.User;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupDynamicsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupDynamicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupDynamicsFragment extends Fragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String TAG = getClass().getSimpleName();
    private int mCurrentCounter = 0;

    private static final int PAGE_SIZE = 10;
    private static final int TOTAL_COUNTER = 18;
    private int delayMillis = 0;
    private List<Dynamic> mainItems = new ArrayList<>();

    private LayoutInflater mInflater;
    private View notLoadingView;
    private RecyclerView rvList;
    private MainItemAdapter mainItemAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private User user;

    public GroupDynamicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupDynamicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupDynamicsFragment newInstance(String param1, String param2) {
        GroupDynamicsFragment fragment = new GroupDynamicsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_dynamics, container, false);
        mInflater = inflater;
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.srl_main);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        rvList = (RecyclerView)view.findViewById(R.id.rv_list);

        mainItemAdapter = new MainItemAdapter(getContext(), mainItems);
        mainItemAdapter.setOnLoadMoreListener(this);

        user = User.getCurrentUser(User.class);
        queryDefaultDynamic();
        return view;
    }

    private void queryDefaultDynamic(){
        BmobQuery<Dynamic> query = new BmobQuery<Dynamic>();
        query.setLimit(10);
        Log.e(TAG, "group = " + user.getGroup());
        query.addWhereEqualTo("group", user.getGroup());
        query.findObjects(new FindListener<Dynamic>() {
            @Override
            public void done(List<Dynamic> list, BmobException e) {
                if(e == null){
                    if(list != null){
                        Log.e(TAG, "首页同组动态条数:" + list.size());

                        mainItemAdapter.openLoadAnimation();
                        mCurrentCounter = mainItemAdapter.getData().size();
                        mainItemAdapter.setNewData(list);
                        mainItemAdapter.openLoadMore(1, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);

                        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvList.setAdapter(mainItemAdapter);
                    }
                }else{
                    Log.e(TAG, "没有数据!");
                }
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        Log.e(TAG, "onLoadMoreRequested");
        rvList.post(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        BmobQuery<Dynamic> query = new BmobQuery<Dynamic>();
                        query.addWhereEqualTo("group", user.getGroup());
                        query.setLimit(10);
                        query.setSkip(10);
                        query.findObjects(new FindListener<Dynamic>() {
                            @Override
                            public void done(List<Dynamic> list, BmobException e) {
                                if(e == null){
                                    if(list != null){
                                        Log.e(TAG, "下拉加载更多" + list.size());
                                        if(list.size() > 0){
                                            mainItemAdapter.notifyDataChangedAfterLoadMore(list, true);
                                            mCurrentCounter = mainItemAdapter.getData().size();
                                        }else{
                                            Log.e(TAG, "下拉加载更多无数据");
                                            mainItemAdapter.notifyDataChangedAfterLoadMore(false);
                                            if (notLoadingView == null) {
                                                notLoadingView = mInflater.inflate(R.layout.not_loading, (ViewGroup) rvList.getParent(), false);
                                            }
                                            mainItemAdapter.addFooterView(notLoadingView);
                                        }
                                    }else{
                                        Log.e(TAG, "下拉加载更多-异常");
                                    }
                                }
                            }
                        });
                    }
                }, delayMillis);
            }

        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                BmobQuery<Dynamic> query = new BmobQuery<Dynamic>();
                query.setLimit(10);
                query.addWhereEqualTo("group", user.getGroup());
                query.findObjects(new FindListener<Dynamic>() {
                    @Override
                    public void done(List<Dynamic> list, BmobException e) {
                        if(e == null){
                            if(list != null){
                                Log.e(TAG, "更新数据" + list.size());
                                mainItemAdapter.setNewData(list);
                                mainItemAdapter.openLoadMore(1, true);
                                mainItemAdapter.notifyDataSetChanged();
                                mainItemAdapter.removeAllFooterView();
                                mCurrentCounter = PAGE_SIZE;
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    }
                });

            }
        }, delayMillis);
    }

}
