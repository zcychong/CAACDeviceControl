package com.caac.android.caacdevicecontrol.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.utils.DensityUtil;

/**
 * Created by YHT on 2017/2/15.
 */

public class ExchangeColorView extends View {
    private Context mContext;
    private Bitmap mIconBitmap;
    private String mText;
    private float mTextSize;
    private int mColor;

    private Paint mTextPint;
    //绘制文字的矩形区域
    private Rect mTextBound = new Rect();
    private Rect mIconBound = new Rect();

    private int mTextXPosition;
    private int mTextYPosition;

    private float mAlpha;//透明度
    private Bitmap mBitmap;//缓存的bitmap
    private Bitmap mTextBitmap;//缓存的bitmap
    private Canvas mCanvas;//缓存画板
    private Canvas mTextCanvas;
    private Paint mPaint;//缓存画笔

    public ExchangeColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        //关闭硬件加速
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        Log.e("CAAC", "ExchangeColorView" );
        //接收自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExchangeColorView);
        Log.e("CAAC", "TypedArray.length()= " + a.length());
        for(int i=0; i<a.length(); i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.ExchangeColorView_ecv_icon:
                    BitmapDrawable bd = (BitmapDrawable)a.getDrawable(attr);
                    mIconBitmap = bd.getBitmap();
                    break;
                case R.styleable.ExchangeColorView_ecv_text:
                    mText = a.getString(attr);
                    break;
                case R.styleable.ExchangeColorView_ecv_text_size:
                    mTextSize = a.getDimension(attr, 10);
                    break;
                case R.styleable.ExchangeColorView_ecv_color:
                    mColor = a.getColor(attr, 0x111111);
                    break;
                default:

                    break;
            }
        }
        a.recycle();

        //初始化画笔
        mTextPint = new Paint();
        mTextPint.setColor(mColor);
        mTextPint.setTextSize(mTextSize);
        //得到文字的绘制矩形区域的宽高
        if(mText == null){
            Log.e("CAAC", "mText = null");
        }else{
            Log.e("CAAC", "mText.length() = " + mText.length());
        }
        mTextPint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    //测量子空间大小时调用
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 测量子空间大小时调用
         * 比较宽高 得到最小的作为我们的宽高
         */
        Log.e("CAAC", "getMeasuredWidth() = " + getMeasuredWidth());

//        int bmpWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
//            - getPaddingRight(), getMeasuredHeight() - getPaddingBottom() - getPaddingTop());
        int bmpWidth = Math.min(mIconBitmap.getWidth(), mIconBitmap.getHeight());
        //根据控件大小 保证字体大小 和 上下间距-3*5dp -> 调节图片大小
        int tempWidth = getMeasuredWidth() - 3*(DensityUtil.dip2px(mContext, 10) - mTextBound.height());
        bmpWidth = Math.min(tempWidth, bmpWidth);

        Log.e("CAAC", "bmpWidth = " + bmpWidth);

        int left = getMeasuredWidth() /2 - bmpWidth/2;
        int top = (getMeasuredHeight() - mTextBound.height() - bmpWidth) /3;
        Log.e("CAAC", "left = " + left + "   top = " +top);
        //设置图片要绘制的矩形区域
        mIconBound = new Rect(left, top, left + bmpWidth, top + bmpWidth);

        mTextXPosition = (getMeasuredWidth() - mTextBound.width())/2;
        mTextYPosition = top * 2 + bmpWidth + mTextBound.height();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 0.清空画板
         * 1.绘制矩形色块
         * 2.先绘制图片
         * 3.绘制文字
         */

        //0.清空画板
        canvas.drawBitmap(mIconBitmap, null, mIconBound, null );

        //1.绘制矩形色块   2.先绘制图片
        //ceil: 向上取整
        int alpha = (int)Math.ceil(255*mAlpha);
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        drawSrcText(canvas, alpha);
        setBitmap(alpha);



        //3.绘制文字
        //3.1 绘制黑色文字 -- 控制 alpha透明度

        //3.2 绘制绿色文字 -- 控制 alpha透明度 -- 相反
//        drawDstText(canvas, alpha);

        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private void drawSrcText(Canvas canvas, int alpha){

        //绘制颜色块
        mTextPint = new Paint();
//        mTextPint.setAlpha(alpha);
//        mTextPint.setColor(mContext.getResources().getColor(R.color.light_black));
//        mTextPint.setTextSize(mTextSize);
//        mCanvas.drawText(mText, mTextXPosition, mTextYPosition,mTextPint);
        //系统API Graphics/Xformodes

//        mTextPint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        Log.e("CAAC", "alpha = " + alpha);
//        mTextPint.setAlpha(alpha);
//        mTextPint.setColor(mColor);
//        mTextPint.setTextSize(mTextSize);
//        mCanvas.drawText(mText, mTextXPosition, mTextYPosition, mTextPint);



        mTextPint.setColor(mColor);
        //防锯齿
        mTextPint.setAntiAlias(true);
        //防抖动
        mTextPint.setDither(true);

//        //绘制颜色块
//        mCanvas.drawRect(mTextXPosition,mTextYPosition, mTextXPosition + mTextBound.width(), mTextYPosition
//                - mTextBound.height() , mTextPint);
//
//        mTextPint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//
//        {
//            mTextBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//            mTextCanvas = new Canvas(mTextBitmap);
//            Paint mTempPoint = new Paint();
//            mTempPoint.setColor(mContext.getResources().getColor(R.color.light_black));
//            mTempPoint.setTextSize(mTextSize);
//            mTempPoint.setAlpha(255);
//            mTextCanvas.drawText(mText, mTextXPosition, mTextYPosition, mTempPoint);
//        }
//
//        mTextPint.setColor(mContext.getResources().getColor(R.color.light_black));
//        mTextPint.setAlpha(255);
//        mCanvas.drawBitmap(mTextBitmap, 1, -3, mTextPint);

        mTextPint.setColor(mColor);
        mTextPint.setAlpha(alpha);
        mTextPint.setTextSize(mTextSize);
        mCanvas.drawText(mText, mTextXPosition, mTextYPosition, mTextPint);


        mTextPint.setColor(mContext.getResources().getColor(R.color.light_black));
        mTextPint.setAlpha(255-alpha);
        mTextPint.setTextSize(mTextSize);
        mCanvas.drawText(mText, mTextXPosition, mTextYPosition, mTextPint);

    }

    private void drawDstText(Canvas canvas, int alpha){


        Log.e("CAAC", "mText = " + mText);
        Log.e("CAAC", "mTextBound.left = " + mTextBound.left);
        Log.e("CAAC", "mTextBound.top = " + mTextBound.top);
        Log.e("CAAC", "mTextBound.right = " + mTextBound.right);
        Log.e("CAAC", "mTextBound.bottom = " + mTextBound.bottom);

//        int textLeft = (getMeasuredWidth() - mText.length())/2;
//        int textTop = (getMeasuredHeight() - )

    }

    public void setIconAlpha(float positionOffect){
        this.mAlpha = positionOffect;
        //让自定义控件刷新
        invalidate();
    }

    public void setBitmap(int alpha) {
        //绘制图片
        //二级缓存 -- 先在内存的画板上canvas上面绘制好, 再刷新到自定义控件上面


        //画笔
        mPaint = new Paint();
        //颜色块
        mPaint.setColor(mColor);
        //防锯齿
        mPaint.setAntiAlias(true);
        //防抖动
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);

        //绘制颜色块
        mCanvas.drawRect(mIconBound, mPaint);

        //系统API Graphics/Xformodes
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //再绘制图片
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIconBitmap, null, mIconBound, mPaint);

    }
}
