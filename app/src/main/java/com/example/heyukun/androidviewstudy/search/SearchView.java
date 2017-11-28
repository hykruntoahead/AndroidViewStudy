package com.example.heyukun.androidviewstudy.search;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by heyukun on 2017/11/27.
 */

public class SearchView extends View implements View.OnClickListener{
    private Paint mPaint;
    private Path mSearchBtnPath, mSearchCirclePath;
    private PathMeasure mPathMeasure;
    private int mHeight;
    private float mRadius;
    private State mState = State.NONE;
    private ValueAnimator mStartAnimator, mRunningAnimator, mEndingAnimator;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;
    private static final int ANIM_TIMEOUT = 2000;
    private float mValue;
    private OnStartSearchListener mOnStartSearchListener;

    private Handler mHandler ;

    public SearchView(Context context) {
        super(context);
        init();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void onClick(View v) {
        if(mOnStartSearchListener != null) {
            if (mHandler != null) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler.removeMessages(1);

                mState = State.START_SEARCH;
                mHandler.sendEmptyMessage(1);
                setClickable(false);
            }
            mOnStartSearchListener.onStartClick();
        }
    }

    enum State {
        NONE,
        START_SEARCH,
        IS_SEARCHING,
        END_SEARCH
    }


    private void init() {
        initPaint();
        initListener();
        initAnimators();

        initHandler();
    }

    private void initHandler() {
       mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1) {
                    switch (mState) {
                        case START_SEARCH:
                            mStartAnimator.start();
                            break;
                        case IS_SEARCHING:
                            mRunningAnimator.start();
                            break;
                        case NONE:
                            setClickable(true);
                            break;
                        case END_SEARCH:
                            mEndingAnimator.start();
                            break;
                    }
                }
                return false;
            }
        });
    }


    private void initListener() {
        this.setOnClickListener(this);
        mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue = (float) animation.getAnimatedValue();
                Log.d("mStartAnimator","AnimatedValue="+mValue);
                invalidate();
            }
        };

        mAnimatorListener = new ValueAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                switch (mState) {
                    case START_SEARCH:
                        mState = State.IS_SEARCHING;
                        break;
                    case IS_SEARCHING:
                        mState = State.END_SEARCH;
                        break;
                    case END_SEARCH:
                        mState = State.NONE;
                        break;
                    default:
                        break;
                }
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    private void initAnimators() {
        mStartAnimator = ValueAnimator.ofFloat(0, 1).setDuration(ANIM_TIMEOUT);
        mEndingAnimator = ValueAnimator.ofFloat(1, 0).setDuration(ANIM_TIMEOUT);

        mRunningAnimator = ValueAnimator.ofFloat(0, 1).setDuration(ANIM_TIMEOUT);
        mRunningAnimator.setRepeatCount(ValueAnimator.INFINITE);

        mStartAnimator.addUpdateListener(mAnimatorUpdateListener);
        mRunningAnimator.addUpdateListener(mAnimatorUpdateListener);
        mEndingAnimator.addUpdateListener(mAnimatorUpdateListener);
        mStartAnimator.addListener(mAnimatorListener);
        mRunningAnimator.addListener(mAnimatorListener);
        mEndingAnimator.addListener(mAnimatorListener);

        Log.d("mStartAnimator","Anim="+(mStartAnimator==null));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = Math.min(w, h);
        mRadius = mHeight * 0.96f / 2;

        initPath();
    }

    private void initPath() {
        mSearchBtnPath = new Path();
        mSearchCirclePath = new Path();
        mPathMeasure = new PathMeasure();


        RectF rect1 = new RectF(-mRadius / 2, -mRadius / 2, mRadius / 2, mRadius / 2);
        //rect 开始弧度 扫过弧度 不要设置为360 内部会做处理可能导致效果有误
        mSearchBtnPath.addArc(rect1, 45, 359.5f);
        RectF rect2 = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        mSearchCirclePath.addArc(rect2, 45, 359.5f);
        mPathMeasure.setPath(mSearchCirclePath,false);
        float[] pos = new float[2];

        mPathMeasure.getPosTan(0, pos, null);

        mSearchBtnPath.lineTo(pos[0], pos[1]);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(16);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mHeight / 2, mHeight / 2);
        canvas.drawColor(Color.parseColor("#0078C7"));

        switch (mState) {
            case NONE:
                canvas.drawPath(mSearchBtnPath, mPaint);
                break;
            case START_SEARCH:
                mPathMeasure.setPath(mSearchBtnPath,false);
                Path dst = new Path();
                mPathMeasure.getSegment(mPathMeasure.getLength()*mValue,mPathMeasure.getLength(),dst,true);
                canvas.drawPath(dst,mPaint);
                break;
            case IS_SEARCHING:
                mPathMeasure.setPath(mSearchCirclePath,false);
                Path dst1 = new Path();
                mPathMeasure.getSegment(mPathMeasure.getLength()*mValue,
                        mPathMeasure.getLength()*mValue + mPathMeasure.getLength()/3*(0.5f-Math.abs(0.5f-mValue)),dst1,true);
                canvas.drawPath(dst1,mPaint);
                break;
            case END_SEARCH:
                mPathMeasure.setPath(mSearchBtnPath,false);
                Path dst2 = new Path();
                mPathMeasure.getSegment(mPathMeasure.getLength()*mValue,mPathMeasure.getLength(),dst2,true);
                canvas.drawPath(dst2,mPaint); 
                break;
            default:
                break;
        }
    }


    public void endSearching(){
        if(mRunningAnimator!=null) {
            mRunningAnimator.setupEndValues();
            mRunningAnimator.end();
        }
    }


    public void setOnStartSearchListener(OnStartSearchListener onStartSearchListener) {
        this.mOnStartSearchListener = onStartSearchListener;
    }

    interface OnStartSearchListener{
        void onStartClick();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("mStartAnimator","onDetachedFromWindow");
        if(mRunningAnimator!=null){
            mRunningAnimator.cancel();
        }
        if(mStartAnimator != null){
            mStartAnimator.cancel();
        }
        if(mEndingAnimator != null){
            mEndingAnimator.cancel();
        }

        if(mHandler!=null){
            mHandler.removeMessages(1);
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
