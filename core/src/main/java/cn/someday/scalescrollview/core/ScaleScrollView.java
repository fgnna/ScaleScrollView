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


    private int mMinValue;//最小值
    private int mMaxValue;//最大值
    private int mLargeAliquots;//大等份数
    private int mSmallAliquots;//小等份数
    private float mLargeAverage;//大等份平均值
    private float mSmallAverage;//小等份平均值
    private BaseTheme mViewTheme;//刻度样式

    private int mOrientation;//滚动方向

    private float mCenterPrintX;//中心点 X
    private float mCenterPrintY;//中心点 Y
    private int mCenterLineLength;//中线总长度
    private int mHeight;
    private int mWidth;
    private float mMaxScroll;

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
        mMinValue = minValue;
        mMaxValue = maxValue;
        mLargeAliquots = largeAliquots;
        mSmallAliquots = smallAliquots;

        float count = maxValue - minValue;
        if(count <= 0 )
            throw new IllegalArgumentException("minValue不能大于或等于maxValue");

        mLargeAverage = count / largeAliquots;
        mSmallAverage = mLargeAverage / smallAliquots;
        mCenterLineLength = largeAliquots * smallAliquots * mViewTheme.getSpace();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        if(mCenterPrintX == 0.0)
        {
            mWidth = getWidth();
            mHeight = getHeight();
            mCenterPrintX = mWidth / 2;
            mCenterPrintY = mHeight / 2;
            mMaxScroll = mCenterLineLength - mCenterPrintX;
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if(mCenterPrintX == 0.0)
            return ;
        float startx = mCurrentScroll >= mCenterPrintX ? 0 : mCenterPrintX - mCurrentScroll;
        float starty = mCenterPrintY;

        float stopx = (mMaxScroll - mCurrentScroll) >= mCenterPrintX ? mWidth : mCenterLineLength - mCurrentScroll;
        float stopy = mCenterPrintY;

        canvas.drawLine(startx,starty,stopx,stopy,mCenterLinePaint);

        /**
         *  如果 当前滚动小于 中点值，从start开始计算
         *  否则 当前滚动 减去 中点值，获取超出屏幕的部份 ，
         *  超出屏部份 除以 每小格平均值，获得从 超出格数，
         *  超出屏部份 模 每小格平均值，获得余数，每格平均值减去余数 即 start点
         */
        if(startx > 0)
        {
            for(int j = 1,i = mSmallAliquots * mLargeAliquots; j < i; j++)
            {
                if( (startx+ mViewTheme.getSpace()*j) > mWidth )
                    break;
                canvas.drawLine(startx+mViewTheme.getSpace()*j,mCenterPrintY-50,startx+mViewTheme.getSpace()*j,mCenterPrintY,mCenterLinePaint);
            }
        }
        else
        {
            float outline = mCurrentScroll - mCenterPrintX;
            int count = (int) (outline / mViewTheme.getSpace());
            startx = mViewTheme.getSpace() - outline % mViewTheme.getSpace();

            for(int j = 1,i = mSmallAliquots * mLargeAliquots - count; j < i; j++)
            {
                if( (startx+ mViewTheme.getSpace()*j) > mWidth )
                    break;
                canvas.drawLine(startx+mViewTheme.getSpace()*j,mCenterPrintY-50,startx+mViewTheme.getSpace()*j,mCenterPrintY,mCenterLinePaint);
            }

        }



    }

    private float mTempTouchXY;//最后滚动到的位置
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
                    if(mCurrentScroll <= 0)
                    {
                        mCurrentScroll = 0;
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
        mViewTheme = viewTheme;

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
