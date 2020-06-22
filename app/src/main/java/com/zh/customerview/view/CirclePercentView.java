package com.zh.customerview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zh.customerview.R;

/**
 * Created by hongzhang on 2020/6/22.
 * 圆形的百分比控件
 */
public class CirclePercentView extends View {

    private int radius;
    private int arcWidth;
    private int textSize;
    private int arcColor;
    private int percentTextColor;
    private int circleBg;
    private Paint circlePaint;
    private Paint arcPaint;
    private Paint textPaint;
    private RectF rectF;
    private float mCurPercent;
    private Rect rectText;

    public CirclePercentView(Context context) {
        this(context, null);
    }

    public CirclePercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 只知识点1：getDimension(),getDimensionPixelOffset(),getDimensionPixelSize()的区别？
     * 3个方法都是把dp/sp数值成衣屏幕scale来换算成px数值，换算后有小数点，对小数点的不同处理方法
     * getDimension() 返回 float的px值 精确
     * getDimensionPixelOffset()  返回int型px值  直接把小数截断
     * getDimensionPixelSize()    返回int型px值  进行四舍五入
     * mTextView.setTextSize()，这个方法默认就是用sp为单位的
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView);
        radius = typedArray.getDimensionPixelOffset(R.styleable.CirclePercentView_radius, 10);
        arcWidth = typedArray.getDimensionPixelOffset(R.styleable.CirclePercentView_arcWidth, 10);
        textSize = typedArray.getDimensionPixelSize(R.styleable.CirclePercentView_percentTextSize, 14);
        arcColor = typedArray.getColor(R.styleable.CirclePercentView_arcColor, getResources().getColor(R.color.colorAccent));
        percentTextColor = typedArray.getColor(R.styleable.CirclePercentView_percentTextColor, getResources().getColor(R.color.colorAccent));
        circleBg = typedArray.getColor(R.styleable.CirclePercentView_circleBg, getResources().getColor(R.color.colorPrimary));
        typedArray.recycle();
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(circleBg);

        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(arcWidth);
        arcPaint.setColor(arcColor);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);//线帽，使圆弧两头圆滑

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(textSize);
        textPaint.setColor(percentTextColor);
        rectF = new RectF();//画圆弧的rectf
        //文字的Rect
        rectText = new Rect();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null !=mOnCircleClickListener){
                    mOnCircleClickListener.onClick(CirclePercentView.this);
                }
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultW;
        int resultH;
        if (mode == MeasureSpec.EXACTLY) {
            //具体数值和match_parent
            resultW = size;
        } else {
            resultW = radius * 2;
            if (mode == MeasureSpec.AT_MOST) {
                resultW = Math.min(hSize, radius * 2);
            }
        }

        if (hMode == MeasureSpec.EXACTLY) {
            resultH = hSize;
        } else {
            resultH = radius * 2;
            if (mode == MeasureSpec.AT_MOST) {
                resultH = Math.min(hSize, radius * 2);
            }
        }

        setMeasuredDimension(resultW, resultH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景圆
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, circlePaint);
        //绘制圆弧
        rectF.set(getWidth() / 2 - radius + arcWidth / 2, getHeight() / 2 - radius + arcWidth / 2, getWidth() / 2 + radius - arcWidth / 2, getHeight() / 2 + radius - arcWidth / 2);
        canvas.drawArc(rectF, 270, 360 * mCurPercent / 100, false, arcPaint);
        //计算文本宽高
        String text = mCurPercent + "%";
        textPaint.getTextBounds(text, 0, text.length(), rectText);
        //绘制文本,绘制文本的x,y参数是bounds的左下角的坐标
        canvas.drawText(text, getWidth() / 2 - rectText.width() / 2, getHeight() / 2 + rectText.height() / 2, textPaint);
    }

    public void setCurPercent(float curPencent){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(mCurPercent, curPencent);
        valueAnimator.setDuration((long)(Math.abs(mCurPercent-curPencent)*20));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                mCurPercent = ((float) Math.round(animatedValue * 10)) / 10;
                invalidate();
            }
        });
        valueAnimator.start();
    }

    private  OnClickListener mOnCircleClickListener;
    public void setOnCircleClickListener(OnClickListener onCircleClickListener){
        mOnCircleClickListener=onCircleClickListener;
    }
}
