package com.zh.customerview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zh.customerview.utils.DeviceUtil;
import com.zh.customerview.view.others.ChartPoint;

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

    private Point[]  mPoint;
    private Point[] mLastPoint=new Point[7];
    private ValueAnimator valueAnimator;
    private int perDotLength;
    private Path xyPath;//画横纵坐标的path

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
//        mPoint[0] = new Point(1, 40);
//        mPoint[1] = new Point(2, 60);
//        mPoint[2] = new Point(3, 10);
//        mPoint[3] = new Point(4, 70);
//        mPoint[4] = new Point(5, 30);
//        mPoint[5] = new Point(6, 20);
//        mPoint[6] = new Point(7, 50);
        xyPath=new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
       if (null ==mPoint || mPoint.length==0){
           return;
       }
       mLinePaint.setStyle(Paint.Style.STROKE);
       mLinePaint.setStrokeWidth(1);
       mLinePaint.setStrokeCap(Paint.Cap.ROUND);

        xyPath.reset();
       xyPath.moveTo(100,0);
       xyPath.lineTo(100,getWidth()-100-100);
       xyPath.lineTo(getWidth(),getWidth()-100-100);
       canvas.drawPath(xyPath,mLinePaint);
        //画横线
//        canvas.drawLine(100, getWidth() - 100 - 100, getWidth() - 50, getWidth() - 100 - 100, mLinePaint);
//        //画竖线
//        canvas.drawLine(100, 0, 100, getWidth() - 100 - 100, mLinePaint);
        perDotLength = (getWidth() - 100 - 100) / 7;
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
            canvas.drawCircle(100 + point.x * perDotLength, getWidth() - 100 - 100 - (float)point.y/10 * perDotLength,20, mPointPaint);
            if (i != 6) {
                Point point1 = mPoint[i + 1];
                canvas.drawLine(100 + point.x * perDotLength, getWidth() - 100 - 100 - (float)point.y/10 * perDotLength, 100 + point1.x * perDotLength, getWidth() - 100 - 100 - (float)point1.y/10 * perDotLength, mLinePaint);
            }
        }
    }

    private Point[] mXPoint=new Point[7];
    public void setPointArray(Point[] pointArray) {

        for (int i=0,len=pointArray.length;i<len;i++){
            mXPoint[i]=new Point(pointArray[i].x,pointArray[i].y);
        }
        this.mPoint=pointArray;
        for (int i=0,len=mPoint.length;i<len;i++){
          final   Point point = mPoint[i];
            if (null !=mLastPoint && mLastPoint.length>0&& mLastPoint[i]!=null){
                int lastY = mLastPoint[i].y;
                int pointY = point.y;
                valueAnimator = ValueAnimator.ofInt(lastY,pointY);
            }else {
                valueAnimator = ValueAnimator.ofInt(point.y);//getWidth() - 100 - 100
            }
            valueAnimator.setDuration(500);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    point.y=value;
                    invalidate();
                }
            });
            valueAnimator.start();
        }
        for (int i=0,len=mXPoint.length;i<len;i++){
            mLastPoint[i]=new Point(mXPoint[i].x,mXPoint[i].y);
        }

//        mLastPoint=mPoint;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       if (event.getAction()==MotionEvent.ACTION_DOWN){
           int  x = (int) event.getX();
            int y = (int) event.getY();
            for(int i=0,len=mPoint.length;i<len;i++){
                Point point = mPoint[i];
                double pow1 = Math.pow(x - (100+point.x*perDotLength), 2);
                double pow2 = Math.pow(y - (float)(70-point.y)/10*perDotLength, 2);
                double sqrt = Math.sqrt(pow2 + pow1);
                if (sqrt<30){
                    if (null !=mOnPointClickListener){
                        mOnPointClickListener.onPointClick(i,point);
                    }
                }

            }
        }
        return super.onTouchEvent(event);
    }

    private OnPointClickListener mOnPointClickListener;
    /**
     * 坐标点点击监听
     */
    public interface OnPointClickListener {
        /**
         * @param index 当前坐标点在数据集中的下标
         * @param point 当前坐标点对象
         */
        void onPointClick(int index, Point point);
    }

    public void setOnPointClickListener(OnPointClickListener onPointClickListener) {
        mOnPointClickListener = onPointClickListener;
    }
}
