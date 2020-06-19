package com.zh.customerview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hongzhang on 2020/6/18.
 * 同心圆
 */
public class ConcentricCircle extends View {

    private Paint mPaint;

    public ConcentricCircle(Context context) {
        this(context, null);
    }

    public ConcentricCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConcentricCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }

    private int mRadius = 60;

    @Override
    protected void onDraw(Canvas canvas) {


        mPaint.setColor(Color.RED);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(mRadius, mRadius, mRadius * 2 / 3, mPaint);
        mPaint.setColor(Color.YELLOW);
        canvas.drawCircle(mRadius, mRadius, mRadius / 3, mPaint);

//        mPaint.setColor(Color.RED);
//        canvas.drawCircle(200, 200, 20, mPaint);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(Color.BLUE);
//        mPaint.setStrokeWidth(20);
//        canvas.drawCircle(200, 200, 20, mPaint);
//
//        mPaint.setColor(Color.GREEN);
//        mPaint.setStrokeWidth(30);
//        canvas.drawCircle(200, 200, 60, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = mRadius * 2;
        int h = mRadius * 2;
//        setMeasuredDimension(500,500);
        setMeasuredDimension(resolveSize(w,widthMeasureSpec),resolveSize(h,heightMeasureSpec));
    }
}
