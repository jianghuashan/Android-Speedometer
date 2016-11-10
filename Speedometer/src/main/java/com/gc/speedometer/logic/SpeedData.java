package com.gc.speedometer.logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;

import com.gc.speedometer.R;
import com.gc.speedometer.application.MyApplication;
import com.gc.speedometer.utils.ConvertUtils;

/**
 * Created by gc on 16/10/10.
 */
public class SpeedData {

    private int currentScale = 0;//当前刻度
    private double currentTotalMileage = 0;//总里程
    //如果需要个性版字体，请将字体的.ttf文件放置于assets目录下
    //private Typeface typefaceIZARTTF = Typeface.createFromAsset(App.app.getAssets(), "xx.ttf");
    private Point[] pointScale = new Point[6];//刻度文本位置数组 0、30、60、90、120、150
    public static final int scaleNum = 100;//刻度总数
    private static final int indicatorRadius = 16;//指示器半径
    private static final int textPadding = 100;//刻度文本偏移
    private static final int padding = 100;//偏移
    private static final int scaleOutsidePadding = 130;//刻度外圆偏移
    private static final int scaleInsidePadding = 100;//刻度内圆偏移
    private int[] color = new int[scaleNum];//每个刻度的颜色
    private int bitmapWidth = 0, bitmapHeight = 0;//位图宽 高
    private int radiusMax, radiusOutSide, radiusInside;//最大半径，刻度半径，刻度内圆半径

    private Rect rect = new Rect();//Rect类主要用于表示坐标系中的一块矩形区域，并可以对其做一些简单操作

    //定义用到的颜色
    private int clrNormal = ContextCompat.getColor(MyApplication.getContext(), android.R.color.white);

    /**
     * 构造方法
     * @param bitmapWidth Bitmap宽
     * @param bitmapHeight Bitmap高
     * @param currentScale 当前刻度
     * @param currentTotalMileage 当前总里程
     */
    public SpeedData(int bitmapWidth, int bitmapHeight, int currentScale, double currentTotalMileage) {
        for (int i = 0; i < pointScale.length; i++) {
            pointScale[i] = new Point();
        }
        this.currentScale = currentScale >= 0 ? currentScale : 0;
        this.bitmapWidth = bitmapWidth;
        this.bitmapHeight = bitmapHeight;
        this.currentTotalMileage = currentTotalMileage;

        radiusMax = bitmapWidth / 2 - padding;
        radiusOutSide = bitmapWidth / 2 - scaleOutsidePadding;
        radiusInside = radiusOutSide - scaleInsidePadding;
        getImgColorsArray();
    }

