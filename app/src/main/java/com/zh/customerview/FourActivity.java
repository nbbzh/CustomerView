package com.zh.customerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zh.customerview.view.others.ChartPoint;
import com.zh.customerview.view.others.LineChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongzhang on 2020/6/23.
 */
public class FourActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);
        final LineChartView chartView = (LineChartView) findViewById(R.id.chartView);
        Button btn = (Button) findViewById(R.id.btn);
        final TextView tv = (TextView) findViewById(R.id.tv);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> xList = new ArrayList<>();
                List<Integer> yList = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    xList.add(i + 1);
                    int y = (int) (Math.random() * 70 + 1);
                    yList.add(y);
                }
                chartView.setDataList(xList, yList);
            }
        });
        chartView.setOnPointClickListener(new LineChartView.OnPointClickListener() {
            @Override
            public void onPointClick(int position, ChartPoint point) {
                tv.setText("position:" + position + "\nx:" + point.getxData() + "\ny:" + point.getyData());
            }
        });
    }
}
