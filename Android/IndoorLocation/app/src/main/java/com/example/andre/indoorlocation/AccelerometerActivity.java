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
import android.widget.TextView;

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

    private float[] gravity = {(float) 9.8,(float) 9.8,(float) 9.8};
    private float [] linear_acceleration={(float) 0.000,(float) 0.000,(float) 0.000};
    /*detecting sHake movement*/
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    TextView txt1 ,txt2,txt3;


    /**
     * Identifying Sensors and Sensor Capabilities
     * TYPE_LINEAR_ACCELERATION  Measures the acceleration force in
     * m/s2 that is applied to a device on all three physical axes (x, y, and z), excluding the force of gravity.
     * Cannot assume that all devices have hardware for LINEAR ACCELERATION
     *
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_layout);
        /*Accelerometer */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        if(mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)!=null){
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION); // NO G FORCE
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        }
        else if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION); // WITH G FORCE
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        }
        else{
            //TODO: Missing failure case
            //Failure, do nothing for now
            Log.d(DEBUG_TAG, "Sensor dosent exist or is inoperable");
        }


        txt1 = (TextView) findViewById(R.id.editText2);
        txt2 = (TextView) findViewById(R.id.editText3);
        txt3 = (TextView) findViewById(R.id.editText4);



    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;

        long curTime;

        float speed,x,y,z;

        final float alpha = (float) 0.8;


        if(mySensor.getType()==Sensor.TYPE_ACCELEROMETER) {

            x = event.values[0];
            y = event.values[1];
            z = event.values[2];


            gravity[0] = alpha * gravity[0] + (1-alpha) * x;
            gravity[1] = alpha * gravity[1] + (1-alpha) * y;
            gravity[2] = alpha * gravity[2] + (1-alpha) * z;



            linear_acceleration[0] = x - gravity[0];
            linear_acceleration[1] = y - gravity[1];
            linear_acceleration[2] = z - gravity[2];


            Log.d(DEBUG_TAG, String.valueOf(x));
            Log.d(DEBUG_TAG, String.valueOf(y));
            Log.d(DEBUG_TAG, String.valueOf(z));


            curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 100) {

                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    //TODO:Missing warning message to the user.
                    //do nothing for now.....
                }
                last_x = x;
                last_y = y;
                last_z = z;

            }

            txt1.setText("aX: " + String.valueOf(x));
            txt2.setText("aY: " + String.valueOf(y));
            txt3.setText("aZ: " + String.valueOf(z));

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
