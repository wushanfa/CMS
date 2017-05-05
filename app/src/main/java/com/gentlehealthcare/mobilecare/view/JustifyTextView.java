package com.gentlehealthcare.mobilecare.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;

/**
 * Created by zhiwei on 2016/5/26.
 */
public class JustifyTextView extends TextView {

    private int mLineY;
    private int mViewWidth;
    public static final String TWO_CHINESE_BLANK = "  ";

    public OrderListBean bean;

    public String orderText;

    public JustifyTextView(Context context) {
        super(context);
    }

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JustifyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mOnLayoutListener != null) {
            mOnLayoutListener.onLayouted(bean, this);
        }
        if (mOnLayoutListenerSimple != null) {
            mOnLayoutListenerSimple.onLayoutedSimple(this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        TextPaint paint = getPaint();
//        paint.setColor(getCurrentTextColor());
//        paint.drawableState = getDrawableState();
//        mViewWidth = getMeasuredWidth();
//        String text = getText().toString();
//        mLineY = 0;
//        mLineY += getTextSize();
//        Layout layout = getLayout();
//        if (layout == null) {
//            return;
//        }
//
//        Paint.FontMetrics fm = paint.getFontMetrics();
//
//        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
//        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout
//                .getSpacingAdd());
//
//        for (int i = 0; i < layout.getLineCount(); i++) {
//            int lineStart = layout.getLineStart(i);
//            int lineEnd = layout.getLineEnd(i);
//            float width = StaticLayout.getDesiredWidth(text, lineStart,
//                    lineEnd, getPaint());
//            String line = text.substring(lineStart, lineEnd);
//            //判断是否拉伸
//            if (needScale(line)) {
//                if (i == layout.getLineCount() - 1) {//最后一行不拉伸
//                    canvas.drawText(line, 0, mLineY, paint);
//                } else {
//                    drawScaledText(canvas, lineStart, line, width);
//                }
//            } else {
//                canvas.drawText(line, 0, mLineY, paint);
//            }
//            mLineY += textHeight;
//        }
    }

    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth) {
        float x = 0;
        if (isFirstLineOfParagraph(lineStart, line)) {
            String blanks = "  ";
            canvas.drawText(blanks, x, mLineY, getPaint());
            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());
            x += bw;
            line = line.substring(3);
        }

        int gapCount = line.length() - 1;
        int i = 0;
        if (line.length() > 2 && line.charAt(0) == 12288
                && line.charAt(1) == 12288) {
            String substring = line.substring(0, 2);
            float cw = StaticLayout.getDesiredWidth(substring, getPaint());
            canvas.drawText(substring, x, mLineY, getPaint());
            x += cw;
            i += 2;
        }

        float d = (mViewWidth - lineWidth) / gapCount;
        for (; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mLineY, getPaint());
            x += cw + d;
        }
    }

    @Override
    public int getLineCount() {
        /**
         * 现在写法太简单了，直接整除取整，非常不好
         */
        int lineCount = super.getLineCount();
        double lineNum = 2d;
        if (bean != null) {
            lineNum = bean.getOrderText().split("\\|\\|").length;
        }
        return (int) Math.ceil(lineCount / lineNum);
    }

    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == ' '
                && line.charAt(1) == ' ';
    }

    private boolean needScale(String line) {
        if (line == null || line.length() == 0) {
            return false;
        } else {
            return line.charAt(line.length() - 1) != '\n';
        }
    }

    public interface OnLayoutListener {
        void onLayouted(OrderListBean bean, TextView view);
    }

    public interface OnLayoutListenerSimple {
        void onLayoutedSimple(TextView view);
    }

    private OnLayoutListener mOnLayoutListener;

    private OnLayoutListenerSimple mOnLayoutListenerSimple;

    public void setOnLayoutListener(OrderListBean bean, OnLayoutListener listener) {
        mOnLayoutListener = listener;
        this.bean = bean;
    }

    public void setOnLayoutListener(OnLayoutListenerSimple listener) {
        mOnLayoutListenerSimple = listener;
    }
}