    /**
     * 获取颜色数组
     */
    public void getImgColorsArray() {
        Bitmap bitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.color);
        int width = bitmap.getWidth();
        int unit = width / scaleNum;
        for (int i = 0; i < scaleNum; i++) {
            color[i] = bitmap.getPixel(unit / 2 + i * unit, 1);
        }
    }

    /**
     * 获取Bitmap
     */
    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        //设置个性版字体
        //paint.setTypeface(typefaceIZARTTF);

        //画外圆
        paint.setColor(color[currentScale == 0 ? 0 : currentScale - 1]);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        RectF oval = new RectF(textPadding, textPadding, bitmapWidth - textPadding, bitmapHeight - textPadding);
        canvas.drawArc(oval, 120, 300, false, paint);
        //画外圆底部直线
        canvas.drawLine(pointScale[0].x, pointScale[0].y, pointScale[5].x, pointScale[5].y, paint);

        double a = 0;
        //指定刻度文本位置值
        for (int i = 0; i < 6; i++) {
            a = 2 * Math.PI * (240 - 60 * i) / 360;
            pointScale[i].x = (int) ((int) radiusMax * Math.cos(a) + bitmapHeight / 2);
            pointScale[i].y = (int) (bitmapWidth / 2 - (int) radiusMax * Math.sin(a));
        }

        //画刻度文本
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        paint.setTextSize(60);
        FontMetrics fontMetrics = paint.getFontMetrics();

        String text = "0";
        paint.getTextBounds(text, 0, text.length(), rect);
        float x = pointScale[0].x - rect.width() / 2;
        float y = pointScale[0].y + textPadding / 2 + Math.abs(fontMetrics.descent + fontMetrics.ascent) / 2;
        canvas.drawText(text, x, y, paint);

        text = "30";
        paint.getTextBounds(text, 0, text.length(), rect);
        x = pointScale[1].x - textPadding / 2 - rect.width() / 2;
        y = pointScale[1].y + Math.abs(fontMetrics.descent + fontMetrics.ascent) / 2;
        canvas.drawText(text, x, y, paint);

        text = "60";
        paint.getTextBounds(text, 0, text.length(), rect);
        x = (float) ((float) (pointScale[2].x - (textPadding / 2 + rect.width() / 2) * Math.cos(2 * Math.PI * (60) / 360))) - 10;
        y = (float) ((float) (pointScale[2].y - (textPadding / 2 + rect.width() / 2) * Math.sin(2 * Math.PI * (60) / 360)) + rect.height()) - 10;
        canvas.drawText(text, x, y, paint);

        text = "90";
        x = (float) ((float) (pointScale[3].x + (textPadding / 2 + rect.width() / 2) * Math.cos(2 * Math.PI * (60) / 360))) - rect.width() + 5;
        y = (float) ((float) (pointScale[3].y - (textPadding / 2 + rect.width() / 2) * Math.sin(2 * Math.PI * (60) / 360)) + rect.height()) - 5;
        canvas.drawText(text, x, y, paint);

        text = "120";
        paint.getTextBounds(text, 0, text.length(), rect);
        x = pointScale[4].x + textPadding / 2 - rect.width() / 2;
        y = pointScale[4].y + Math.abs(fontMetrics.descent + fontMetrics.ascent) / 2;
        canvas.drawText(text, x, y, paint);

        text = "150";
        paint.getTextBounds(text, 0, text.length(), rect);
        x = pointScale[5].x - rect.width() / 2;
        y = pointScale[5].y + textPadding / 2 + Math.abs(fontMetrics.descent + fontMetrics.ascent) / 2;
        canvas.drawText(text, x, y, paint);

        //画刻度
        paint.setStrokeWidth(8);
        for (int i = 0; i < scaleNum; i++) {
            if (i > currentScale || currentScale == 0) paint.setColor(clrNormal);
            else paint.setColor(color[i]);
            a = 2 * Math.PI * (240 - 3 * i - 1.5) / 360;
            double xx = bitmapWidth / 2 + Math.cos(a) * radiusOutSide;
            double yy = bitmapHeight / 2 - Math.sin(a) * radiusOutSide;
            double xx2 = bitmapWidth / 2 + Math.cos(a) * radiusInside;
            double yy2 = bitmapHeight / 2 - Math.sin(a) * radiusInside;
            canvas.drawLine((float) xx2, (float) yy2, (float) xx, (float) yy, paint);
        }

        //画外圆上滚动小球
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color[currentScale == 0 ? 0 : currentScale - 1]);
        a = 2 * Math.PI * (240 - 3 * currentScale) / 360;
        double xx = bitmapWidth / 2 + Math.cos(a) * radiusMax;
        double yy = bitmapHeight / 2 - Math.sin(a) * radiusMax;
        canvas.drawCircle((float) xx, (float) yy, indicatorRadius, paint);

        //画中间文本（圆心处）
        paint.setStrokeWidth(1);
        paint.setTextSize(400);
        fontMetrics = paint.getFontMetrics();
        text = currentScale * 150 / scaleNum + "";
        paint.getTextBounds(text, 0, text.length(), rect);
        float textWidth = paint.measureText(text);
        x = (bitmapWidth - textWidth) / 2;
        y = bitmapHeight / 2 + Math.abs(fontMetrics.descent + fontMetrics.ascent) / 2;
        canvas.drawText(text, x, y, paint);
        rect.left = (int) x;
        rect.right = (int) (x + textWidth);
        rect.top = (bitmapHeight - rect.height()) / 2;
        rect.bottom = (bitmapHeight - rect.height()) / 2 + rect.height();

        //画单位
        paint.setTextSize(80);
        text = "km/h";
        textWidth = paint.measureText(text);
        x = (bitmapWidth - textWidth) / 2;
        y = y + fontMetrics.descent + 50;
        canvas.drawText(text, x, y, paint);

        //画总里程
        paint.setTextSize(60);
        paint.setColor(clrNormal);
        text = MyApplication.getContext().getString(R.string.total_mileage) + ":" + ConvertUtils.saveDecimals(1, currentTotalMileage) + " km";
        paint.getTextBounds(text, 0, text.length(), rect);
        x = (bitmapWidth - rect.width()) / 2;
        y = (float) (pointScale[0].y - padding / 2);
        canvas.drawText(text, x, y, paint);

        //Canvas画布，绘制Bitmap操作
        return bitmap;
    }

    /**
     * 设置当前刻度
     * @param scale
     */
    public void setCurrentScale(int scale) {
        currentScale = scale >= 0 ? scale : 0;
    }

    /**
     * 设置当前总里程
     * @param totalMileage
     */
    public void setCurrentTotalMileage(double totalMileage) {
        currentTotalMileage = totalMileage >= 0 ? totalMileage : 0;
    }

}
