package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

public class SplashActivity extends Activity {

    private static final long sleepTime = 2000;

    private static final String TAG = SplashActivity.class.getName();
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
                //create db
                long costTime = System.currentTimeMillis() - startTime;
                if (sleepTime-costTime>0){
                    L.e(TAG,costTime+"");
                    try {
                        Thread.sleep(sleepTime-costTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                //overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
                //finish();
                MFGT.gotoMainActivity(SplashActivity.this);
                MFGT.finish(SplashActivity.this);
            }
        }).start();
    }
}
