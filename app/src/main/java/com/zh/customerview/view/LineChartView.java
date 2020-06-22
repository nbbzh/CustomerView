package com.zh.customerview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.zh.customerview.utils.DeviceUtil;

/**
 * Created by hongzhang on 2020/6/22.
 * 自定义的折线图
 * https://blog.csdn.net/qq_31715429/article/details/54022507
 */
public class LineChartView extends View {

    private Paint mLinePaint;
    private Paint mPointPaint;
    private Paint mTextPaint;
    private Rect textRect;

    private Point[] mPoint = new Point[7];

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //读取自定义属性

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLUE);

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(Color.BLUE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(14);
        textRect = new Rect();
        mPoint[0] = new Point(1, 40);
        mPoint[1] = new Point(2, 60);
        mPoint[2] = new Point(3, 10);
        mPoint[3] = new Point(4, 70);
        mPoint[4] = new Point(5, 30);
        mPoint[5] = new Point(6, 20);
        mPoint[6] = new Point(7, 50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画横线
        canvas.drawLine(100, getWidth() - 100 - 100, getWidth() - 50, getWidth() - 100 - 100, mLinePaint);
        //画竖线
        canvas.drawLine(100, 0, 100, getWidth() - 100 - 100, mLinePaint);
        int perDotLength = (getWidth() - 100 - 100) / 7;
        //画横向的点
        for (int i = 0; i < 7; i++) {
            canvas.drawCircle(100 + (i + 1) * perDotLength, getWidth() - 100 - 100, 2, mPointPaint);
        }
        //画竖向的点
        for (int i = 0; i < 7; i++) {
            canvas.drawCircle(100, getWidth() - 100 - 100 - perDotLength * (i + 1), 2, mPointPaint);
        }

        //画横向的text
        for (int i = 0; i < 7; i++) {
            String text = (i + 1) + "";
            mTextPaint.getTextBounds(text, 0, text.length(), textRect);
            canvas.drawText(text, 100 + (i + 1) * perDotLength - textRect.width() / 2, getWidth() - 100 - 100 + textRect.height() / 2 + 10, mTextPaint);
        }

        //画竖向的text
        for (int i = 0; i < 7; i++) {
            String text = (i + 1) * 10 + "";
            mTextPaint.getTextBounds(text, 0, text.length(), textRect);
            canvas.drawText(text, 100 - textRect.width() - 10, getWidth() - 100 - 100 - perDotLength * (i + 1) + textRect.height() / 2, mTextPaint);
        }
        //画折线图
        for (int i = 0, len = mPoint.length; i < len; i++) {
            Point point = mPoint[i];
            canvas.drawCircle(100 + point.x * perDotLength, getWidth() - 100 - 100 - point.y/10 * perDotLength,4, mPointPaint);
            if (i != 6) {
                Point point1 = mPoint[i + 1];
                canvas.drawLine(100 + point.x * perDotLength, getWidth() - 100 - 100 - point.y/10 * perDotLength, 100 + point1.x * perDotLength, getWidth() - 100 - 100 - point1.y/10 * perDotLength, mLinePaint);
            }
        }
    }

    public void setPointArray(Point[] mPoint) {
        this.mPoint=mPoint;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);
        int resultSize;
        if (mode==MeasureSpec.EXACTLY){
            resultSize=size;
        }else {
            resultSize= DeviceUtil.getDisplayWidth(getContext());
        }
        setMeasuredDimension(resultSize,resultSize);
    }
}
