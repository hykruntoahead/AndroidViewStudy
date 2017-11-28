package com.example.heyukun.androidviewstudy.search;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.example.heyukun.androidviewstudy.R;

/**
 * Created by heyukun on 2017/11/27.
 */

public class SearchActivity extends FragmentActivity {
    private SearchView mSearchView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setOnStartSearchListener(new SearchView.OnStartSearchListener() {
            @Override
            public void onStartClick() {
                        //do some task
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mSearchView.endSearching();
                            }
                        },5000);
            }
        });

    }
}
