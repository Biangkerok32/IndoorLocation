package com.example.andre.indoorlocation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.VelocityTracker;

/**
 * Created by andre on 06-04-2017.
 */

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {


    /*DEBUG TAG FOR VELOCITY*/
    private static final String DEBUG_TAG="Velocity";

    /*accelerometer */
    private VelocityTracker mVelocityTracker = null;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    /*detecting sHake movement*/
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;



    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_layout);
        /*Accelerometer */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // WITH G FORCE

        /**
         * TYPE_LINEAR_ACCELERATION  Measures the acceleration force in
         * m/s2 that is applied to a device on all three physical axes (x, y, and z), excluding the force of gravity.
         * Cannot assume that all devices have hardware for LINEAR ACCELERATION
         */
        //mAccelerometer=mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION); // NO G FORCE

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;

        long curTime;

        float speed;
        float x;
        float y;
        float z;


        if(mySensor.getType()==Sensor.TYPE_ACCELEROMETER){
             x = event.values[0];
             y = event.values[1];
             z = event.values[2];
            Log.d(DEBUG_TAG, String.valueOf(x));
            Log.d(DEBUG_TAG, String.valueOf(y));
            Log.d(DEBUG_TAG, String.valueOf(z));

            curTime=System.currentTimeMillis();
            if((curTime-lastUpdate)>100){
                long diffTime = (curTime-lastUpdate);
                lastUpdate=curTime;

                speed = Math.abs(x+y+z -last_x -last_y-last_z) / diffTime*10000;

                if(speed> SHAKE_THRESHOLD){
                        //do nothing for now.....
                }
                last_x=x;
                last_y=y;
                last_z=z;
            }



        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        /*do nothing.....*/
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

}
