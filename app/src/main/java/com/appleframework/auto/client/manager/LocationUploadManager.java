package com.appleframework.auto.client.manager;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.appleframework.auto.sdk.android.CIMPushManager;
import com.appleframework.auto.sdk.android.constant.CIMConstant;
import com.appleframework.auto.sdk.android.model.SentBody;

public class LocationUploadManager extends  BaseUploadManager {

    public LocationUploadManager(String account, Context context, LocationManager locationManager){
        this.account = account;
        this.context = context;
        this.locationManager = locationManager;
    }

    public void start() {
        locationListener =  new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle arg2) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}

            @Override
            public void onLocationChanged(Location location) {
                if(location != null) {
                    doLocation(location);
                }
            }
        };
        //监视地理位置变化
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 20, locationListener);
    }

    private void doLocation(Location location){
        sentLocation(location);
    }

    private void sentLocation(Location location) {
        long time = location.getTime();
        long now = System.currentTimeMillis();
        if(Math.abs(now - time) > 10000) {
            return;
        }
        SentBody sent = new SentBody();
        sent.setKey(CIMConstant.RequestKey.CLIENT_LOCATION);
        sent.put("account", account);
        sent.put("latitude", String.valueOf(location.getLatitude()));
        sent.put("longitude", String.valueOf(location.getLongitude()));
        sent.put("altitude", String.valueOf(location.getAltitude()));
        sent.put("speed", String.valueOf(location.getSpeed()));
        sent.put("direction", String.valueOf(location.getBearing()));
        sent.put("time", String.valueOf(location.getTime()));
        CIMPushManager.sendRequest(context, sent);
    }

}
