package com.example.heyukun.androidviewstudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.heyukun.androidviewstudy.path.EasyRadarChart;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private EasyRadarChart mEasyRadarChart;
    private Timer mTimer;
    private float max = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEasyRadarChart = findViewById(R.id.erc);
        mEasyRadarChart.setPolygonText("政治", "武力", "智力", "魅力", "统帅", "义理");
        mEasyRadarChart.setTextSize(50);
//        mEasyRadarChart.setTextColor(Color.BLUE);

        mEasyRadarChart.setGrades(max, max, max, max, max, max);

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEasyRadarChart.setGrades(true, max * (float) Math.random(), max * (float) Math.random(), max * (float) Math.random()
                                , max * (float) Math.random(), max * (float) Math.random(), max * (float) Math.random());
                    }
                });
            }
        }, 1000, 1000);
    }


}
