package com.example.heyukun.androidviewstudy.radar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by heyukun on 2017/11/22.
 * <p>
 * 简单雷达图
 */

public class EasyRadarChart extends View {
    private Context mCtx;
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mGradePaint;
    private Paint mGradeLinePaint;
    private Paint mGradePointPaint;
    private int mWidth;
    private float mRadius;
    private final static int COUNT = 6;
    private int textSize = 32;
    private int maxGrade = 100;

    private ArrayList<String> textArray;

    private float grades[] = {0, 0, 0, 0, 0, 0};


    public EasyRadarChart(Context context) {
        super(context);
        init(context);
    }


    public EasyRadarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EasyRadarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mCtx = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setLinearText(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);


        mGradePaint = new Paint();
        mGradePaint.setStyle(Paint.Style.FILL);
        mGradePaint.setAntiAlias(true);
        mGradePaint.setColor(Color.BLUE);
        mGradePaint.setAlpha(127);


        mGradeLinePaint = new Paint();
        mGradeLinePaint.setStyle(Paint.Style.STROKE);
        mGradeLinePaint.setAntiAlias(true);
        mGradeLinePaint.setStrokeWidth(5);
        mGradeLinePaint.setColor(Color.BLUE);


        mGradePointPaint = new Paint();
        mGradePointPaint.setStyle(Paint.Style.FILL);
        mGradePointPaint.setAntiAlias(true);
        mGradePointPaint.setColor(Color.BLACK);

        textArray = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            textArray.add("def:" + (i + 1));
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = Math.min(w, h);


        mRadius = (float) ((mWidth / 2) * 0.9);

        postInvalidate();

        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("onDraw", "isOnDrawing");
        canvas.translate(mWidth / 2, mWidth / 2);
        drawPolygon(canvas);

        drawLines(canvas);

        drawTexts(canvas);

