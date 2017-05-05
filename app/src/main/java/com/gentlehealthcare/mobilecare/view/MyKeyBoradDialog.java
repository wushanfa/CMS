package com.gentlehealthcare.mobilecare.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.gentlehealthcare.mobilecare.R;

public class MyKeyBoradDialog extends Dialog implements View.OnClickListener {

    private Button one, two, three, four, five, six, seven, eight, nine, zero,
            point, btn_e, clear, btn_slash, maohao, underline, thirtysix,
            thirtyseven, thirtyeight, thirtynine;
    private Button forth, g, ml, btn_star, xiangshang;
    private myKeyListener listener;
    private Context context;
    private int myGravity;

    public MyKeyBoradDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public MyKeyBoradDialog(Context context, int myGravity, myKeyListener listener) {

        // dialog的视图风格
        // super(context, AlertDialog.THEME_HOLO_LIGHT);
        super(context, R.style.mykeyboard);
        this.context = context;
        this.listener = listener;
        this.myGravity = myGravity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置布局文件
        this.setContentView(R.layout.mykeyboard);

        // 单击dialog之外的地方，可以dismiss掉dialog。
        setCanceledOnTouchOutside(true);

        LayoutParams a = getWindow().getAttributes();
        a.gravity = myGravity;
        a.dimAmount = 0.0f; // 去背景遮盖
        a.alpha = 0.9f;

        getWindow().setAttributes(a);

        getWindow().addFlags(LayoutParams.FLAG_DIM_BEHIND);

        getWindow().setWindowAnimations(R.style.mykeyboard);

        initView();
    }

    private void initView() {
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);
        seven = (Button) findViewById(R.id.seven);
        eight = (Button) findViewById(R.id.eight);
        nine = (Button) findViewById(R.id.nine);
        zero = (Button) findViewById(R.id.zero);
        point = (Button) findViewById(R.id.point);
        btn_e = (Button) findViewById(R.id.btn_e);
        clear = (Button) findViewById(R.id.clear);
        btn_slash = (Button) findViewById(R.id.btn_slash);
        thirtyeight = (Button) findViewById(R.id.thirtyeight);
        thirtynine = (Button) findViewById(R.id.thirtynine);
        thirtyseven = (Button) findViewById(R.id.thirtyseven);
        thirtysix = (Button) findViewById(R.id.thirtysix);
        underline = (Button) findViewById(R.id.underline);
        maohao = (Button) findViewById(R.id.maohao);
        forth = (Button) findViewById(R.id.forth);
        g = (Button) findViewById(R.id.g);
        ml = (Button) findViewById(R.id.ml);
        btn_star = (Button) findViewById(R.id.btn_star);
        xiangshang = (Button) findViewById(R.id.xiangshang);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        point.setOnClickListener(this);
        btn_e.setOnClickListener(this);
        clear.setOnClickListener(this);
        btn_slash.setOnClickListener(this);
        thirtyeight.setOnClickListener(this);
        thirtynine.setOnClickListener(this);
        thirtyseven.setOnClickListener(this);
        thirtysix.setOnClickListener(this);
        underline.setOnClickListener(this);
        maohao.setOnClickListener(this);
        forth.setOnClickListener(this);
        g.setOnClickListener(this);
        ml.setOnClickListener(this);
        btn_star.setOnClickListener(this);
        xiangshang.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }

    public interface myKeyListener {
        void onClick(View view);
    }

}
