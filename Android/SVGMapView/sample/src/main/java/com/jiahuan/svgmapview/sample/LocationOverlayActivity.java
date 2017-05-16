package com.jiahuan.svgmapview.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.jiahuan.svgmapview.SVGMapView;
import com.jiahuan.svgmapview.SVGMapViewListener;
import com.jiahuan.svgmapview.core.data.SVGPicture;
import com.jiahuan.svgmapview.core.helper.ImageHelper;
import com.jiahuan.svgmapview.core.helper.map.SVGBuilder;
import com.jiahuan.svgmapview.overlay.SVGMapLocationOverlay;
import com.jiahuan.svgmapview.sample.helper.AssetsHelper;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Integer.parseInt;


public class LocationOverlayActivity extends ActionBarActivity
{
    private SVGMapView mapView;
    private int LIMIT_INICIAL_X=85;
    private int LIMIT_FINAL_X=630;
    private SVGMapLocationOverlay locationOverlay;
    private final int FIVE_SECONDS = 5000;
    private WifiActivity wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        mapView = (SVGMapView) findViewById(R.id.location_mapview);

        Date data = new Date();
        wifi = new WifiActivity(data);

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
            public void onMapLoadError()
            {

            }

            @Override
            public void onGetCurrentMap(Bitmap bitmap)
            {
            }
        });

        mapView.loadMap(AssetsHelper.getContent(this, "dcc-piso1-cortado.svg"));
        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            int i=0;
            @Override
            public void run() {
                //mapView.getOverLays().remove(locationOverlay);
                testpath(calculatedistance(wifi.getData()));
                if(LIMIT_INICIAL_X+i<LIMIT_FINAL_X){i=i+5;}

            }
        };


    timer.schedule(timerTask, 0, 3000);
    }



    private void testpath(int i) {
        Log.i("path","entrei aqui!");
        Log.i("path",String.valueOf(i));
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


    int calculatedistance(int recv){
    //int real_dist;
        //real_dist=Integer.valueOf(recv);
        int map_dist=(545*recv)/100;
        if(map_dist>LIMIT_FINAL_X){
            CharSequence text = "OFF limits on map calculation";
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            toast.show();
            return LIMIT_FINAL_X;

        }
    return map_dist;
}


}
