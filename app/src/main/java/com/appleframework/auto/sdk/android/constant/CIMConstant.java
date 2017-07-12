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
package com.appleframework.auto.sdk.android.constant;

/** 
 * 常量
 */
public interface CIMConstant {
    
	long RECONN_INTERVAL_TIME= 30 * 1000;
	//消息头长度为3个字节，第一个字节为消息类型，第二，第三字节 转换int后为消息长度
	int DATA_HEADER_LENGTH = 3;

    public static interface ReturnCode{
		String CODE_404 ="404";
		String CODE_403 ="403";
		String CODE_405 ="405";
		String CODE_200 ="200";
		String CODE_206 ="206";
		String CODE_500 ="500";
	}

   public static interface ProtobufType{
	   byte C_H_RS = 0;
	   byte S_H_RQ = 1;
	   byte MESSAGE = 2;
	   byte SENTBODY = 3;
	   byte REPLYBODY = 4;
   }
	
   public static interface RequestKey{
	   String CLIENT_BIND ="client_bind";
	   String CLIENT_LOGOUT ="client_logout";
	   @Deprecated
	   String CLIENT_PULL_MESSAGE ="client_pull_message";
	   String CLIENT_LOCATION = "client_realtime_location";
	}
   
   
   public static interface MessageAction{
		
	    //被其他设备登录挤下线消息
		String ACTION_999 ="999";
		//被系统禁用消息
		String ACTION_444 ="444";
   }
   
   
   public static interface IntentAction{
		
	// 消息广播action
	String ACTION_MESSAGE_RECEIVED = "com.appleframework.auto.MESSAGE_RECEIVED";
	
	// 发送sendbody失败广播
	String ACTION_SENT_FAILED = "com.appleframework.auto.SENT_FAILED";
	
	// 发送sendbody成功广播
	String ACTION_SENT_SUCCESSED = "com.appleframework.auto.SENT_SUCCESSED";
	
	// 链接意外关闭广播
	String ACTION_CONNECTION_CLOSED = "com.appleframework.auto.CONNECTION_CLOSED";
	
	// 链接失败广播
	String ACTION_CONNECTION_FAILED = "com.appleframework.auto.CONNECTION_FAILED";
	
	// 链接成功广播
	String ACTION_CONNECTION_SUCCESSED = "com.appleframework.auto.CONNECTION_SUCCESSED";
	
	// 发送sendbody成功后获得replaybody回应广播
	String ACTION_REPLY_RECEIVED = "com.appleframework.auto.REPLY_RECEIVED";
	
	// 网络变化广播
	String ACTION_NETWORK_CHANGED = "android.net.conn.CONNECTIVITY_CHANGE";
	
	// 未知异常
	String ACTION_UNCAUGHT_EXCEPTION = "com.appleframework.auto.UNCAUGHT_EXCEPTION";
	
	//重试连接
	String ACTION_CONNECTION_RECOVERY = "com.appleframework.auto.CONNECTION_RECOVERY";
  }
  
}
