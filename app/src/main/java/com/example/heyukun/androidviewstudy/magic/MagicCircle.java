package com.example.heyukun.androidviewstudy.magic;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

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
    //绘制圆的半径为设置高度1／3 * 0.98
    private float mRadius;
    //y轴向下为正
    private PointF mPyTop, mPxRight, mPyBottom, mPxLeft;
    private float rightRadio = 1;
    private float leftRadio = 1;
    private float mCtrRadius;
    private ValueAnimator mValueAnimator;


    @ColorInt
    private int[] mColors = {Color.RED, Color.BLUE, Color.GREEN};


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
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = Math.min(w, h);
        mRadius = (float) (mHeight / 3 * 0.98);
        mCtrRadius = mRadius * C;

        initPoints();

    }

    //初始化数据点的坐标
    private void initPoints() {
        mPyTop = new PointF(0, mRadius);
        mPxRight = new PointF(mRadius, 0);
        mPyBottom = new PointF(0, -mRadius);
        mPxLeft = new PointF(-mRadius, 0);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mHeight / 2, mHeight / 2);
        Path path = new Path();

        path.moveTo(mPyTop.x, mPyTop.y);

        mPxRight.set(mRadius * rightRadio, 0);
        mPxLeft.set(-mRadius * leftRadio, 0);

        path.cubicTo(mCtrRadius * rightRadio, mPyTop.y, mPxRight.x, mCtrRadius, mPxRight.x, mPxRight.y);
        path.cubicTo(mPxRight.x, -mCtrRadius, mCtrRadius * rightRadio, mPyBottom.y, mPyBottom.x, mPyBottom.y);

        path.cubicTo(-mCtrRadius * leftRadio, mPyBottom.y, mPxLeft.x, -mCtrRadius, mPxLeft.x, mPxLeft.y);

        path.cubicTo(mPxLeft.x, mCtrRadius, -mCtrRadius * leftRadio, mPyTop.y, mPyTop.x, mPyTop.y);

        canvas.drawPath(path, mPaint);
    }


    /**
     * 开始移动
     *
     * @param range 移动范围
     */
    public void startMove(final int range) {
        this.setClickable(false);

        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.end();
            mValueAnimator.cancel();
        }
        if (mColors.length == 1) {
            mValueAnimator = ValueAnimator.ofInt(mColors[0], mColors[0]);
        } else {
            mValueAnimator = ValueAnimator.ofInt(mColors);
        }

        mValueAnimator.setEvaluator(new ArgbEvaluator());
        mValueAnimator.setDuration(2000);
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
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
                    mValueAnimator.cancel();
                    MagicCircle.this.setClickable(true);
                }

                invalidate();
            }
        });

        mValueAnimator.start();

        this.animate()
                .setDuration(2000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .translationX(range).start();


    }

    public void setCircleColors(@ColorInt int... colors) {
        if (colors == null || colors.length == 0) {
            return;
        }
        mColors = colors;
    }


    /**
     * 开始移动
     *
     * @param range  移动范围
     * @param colors 渐变颜色
     */
    public void startMove(int range, @ColorInt int... colors) {
        setCircleColors(colors);
        startMove(range);
    }


}
