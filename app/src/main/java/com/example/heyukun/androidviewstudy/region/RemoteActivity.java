package com.example.heyukun.androidviewstudy.region;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.heyukun.androidviewstudy.R;

/**
 * Created by heyukun on 2017/12/1.
 */

public class RemoteActivity extends FragmentActivity {
    private RemoteControllerView mRemoteControllerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        initView();
    }

    private void initView() {
        mRemoteControllerView = findViewById(R.id.rcv);
        mRemoteControllerView.setOnRemoteTouchListener(new OnRemoteTouchListener() {
            @Override
            public void onLeftTouch() {
                Log.d("mRemoteControllerView", "left-touch");
            }

            @Override
            public void onTopTouch() {
                Log.d("mRemoteControllerView", "top-touch");
            }

            @Override
            public void onRightTouch() {
                Log.d("mRemoteControllerView", "right-touch");
            }

            @Override
            public void onBottomTouch() {
                Log.d("mRemoteControllerView", "bottom-touch");
            }

            @Override
            public void onCenterTouch() {
                Log.d("mRemoteControllerView", "center-touch");
            }
        });
    }
}
