package com.jiahuan.svgmapview.sample;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.jiahuan.svgmapview.SVGMapView;
import com.jiahuan.svgmapview.sample.helper.AssetsHelper;

/**
 * Created by andre on 13-05-2017.
 */

public class SparkStepActivity extends ActionBarActivity implements SensorEventListener {
    private SVGMapView mapView;
    private SensorManager sManager;
    private Sensor stepSensor;
    private long steps = 0;
    TextView tx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spark);
        mapView = (SVGMapView) findViewById(R.id.spark_mapview);
        mapView.loadMap(AssetsHelper.getContent(this, "dcc-piso1-cortado.svg"));

        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        tx=(TextView)findViewById(R.id.textView);

        if (sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            Log.d("Step","ta a dar!!!");
            stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            sManager.registerListener(this, stepSensor,SensorManager.SENSOR_DELAY_NORMAL );

        }


    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }


        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps++;
            Log.i("Step",String.valueOf(steps));
            tx.setText(String.valueOf(getDistanceRun(steps)));
        }

    }


    /**
     * here are many ways to determine step length: you can measure it yourself,
     * estimate by multiplying your height in centimeters by 0.415 for men and 0.413 for women
     * or if you’re not overly concerned with accuracy you can use the averages 78cm for men and 70cm for women.
     * @param steps
     * @return
     */

    //function to determine the distance run in kilometers using average step length for men and number of steps
    public float getDistanceRun(long steps){
        float distance = (float)(steps*78)/(float)100000;
        return distance;
    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void onResume() {
        super.onResume();
        sManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    protected void onStop() {
        super.onStop();
        sManager.unregisterListener(this, stepSensor);
    }

}
