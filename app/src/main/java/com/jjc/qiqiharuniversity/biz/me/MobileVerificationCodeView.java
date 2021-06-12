package com.jjc.qiqiharuniversity.biz.me;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.jjc.qiqiharuniversity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author jiajingchao
 * Created on 2021/5/25
 * Description:验证码view
 */
public class MobileVerificationCodeView extends RelativeLayout {

    private TextView tvCode1;
    private TextView tvCode2;
    private TextView tvCode3;
    private TextView tvCode4;
    private TextView tvCode5;
    private TextView tvCode6;
    private View vCode1;
    private View vCode2;
    private View vCode3;
    private View vCode4;
    private View vCode5;
    private View vCode6;
    private EditText et_code;
    private List<String> codes = new ArrayList<>();
    private InputMethodManager inputMethodManager;
    private View mobileVerificationView;


    public MobileVerificationCodeView(Context context) {
        super(context);
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mobileVerificationView = LayoutInflater.from(context).inflate(R.layout.layout_mobile_verification_code, this);
        initView(mobileVerificationView);
        initEvent();
    }

    public MobileVerificationCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mobileVerificationView = LayoutInflater.from(context).inflate(R.layout.layout_mobile_verification_code, this);
        initView(mobileVerificationView);
        initEvent();
    }

    private void initView(View view) {
        tvCode1 = (TextView) view.findViewById(R.id.tv_code_1);
        tvCode2 = (TextView) view.findViewById(R.id.tv_code_2);
        tvCode3 = (TextView) view.findViewById(R.id.tv_code_3);
        tvCode4 = (TextView) view.findViewById(R.id.tv_code_4);
        tvCode5 = (TextView) view.findViewById(R.id.tv_code_5);
        tvCode6 = (TextView) view.findViewById(R.id.tv_code_6);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "DIN_Alternate_Bold.ttf");
        tvCode1.setTypeface(typeface);
        tvCode2.setTypeface(typeface);
        tvCode3.setTypeface(typeface);
        tvCode4.setTypeface(typeface);
        tvCode5.setTypeface(typeface);
        tvCode6.setTypeface(typeface);
        et_code = (EditText) view.findViewById(R.id.et_code);
        et_code.setLongClickable(false);
        vCode1 = view.findViewById(R.id.v1);
        vCode2 = view.findViewById(R.id.v2);
        vCode3 = view.findViewById(R.id.v3);
        vCode4 = view.findViewById(R.id.v4);
        vCode5 = view.findViewById(R.id.v5);
        vCode6 = view.findViewById(R.id.v6);
        et_code.setFocusable(true);
        et_code.requestFocus();
        et_code.setFocusableInTouchMode(true);
        et_code.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void initEvent() {
        //验证码输入
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() > 0) {
                    et_code.setText("");
                    if (codes.size() < 6) {
                        codes.add(editable.toString());
                        showCode();
                    }
                }
            }
        });

        // 监听验证码删除按键
        et_code.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN && codes.size() > 0) {
                    codes.remove(codes.size() - 1);
                    showCode();
                    return true;
                }
                return false;
            }
        });
    }

    private void showCode() {
        String code1 = "";
        String code2 = "";
        String code3 = "";
        String code4 = "";
        String code5 = "";
        String code6 = "";
        if (codes.size() >= 1) {
            code1 = codes.get(0);
        }
        if (codes.size() >= 2) {
            code2 = codes.get(1);
        }
        if (codes.size() >= 3) {
            code3 = codes.get(2);
        }
        if (codes.size() >= 4) {
            code4 = codes.get(3);
        }
        if (codes.size() >= 5) {
            code5 = codes.get(4);
        }
        if (codes.size() >= 6) {
            code6 = codes.get(5);
        }
        tvCode1.setText(code1);
        tvCode2.setText(code2);
        tvCode3.setText(code3);
        tvCode4.setText(code4);
        tvCode5.setText(code5);
        tvCode6.setText(code6);

        setColor();//设置高亮颜色
        callBack();//回调
    }

    private void setColor() {
        int color_default = Color.parseColor("#ebebeb");
        int color_focus = Color.parseColor("#ff5c4d");
        vCode1.setBackgroundColor(color_default);
        vCode2.setBackgroundColor(color_default);
        vCode3.setBackgroundColor(color_default);
        vCode4.setBackgroundColor(color_default);
        vCode5.setBackgroundColor(color_default);
        vCode6.setBackgroundColor(color_default);
        if (codes.size() == 0) {
            vCode1.setBackgroundColor(color_default);
        }
        if (codes.size() == 1) {
            vCode1.setBackgroundColor(color_focus);
        }
        if (codes.size() == 2) {
            vCode1.setBackgroundColor(color_focus);
            vCode2.setBackgroundColor(color_focus);
        }
        if (codes.size() == 3) {
            vCode1.setBackgroundColor(color_focus);
            vCode2.setBackgroundColor(color_focus);
            vCode3.setBackgroundColor(color_focus);
        }
        if (codes.size() == 4) {
            vCode1.setBackgroundColor(color_focus);
            vCode2.setBackgroundColor(color_focus);
            vCode3.setBackgroundColor(color_focus);
            vCode4.setBackgroundColor(color_focus);
        }
        if (codes.size() == 5) {
            vCode1.setBackgroundColor(color_focus);
            vCode2.setBackgroundColor(color_focus);
            vCode3.setBackgroundColor(color_focus);
            vCode4.setBackgroundColor(color_focus);
            vCode5.setBackgroundColor(color_focus);
        }
        if (codes.size() == 6) {
            vCode1.setBackgroundColor(color_focus);
            vCode2.setBackgroundColor(color_focus);
            vCode3.setBackgroundColor(color_focus);
            vCode4.setBackgroundColor(color_focus);
            vCode5.setBackgroundColor(color_focus);
            vCode6.setBackgroundColor(color_focus);
        }
    }

    private void callBack() {
        if (onInputListener == null) {
            return;
        }
        if (codes.size() == 6) {
            onInputListener.onSuccess(getPhoneCode());
        } else {
            onInputListener.onInput();
        }
    }

    //定义回调
    public interface OnInputListener {
        void onSuccess(String code);

        void onInput();
    }

    private OnInputListener onInputListener;

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }

    /**
     * 显示键盘
     */
    public void showSoftInput() {
        //显示软键盘
        if (inputMethodManager != null && et_code != null) {
            et_code.postDelayed(new Runnable() {
                @Override
                public void run() {
                    inputMethodManager.showSoftInput(et_code, 0);
                }
            }, 200);
        }
    }

    /**
     * 获得手机号验证码
     *
     * @return 验证码
     */
    public String getPhoneCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String code : codes) {
            stringBuilder.append(code);
        }
        return stringBuilder.toString();
    }

}
