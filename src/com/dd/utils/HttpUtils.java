package com.dd.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.dd.config.DataConfig;

public class HttpUtils {

	/**
	 * POST请求
	 * 
	 * @return
	 */
	public static String doPost(String action, List<NameValuePair> params) {

		HttpPost httpRequest = new HttpPost(DataConfig.URL + action);
		String json = "";
		try {
			// 添加请求参数到请求对象
			if (params != null)
				httpRequest.setEntity(new UrlEncodedFormEntity(params,
						HTTP.UTF_8));
			HttpClient httpClient = new DefaultHttpClient();
			// 发送请求并等待响应
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			int code = httpResponse.getStatusLine().getStatusCode();
			if (code == 200) {
				json = EntityUtils.toString(httpResponse.getEntity());
			} else {
				json = "";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}
}
