package com.example.andre.indoorlocation;


import android.app.Activity;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;

import SVG.SVGMapView;
import SVG.core.helper.map.SVGParser;


public class MainActivity extends Activity  {

    /*check the different states of the activity ()*/
    private static final String TAG ="IndoorLocation";
    SVGMapView mapView;

    //TODO:Communication to PI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.i(TAG,"onCreate");
        Path svg = SVGParser.parsePath("drawable/method_draw_image.xml");

        mapView = (SVGMapView) findViewById(R.id.mapView);
        // load svg string
        mapView.loadMap(AssetsHelper.getContent(this, svg));
        //tv1.setText("Hello, welcome to Indoor Location");

       /* Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(),AccelerometerActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();

    }*/
    }
    @Override
    protected void onStart() {
        super.onStart();

        Log.i(TAG,"onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
            mapView.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
            mapView.onPause();
            Log.i(TAG, "onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
            mapView.onDestroy();
        Log.i(TAG, "onDestroy");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }





}
