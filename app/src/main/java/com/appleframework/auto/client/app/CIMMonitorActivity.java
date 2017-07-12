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
package com.appleframework.auto.client.app;

import com.appleframework.auto.sdk.android.CIMEventListener;
import com.appleframework.auto.sdk.android.CIMListenerManager;
import com.appleframework.auto.sdk.android.model.Message;
import com.appleframework.auto.sdk.android.model.ReplyBody;
import com.appleframework.auto.sdk.android.model.SentBody;

import android.app.Activity;
import android.net.NetworkInfo;
import android.os.Bundle;

public abstract class CIMMonitorActivity extends Activity implements CIMEventListener{

	CommonBaseControl commonBaseControl;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CIMListenerManager.registerMessageListener(this);
		commonBaseControl = new CommonBaseControl(this);
	}

	@Override
	public void finish() {
		super.finish();
		CIMListenerManager.removeMessageListener(this);
	}
 
	@Override
	public void onRestart() {
		super.onRestart();
		CIMListenerManager.registerMessageListener(this);
	}
	
	
	public void showProgressDialog(String title,String content) {
		commonBaseControl.showProgressDialog(title, content);
	}
	
	public void hideProgressDialog() {
		commonBaseControl.hideProgressDialog();
	}
	
	public void showToask(String hint){
		commonBaseControl.showToask(hint);
	}

	@Override
	public void onMessageReceived(Message arg0){};
	 
	@Override
	public void onNetworkChanged(NetworkInfo info){}

	/**
     * 与服务端断开连接时回调,不要在里面做连接服务端的操作
     */
	@Override
	public void onConnectionClosed() {}

	@Override
	public void onConnectionFailed() {

	}

	@Override
	public int getEventDispatchOrder() {
		return 0;
	}

	/**
    * 连接服务端成功时回调
    */
	
	@Override
	public void onConnectionSuccessed(boolean arg0) {}

	
	@Override
	public void onReplyReceived(ReplyBody arg0) {}

	@Override
	public void onSentSuccessed(SentBody sentBody) {

	}
}
