package com.appleframework.auto.client.manager;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.appleframework.auto.client.app.CommonBaseControl;
import com.appleframework.auto.sdk.android.CIMPushManager;
import com.appleframework.auto.sdk.android.constant.CIMConstant;
import com.appleframework.auto.sdk.android.model.SentBody;

public class JourneyUploadManager extends BaseUploadManager {

    private Location lastLocation = null;

    private Long startTime; // 开始时间
    private Long endTime; // 结束时间
    private Long totalTime = 0L; // 行驶时长 (s)
    private Integer mileage = 0; // 行驶里程 (m)
    private Double oilWear = 0d; // 油耗 （ml）
    private Integer idleTime = 0; // 怠速时长 (s)
    private Integer rushUpNO = 0; // 急加速次数
    private Integer rushSlowNO = 0; // 急减速次数
    private Integer rushTurnNO = 0; // 急转弯次数
    private Integer overSpeedNO = 0; // 超速次数

    public JourneyUploadManager(String account, Context context, LocationManager locationManager, CommonBaseControl commonBaseControl){
        this.account = account;
        this.context = context;
        this.locationManager = locationManager;
        this.commonBaseControl = commonBaseControl;
    }

    public void start() {
        this.lastLocation = null;
        this.startTime = System.currentTimeMillis();
        this.endTime = 0L;
        this.totalTime = 0L; // 行驶时长 (s)
        this.mileage = 0; // 行驶里程 (m)
        this.oilWear = 0d; // 油耗 （ml）
        this.idleTime = 0; // 怠速时长 (s)
        this.rushUpNO = 0; // 急加速次数
        this.rushSlowNO = 0; // 急减速次数
        this.rushTurnNO = 0; // 急转弯次数
        this.overSpeedNO = 0; // 超速次数

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

    private void doLocation(Location location){
        long time = location.getTime();
        long now = System.currentTimeMillis();
        if(Math.abs(now - time) > 10000) {
            return;
        }

        if (null == lastLocation) {
            lastLocation = location;
        }
        else {

            float nowSpeed = location.getSpeed();
            float lastSpeed = lastLocation.getSpeed();

            //判断急加速
            if (nowSpeed - lastSpeed > 0 && nowSpeed - lastSpeed >= 10) {
                rushUpNO ++;
            }

            //判断急减速
            if (nowSpeed - lastSpeed > 0 && nowSpeed - lastSpeed <= -10) {
                rushSlowNO ++;
            }

            lastLocation = location;

            String locationStr = "行程\n维度：" + location.getLatitude() +"\n" + "经度：" + location.getLongitude();
            showToask(locationStr);

        }
    }

    public void sentJourney() {
        this.endTime = System.currentTimeMillis();
        this.totalTime = endTime - startTime;
        SentBody sent = new SentBody();
        sent.setKey(CIMConstant.RequestKey.CLIENT_JOURNEY);
        sent.put("account", account);
        sent.put("startTime", String.valueOf(startTime));
        sent.put("endTime", String.valueOf(endTime));
        sent.put("totalTime", String.valueOf(totalTime));
        sent.put("rushUpNO", String.valueOf(rushUpNO));
        sent.put("rushSlowNO", String.valueOf(rushSlowNO));
        CIMPushManager.sendRequest(context, sent);
    }

}
