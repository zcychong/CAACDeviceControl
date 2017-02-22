package com.caac.android.caacdevicecontrol.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

/**
 * Created by Liu on 2015-04-17.
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获得 手机分辨率宽度
     *
     * @return
     */
    public static int getScreenWidthPix(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获得手机分辨率宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHighPix(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 根据屏幕宽度设置图片宽度
     *
     * @param view
     * @param scale
     */
//    public static void setViewLayoutParams(ImageView view, float scale) {
//        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
//        lp.width = (int) (((float) getScreenWidthPix(view.getContext()) - DensityUtil.dip2px(view.getContext(), 25)) / scale);
//        lp.height = (int) (((float) getScreenWidthPix(view.getContext()) - DensityUtil.dip2px(view.getContext(), 25)) / scale);
//        lp.setMargins(0, DensityUtil.dip2px(view.getContext(), 5),
//                DensityUtil.dip2px(view.getContext(), 5), DensityUtil.dip2px(view.getContext(), 5));
//        view.setLayoutParams(lp);
//    }
    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredHeight(View view)
    {
        // int height = view.getMeasuredHeight();
        // if(0 < height){
        // return height;
        // }
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }
    /**
     * 测量控件的尺寸
     *
     * @param view
     */
    public static void calcViewMeasure(View view)
    {
        // int width =
        // View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        // int height =
        // View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        // view.measure(width,height);

        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }
}
