package com.gentlehealthcare.mobilecare.common;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;

/**
 * Created by Administrator on 2016/12/9.
 */

public class ErrorView extends View implements ValueAnimator.AnimatorUpdateListener {

    private Paint mPaint;
    private Path dst1;
    private Path dst2;
    private Path dst3;
    private float v1 ;
    private float v2 ;
    private float v3 ;
    private Path mPath1;
    private Path mPath2;
    private Path mPath3;
    private PathMeasure mPathMeasure;
    private ValueAnimator valueAnimator1;
    private ValueAnimator valueAnimator2;
    private ValueAnimator valueAnimator3;
    private float mLineWidth = 10;
    private Path path;

    public ErrorView(Context context) {
        super(context);
        initPaint();
        initPath();
    }

    private void initPath() {
        path = new Path();
        mPath1 = new Path();
        mPath2 = new Path();
        mPath3 = new Path();
        mPathMeasure = new PathMeasure();
        valueAnimator1 = ValueAnimator.ofFloat(0, 1);
        valueAnimator1.setDuration(1000);
        valueAnimator1.start();
        valueAnimator2 = ValueAnimator.ofFloat(0, 1);
        valueAnimator2.setDuration(500);
        valueAnimator3 = ValueAnimator.ofFloat(0, 1);
        valueAnimator3.setDuration(500);
        valueAnimator1.addUpdateListener(this);
        valueAnimator2.addUpdateListener(this);
        valueAnimator3.addUpdateListener(this);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath1.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mLineWidth / 2, Path.Direction.CW);
        mPath2.moveTo(getWidth() * 0.3f, getHeight() * 0.3f);
        mPath2.lineTo(getWidth() * 0.7f, getHeight() * 0.7f);
        mPath3.moveTo(getWidth() * 0.7f, getHeight() * 0.3f);
        mPath3.lineTo(getWidth() * 0.3f, getHeight() * 0.7f);
        mPathMeasure.setPath(mPath1, false);
        mPathMeasure.getSegment(0, v1 * mPathMeasure.getLength(), mPath1, true);
        canvas.drawPath(mPath1, mPaint);
        if (v1 == 1) {
            mPathMeasure.nextContour();
            mPathMeasure.setPath(mPath2, false);
            mPathMeasure.getSegment(0, v2 * mPathMeasure.getLength(), mPath2, true);
            canvas.drawPath(mPath2, mPaint);
        }
        if (v2 == 1) {
            mPathMeasure.nextContour();
            mPathMeasure.setPath(mPath3, false);
            mPathMeasure.getSegment(0, v3 * mPathMeasure.getLength(), mPath3, true);
            canvas.drawPath(mPath3, mPaint);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation.equals(valueAnimator1)) {
            v1 = (Float) animation.getAnimatedValue();
            invalidate();
            if (v1 == 1) {
                valueAnimator2.start();
            }
        } else if (animation.equals(valueAnimator2)) {
            v2 = (Float) animation.getAnimatedValue();
            invalidate();
            if (v2 == 1) {
                valueAnimator3.start();
            }
        } else if (animation.equals(valueAnimator3)) {
            v3 = (Float) animation.getAnimatedValue();
            invalidate();
        }
    }
}