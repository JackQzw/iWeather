package com.iweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtil {

	public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {

		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					Log.v("heool", "----->>>url");
					connection = (HttpURLConnection) url.openConnection();
					Log.v("heool", "----->>>connection");
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					Log.v("heool", "----->>>111");
					InputStream is = connection.getInputStream();
					
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String line = null;
					StringBuilder response = new StringBuilder();
					
					while((line = br.readLine()) != null)
					{
						response.append(line);
					}
					
					if(listener != null)
					{
						listener.onFinish(response.toString());
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					if(listener != null)
					{
						Log.v("heool", "----->>>");
						listener.onError(e);
					}
					
				}finally
				{
					if(connection != null)
					{
						connection.disconnect();
					}
				}
			}
			
		}).start();;
	}

}
