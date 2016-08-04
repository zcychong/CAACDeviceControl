package com.caac.android.caacdevicecontrol.fragment;


import android.widget.ImageView;

import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.entity.DataService;
import com.caac.android.caacdevicecontrol.entity.MainItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YHT on 2016/7/29.
 */
public class MainItemAdapter extends BaseQuickAdapter<MainItem> {
    private List<MainItem> mainItems;

    public MainItemAdapter(List<MainItem> mainItems){
        super(R.layout.main_item,mainItems );
    }

    public MainItemAdapter(int size){
        super(R.layout.main_item, DataService.createMainItemTestData(size) );
    }

    private static List<MainItem> createTestData(int size){
        List<MainItem> mainItems = new ArrayList<>();
        for(int i=0; i < size; i++){
            MainItem temp = new MainItem();
            if(i%3 == 0){
                temp.setAvatarRes(R.mipmap.default_image_4);
            }else  if(i%3 == 1){
                temp.setAvatarRes(R.mipmap.default_image_8);
            }else  if(i%3 == 2){
                temp.setAvatarRes(R.mipmap.default_image_9);
            }
            temp.setName("姓名");
            temp.setGroup("上报科室");
            temp.setValue(i + "哲学家最重要的品质就是独立思考。不诡辩、不畏惧，我们承认自己的无知，我们敢于质疑我们的知识，质疑不是为了凸显自己的牛B而是为了更好的重建.");
            temp.setTime("2016/8/7");
            temp.setLeaveMessageCount("5");
            mainItems.add(temp);
        }
        return mainItems;
    }

    @Override
    protected void convert(BaseViewHolder helper, MainItem item) {
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_grouop, item.getGroup())
                .setText(R.id.tv_text, item.getValue())
                .setText(R.id.tv_text, item.getValue())
                .setText(R.id.tv_leave_message_count, item.getLeaveMessageCount())
                .setImageResource(R.id.iv_first_img, item.getAvatarRes());


//        Glide.with(mContext).load(item.getUserAvatar()).crossFade().placeholder(R.mipmap.def_head).transform(new GlideCircleTransform(mContext)).into((ImageView) helper.getView(R.id.tweetAvatar));
    }


}
