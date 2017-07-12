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
 *                        Website : http://www.appleframework.com                      *
 *                                                                                     *
 ***************************************************************************************
 */
package com.appleframework.auto.client.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.appleframework.auto.sdk.android.CIMPushManager;
import com.appleframework.auto.client.R;
import com.appleframework.auto.client.app.CIMMonitorActivity;
import com.appleframework.auto.client.app.Constant;

public class SplanshActivity extends CIMMonitorActivity{
	
	boolean initComplete = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//连接服务端
		CIMPushManager.connect(SplanshActivity.this,Constant.CIM_SERVER_HOST, Constant.CIM_SERVER_PORT);
		final View view = View.inflate(this, R.layout.activity_splansh, null);
		setContentView(view);
		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
	}

	@Override
	public void onConnectionSuccessed(boolean autoBind) {
		Intent intent = new Intent(SplanshActivity.this,LoginActivity.class);  
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		finish();
		CIMPushManager.destroy(this);
	}

	 public void onConnectionFailed(Exception e){
		 showToask("连接服务器失败，请检查当前设备是否能连接上服务器IP和端口");
	 }
}
