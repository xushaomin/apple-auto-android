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
package com.appleframework.auto.sdk.android;

import com.appleframework.auto.sdk.android.constant.CIMConstant;
import com.appleframework.auto.sdk.android.exception.SessionDisconnectedException;
import com.appleframework.auto.sdk.android.model.Message;
import com.appleframework.auto.sdk.android.model.ReplyBody;
import com.appleframework.auto.sdk.android.model.SentBody;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *  消息入口，所有消息都会经过这里
 */
public abstract class CIMEventBroadcastReceiver extends BroadcastReceiver{
 
	protected Context context;
	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		context = ctx;
		/*
		 * 操作事件广播，用于提高service存活率
		 */
		if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)
			 ||intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)
			 ||intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
			startPushService();
       	}

		/*
         * 设备网络状态变化事件
         */
		 if(intent.getAction().equals(CIMConstant.IntentAction.ACTION_NETWORK_CHANGED)) {
			 ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			 onDevicesNetworkChanged(connectivityManager.getActiveNetworkInfo());
		 }
		  
		  /*
           * cim断开服务器事件
           */
          if(intent.getAction().equals(CIMConstant.IntentAction.ACTION_CONNECTION_CLOSED)) {
			  onInnerConnectionClosed();
          }
          
          /*
           * cim连接服务器失败事件
           */
          if(intent.getAction().equals(CIMConstant.IntentAction.ACTION_CONNECTION_FAILED)) {
        	  long interval = intent.getLongExtra("interval", CIMConstant.RECONN_INTERVAL_TIME);
        	  onConnectionFailed((Exception) intent.getSerializableExtra(Exception.class.getName()),interval);
          }
          
          /*
           * cim连接服务器成功事件
           */
          if(intent.getAction().equals(CIMConstant.IntentAction.ACTION_CONNECTION_SUCCESSED)) {
        	  onInnerConnectionSuccessed();
          }
          
          /*
           * 收到推送消息事件
           */
          if(intent.getAction().equals(CIMConstant.IntentAction.ACTION_MESSAGE_RECEIVED)) {
        	  onInnerMessageReceived((Message)intent.getSerializableExtra(Message.class.getName()),intent);
          }

          /*
           * 获取收到replybody成功事件
           */
          if(intent.getAction().equals(CIMConstant.IntentAction.ACTION_REPLY_RECEIVED)) {
        	  onReplyReceived((ReplyBody)intent.getSerializableExtra(ReplyBody.class.getName()));
          }

          /*
           * 获取sendbody发送失败事件
           */
          if(intent.getAction().equals(CIMConstant.IntentAction.ACTION_SENT_FAILED)) {
			  Exception exception = (Exception) intent.getSerializableExtra(Exception.class.getName());
        	  SentBody  sentBody = (SentBody)intent.getSerializableExtra(SentBody.class.getName());
        	  onSentFailed(exception,sentBody);
          }
          
          /*
           * 获取sendbody发送成功事件
           */
          if(intent.getAction().equals(CIMConstant.IntentAction.ACTION_SENT_SUCCESSED)) {
        	  onSentSucceed((SentBody)intent.getSerializableExtra(SentBody.class.getName()));
          }

          /*
           * 获取cim数据传输异常事件
           */
          if(intent.getAction().equals(CIMConstant.IntentAction.ACTION_UNCAUGHT_EXCEPTION)) {
        	  onUncaughtException((Exception)intent.getSerializableExtra(Exception.class.getName()));
          }

          /*
           * 重新连接，如果断开的话
           */
          if(intent.getAction().equals(CIMConstant.IntentAction.ACTION_CONNECTION_RECOVERY)) {
        	  CIMPushManager.connect(context,0);
          }
	}

	private void startPushService(){
		Intent intent  = new Intent(context, CIMPushService.class);
		intent.setAction(CIMPushManager.ACTION_ACTIVATE_PUSH_SERVICE);
		context.startService(intent);
	}
	
	private  void onInnerConnectionClosed(){
		CIMCacheToolkit.getInstance(context).putBoolean(CIMCacheToolkit.KEY_CIM_CONNECTION_STATE, false);
		if(CIMConnectorManager.isNetworkConnected(context)) {
			CIMPushManager.connect(context,0);
		}
		onConnectionClosed();
	}

	private void onConnectionFailed(Exception e,long reinterval){
		if(CIMConnectorManager.isNetworkConnected(context)) {
			onConnectionFailed();
			CIMPushManager.connect(context,reinterval);
		}
	}

	private void onInnerConnectionSuccessed(){
		CIMCacheToolkit.getInstance(context).putBoolean(CIMCacheToolkit.KEY_CIM_CONNECTION_STATE, true);
		boolean autoBind = CIMPushManager.autoBindAccount(context);
		onConnectionSuccessed(autoBind);
	}

	private void onUncaughtException(Throwable arg0) {}

	private void onDevicesNetworkChanged(NetworkInfo info) {
		if(info !=null) {
			CIMPushManager.connect(context,0);
		}
		onNetworkChanged(info);
	}

	private void onInnerMessageReceived(com.appleframework.auto.sdk.android.model.Message message, Intent intent){
		if(isForceOfflineMessage(message.getAction())) {
			CIMPushManager.stop(context);
		}
		onMessageReceived(message,intent);
	}
	
	private boolean isForceOfflineMessage(String action) {
		return CIMConstant.MessageAction.ACTION_999.equals(action) || CIMConstant.MessageAction.ACTION_444.equals(action);
	}
	
	private void onSentFailed(Exception e, SentBody body){
		//与服务端端开链接，重新连接
		if(e instanceof SessionDisconnectedException) {
			CIMPushManager.connect(context,0);
		}else {
			//发送失败 重新发送
			CIMPushManager.sendRequest(context, body);
		}
		
	}

	public abstract void onMessageReceived(com.appleframework.auto.sdk.android.model.Message message,Intent intent);
	
	public void onNetworkChanged(NetworkInfo info) {
		CIMListenerManager.notifyOnNetworkChanged(info);
	}

	public void onConnectionSuccessed(boolean hasAutoBind) {
		CIMListenerManager.notifyOnConnectionSuccessed(hasAutoBind);
	}

	public void onConnectionClosed() {
		CIMListenerManager.notifyOnConnectionClosed();
	}

	public void onConnectionFailed() {
		CIMListenerManager.notifyOnConnectionFailed();
	}
	
	public void onReplyReceived(ReplyBody body) {
		CIMListenerManager.notifyOnReplyReceived(body);
	}
	 
	public void onSentSucceed(SentBody body){
		CIMListenerManager.notifyOnSentSucceed(body);
	}
}
