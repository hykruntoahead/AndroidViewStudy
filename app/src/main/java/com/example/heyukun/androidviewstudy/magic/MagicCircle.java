package com.example.heyukun.androidviewstudy.magic;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;

/**
 * Created by heyukun on 2017/11/24.
 * <p>
 * 三次贝塞尔曲线 弹性圆
 */

public class MagicCircle extends View {
    //参见  a. http://spencermortensen.com/articles/bezier-circle/
    //or b.https://stackoverflow.com/questions/1734745/how-to-create-circle-with-bézier-curves
    private static final float C = 0.551915024494f;

    private Paint mPaint;
    private int mHeight;
    private float mRadius;
    private PointF p0, p1, p2, p3;
    private float rightRadio = 1;
    private float leftRadio = 1;
    private float mCtrRadius;


    public MagicCircle(Context context) {
        super(context);
        init();
    }

    public MagicCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MagicCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = Math.min(w, h);
        mRadius = (float) (mHeight / 3 * 0.95);
        mCtrRadius = mRadius * C;

        initPoints();

    }

    //初始化数据点的坐标
    private void initPoints() {
        p0 = new PointF(0, mRadius);
        p1 = new PointF(mRadius, 0);
        p2 = new PointF(0, -mRadius);
        p3 = new PointF(-mRadius, 0);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mHeight / 2, mHeight / 2);
        Path path = new Path();

        path.moveTo(p0.x, p0.y);

        p1.set(mRadius * rightRadio, 0);
        p3.set(-mRadius * leftRadio, 0);

        path.cubicTo(mCtrRadius * rightRadio, p0.y, p1.x, mCtrRadius, p1.x, p1.y);
        path.cubicTo(p1.x, -mCtrRadius, mCtrRadius * rightRadio, p2.y, p2.x, p2.y);

        path.cubicTo(-mCtrRadius * leftRadio, p2.y, p3.x, -mCtrRadius, p3.x, p3.y);

        path.cubicTo(p3.x, mCtrRadius, -mCtrRadius * leftRadio, p0.y, p0.x, p0.y);

        canvas.drawPath(path, mPaint);
    }


    public void startMove(final int range) {
        mPaint.setColor(Color.RED);
        this.setClickable(false);

        final ValueAnimator valueAnimator = ValueAnimator.ofInt(Color.RED, Color.BLUE);
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();

                mPaint.setColor((Integer) animation.getAnimatedValue());
                if (fraction < 0.25) {
                    rightRadio = 1 + fraction * 2;
                } else if (fraction < 0.49) {
                    leftRadio = 1 + (fraction - 0.25f) * 2;
                } else if (fraction >= 0.49 && fraction < 0.51) {
                    rightRadio = 1.5f;
                    leftRadio = 1.5f;
                } else if (fraction < 0.75) {
                    rightRadio = 1.5f - (fraction - 0.5f) * 2;
                } else if (fraction < 1) {
                    leftRadio = 1.5f - (fraction - 0.75f) * 2;
                } else {
                    leftRadio = 1;
                    rightRadio = 1;
                    valueAnimator.cancel();
                    MagicCircle.this.setClickable(true);
                }

                invalidate();
            }
        });

        valueAnimator.start();

        this.animate()
                .setDuration(2000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .translationX(range)
                .start();

    }


}
