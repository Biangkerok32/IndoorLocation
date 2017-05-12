package com.jiahuan.svgmapview.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.VelocityTracker;

import com.jiahuan.svgmapview.SVGMapView;
import com.jiahuan.svgmapview.sample.helper.AssetsHelper;

import java.util.Random;

public class SparkActivity extends ActionBarActivity  implements SensorEventListener
{
    private SVGMapView mapView;

    /*DEBUG TAG FOR VELOCITY*/
    private static final String DEBUG_TAG="Velocity";
    private static final int SHAKE_THRESHOLD = 600;

    private VelocityTracker mVelocityTracker = null;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private float[] gravity = {(float) 9.8,(float) 9.8,(float) 9.8};
    private float [] linear_acceleration={(float) 0.000,(float) 0.000,(float) 0.000};

    /*detecting Shake movement*/
    private long lastUpdate = 0;
    long last_timestamp = 0;
    private float last_x, last_y, last_z;

    /*for distance*/

    static  final float NS2S = 1.0f / 1000000000.0f;

    float[] last_values=null;
    float[] velocity = null;
    float[] position = null;
    float dpositon;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spark);

        mapView = (SVGMapView) findViewById(R.id.spark_mapview);
        mapView.loadMap(AssetsHelper.getContent(this, "dcc-piso1-cortado.svg"));

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            Log.d(DEBUG_TAG,"Not Linear");
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // WITH G FORCE
            mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL );
        }




        Random random = new Random();
        for (int i = 0; i < 4; i++)
        {
            int color = i % 2 == 0 ? Color.RED : Color.BLUE;
            mapView.getController().sparkAtPoint(new PointF(random.nextInt(1000), random.nextInt(1000)), 100, color, 10);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        long curTime;

        float speed=0, x, y, z;

        final float alpha = (float) 0.8;


        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            Log.d(DEBUG_TAG, String.valueOf(x));
            Log.d(DEBUG_TAG, String.valueOf(y));
            Log.d(DEBUG_TAG, String.valueOf(z));


            //Não preciso disto afinal
            /*
            gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
            gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
            gravity[2] = alpha * gravity[2] + (1 - alpha) * z;


            linear_acceleration[0] = x - gravity[0];
            linear_acceleration[1] = y - gravity[1];
            linear_acceleration[2] = z - gravity[2];
                */



            curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 1000) {

                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                speed = Math.abs(x + y + z- last_x - last_y - last_z) / diffTime * 10000;
                float dt = curTime * NS2S;

                dpositon = speed * dt;
                Log.i("DIST",String.valueOf(dpositon));

                if (speed > SHAKE_THRESHOLD) {
                    //onPause();
                    //AlertShakeMovement();
                }
                last_x = x;
                last_y = y;
                last_z = z;
                Log.i("SPEED",String.valueOf(speed));
            }
            /*
            txt1.setText("aX: " + String.valueOf(linear_acceleration[0]));
            txt2.setText("aY: " + String.valueOf(linear_acceleration[1]));
            txt3.setText("aZ: " + String.valueOf(linear_acceleration[2]));
            */
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.i(DEBUG_TAG, "onResume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        /*It's good practice to unregister  the sensor when the application hibernates  */
        mSensorManager.unregisterListener(this);
        Log.i(DEBUG_TAG, "onPause");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(DEBUG_TAG, "onDestroy");
    }
}

