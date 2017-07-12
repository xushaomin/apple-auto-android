/**
 * Copyright 2013-2023 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.appleframework.auto.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;

import com.appleframework.auto.sdk.android.CIMPushManager;
import com.appleframework.auto.sdk.android.constant.CIMConstant;
import com.appleframework.auto.sdk.android.model.Message;
import com.appleframework.auto.sdk.android.model.SentBody;
import com.appleframework.auto.client.R;
import com.appleframework.auto.client.adapter.SystemMsgListViewAdapter;
import com.appleframework.auto.client.app.CIMMonitorActivity;
import com.appleframework.auto.client.app.Constant;

public class SystemMessageActivity extends CIMMonitorActivity implements OnClickListener{

	protected ListView chatListView;
	protected SystemMsgListViewAdapter adapter;
	private ArrayList<Message> list;

	private LocationManager locationManager;
	private LocationListener locationListener;
	private Long startTime = 0L;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_chat);
		initViews();
		startTime = System.currentTimeMillis();

		//绑定账号成功，获取离线消息
		getOfflineMessage();

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location != null){
			doLocation(location);
		}
		//监视地理位置变化
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, locationListener);
	}


	/**
	 * 显示地理位置经度和纬度信息
	 * @param location
	 */
	private void doLocation(Location location){
		String locationStr = "维度：" + location.getLatitude() +"\n" + "经度：" + location.getLongitude();
		showToask(locationStr);
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
		sent.put("account", this.getIntent().getStringExtra("account"));
		sent.put("latitude", String.valueOf(location.getLatitude()));
		sent.put("longitude", String.valueOf(location.getLongitude()));
		sent.put("altitude", String.valueOf(location.getAltitude()));
		sent.put("speed", String.valueOf(location.getSpeed()));
		sent.put("direction", String.valueOf(location.getBearing()));
		sent.put("time", String.valueOf(location.getTime()));
		CIMPushManager.sendRequest(this, sent);
	}

	private void sentJourney() {
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		SentBody sent = new SentBody();
		sent.setKey(CIMConstant.RequestKey.CLIENT_JOURNEY);
		sent.put("account", this.getIntent().getStringExtra("account"));
		sent.put("startTime", String.valueOf(startTime));
		sent.put("endTime", String.valueOf(endTime));
		sent.put("totalTime", String.valueOf(totalTime));
		CIMPushManager.sendRequest(this, sent);
	}

	public void initViews() {
		list = new ArrayList<Message>();
		chatListView = (ListView) findViewById(R.id.chat_list);
		findViewById(R.id.TOP_BACK_BUTTON).setOnClickListener(this);
		findViewById(R.id.TOP_BACK_BUTTON).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.TOP_BACK_BUTTON)).setText("登录");
		((TextView) findViewById(R.id.TITLE_TEXT)).setText("系统消息");
		((TextView) findViewById(R.id.account)).setText(this.getIntent().getStringExtra("account"));
		 adapter = new SystemMsgListViewAdapter(this, list);
		 chatListView.setAdapter(adapter);
		 showToask("登录成功，请通过后台页面发送消息吧^_^");
	}
	
	//收到消息
	@Override
	public void onMessageReceived(Message message) {
		if(message.getAction().equals(Constant.MessageType.TYPE_999)) {
			  //返回登录页面，停止接受消息
		      CIMPushManager.stop(this);
			  this.showToask("你被迫下线!");
			  Intent intent = new Intent(this, LoginActivity.class);
			  startActivity(intent);
			  this.finish();
		}else {
			MediaPlayer.create(this, R.raw.classic).start();
			list.add(message);
			adapter.notifyDataSetChanged();
			chatListView.setSelection(chatListView.getTop());
		}
	}
 
	//获取离线消息，代码示例，前提是服务端要实现此功能,建议使用http 接口拉去大量的离线消息
	private void getOfflineMessage() {
		SentBody sent = new SentBody();
		sent.setKey(CIMConstant.RequestKey.CLIENT_PULL_MESSAGE);
		sent.put("account", this.getIntent().getStringExtra("account"));
		CIMPushManager.sendRequest(this, sent);
	}
	
	@Override
	public void onNetworkChanged(NetworkInfo info){
		if(info == null) {
			showToask("网络已断开");
		}else {
			showToask("网络已恢复，重新连接....");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.TOP_BACK_BUTTON: {
				onBackPressed();
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		this.locationManager.removeUpdates(locationListener);
		this.sentJourney();
		//返回登录页面，停止接受消息
	    CIMPushManager.stop(this);
	    Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		this.finish();
	}

}
