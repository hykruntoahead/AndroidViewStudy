package com.example.heyukun.androidviewstudy.magic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.example.heyukun.androidviewstudy.R;

/**
 * Created by heyukun on 2017/11/24.
 */

public class MagicActivity extends FragmentActivity {
    private MagicCircle magicCircle;
    private int range = 800;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_magic);
        magicCircle = findViewById(R.id.mc);
        magicCircle.setCircleColors(Color.GREEN);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magicCircle.startMove(range);
                if (range == 800) {
                    range = 0;
                } else {
                    range = 800;
                }
            }
        });
    }
}