        drawScoreRegion(canvas);
    }


    /**
     * 绘制六边形
     *
     * @param canvas 画布
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        float litterR = mRadius / COUNT;
        for (int i = 1; i < COUNT; i++) {
            float r = litterR * i;
            path.reset();
            path.moveTo(r, 0);
            path.lineTo((float) Math.cos(Math.PI * 2 / COUNT) * r, (float) Math.sin(Math.PI * 2 / COUNT) * r);
            path.lineTo(-(float) Math.cos(Math.PI * 2 / COUNT) * r, (float) Math.sin(Math.PI * 2 / COUNT) * r);
            path.lineTo(-r, 0);
            path.lineTo(-(float) Math.cos(Math.PI * 2 / COUNT) * r, -(float) Math.sin(Math.PI * 2 / COUNT) * r);
            path.lineTo((float) Math.cos(Math.PI * 2 / COUNT) * r, -(float) Math.sin(Math.PI * 2 / COUNT) * r);
            path.close();
            canvas.drawPath(path, mPaint);
        }
    }

    /**
     * 绘制3条直线
     *
     * @param canvas 画布
     */
    private void drawLines(Canvas canvas) {
        float polygonMaxRadius = mRadius / COUNT * (COUNT - 1);
        canvas.drawLine(polygonMaxRadius, 0, -polygonMaxRadius, 0, mPaint);
        canvas.drawLine((float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius, (float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius
                , -(float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius, -(float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius, mPaint);
        canvas.drawLine(-(float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius, (float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius
                , (float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius, -(float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius, mPaint);
    }


    /**
     * 绘制字符串
     *
     * @param canvas h画布
     */
    private void drawTexts(Canvas canvas) {
        float polygonMaxRadius = mRadius / COUNT * (COUNT - 1);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();

        //获取文字宽度
        float textWidth = mTextPaint.measureText(textArray.get(0));

        //y=center-fm.top / 2 - fm.bottom / 2 目的是为了将文字与中线垂直对齐 center = 0
        canvas.drawText(textArray.get(0), polygonMaxRadius + textWidth / 2 + 8, -fm.top / 2 - fm.bottom / 2, mTextPaint);

        canvas.drawText(textArray.get(1), (float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius + 8,
                (float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius - fm.top + 8, mTextPaint);

        canvas.drawText(textArray.get(2), -(float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius - 8,
                (float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius - fm.top + 8, mTextPaint);

        canvas.drawText(textArray.get(3), -polygonMaxRadius - (textWidth / 2 + 8),
                -fm.top / 2 - fm.bottom / 2, mTextPaint);

        canvas.drawText(textArray.get(4), -(float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius - 8,
                -(float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius - fm.bottom - 8, mTextPaint);

        canvas.drawText(textArray.get(5), (float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius + 8,
                -(float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius - fm.bottom - 8, mTextPaint);


    }


    /**
     * 设置六个角表示字符
     *
     * @param strings 字符串数组
     */
    public void setPolygonText(String... strings) {
        if (strings == null || strings.length == 0) {
            return;
        }
        if (strings.length != COUNT) {
            throw new RuntimeException("please set six item in array");
        }

        textArray.clear();
        textArray.addAll(Arrays.asList(strings));
    }

    /**
     * 设置六个角字符
     *
     * @param resArray 字符串资源数组
     */
    public void setPolygonText(@StringRes int... resArray) {
        if (resArray == null || resArray.length == 0) {
            return;
        }
        if (resArray.length != COUNT) {
            throw new RuntimeException("please set six item in array");
        }

        textArray.clear();
        for (int res : resArray) {
            textArray.add(mCtx.getString(res));
        }
    }


    /**
     * 绘制评分区域
     *
     * @param canvas 画布
     */
    private void drawScoreRegion(Canvas canvas) {
        Path path = new Path();
        float polygonMaxRadius = mRadius / COUNT * (COUNT - 1);
        for (int i = 0; i < grades.length; i++) {
            float ratio = grades[i] / maxGrade;
            //超过最大值按最大值绘制
            if (ratio > 1) {
                ratio = 1;
            }

            if (i == 0) {
                path.moveTo(ratio * polygonMaxRadius, 0);
                canvas.drawCircle(ratio * polygonMaxRadius, 0, 10, mGradePointPaint);
            } else if (i == 1) {
                path.lineTo((float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio,
                        (float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio);
                canvas.drawCircle((float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio,
                        (float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio, 10, mGradePointPaint);
            } else if (i == 2) {
                path.lineTo(-(float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio,
                        (float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio);
                canvas.drawCircle(-(float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio,
                        (float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio, 10, mGradePointPaint);
            } else if (i == 3) {
                path.lineTo(-ratio * polygonMaxRadius, 0);
                canvas.drawCircle(-ratio * polygonMaxRadius, 0, 10, mGradePointPaint);
            } else if (i == 4) {
                path.lineTo(-(float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio,
                        -(float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio);
                canvas.drawCircle(-(float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio,
                        -(float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio, 10, mGradePointPaint);
            } else if (i == 5) {
                path.lineTo((float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio,
                        -(float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio);
                canvas.drawCircle((float) Math.cos(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio,
                        -(float) Math.sin(Math.PI * 2 / COUNT) * polygonMaxRadius * ratio, 10, mGradePointPaint);
            }
        }
        path.close();
        canvas.drawPath(path, mGradeLinePaint);
        canvas.drawPath(path, mGradePaint);
    }


    /**
     * 设置 字体大小
     *
     * @param textSize 字体尺寸
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
        mTextPaint.setTextSize(textSize);
    }

    /**
     * 设置 字体颜色
     *
     * @param textColor 字体颜色
     */
    public void setTextColor(@ColorInt int textColor) {
        mTextPaint.setColor(textColor);
    }


    /**
     * 设置评分
     *
     * @param floats 评分数组
     */
    public void setGrades(float... floats) {
        if (floats == null || floats.length == 0) {
            return;
        }
        if (floats.length != COUNT) {
            throw new RuntimeException("please set six item in array");
        }

        grades = floats;
    }


    /**
     * 设置评分
     *
     * @param refresh 是否强制刷新
     * @param floats  评分数组
     */
    public void setGrades(boolean refresh, float... floats) {
        if (floats == null || floats.length == 0) {
            return;
        }
        if (floats.length != COUNT) {
            throw new RuntimeException("please set six item in array");
        }

        grades = floats;

        if (refresh) {
            invalidate();
        }
    }


    /**
     * 设置 评分区域颜色
     *
     * @param gradeRegionColor 区域颜色
     */
    public void setGradeRegionColor(@ColorInt int gradeRegionColor) {
        mGradePaint.setColor(gradeRegionColor);
    }


    /**
     * 设置 评分区域面变颜色
     *
     * @param gradeRegionLineColor 区域颜色
     */
    public void setGradeRegionLineColor(@ColorInt int gradeRegionLineColor) {
        mGradeLinePaint.setColor(gradeRegionLineColor);
    }


    /**
     * 设置 评分区域面变颜色
     *
     * @param gradeRegionPointColor 区域颜色
     */
    public void setGradeRegionPointColor(@ColorInt int gradeRegionPointColor) {
        mGradePointPaint.setColor(gradeRegionPointColor);
    }


}
