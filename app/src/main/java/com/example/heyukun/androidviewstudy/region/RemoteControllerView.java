package com.example.heyukun.androidviewstudy.region;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by heyukun on 2017/12/1.
 */

public class RemoteControllerView extends View {
    private Paint mPaint, mFlagPaint;
    private int mWidth;
    private float mRadius;

    private Path mLeftP, mTopP, mRightP, mBottomP, mCenterP;
    private Region mLeftReg, mTopReg, mRightReg, mBottomReg, mCenterReg;
    private Path mLeftFlagP, mTopFlagP, mRightFlagP, mBottomFlagP, mCenterFlagP;

    private final int DEFAULT_COLOR = 0xFF4E5268;
    private final int TOUCHED_COLOR = 0xFFDF9C81;

    private Matrix matrix;
    private RemoteTouchRegion mTouchRegion = RemoteTouchRegion.NONE;
    private OnRemoteTouchListener mOnRemoteTouchListener;


    public RemoteControllerView(Context context) {
        super(context);
        init();
    }


    public RemoteControllerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RemoteControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mFlagPaint = new Paint();
        mFlagPaint.setColor(Color.WHITE);
        mFlagPaint.setStyle(Paint.Style.STROKE);
        mFlagPaint.setStrokeWidth(4);
        mFlagPaint.setAntiAlias(true);


        mLeftP = new Path();
        mRightP = new Path();
        mTopP = new Path();
        mBottomP = new Path();
        mCenterP = new Path();

        mLeftReg = new Region();
        mRightReg = new Region();
        mTopReg = new Region();
        mBottomReg = new Region();
        mCenterReg = new Region();

        mLeftFlagP = new Path();
        mRightFlagP = new Path();
        mTopFlagP = new Path();
        mBottomFlagP = new Path();
        mCenterFlagP = new Path();

