package com.zh.customerview;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

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

        FrameLayout fl = (FrameLayout) findViewById(R.id.fl);
        if (isDarkTheme()){
            fl.setVisibility(View.GONE);
        }

        findViewById(R.id.changeMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDarkTheme()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });
        findViewById(R.id.toSecond).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
        findViewById(R.id.toThree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ThreeActivity.class));

            }
        });
        findViewById(R.id.toFour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FourActivity.class));

            }
        });
    }

    /**
     * 适配深色主题模式
     * 1、Force Dark
     * 优点：快速适配，几乎不用编写额外代码；
     * 范围：应用使用的是浅色主题，原本使用深色主题的不适用；
     * 原理：系统会分享浅色主题应用下的每一层view,并且在view绘制到屏幕的之前自动将它们的颜色转换成更加适合深色主题的颜色
     * 操作：android:forceDarkAllowe
     *
     * 2、而是应该针对每一个界面都进行浅色和深色两种主题的界面设计
     * 需求1：根据不同的主题设置不同的代码
     * 3、在绝大多数情况下，让应用程序跟随系统的设置来决定使用浅色主题还是深色主题是最合适的一种做法。
     *然而如果你一定想要脱离系统设置，让自己的应用程序独立控制使用浅色主题还是深色主题，Android对此也是支持的，
     * 只要使用AppCompatDelegate.setDefaultNightMode()方法即可。
     * setDefaultNightMode()方法接收一个mode参数，用于控制当前应用程序的夜间模式。mode参数主要有以下值可供选择：
     *
     * MODE_NIGHT_FOLLOW_SYSTEM：默认模式，表示让当前应用程序跟随系统设置来决定使用浅色主题还是深色主题。
     * MODE_NIGHT_YES：脱离系统设置，强制让当前应用程序使用深色主题。
     * MODE_NIGHT_NO：脱离系统设置，强制让当前应用程序使用浅色主题。
     * MODE_NIGHT_AUTO_BATTERY：根据手机的电池状态来决定使用浅色主题还是深色主题，如果开启了节点模式，则使用深色主题。
     *
     *
     * 当调用setDefaultNightMode()方法并成功切换主题时,应用程序中所有处于started状态的activity都会重新创建
     *
     * 如果不想被重新创建以在Activity的configChanges属性当中配置uiMode来让当前Activity避免被重新创建
     * 现在当应用程序的主题发生变化时，MainActivity并不会重新创建，而是会触发onConfigurationChanged()方法的回调，
     * 你可以在回调当中手动做一些逻辑处理。
     *
     */

    private boolean isDarkTheme(){
        int  flag =getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return flag == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int  currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode==Configuration.UI_MODE_NIGHT_NO){
            // 夜间模式未启用，使用浅色主题
        }else if (currentNightMode==Configuration.UI_MODE_NIGHT_YES){
            //夜间模式启用，使用深色主题
        }
    }


}
