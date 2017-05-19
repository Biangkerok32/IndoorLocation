package com.jiahuan.svgmapview.sample;


import com.jiahuan.svgmapview.SVGMapView;
import com.jiahuan.svgmapview.SVGMapViewListener;
import com.jiahuan.svgmapview.overlay.SVGMapLocationOverlay;
import com.jiahuan.svgmapview.sample.helper.AssetsHelper;


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


import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Activity responsible for showing a SVG map with a beacon(position) based on the comunication with the raspberry pi
 */
public class LocationOverlayActivity extends ActionBarActivity implements SensorEventListener {


    private SVGMapView mapView; // var: mapView responsible to be the layout for the SVG , class from a SVG parser for android

    private SVGMapLocationOverlay locationOverlay; // var : for marking the position on mapView


    private final int ONE_SECOND = 1000; // var: determines the time loop (1s) for a Timer Task
    private final int LIMIT_INICIAL_X = 85; // var : inicial point of the SVG map that is considered to be a path on witch we locate the position
    private final int LIMIT_FINAL_X = 630; // var : final point of the SVG map that is considered to be a path on witch we locate the position

    private WifiActivity wifi; // var: responsible for the UDP communication

    private CharSequence connection_text = "Connected to Rpi";
    private CharSequence off_limits_text = "OFF limits on map calculation";

    private float azimuth = 0; // var: show current orientation

    private Toast toast_off_limits; // var: show some err msg
    //private Toast toast_connection;

    private SensorManager mSensorManager; // var: for getting a instance of the hardware device's ( lets us access the sensor device's)
    private Sensor OrientationSensor; // var : sensor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        mapView = (SVGMapView) findViewById(R.id.location_mapview);

        Date data = new Date();
        wifi = new WifiActivity(data); // Criation on the connection on the Creation of the activity

        initSensor(); // Iniciation the sensor

        makeToasts(); // Preparation for error msgs


        /*
        Listener while loading of the map
         */
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

        /***
         *
         * Creation of task that will check for updates of Rpi (1s) and mark the current position
         *
         *  */
        final Timer timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                //mapView.getOverLays().remove(locationOverlay);

                calculatepath(calculatedistance(wifi.getData())); // Presented with real distance and calculate to corresponding  map reason
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


    /**
     * Get's default sensor, iniciates the desire sensor, then register's a listener
     *
     */
    private void initSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        OrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(this, OrientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Making toast for err msg ....
     */
    private void makeToasts() {
        toast_off_limits = Toast.makeText(this, off_limits_text, LENGTH_SHORT);
        //toast_connection = Toast.makeText(this, connection_text, Toast.LENGTH_LONG);
    }

    /**
     * Method responsible for showing the marker on the map on our current position
     * @param i : number of points correspondent on the X-axis
     */
    private void calculatepath(int i) {
        Log.i("path", "entrei aqui!");
        Log.i("path", String.valueOf(i));

        mapView.getOverLays().remove(locationOverlay); // removes any previous marker

        locationOverlay = new SVGMapLocationOverlay(mapView); // instanciates a new marker
        locationOverlay.setIndicatorArrowBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.indicator_arrow)); // adding layer type of marker
        locationOverlay.setPosition(new PointF(LIMIT_INICIAL_X + i, 350)); // setting position

        locationOverlay.setIndicatorCircleRotateDegree(180);
        locationOverlay.setMode(SVGMapLocationOverlay.MODE_COMPASS);
        locationOverlay.setIndicatorArrowRotateDegree(getazimuth()); // set indication on the correct orientation

        mapView.getOverLays().add(locationOverlay); // add marker to map

        mapView.refresh();

        //mapView.loadMap(AssetsHelper.getContent(this, "dcc-piso1-cortado.svg"));


    }

    /**
     * Listener that captures any changes measured  by  the sensor's
     * @param sensorEvent : any event capture by hardware sensor's
     */
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
        //DO NOTHING
    }

    /**
     * Calculates correspondet position for the Current chosen Map
     * @param recv: real distance of a pre determined position
     * @return :  correspondent position for the map
     */
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

    /**
     *
     * @return : current orientation
     */
    private float getazimuth(){
        return this.azimuth;
    }


}
