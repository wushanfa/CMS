package com.gentlehealthcare.mobilecare.activity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gentlehealthcare.mobilecare.R;

/**
 * 带有工具栏的Activity父类
 * Created by ouyang on 15/5/20.
 */
public abstract class ABToolBarActivity extends BaseActivity {
    private ImageButton ibtn_toolbar_left, ibtn_toolbar_right;
    private RadioGroup rg_toolbar;
    private boolean valid;//有效

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        valid = false;
        ibtn_toolbar_left = (ImageButton) findViewById(R.id.ibtn_toolbar_left);
        ibtn_toolbar_right = (ImageButton) findViewById(R.id.ibtn_toolbar_right);
        rg_toolbar = (RadioGroup) findViewById(R.id.rg_toolbar);
        ibtn_toolbar_right.setVisibility(View.INVISIBLE);
        ibtn_toolbar_left.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ShowGestWindow();
    }

    /**
     * toolbar 动态添加多个工具栏下的图标
     *
     * @param drawableIds 图标ID
     */
    public void setToolBarDrawable(int[] drawableIds) {
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM;
        for (int i = 0; i < drawableIds.length; i++) {
            RadioButton radioButton = new RadioButton(ABToolBarActivity.this);
            radioButton.setBackgroundResource(drawableIds[i]);
            radioButton.setLayoutParams(layoutParams);
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setButtonDrawable(null);

            radioButton.setId(i);
            rg_toolbar.addView(radioButton);
        }
    }

    /**
     * 按键响应事件
     *
     * @param abDoToolBar
     */
    public void setAbDoToolBar(final ABDoToolBar abDoToolBar) {
        rg_toolbar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!valid) {
                    valid = true;
                    return;
                }
                abDoToolBar.onCheckedChanged(i);
            }
        });
        ibtn_toolbar_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abDoToolBar.onLeftBtnClick();
            }
        });
        ibtn_toolbar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abDoToolBar.onRightBtnClick();
            }
        });
    }

    /**
     * 当前界面
     *
     * @param checkId 对应ID
     */
    public void setCheck(int checkId) {
        setValid(false);
        rg_toolbar.check(checkId);
        setValid(true);
    }

    public void changeCheck(int checkId) {
        setValid(true);
        rg_toolbar.check(checkId);
    }


    /**
     * 设置右边的按钮图标
     *
     * @param drawableId
     */
    public void setRightBtnDrawable(int drawableId) {
        ibtn_toolbar_right.setBackgroundResource(drawableId);
        ibtn_toolbar_right.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边按钮是否可见
     *
     * @param visible
     */
    public void setRightBtnVisible(int visible) {
        ibtn_toolbar_right.setVisibility(visible);
    }

    /**
     * 设置左边的按钮图标
     *
     * @param drawableId
     */
    public void setLeftBtnDrawable(int drawableId) {
        ibtn_toolbar_left.setBackgroundResource(drawableId);
    }

    /**
     * 设置左边按钮是否可见
     *
     * @param visible
     */
    public void setLeftBtnVisible(int visible) {
        ibtn_toolbar_left.setVisibility(visible);
    }


}
