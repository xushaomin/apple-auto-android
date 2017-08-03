package com.appleframework.auto.client.manager;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

import com.appleframework.auto.client.app.CommonBaseControl;

public abstract class BaseUploadManager {

    public static final int STATE_START = 1; // 正常状态
    public static final int STATE_STOP  = 0; // 暂停状态

    protected int state = STATE_START;

    protected CommonBaseControl commonBaseControl;

    protected LocationManager locationManager;

    protected LocationListener locationListener;

    protected Context context;

    protected String account; // 用户

    public abstract void start();

    public void stop() {
        this.state = STATE_STOP;
        this.locationManager.removeUpdates(locationListener);
    }

    public LocationListener getLocationListener(){
        return this.locationListener;
    }

    public void showToask(String hint){
        commonBaseControl.showToask(hint);
    }

}
