package com.zh.customerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zh.customerview.R;

import java.util.ArrayList;

/**
 * Created by hongzhang on 2020/6/19.
 * 自定义流式布局
 */
public class FlowLayout extends ViewGroup {


    private int mVerticalGap;//垂直方向间距
    private int  mHorizonGap;//水平方向间距
    private boolean mIsDivide;//是否平分剩余空间
    private ArrayList<Row> mRows=new ArrayList<>();//存放的是每一行的数据
    private View child;
    private Row row;

    public FlowLayout(Context context) {
        this(context,null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mVerticalGap = typedArray.getDimensionPixelOffset(R.styleable.FlowLayout_v_margin, 10);
        mHorizonGap = typedArray.getDimensionPixelOffset(R.styleable.FlowLayout_h_margin, 10);
        mIsDivide = typedArray.getBoolean(R.styleable.FlowLayout_isDivideEqually, true);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        mRows.clear();
        Row row=new Row(widthSize);
        mRows.add(row);
        //遍历view,给view分行，分行的同时计算行高
        for (int i=0,len=getChildCount();i<len;i++){
            child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            if (row.isOutOfThisView(child)){
                //创建一个新的行
                row=new Row(widthSize);
                mRows.add(row);
                row.addView(child);
            }else {
                //这一行能放下
                row.addView(child);
            }
        }
        //遍历每一行计算行高
        int height=0;//最后需要的行高
        for (int i=0,len=mRows.size();i<len;i++){
            height+=mRows.get(i).mHeight+mVerticalGap;
        }

        setMeasuredDimension(widthSize,heightMode==MeasureSpec.EXACTLY?heightSize:height+getPaddingTop()+getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mRows.size()==0){
            return;
        }
        int  top=0;
        for (int i=0,len=mRows.size();i<len;i++){
            row = mRows.get(i);
            row.layout(top);
            top+=row.mHeight+mVerticalGap;
        }
    }

    private class Row{
        private int mMaxWidth;//最大行宽
        private int mNewWidth;//加入新view后的行宽
        private int mWidth;//之前view占有的宽度
        private int mHeight;//行高
        private ArrayList<View> mViews=new ArrayList<>();//存放这一行的view
        private View view;
        private int childWidth;

        public Row(int sizeWidth) {
            this.mMaxWidth=sizeWidth;
        }

        /**
         * 判断这一行能不能放下这个view
         */
        public boolean isOutOfThisView(View child) {
            if (mViews.size()==0){
                mNewWidth=child.getMeasuredWidth();
            }else {
                mNewWidth=mWidth+child.getMeasuredWidth()+mHorizonGap;
            }
            return mNewWidth>mMaxWidth;
        }

        public void addView(View child) {
            mViews.add(child);
            if (mViews.size()==1){
                mWidth=child.getMeasuredWidth();
            }else {
                mWidth=mNewWidth;
            }
            mHeight=child.getMeasuredHeight()<mHeight?mHeight:child.getMeasuredHeight();
        }

        public void layout(int top) {
            int left = getPaddingLeft();
            for (int i=0,len=mViews.size();i<len;i++){
                view = mViews.get(i);

                //平分剩余空间
                int splitCount = (mMaxWidth - mWidth) / len;
                if (mIsDivide && splitCount>0){
                    //更改view的宽
                    childWidth = view.getMeasuredWidth()+splitCount;
                    view.getLayoutParams().width= childWidth;
                    //重新测量view
                    int widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth , MeasureSpec.EXACTLY);
                    int heightMeasureSpec = MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), MeasureSpec.EXACTLY);
                    view.measure(widthMeasureSpec,heightMeasureSpec);
                }
                int measuredWidth = view.getMeasuredWidth();
                int measuredHeight = view.getMeasuredHeight();
                view.layout(left,top,left+measuredWidth,top+ measuredHeight);
                left+=measuredWidth+mHorizonGap;
            }

        }
    }
}