        matrix = new Matrix();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = Math.min(w, h);
        mRadius = mWidth * 0.96f;
        matrix.reset();
        Region globalRegion = new Region(-w, -h, w, h);
        initPaths(globalRegion);
    }

    private void initPaths(Region globalRegion) {
        RectF rectBig = new RectF(-mRadius / 2, -mRadius / 2, mRadius / 2, mRadius / 2);
        float middle = mRadius / 2 * 0.50f;
        float flagPos = mRadius / 2 * 0.73f;
        float lineLength = mRadius / 2 * 0.1f;
        RectF rectMiddle = new RectF(-middle, -middle, middle, middle);
        float small = mRadius / 2 * 0.35f;

        int sweepAngle = 80;


        mLeftP.addArc(rectBig, 140, sweepAngle);
        mLeftP.arcTo(rectMiddle, -140, -sweepAngle);
        mLeftP.close();
        mLeftReg.setPath(mLeftP, globalRegion);
        mLeftFlagP.moveTo(-flagPos, 0);
        mLeftFlagP.rLineTo(lineLength, lineLength);
        mLeftFlagP.moveTo(-flagPos, 0);
        mLeftFlagP.rLineTo(lineLength, -lineLength);

        mTopP.addArc(rectBig, -130, sweepAngle);
        mTopP.arcTo(rectMiddle, -50, -sweepAngle);
        mTopP.close();
        mTopReg.setPath(mTopP, globalRegion);
        mTopFlagP.moveTo(0, -flagPos);
        mTopFlagP.rLineTo(-lineLength, lineLength);
        mTopFlagP.moveTo(0, -flagPos);
        mTopFlagP.rLineTo(lineLength, lineLength);


        mRightP.addArc(rectBig, -40, sweepAngle);
        mRightP.arcTo(rectMiddle, 40, -sweepAngle);
        mRightP.close();
        mRightReg.setPath(mRightP, globalRegion);
        mRightFlagP.moveTo(flagPos, 0);
        mRightFlagP.rLineTo(-lineLength, -lineLength);
        mRightFlagP.moveTo(flagPos, 0);
        mRightFlagP.rLineTo(-lineLength, lineLength);


        mBottomP.addArc(rectBig, 50, sweepAngle);
        mBottomP.arcTo(rectMiddle, 130, -sweepAngle);
        mBottomP.close();
        mBottomReg.setPath(mBottomP, globalRegion);
        mBottomFlagP.moveTo(0, flagPos);
        mBottomFlagP.rLineTo(-lineLength, -lineLength);
        mBottomFlagP.moveTo(0, flagPos);
        mBottomFlagP.rLineTo(lineLength, -lineLength);

        mCenterP.addCircle(0, 0, small, Path.Direction.CW);
        mCenterReg.setPath(mCenterP, globalRegion);
        mCenterFlagP.addCircle(0, 0, small / 2, Path.Direction.CW);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mWidth / 2);

        if (matrix.isIdentity()) {
            canvas.getMatrix().invert(matrix);
        }
        Log.d("mRemoteControllerView", "onDraw--:" + mTouchRegion.name());

        drawControls(canvas);

    }

    private void drawControls(Canvas canvas) {
        switch (mTouchRegion) {
            case NONE:
                resetPathColors(canvas);
                break;
            case LEFT:
                resetPathColors(canvas);
                mPaint.setColor(TOUCHED_COLOR);
                canvas.drawPath(mLeftP, mPaint);
                break;
            case RIGHT:
                resetPathColors(canvas);
                mPaint.setColor(TOUCHED_COLOR);
                canvas.drawPath(mRightP, mPaint);
                break;
            case CENTER:
                resetPathColors(canvas);
                mPaint.setColor(TOUCHED_COLOR);
                canvas.drawPath(mCenterP, mPaint);
                break;
            case TOP:
                resetPathColors(canvas);
                mPaint.setColor(TOUCHED_COLOR);
                canvas.drawPath(mTopP, mPaint);
                break;
            case BOTTOM:
                resetPathColors(canvas);
                mPaint.setColor(TOUCHED_COLOR);
                canvas.drawPath(mBottomP, mPaint);
                break;
            default:
                resetPathColors(canvas);
                break;
        }

        drawFlagLines(canvas);
    }

    private void drawFlagLines(Canvas canvas) {
        canvas.drawPath(mLeftFlagP, mFlagPaint);
        canvas.drawPath(mRightFlagP, mFlagPaint);
        canvas.drawPath(mTopFlagP, mFlagPaint);
        canvas.drawPath(mBottomFlagP, mFlagPaint);
        canvas.drawPath(mCenterFlagP, mFlagPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(0,0,mRadius/2*0.05f,mPaint);
    }


    private void resetPathColors(Canvas canvas) {
        mPaint.setColor(DEFAULT_COLOR);
        canvas.drawPath(mLeftP, mPaint);
        canvas.drawPath(mRightP, mPaint);
        canvas.drawPath(mTopP, mPaint);
        canvas.drawPath(mBottomP, mPaint);
        canvas.drawPath(mCenterP, mPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float[] pos = new float[2];
        pos[0] = event.getX();
        pos[1] = event.getY();
        matrix.mapPoints(pos);

        int x = (int) pos[0];
        int y = (int) pos[1];

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchRegion = touchLoc(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mTouchRegion = touchLoc(x, y);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchRegion = RemoteTouchRegion.NONE;
                break;
            default:
                break;
        }

        invalidate();

        return true;
    }

    /**
     * 触摸位置
     *
     * @param x
     * @param y
     */
    private RemoteTouchRegion touchLoc(int x, int y) {

        if (mLeftReg.contains(x, y)) {
            if (mOnRemoteTouchListener != null) {
                mOnRemoteTouchListener.onLeftTouch();
            }
            return RemoteTouchRegion.LEFT;
        } else if (mTopReg.contains(x, y)) {
            if (mOnRemoteTouchListener != null) {
                mOnRemoteTouchListener.onTopTouch();
            }
            return RemoteTouchRegion.TOP;
        } else if (mRightReg.contains(x, y)) {
            if (mOnRemoteTouchListener != null) {
                mOnRemoteTouchListener.onRightTouch();
            }
            return RemoteTouchRegion.RIGHT;
        } else if (mBottomReg.contains(x, y)) {
            if (mOnRemoteTouchListener != null) {
                mOnRemoteTouchListener.onBottomTouch();
            }
            return RemoteTouchRegion.BOTTOM;
        } else if (mCenterReg.contains(x, y)) {
            if (mOnRemoteTouchListener != null) {
                mOnRemoteTouchListener.onCenterTouch();
            }
            return RemoteTouchRegion.CENTER;
        } else {
            Log.d("mRemoteControllerView", "onTouchEvent--x:" + x + ";y:" + y);
            return RemoteTouchRegion.NONE;
        }
    }


    public void setOnRemoteTouchListener(OnRemoteTouchListener onRemoteTouchListener) {
        mOnRemoteTouchListener = onRemoteTouchListener;
    }

}
