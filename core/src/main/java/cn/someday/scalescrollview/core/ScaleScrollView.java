package cn.someday.scalescrollview.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import cn.someday.scalescrollview.core.theme.BaseTheme;
import cn.someday.scalescrollview.core.theme.SimpleTheme;

import static android.R.attr.width;

/**
 * 可滚动选择刻度值的控件
 * 提供：垂直/横向滚动，自定义样式
 * 必须调用{@link #setOption(int, int, int, int)} 方法来使控件生效
 *
 * @see #setOption(int, int)
 * @see #setOption(int, int, int, int)
 *
 * @author shaojunjie on 17-5-6
 * @Email fgnna@qq.com
 *
 */
public class ScaleScrollView extends View
{
    private static final String TAG = "ScaleScrollView";

    public ScaleScrollView(Context context) {super(context);initialize();}
    public ScaleScrollView(Context context, @Nullable AttributeSet attrs) {super(context, attrs);initialize();}
    public ScaleScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {super(context, attrs, defStyleAttr);initialize();}


    private int mMinValue;
    private int mMaxValue;
    private int mLargeAliquots;
    private int mSmallAliquots;

    private int mOrientation;//滚动方向

    private float mCenterPrintX;//中心点 X
    private float mCenterPrintY;//中心点 Y

    /**
     * 初始化view属性
     */
    private void initialize()
    {

        mOrientation = LinearLayout.HORIZONTAL;
        setViewTheme(new SimpleTheme());
        Log.d(TAG,"initialize()");
    }


    /**
     * 默认 大小等份都为10
     * @see #setOption(int, int, int, int)
     */
    public void setOption(int minValue,int maxValue)
    {
        setOption(minValue,maxValue,10,10);
    }


    /**
     * 刻度分割的属性设置
     * 必须调此方法，否则不会绘制刻度
     * 大份数刻度间隔 = (刻度最大值 - 刻度最小值)/大等分份数
     * 小份数刻度间隔 =  大份数刻度间隔 / 小等分份数
     *
     * @param minValue 刻度最小值
     * @param maxValue 刻度最大值
     * @param largeAliquots 大等分份数， 分为多小个大等份
     * @param smallAliquots 小等分份数， 每一个大份数分多少个小份数
     *
     * 大份数刻度
     *   |              |       |
     *   |   小份数刻度  |       |
     *   |  |  |  |  |  | | | | |
     */
    public void setOption(int minValue,int maxValue,int largeAliquots,int smallAliquots)
    {


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        if(mCenterPrintX == 0.0)
        {
            float width = getWidth();
            float height = getHeight();
            mCenterPrintX = width/2;
            mCenterPrintY = height/2;
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if(mCenterPrintX == 0.0)
            return ;
        float startx = mCenterPrintX - mCurrentScroll;
        float starty = mCenterPrintY;
        float stopx = getWidth();
        float stopy = mCenterPrintY;

        canvas.drawLine(startx,starty,stopx,stopy,mCenterLinePaint);
    }

    private float mTempTouchXY;//最后滚动到的位置
    private final int  mMinScroll = 0;
    private int  mMaxScroll = 1080;
    private float mCurrentScroll = 0;//当前已滚动的路程

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                if (mScroller != null && !mScroller.isFinished()) {
//                    mScroller.abortAnimation();
//                }
                mTempTouchXY = x;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dataX = mTempTouchXY - x;
                //向右边滑动
                if (dataX < 0)
                {
                    if(mCurrentScroll <= mMinScroll)
                    {
                        mCurrentScroll = mMinScroll;
                        Log.d(TAG,"右滑动 到底部"+mCurrentScroll);
                        return super.onTouchEvent(event);
                    }
                    else
                    {
                        Log.d(TAG,"右滑动 "+mCurrentScroll);
                        mCurrentScroll += dataX;
                    }
                }
                //向左边滑动
                else if (dataX > 0)
                {
                    if(mCurrentScroll >= mMaxScroll)
                    {
                        mCurrentScroll = mMaxScroll;
                        Log.d(TAG,"左滑动 到底部"+mCurrentScroll);
                        return super.onTouchEvent(event);
                    }
                    else
                    {
                        mCurrentScroll += dataX;
                        Log.d(TAG,"左滑动 "+mCurrentScroll);
                    }
                }
                //smoothScrollBy(dataX, 0);
                mTempTouchXY = x;
                Log.d(TAG,"当前滚动距离： "+mCurrentScroll);
                postInvalidate();
                return true;
            case MotionEvent.ACTION_UP:
                postInvalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }


    private Paint mLargeLinePaint;
    private Paint mSmallLinePaint;
    private Paint mTextPaint;
    private Paint mCenterLinePaint;


    /**
     * 设置刻度样式
     * @param viewTheme
     */
    public void setViewTheme(BaseTheme viewTheme)
    {
        // 画笔
        mCenterLinePaint = new Paint();
        mCenterLinePaint.setColor(Color.BLUE);
        // 抗锯齿
        mCenterLinePaint.setAntiAlias(false);
        // 设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        mCenterLinePaint.setDither(false);
        // 设置实心
        mCenterLinePaint.setStyle(Paint.Style.STROKE);
        mCenterLinePaint.setStrokeWidth(3);

    }

}
