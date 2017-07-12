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
 
public interface Constant {

	//服务端IP地址
	public static final String CIM_SERVER_HOST = "cim.appleframework.com";

	//注意，这里的端口不是tomcat的端口，CIM端口在服务端spring-cim.xml中配置的，没改动就使用默认的23456
	public static final int CIM_SERVER_PORT = 9001;

	public static interface MessageType{
		//用户之间的普通消息
		public static final String TYPE_0 = "0";

		//下线类型
		String TYPE_999 = "999";
	}

	public static interface MessageStatus{
		//消息未读
		public static final String STATUS_0 = "0";
		//消息已经读取
		public static final String STATUS_1 = "1";
	}
		
}
