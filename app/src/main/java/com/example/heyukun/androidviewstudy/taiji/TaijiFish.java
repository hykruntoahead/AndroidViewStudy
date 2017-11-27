package com.example.heyukun.androidviewstudy.taiji;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by heyukun on 2017/11/27.
 */

public class TaijiFish extends View{
    private int mHeight;
    private float mRadius;
    private Paint mPaint;

    public TaijiFish(Context context) {
        super(context);
        init();
    }


    public TaijiFish(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaijiFish(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = Math.min(w, h);
        mRadius = (mHeight * 0.95f)/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectBg = new RectF(0,0,mHeight,mHeight);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(rectBg,mPaint);

        canvas.translate(mHeight/2,mHeight/2);
        Path path = new Path();

        path.addCircle(0,0,mRadius, Path.Direction.CW);

        Path path0 = new Path();
        RectF rectF = new RectF(0,-mRadius,mRadius,mRadius);
        path0.addRect(rectF, Path.Direction.CW);

        Path path1 = new Path();

        path1.addCircle(0,mRadius/2,mRadius/2, Path.Direction.CW);

        Path path2 = new Path();
        path2.addCircle(0,-mRadius/2,mRadius/2 , Path.Direction.CW);

        Path pathHalf = new Path();

        pathHalf.op(path,path0, Path.Op.DIFFERENCE);
        pathHalf.op(path1, Path.Op.UNION);
        pathHalf.op(path2, Path.Op.DIFFERENCE);

        mPaint.setColor(Color.BLACK);
        canvas.drawPath(pathHalf,mPaint);

        path.op(pathHalf, Path.Op.DIFFERENCE);

        mPaint.setColor(Color.WHITE);
        canvas.drawPath(path,mPaint);
        canvas.drawCircle(0,mRadius/2,mRadius/8,mPaint);
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(0,-mRadius/2,mRadius/8,mPaint);
    }


}

