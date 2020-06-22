package com.zh.customerview;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zh.customerview.view.CirclePercentView;
import com.zh.customerview.view.LineChartView;

/**
 * Created by hongzhang on 2020/6/22.
 */
public class ThreeActivity extends AppCompatActivity {

    private Point[] mPoint;
    private LineChartView lineChartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        final CirclePercentView circlePercentView = findViewById(R.id.circleView);
        circlePercentView.setOnCircleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float v1 = (float) (Math.random() * 99 + 1);
                circlePercentView.setCurPercent(v1);
            }
        });
        mPoint = new Point[7];
        lineChartView = findViewById(R.id.lineChartView);
        findViewById(R.id.setData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPoint[0] = new Point(1, 70);
                mPoint[1] = new Point(2, 60);
                mPoint[2] = new Point(3, 50);
                mPoint[3] = new Point(4, 40);
                mPoint[4] = new Point(5, 30);
                mPoint[5] = new Point(6, 20);
                mPoint[6] = new Point(7, 10);
                lineChartView.setPointArray(mPoint);
            }
        });
    }
}
