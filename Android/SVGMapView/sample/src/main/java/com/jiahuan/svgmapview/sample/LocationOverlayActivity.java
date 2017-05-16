package com.jiahuan.svgmapview.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
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


public class LocationOverlayActivity extends ActionBarActivity {
    private final int THREE_SECONDS = 3000;
    private SVGMapView mapView;
    private int LIMIT_INICIAL_X = 85;
    private int LIMIT_FINAL_X = 630;
    private SVGMapLocationOverlay locationOverlay;
    private WifiActivity wifi;
    private CharSequence connection_text = "Connected to Rpi";
    private CharSequence off_limits_text = "OFF limits on map calculation";

    private Toast toast_off_limits;
    private Toast toast_connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        mapView = (SVGMapView) findViewById(R.id.location_mapview);

        Date data = new Date();
        wifi = new WifiActivity(data);

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






        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                //mapView.getOverLays().remove(locationOverlay);

                testpath(calculatedistance(wifi.getData()));

                if (wifi.getisConnected()) {
                    Log.d("wifi",String.valueOf(wifi.getisConnected()));
                    toast_connection.show();
                    toast_connection.cancel();
                    //Toast k = Toast.makeText(this, text, Toast.LENGTH_SHORT);
                    // k.show();
                }

            }
        };


        timer.schedule(timerTask, 0, THREE_SECONDS);
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
        locationOverlay.setIndicatorArrowRotateDegree(0);
        //
        mapView.getOverLays().add(locationOverlay);

        mapView.refresh();
        //mapView.loadMap(AssetsHelper.getContent(this, "dcc-piso1-cortado.svg"));


    }


    int calculatedistance(int recv) {
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


}
