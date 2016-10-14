package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.ucai.fulicenter.R;

public class SplashActivity extends Activity {

    private long sleepTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                long costTime = System.currentTimeMillis() - startTime;
                if (sleepTime-costTime>0){
                    try {
                        Thread.sleep(sleepTime-costTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        }).start();
    }
}
