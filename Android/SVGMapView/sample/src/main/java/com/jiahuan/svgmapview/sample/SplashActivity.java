package com.jiahuan.svgmapview.sample;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


/***
 *
 * Startup activity for the app
 * Splash screen
 *
 ***/

public class SplashActivity extends Activity {

    /**
     * Call upon the startup of the app
     * Thread responsible for calling MainActivity after waiting 3 seconds
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}