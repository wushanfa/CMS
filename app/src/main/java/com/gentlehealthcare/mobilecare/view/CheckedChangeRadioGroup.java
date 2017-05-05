package com.gentlehealthcare.mobilecare.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RadioGroup;

/**
 * Created by ouyang on 15/6/24.
 */
public class CheckedChangeRadioGroup extends RadioGroup {
    private boolean canChange;
    public CheckedChangeRadioGroup(Context context) {
        super(context);
    }

    public CheckedChangeRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
         super.dispatchTouchEvent(ev);
        return false;
    }

    public boolean isCanChange() {
        return canChange;
    }

    public void setCanChange(boolean canChange) {
        this.canChange = canChange;
    }
}
