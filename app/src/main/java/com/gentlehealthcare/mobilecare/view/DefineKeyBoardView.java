package com.gentlehealthcare.mobilecare.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义键盘界面
 */

public class DefineKeyBoardView extends RelativeLayout {
    //分别对应回退键,清空键，1，2，3，4，5，6，7，8，9，0，小数点，关闭按钮。
    private TextView mBackOff, mClear, mOne, mTwo, mThree, mFour, mFive, mSix, mSeven, mEight, mNine, mZero, mDot, mShutDown;
    //需要修改的数字键
    private TextView mOneChange, mTwoChange, mThreeChange, mFourChange, mFiveChange, mSixChange, mSevenChange, mEightChange;
    private RelativeLayout mRelativeLayout;

    public DefineKeyBoardView(Context context) {
        this(context, null);
    }

    public DefineKeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.layout_define_keyboard, null);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.layout_define_keyboard_layoutBack);
        initUnChangeableView(view);
        initChangeableView(view);
        addView(view);      //必须要，不然不显示控件
    }

    //初始化改变的数字键
    private void initChangeableView(View view) {
        mOneChange = (TextView) view.findViewById(R.id.layout_define_keyboard_area_one);
        mTwoChange = (TextView) view.findViewById(R.id.layout_define_keyboard_area_two);
        mThreeChange = (TextView) view.findViewById(R.id.layout_define_keyboard_area_three);
        mFourChange = (TextView) view.findViewById(R.id.layout_define_keyboard_area_four);
        mFiveChange = (TextView) view.findViewById(R.id.layout_define_keyboard_area_six);
        mSixChange = (TextView) view.findViewById(R.id.layout_define_keyboard_area_seven);
        mSevenChange = (TextView) view.findViewById(R.id.layout_define_keyboard_area_eight);
        mEightChange = (TextView) view.findViewById(R.id.layout_define_keyboard_area_nine);
    }

    //初始化固定的数字键
    private void initUnChangeableView(View view) {
        mBackOff = (TextView) view.findViewById(R.id.layout_define_keyboard_area_five);
        mClear = (TextView) view.findViewById(R.id.layout_define_keyboard_area_ten);
        mOne = (TextView) view.findViewById(R.id.layout_define_keyboard_area_eleven);
        mTwo = (TextView) view.findViewById(R.id.layout_define_keyboard_area_twelve);
        mThree = (TextView) view.findViewById(R.id.layout_define_keyboard_area_thirteen);
        mFour = (TextView) view.findViewById(R.id.layout_define_keyboard_area_fourteen);
        mFive = (TextView) view.findViewById(R.id.layout_define_keyboard_area_sixteen);
        mSix = (TextView) view.findViewById(R.id.layout_define_keyboard_area_seventeen);
        mSeven = (TextView) view.findViewById(R.id.layout_define_keyboard_area_eighteen);
        mEight = (TextView) view.findViewById(R.id.layout_define_keyboard_area_nineteen);
        mNine = (TextView) view.findViewById(R.id.layout_define_keyboard_area_twenty);
        mZero = (TextView) view.findViewById(R.id.layout_define_keyboard_area_twenty_one);
        mDot = (TextView) view.findViewById(R.id.layout_define_keyboard_area_twenty_two);
        mShutDown = (TextView) view.findViewById(R.id.layout_define_keyboard_area_fifteen);
    }

    public RelativeLayout getLayoutBack() {
        return mRelativeLayout;
    }

    public Map<String, TextView> getUnChangeableView() {
        Map<String, TextView> textViewMap = new HashMap<>();
        textViewMap.put("<", mBackOff);
        textViewMap.put("清", mClear);
        textViewMap.put("1", mOne);
        textViewMap.put("2", mTwo);
        textViewMap.put("3", mThree);
        textViewMap.put("4", mFour);
        textViewMap.put("5", mFive);
        textViewMap.put("6", mSix);
        textViewMap.put("7", mSeven);
        textViewMap.put("8", mEight);
        textViewMap.put("9", mNine);
        textViewMap.put("0", mZero);
        textViewMap.put(".", mDot);
        textViewMap.put("关闭", mShutDown);
        return textViewMap;
    }

    public Map<String, TextView> getChangeableView() {
        Map<String, TextView> map = new HashMap<>();
        map.put("one", mOneChange);
        map.put("two", mTwoChange);
        map.put("three", mThreeChange);
        map.put("four", mFourChange);
        map.put("five", mFiveChange);
        map.put("six", mSixChange);
        map.put("seven", mSevenChange);
        map.put("eight", mEightChange);
        return map;
    }
}
