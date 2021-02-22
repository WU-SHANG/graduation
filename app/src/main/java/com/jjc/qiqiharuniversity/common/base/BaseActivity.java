package com.jjc.qiqiharuniversity.common.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.view.TitleBarView;


/**
 * Author jiajingchao
 * Created on 2021/2/19
 * Description:
 */
public abstract class BaseActivity extends AppCompatActivity {

    public TitleBarView titleBarView;
    public static final String KEY_DATA = "data";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setScreenOrientation();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getRootLayout());
        hideStatusBarMode(true, Color.BLACK);
        initView(savedInstanceState);
        initData();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void setScreenOrientation() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void initView(@Nullable Bundle savedInstanceState) {

    }

    public void initData() {

    }

    public abstract int getRootLayout();

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 用于防止崩溃时，Fragment的重叠问题
//        super.onSaveInstanceState(outState);
    }

    public void initTitleBar() {
        titleBarView = findViewById(R.id.title_bar);
    }

    public static void start(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public void showContent() {

    }

    public int getScreenOrientation() {
        return getResources().getConfiguration().orientation;
    }

    public boolean isLandscape() {
        return getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    public void release() {

    }

    public void pause() {

    }

    public void resume() {

    }

    public ViewGroup getRootView() {
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }

    /**
     * 修改状态栏模式，支持6.0以上版本
     *
     * @param isTextWhite 文字、图标是否为黑色 （true为默认的白色）
     * @param color       状态栏颜色
     */
    public void hideStatusBarMode(boolean isTextWhite, int color) {
        //Android6.0（API 23）以上使用系统方法，6.0以下使用默认方法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //默认显示导航栏
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            //取消状态栏透明和变为全屏模式模式。
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //表明会Window负责系统bar的background 绘制，绘制透明背景的系统bar,设置setStatusBarColor需要配合这个参数
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(color);
            if (!isTextWhite) {
                //设置字体的颜色为亮色模式
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void hideStatusBarMode() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
