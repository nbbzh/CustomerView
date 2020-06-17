package com.zh.customerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zh.customerview.R;

/**
 * Created by hongzhang on 2020/6/16.
 */
public class StepView extends View {

    private int defaultNormalLineColor;
    private int defaultPassLineColor;
    private int defaultTextColor;
    private Bitmap normal_pic;
    private Bitmap target_pic;
    private Bitmap passed_pic;
    private int defaultLineStikeWidth;
    private int defaultTextSize;
    private int defaultText2DotMargin;
    private int defalutMargin;
    private int defaultLine2TopMargin;
    private int defaultText2BottomMargin;

    private int defaultDotCount = 4;
    private int defaultStepNum = 0;
    private int defaultLineLength = 0;
    private int defaultMaxDotCount = 0;
    private boolean defaultViewClickable = true;
    private int dotCount;//步骤数
    private int stepNum;//当前第几步骤
    private int lineLength;//线条长度是否可变
    private int normalLineColor;
    private int passLineColor;
    private int textColor;
    private float lineStikeWidth;
    private float textSize;
    private float margin;//view距离左右两边的距离
    private float line2TopMargin;//线到顶部的距离
    private float line2BottomMargin;//线到底部的距离
    private float text2BottomMargin;//文字距离底部的距离
    private float text2TopMargin;//文字距离顶部的距离
    private boolean clickable;
    private boolean isTextBelowLine = true;//文字在线的下面
    private Paint linePaint;
    private Paint textPaint;
    private Rect bounds;
    private float width;//view的宽
    private int height;//view的高
    private float perLineLength;
    private int maxDotCount;
    private int[] passWH;//存放已通过步骤图片的宽高
    private int[] normalWH;//存放未通过步骤图片的宽高
    private int[] targetWH;//存放当前步骤图片的宽高

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private int dp2px(Context context, int value) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * value + 0.5f);
    }

    private int sp2px(Context context, int value) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (value / scaledDensity + 0.5f);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        defaultNormalLineColor = Color.parseColor("#545454");
        defaultPassLineColor = Color.WHITE;
        defaultTextColor = Color.WHITE;
        defaultLineStikeWidth = dp2px(context, 1);
        defaultTextSize = sp2px(context, 80);
        defaultText2DotMargin = dp2px(context, 15);
        defalutMargin = dp2px(context, 100);
        defaultLine2TopMargin = dp2px(context, 30);
        defaultText2BottomMargin = dp2px(context, 20);

        normal_pic = BitmapFactory.decodeResource(getResources(), R.mipmap.normal_pic);
        target_pic = BitmapFactory.decodeResource(getResources(), R.mipmap.target_pic);
        passed_pic = BitmapFactory.decodeResource(getResources(), R.mipmap.passed_pic);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StepView, defStyleAttr, 0);
        dotCount = a.getInt(R.styleable.StepView_count, defaultDotCount);
        if (dotCount < 2) {
            throw new IllegalArgumentException("Steps can't be less than 2");
        }
        stepNum = a.getInt(R.styleable.StepView_step, defaultStepNum);
        lineLength = a.getInt(R.styleable.StepView_line_length, defaultLineLength);
        maxDotCount = a.getInt(R.styleable.StepView_max_dot_count, defaultMaxDotCount);
        if (maxDotCount < dotCount) {//当最多点小于设置点数量时，设置线条长度可变
            lineLength = defaultLineLength;
        }
        normalLineColor = a.getColor(R.styleable.StepView_normal_line_color, defaultNormalLineColor);
        passLineColor = a.getColor(R.styleable.StepView_passed_line_color, defaultPassLineColor);
        lineStikeWidth = a.getDimension(R.styleable.StepView_line_stroke_width, defaultLineStikeWidth);
        textColor = a.getColor(R.styleable.StepView_text_color, defaultTextColor);
        textSize = a.getDimension(R.styleable.StepView_text_size, defaultTextSize);
        margin = (int) a.getDimension(R.styleable.StepView_margin, defalutMargin);
        line2TopMargin = a.getDimension(R.styleable.StepView_line_to_top_margin, defaultLine2TopMargin);
        text2BottomMargin = a.getDimension(R.styleable.StepView_text_to_bottom_margin, defaultText2BottomMargin);
        clickable = a.getBoolean(R.styleable.StepView_is_view_clickable, defaultViewClickable);
        a.recycle();

        if (!isTextBelowLine) {
            line2BottomMargin = line2TopMargin;
            text2TopMargin = text2BottomMargin;
        }

        //线条画笔
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineStikeWidth);
        //文字画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        //存放说明文字的矩形
        bounds = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w - margin * 2;
        height = h;
        //线条长度是否可变
        if (lineLength == defaultLineLength) {
            //可变
            perLineLength = width / (dotCount - 1);
        } else {
            perLineLength = width / (maxDotCount - 1);
        }

        passWH = calculateWidthAndHeight(passed_pic);
        normalWH = calculateWidthAndHeight(normal_pic);
        targetWH = calculateWidthAndHeight(target_pic);
    }

    /*计算 bitmap 宽高*/
    private int[] calculateWidthAndHeight(Bitmap bitmap) {
        int[] wh = new int[2];
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        wh[0] = width;
        wh[1] = height;
        return wh;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawConnectLine(canvas, stepNum);
        drawTargetSquar(canvas, stepNum);
        drawNormalSquar(canvas, stepNum);
        drawDescText(canvas);
    }

    /**
     * 绘制连接步骤点之间的线
     */
    private void drawConnectLine(Canvas canvas, int stepNum) {
        float startX;
        float stopX ;
        for (int i = 0; i < dotCount - 1; i++) {
            //设置线条起点x轴坐标
            if (i == stepNum) {
                startX = margin + perLineLength * i + targetWH[0] / 2;
            } else if (i > stepNum) {
                startX = margin + perLineLength * i + normalWH[0] / 2;
            } else {
                startX = margin + perLineLength * i + passWH[0] / 2;
            }
            //设置线条终点x轴坐标
            if (i + 1 == stepNum) {
                stopX = margin + perLineLength * (i + 1) - targetWH[0] / 2;
            } else if (i + 1 < stepNum) {
                stopX = margin + perLineLength * (i + 1) - passWH[0] / 2;
            } else {
                stopX = margin + perLineLength * (i + 1) - normalWH[0] / 2;
            }

            /*当目标步骤超过 i 时，线条设置为已过颜色，不超过时，设置为普通颜色*/
            if (stepNum > i) {
                linePaint.setColor(passLineColor);
            } else {
                linePaint.setColor(normalLineColor);
            }

            if (isTextBelowLine) {
                /*当文字在线条下方时，设置线条 y 轴的位置并绘制*/
                canvas.drawLine(startX, line2TopMargin, stopX, line2TopMargin, linePaint);
            } else {
                canvas.drawLine(startX, height - line2BottomMargin, stopX, height - line2BottomMargin, linePaint);
            }
        }
    }

    /**
     * 画普通步骤点
     */
    private void drawNormalSquar(Canvas canvas, int stepNum) {
        for (int i = 0; i < dotCount; i++) {
            if (stepNum == i) {
                continue;
            }
            if (stepNum > i) {
                float left = margin + perLineLength * i - passWH[0] / 2;
                float top = 0;
                if (isTextBelowLine) {
                    top = line2TopMargin - passWH[1] / 2;
                } else {
                    top = height - line2BottomMargin - passWH[1] / 2;
                }
                canvas.drawBitmap(passed_pic, left, top, null);
            } else {
                float left = margin + perLineLength * i - normalWH[0] / 2;
                float top;
                if (isTextBelowLine) {
                    top = line2TopMargin - normalWH[1] / 2;
                } else {
                    top = height - line2BottomMargin - normalWH[1] / 2;
                }
                canvas.drawBitmap(normal_pic, left, top, null);
            }
        }
    }

    /**
     * 画目标步骤图片
     */
    private void drawTargetSquar(Canvas canvas, int i) {
        float left = margin + perLineLength * i - targetWH[0] / 2;
        float top = 0;
        if (isTextBelowLine) {
            top = line2TopMargin - targetWH[1] / 2;
        } else {
            top = height - line2BottomMargin - targetWH[1] / 2;
        }
        canvas.drawBitmap(target_pic, left, top, null);
    }

    /**
     * 画步骤点说明文字
     */
    private void drawDescText(Canvas canvas) {
        for (int i = 0; i < dotCount; i++) {
            String text = "test text!";
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            int textWidth = bounds.width();
            int textHeight = bounds.height();
            float x = margin + perLineLength * i - textWidth / 2;
            float y;
            if (isTextBelowLine) {
                y = height - text2BottomMargin;
            } else {
                y = text2TopMargin + textHeight;
            }
            canvas.drawText(text, x, y, textPaint);
        }
    }

    /**
     * 根据用户设置的是否可点击，给StepView添加点击事件
     */
    //思路：当用户点击时，View通过touch事件监听用户点击的x/y值，然后转化为point,
    //再通过判断point是否在几个步骤点区域范围内，返回对应的步骤点值，然后重新绘制View--
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (clickable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Point point = new Point();
                    point.x = (int) event.getX();
                    point.y = (int) event.getY();
                    int stepInDots = getStepInDots(point);
                    if (stepInDots != -1) {
                        stepNum = stepInDots;
                        invalidate();
                    }
                    break;
                default:
                    break;

            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取手指触摸点是第几个步骤点，不是触摸点是返回-1
     */
    private int getStepInDots(Point point) {
        for (int i = 0; i < dotCount; i++) {
            Rect rect = getStepSquarRects()[i];
            int x = point.x;
            int y = point.y;
            if (rect.contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取所有步骤点的矩阵区域
     */
    private Rect[] getStepSquarRects() {
        Rect[] rects = new Rect[dotCount];
        int left, top, right, bottom;
        for (int i = 0; i < dotCount; i++) {
            //此处默认所有点的区域范围为被选中图片的区域范围
            Rect rect = new Rect();
            left = (int) (margin + perLineLength * i - targetWH[0] / 2);
            right = (int) (margin + perLineLength * i + targetWH[0] / 2);
            if (isTextBelowLine) {
                top = (int) (line2TopMargin - targetWH[1] / 2);
                bottom = (int) (line2TopMargin + targetWH[1] / 2);
            } else {
                top = (int) (height - line2BottomMargin - targetWH[1] / 2);
                bottom = (int) (height - line2BottomMargin + targetWH[1 / 2]);
            }
            rect.set(left, top, right, bottom);
            rects[i] = rect;
        }

        return rects;

    }

    /*给外部调用接口，设置步骤总数*/
    public void setDotCount(int count) {
        if (count < 2) {
            throw new IllegalArgumentException("dot count can't be less than 2.");
        }
        dotCount = count;
    }

    private String[] texts;

    /*给外部调用接口，设置说明文字信息*/
    public void setDescription(String[] descs) {
        if (descs == null || descs.length < dotCount) {
            throw new IllegalArgumentException("Descriptions can't be null or its length must maore than dot count");
        }
        texts = descs;
    }

    /*给外部调用接口，设置该 view 是否可点击*/
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    /*给外部调用接口，设置步骤*/
    public void setStep(Step step) {
        switch (step) {
            case ONE:
                stepNum = 0;
                break;
            case TWO:
                stepNum = 1;
                break;
            case THREE:
                stepNum = 2;
                break;
            case FOUR:
                stepNum = 3;
                break;
            case FIVE:
                stepNum = 4;
                break;
            default:
        }
        invalidate();
    }

    public enum Step {
        ONE, TWO, THREE, FOUR, FIVE
    }
}
