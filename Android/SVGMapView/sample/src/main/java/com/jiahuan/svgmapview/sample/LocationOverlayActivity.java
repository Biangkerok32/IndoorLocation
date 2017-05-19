package com.jiahuan.svgmapview.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.jiahuan.svgmapview.SVGMapView;
import com.jiahuan.svgmapview.SVGMapViewListener;
import com.jiahuan.svgmapview.overlay.SVGMapLocationOverlay;
import com.jiahuan.svgmapview.sample.helper.AssetsHelper;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.Toast.LENGTH_SHORT;


public class LocationOverlayActivity extends ActionBarActivity implements SensorEventListener {

    private final int ONE_SECOND = 3000;

    private SVGMapView mapView;

    private int LIMIT_INICIAL_X = 85;
    private int LIMIT_FINAL_X = 630;

    private SVGMapLocationOverlay locationOverlay;

    private WifiActivity wifi;

    private CharSequence connection_text = "Connected to Rpi";
    private CharSequence off_limits_text = "OFF limits on map calculation";

    private float azimuth = 0;
    private float NORTH = 0;
    private float SOUTH = 180;
    private float EST = 90;
    private float WEST = 270;

    private Toast toast_off_limits;
    private Toast toast_connection;


    private SensorManager mSensorManager;
    private Sensor OrientationSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        mapView = (SVGMapView) findViewById(R.id.location_mapview);

        Date data = new Date();
        wifi = new WifiActivity(data);

        initSensor();

        makeToasts();

        mapView.registerMapViewListener(new SVGMapViewListener() {
            @Override
            public void onMapLoadComplete() {
              /*  locationOverlay = new SVGMapLocationOverlay(mapView);
                locationOverlay.setIndicatorArrowBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.indicator_arrow));
                locationOverlay.setPosition(new PointF(LIMIT_INICIAL_X, 350));
                locationOverlay.setIndicatorCircleRotateDegree(180);
                locationOverlay.setMode(SVGMapLocationOverlay.MODE_COMPASS);
                locationOverlay.setIndicatorArrowRotateDegree(0);
                mapView.getOverLays().add(locationOverlay);
                mapView.refresh();*/
            }

            @Override
            public void onMapLoadError() {

            }

            @Override
            public void onGetCurrentMap(Bitmap bitmap) {
            }
        });

        mapView.loadMap(AssetsHelper.getContent(this, "dcc-piso1-cortado.svg"));


        final Timer timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                //mapView.getOverLays().remove(locationOverlay);

                testpath(calculatedistance(wifi.getData()));
/*
                if (wifi.getisConnected()) {
                    Log.d("wifi",String.valueOf(wifi.getisConnected()));
                    toast_connection.show();
                    toast_connection.cancel();
                    //Toast k = Toast.makeText(this, text, Toast.LENGTH_SHORT);
                    // k.show();
                }else{
                    Intent intent = new Intent(getApplicationContext(),SparkActivity.class);
                    startActivity(intent);
                    timer.cancel();
                    //timerTask.cancel();
                    LocationOverlayActivity.super.finish();
                }*/

            }
        };


        timer.schedule(timerTask, 0, ONE_SECOND);
    }

    private void initSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        OrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(this, OrientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void makeToasts() {
        toast_off_limits = Toast.makeText(this, off_limits_text, LENGTH_SHORT);
        toast_connection = Toast.makeText(this, connection_text, Toast.LENGTH_LONG);
    }


    private void testpath(int i) {
        Log.i("path", "entrei aqui!");
        Log.i("path", String.valueOf(i));
        mapView.getOverLays().remove(locationOverlay);
        //mapView.getOverLays().
        locationOverlay = new SVGMapLocationOverlay(mapView);
        locationOverlay.setIndicatorArrowBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.indicator_arrow));
        locationOverlay.setPosition(new PointF(LIMIT_INICIAL_X + i, 350));
        locationOverlay.setIndicatorCircleRotateDegree(180);
        locationOverlay.setMode(SVGMapLocationOverlay.MODE_COMPASS);
        locationOverlay.setIndicatorArrowRotateDegree(getazimuth());
        //
        mapView.getOverLays().add(locationOverlay);

        mapView.refresh();
        //mapView.loadMap(AssetsHelper.getContent(this, "dcc-piso1-cortado.svg"));


    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ORIENTATION) {
            azimuth = sensorEvent.values[0];
            Log.d("azimuth", String.valueOf(azimuth));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    private int calculatedistance(int recv) {
        //int real_dist;
        //real_dist=Integer.valueOf(recv);
        int map_dist = (545 * recv) / 100;
        if (map_dist > LIMIT_FINAL_X) {
            toast_off_limits.show();
            //toast.show();
            toast_off_limits.show();
            return LIMIT_FINAL_X;

        }
        return map_dist;
    }
/*
    public float getOrientation() {
        float azi_local = getazimuth();
        if(azi_local>=NORTH && azi_local<){

        }

        return azi_local;
    }
*/
    private float getazimuth(){
        return this.azimuth;
    }


}
