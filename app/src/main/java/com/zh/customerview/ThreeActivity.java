package com.zh.customerview;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zh.customerview.view.CirclePercentView;
import com.zh.customerview.view.LineChartView;

/**
 * Created by hongzhang on 2020/6/22.
 */
public class ThreeActivity extends AppCompatActivity {

    private Point[] mPoint;
    private LineChartView lineChartView;
    private TextView clickPoint;

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
                for (int i=0;i<7;i++) {
                    mPoint[i] = new Point(i + 1, (int) (Math.random() * 70 + 1));
                }
                lineChartView.setPointArray(mPoint);
            }
        });
        clickPoint = findViewById(R.id.click_point);
        lineChartView.setOnPointClickListener(new LineChartView.OnPointClickListener() {
            @Override
            public void onPointClick(int index, Point point) {
                clickPoint.setText("点击的位置："+point.x +":"+point.y);
            }
        });
    }
}
