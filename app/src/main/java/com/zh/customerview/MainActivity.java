package com.zh.customerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.zh.customerview.view.StepView;

public class MainActivity extends AppCompatActivity {
    private String[] texts = {"确认身份信息", "确认入住信息", "选择房型", "支付押金", "完成入住 "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StepView step1 = (StepView) findViewById(R.id.step1);
        step1.setDescription(texts);
        step1.setStep(StepView.Step.TWO);
        step1.setClickable(true);
    }

}
