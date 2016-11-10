package com.gc.speedometer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gc.speedometer.logic.SpeedData;

/**
 * Created by gc on 16/10/10.
 */
public class SpeedView extends View {

    private int currentScale = 0;//当前刻度
    private double currentSpeedValue = 0;//当前速度值
    private double totalMileage = 0;//当前总里程

    private int offsetScale, tagetScale;//偏移刻度，目标刻度
    private SpeedData speedData;
    private Paint mBitmapPaint;
    private Matrix matrix;
    private float[] array = {1, 0, 0, 0, 1, 0, 0, 0, 1};// 矩阵
    private final static int ANIMATION_START = 1;//开始显示动画
    private final static int ANIMATION_ING = 2;//显示动画中
    private final static int ANIMATION_END = 3;//显示动画结束
    private int animationState = ANIMATION_END;//是否显示动画

    private int rangeStyle = CLICK_RANGE_STYLE;
    private final static int CLICK_RANGE_STYLE = 1;//点击变化
    private final static int BROADCAST_RANGE_STYLE = 2;//广播变化

    private final static int ANIMATION_NULL = 0;//
    private final static int ANIMATION_BACK = 1;//后退动画
    private final static int ANIMATION_FORWARD = 2;//前进动画
    private int animationStyle = ANIMATION_NULL;

    private final static double SPEED_MAX = 150;//最大速度

    int parentWidth, parentHeight;

    //直接new 初始化的时候调用
    public SpeedView(Context context) {
        super(context);
        initData();
    }

    //在布局里面使用的时候调用
    public SpeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    //在布局里面使用的时候调用,多了个样式
    public SpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    /**
     * 1.测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        parentWidth=MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int size = (parentWidth >= parentHeight ? parentHeight : parentWidth);
        setMeasuredDimension(size, size);
    }

    /**
     * 2.布局
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int h = bottom - top;
        int w = right - left;
        if (parentHeight > h) {
            top = (parentHeight - h) / 2;
            bottom = h + (parentHeight - h) / 2;
        }
        if (parentWidth > w) {
            left = (parentWidth - w) / 2;
            right = w + (parentWidth - w) / 2;
        }
        super.onLayout(changed, left, top, right, bottom);
        speedData = new SpeedData(getWidth(), getHeight(), currentScale, totalMileage);
        offsetScale = currentScale;
    }

    /**
     * 3.绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (speedData.getBitmap() == null) {
            return;
        }
        canvas.drawBitmap(speedData.getBitmap(), matrix, mBitmapPaint);
        // 隔一段时间重绘一次, 动画效果
        if (animationState == ANIMATION_START || animationState == ANIMATION_ING) {
            if (rangeStyle == CLICK_RANGE_STYLE) {
                updateClick();
            } else if (rangeStyle == BROADCAST_RANGE_STYLE) {
                updateBroadcast();
            }
        }
    }

    private void initData() {
        //初始化矩阵
        matrix = new Matrix();
        matrix.setValues(array);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    /**
     * 响应速度更新事件，滚动至指定速度值
     */
    public void updateBroadcast() {
        if (animationStyle == ANIMATION_BACK) {
            animationState = ANIMATION_ING;
            offsetScale = offsetScale - 3;
            if (offsetScale <= tagetScale) {
                animationStyle = ANIMATION_NULL;
                animationState = ANIMATION_END;
                currentScale = tagetScale;
                offsetScale = tagetScale;
            }
        } else if (animationStyle == ANIMATION_FORWARD) {
            animationState = ANIMATION_ING;
            offsetScale = offsetScale + 3;
            if (offsetScale >= tagetScale) {
                animationStyle = ANIMATION_NULL;
                animationState = ANIMATION_END;
                currentScale = tagetScale;
                offsetScale = tagetScale;
            }
        }
        speedData.setCurrentScale(offsetScale);
        invalidate();
    }

    /**
     * 响应点击事件，加速减速动画
     */
    public void updateClick() {
        if (currentScale == 99) {
            animationState = ANIMATION_END;
            animationStyle = ANIMATION_NULL;
            return;
        }
        if (animationStyle == ANIMATION_FORWARD) { //前进动画
            animationState = ANIMATION_ING;
            if (offsetScale <= currentScale) {
                offsetScale--;
            } else {
                offsetScale = offsetScale - 2;
            }
            if (offsetScale <= currentScale) {
                animationStyle = ANIMATION_NULL;
                animationState = ANIMATION_END;
            }
        } else if (animationStyle == ANIMATION_BACK) { //后退动画
            animationState = ANIMATION_ING;
            if (offsetScale >= 99) {
                offsetScale--;
            } else {
                offsetScale = offsetScale + 2;
            }
            if (offsetScale >= 99) {
                animationStyle = ANIMATION_FORWARD; //前进动画
            }
        }
        speedData.setCurrentScale(offsetScale);
        invalidate();
    }

    /**
     * 监听屏幕触摸事件，包括按下、抬起、移动
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 开始模拟加速减速动画
     */
    public void gotoRotate(){
        if (animationState == ANIMATION_END) {
            rangeStyle = CLICK_RANGE_STYLE;
            animationState = ANIMATION_START;
            animationStyle = ANIMATION_BACK;
            invalidate();
        }
    }

    /**
     * 设置速度值，并滑动到指定位置
     */
    public void setSpeedValue(double value) {
        if (value < 0 || value > SPEED_MAX) return;
        if (animationState == ANIMATION_END) { //有动画没开始
            currentSpeedValue = value;
            rangeStyle = BROADCAST_RANGE_STYLE;
            animationState = ANIMATION_START;
            tagetScale = (int)(value/(SPEED_MAX/speedData.scaleNum));
            if (tagetScale >= currentScale) animationStyle = ANIMATION_FORWARD;
            else animationStyle = ANIMATION_BACK;
            invalidate();
        }
    }

    /**
     * 获取当前速度
     * @return
     */
    public double getSpeedValue(){
        return currentSpeedValue;
    }

    /**
     * 更新总里程
     */
    public void setTotalMileage(double totalMileage){
        this.totalMileage = totalMileage;
        speedData.setCurrentTotalMileage(totalMileage);
        invalidate();
    }

}
