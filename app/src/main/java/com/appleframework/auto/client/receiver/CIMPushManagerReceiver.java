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
package com.appleframework.auto.client.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;

import com.appleframework.auto.sdk.android.CIMEventBroadcastReceiver;
import com.appleframework.auto.sdk.android.CIMListenerManager;
import com.appleframework.auto.sdk.android.model.Message;
import com.appleframework.auto.sdk.android.model.ReplyBody;
import com.appleframework.auto.client.R;
import com.appleframework.auto.client.ui.SystemMessageActivity;

import static android.R.id.message;
import static com.appleframework.auto.client.R.id.content;

/**
 *  消息入口，所有消息都会经过这里
 * @author 3979434
 *
 */
public final class CIMPushManagerReceiver extends CIMEventBroadcastReceiver {
 
	private NotificationManager notificationManager;

	//当收到消息时，会执行onMessageReceived，这里是消息第一入口

	@Override
	public void onMessageReceived(Message message, Intent intent) {

        //调用分发消息监听
		CIMListenerManager.notifyOnMessageReceived(message);
		
		//以开头的为动作消息，无须显示,如被强行下线消息Constant.TYPE_999
		if(message.getAction().startsWith("9")) {
			return ;
		}
		showNotify(context,message);
	}


	 
	private void  showNotify(Context context , Message msg) {
				
		this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		String title  = "系统消息";

		PendingIntent contentIntent = PendingIntent.getActivity(context,1, new Intent(context,SystemMessageActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setAutoCancel(true);
		builder.setDefaults(Notification.DEFAULT_ALL);
		builder.setWhen(msg.getTimestamp());
		builder.setSmallIcon(R.drawable.icon);
		builder.setTicker(title);
		builder.setContentTitle(title);
		builder.setContentText(msg.getContent());
		builder.setDefaults(Notification.DEFAULT_LIGHTS);
		builder.setContentIntent(contentIntent);
		final Notification notification = builder.build();

		notificationManager.notify(R.drawable.icon, notification);
	}


	@Override
	public void onNetworkChanged(NetworkInfo info) {
		CIMListenerManager.notifyOnNetworkChanged(info);
	}


	@Override
	public void onConnectionSuccessed(boolean hasAutoBind) {
		CIMListenerManager.notifyOnConnectionSuccessed(hasAutoBind);
	}

	@Override
	public void onConnectionClosed() {
		CIMListenerManager.notifyOnConnectionClosed();
	}


	@Override
	public void onReplyReceived(ReplyBody body) {
		CIMListenerManager.notifyOnReplyReceived(body);
	}



	@Override
	public void onConnectionFailed() {
		// TODO Auto-generated method stub
		CIMListenerManager.notifyOnConnectionFailed();
	}
 
}
