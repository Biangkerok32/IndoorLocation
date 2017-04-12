package com.example.andre.indoorlocation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    //TODO:SWIPE MENU
    /*DEBUG TAG FOR VELOCITY*/
    private static final String DEBUG_TAG="Velocity";
    private static final int SHAKE_THRESHOLD = 600;

    TextView txt1 ,txt2,txt3,vx,vy,vz;

    /*PopUp*/
    Context cv = this;

    /*accelerometer */
    private VelocityTracker mVelocityTracker = null;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float[] gravity = {(float) 9.8,(float) 9.8,(float) 9.8};
    private float [] linear_acceleration={(float) 0.000,(float) 0.000,(float) 0.000};
    /*detecting Shake movement*/
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;

    /*for distance*/
    static  final float NS2S = 1.0f / 1000000000.0f;
    float[] last_values=null;
    float[] velocity = null;
    float[] position = null;

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
            Log.d(DEBUG_TAG,"Linear");
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION); // NO G FORCE
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        }
        else if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            Log.d(DEBUG_TAG,"Not Linear");
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION); // WITH G FORCE
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        }
        else{
            //Failure
            new AlertDialog.Builder(cv)
                    .setTitle("No accelerometer detected")
                    .setMessage("Are you sure you want to Continue with the application IndoorLocation?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO:maybe send a signal only to use gyroscope
                            //onResume();
                            dialog.cancel();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            onDestroy();
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            Log.d(DEBUG_TAG, "Sensor does not exist or is inoperable");
        }


        txt1 = (TextView) findViewById(R.id.editText2);
        txt2 = (TextView) findViewById(R.id.editText3);
        txt3 = (TextView) findViewById(R.id.editText4);

        vx = (TextView) findViewById(R.id.textView);
        vy = (TextView) findViewById(R.id.textView3);
        vz = (TextView) findViewById(R.id.textView4);

    }

    /**
     * Detects any change on the sensor
     *@param event
     */

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
                    onPause();
                    AlertShakeMovement();
                }
                last_x = x;
                last_y = y;
                last_z = z;

            }

            txt1.setText("aX: " + String.valueOf(x));
            txt2.setText("aY: " + String.valueOf(y));
            txt3.setText("aZ: " + String.valueOf(z));
            //TODO: MISSING DISTANCE FOR NON LINEAR

        if(last_values!=null) {
            float dt = curTime * NS2S;
            for(int index = 0; index < 3;++index){
                velocity[index] += (event.values[index] + last_values[index])/2 * dt;
                position[index] += velocity[index] * dt;
            }
            //debug
            vx.setText("Vx: " + String.valueOf(velocity[0]));
            vy.setText("Vy: " + String.valueOf(velocity[1]));
            vz.setText("Vz: " + String.valueOf(velocity[2]));

        }
            else{
            //do nothing for now
        }



        }else{
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];


            Log.d(DEBUG_TAG, String.valueOf(x));
            Log.d(DEBUG_TAG, String.valueOf(y));
            Log.d(DEBUG_TAG, String.valueOf(z));


            curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 100) {

                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    onPause();
                    AlertShakeMovement();
                }
                last_x = x;
                last_y = y;
                last_z = z;

            }

            txt1.setText("aX: " + String.valueOf(x));
            txt2.setText("aY: " + String.valueOf(y));
            txt3.setText("aZ: " + String.valueOf(z));
            //TODO: MISSING DISTANCE FOR LINEAR
        }

    }


    /**
     * Alert message  when Shake Movement it's detected
     *
     */

    private void AlertShakeMovement(){
        new AlertDialog.Builder(cv)
                .setTitle("Shaking Movement Detected")
                .setMessage("Are you sure you want to Continue with the application IndoorLocation?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onResume();


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onDestroy();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


    /**
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        /*do nothing.....*/
    }

    /**
     *
     */
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.i(DEBUG_TAG, "onResume");
    }

    /**
     *
     */
    @Override
    protected void onPause() {
        super.onPause();
        /*It's good practice to unregister  the sensor when the application hibernates  */
        mSensorManager.unregisterListener(this);
        Log.i(DEBUG_TAG, "onPause");
    }


    /**
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(DEBUG_TAG, "onDestroy");
    }
}
