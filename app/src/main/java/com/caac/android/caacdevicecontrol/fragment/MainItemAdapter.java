package com.caac.android.caacdevicecontrol.fragment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caac.android.caacdevicecontrol.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YHT on 2016/7/29.
 */
public class MainItemAdapter extends RecyclerView.Adapter<MainItemAdapter.MyViewHolder> {
    private Activity mContext;
    private List<String> mDatas;
    public MainItemAdapter(Activity context) {
        mContext = context;
        mDatas = new ArrayList<String>();
        for (int i = 0; i <  10; i++)
        {
            mDatas.add(i + "");
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.main_item, parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.tv.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView tv;

        public MyViewHolder(View view)
        {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_text);
        }
    }
}
