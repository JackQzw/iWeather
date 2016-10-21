package com.iweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtil {

	public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					Log.v("heool", "----->>>url" + address);
					connection = (HttpURLConnection) url.openConnection();
					Log.v("heool", "----->>>connection");
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					Log.v("heool", "----->>>111");
					InputStream is = connection.getInputStream();

					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					Log.v("heool", "----->>>222");

					String line = null;
					StringBuilder response = new StringBuilder();

					while ((line = br.readLine()) != null) {
						response.append(line);
						Log.v("heool", line);
					}
					Log.v("heool", "----->>>112");
					if (listener != null) {

						listener.onFinish(response.toString());
					}
					br.close();
					is.close();

				} catch (Exception e) {
					// TODO: handle exception
					if (listener != null) {
						Log.v("heool", "----->>>");
						e.printStackTrace();
						listener.onError(e);
					}

				} finally {

					if (connection != null) {

						Log.v("heool", "----->>>disconnect");
						connection.disconnect();
					}
				}
			}

		}).start();
	}

}
