package com.caac.android.caacdevicecontrol.fragment;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.bumptech.glide.Glide;
import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.entity.Dynamic;
import com.caac.android.caacdevicecontrol.utils.StringUtils;
import com.caac.android.caacdevicecontrol.view.CircleImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YHT on 2016/7/29.
 */
public class MainItemAdapter extends BaseQuickAdapter<Dynamic> {
    private List<Dynamic> mainItems;
    private Context mContext;

    public MainItemAdapter(Context context, List<Dynamic> mainItems){
        super(R.layout.main_item,mainItems );
        mContext = context;
    }



    @Override
    protected void convert(BaseViewHolder helper, Dynamic item) {
        helper.setText(R.id.tv_name, item.getUserName())
                .setText(R.id.tv_grouop, item.getGroup())
                .setText(R.id.tv_text, item.getMessage())
                .setText(R.id.tv_leave_message_count, String.valueOf(item.getLeaveMsgCount()));
        ImageView iv = (ImageView)helper.getView(R.id.iv_first_img);
        if(item.getImages() != null){
            if(item.getImages().size() > 0){
                Glide.with(mContext).load(item.getImages().get(0)).into(iv);
            }else{
                iv.setVisibility(View.GONE);
            }
        }else{
            iv.setVisibility(View.GONE);
        }

        final String assetName = StringUtils.ANIMATION_HEART;
        final LottieAnimationView laview = (LottieAnimationView)helper.getView(R.id.animation_view);
        LottieComposition.fromAssetFileName(mContext, assetName,
                new LottieComposition.OnCompositionLoadedListener() {
                    @Override
                    public void onCompositionLoaded(LottieComposition composition) {
                        laview.setComposition(composition);
                    }
                });
        laview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!laview.isAnimating()){
                    laview.playAnimation();
                }
            }
        });

        CircleImageView civAvatar = (CircleImageView)helper.getView(R.id.civ_avatar);
        Glide.with(mContext).load(item.getAvatar()).into(civAvatar);

//                .setImageResource(R.id.iv_first_img, item.getAvatar());


//        Glide.with(mContext).load(item.getUserAvatar()).crossFade().placeholder(R.mipmap.def_head).transform(new GlideCircleTransform(mContext)).into((ImageView) helper.getView(R.id.tweetAvatar));
    }


}
