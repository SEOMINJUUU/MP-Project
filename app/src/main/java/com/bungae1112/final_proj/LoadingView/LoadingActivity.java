package com.bungae1112.final_proj.LoadingView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.bungae1112.final_proj.R;

public class LoadingActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        startLoading();
    }

    private void startLoading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }
}
