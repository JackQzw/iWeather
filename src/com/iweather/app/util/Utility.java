package com.iweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.iweather.app.db.IWeatherDB;
import com.iweather.app.model.City;
import com.iweather.app.model.County;
import com.iweather.app.model.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class Utility {
	
	/**
	 * �����ʹ�����������ص�ʡ������
	 */
	
	public synchronized static boolean handleProvincesRespone(IWeatherDB iWeatherDB, String respone)
	{
		if(!TextUtils.isEmpty(respone))
		{
			String[] allProvinces = respone.split(",");
			if(allProvinces != null && allProvinces.length > 0)
			{
				for(String p: allProvinces)
				{
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//�������������ݱ��浽Province����
					iWeatherDB.saveProvince(province);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * �����ʹ�����������صĳ�������
	 */
	
	public synchronized static boolean handleCitiesRespone(IWeatherDB iWeatherDB, String respone, int provinceId)
	{
		if(!TextUtils.isEmpty(respone))
		{
			String[] allCities = respone.split(",");
			if(allCities != null && allCities.length > 0)
			{
				for(String c: allCities)
				{
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//�������������ݱ��浽City����
					iWeatherDB.saveCity(city);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * �����ʹ�����������ص��ؼ�����
	 */
	
	public synchronized static boolean handleCountiesRespone(IWeatherDB iWeatherDB, String respone, int cityId)
	{
		if(!TextUtils.isEmpty(respone))
		{
			String[] allCounties = respone.split(",");
			if(allCounties != null && allCounties.length > 0)
			{
				for(String c: allCounties)
				{
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					//�������������ݱ��浽County����
					iWeatherDB.saveCounty(county);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * �������ص�JSON���ݣ����������������ݴ��浽����
	 */
	public static void handleWeatherResponse(Context context, String response)
	{
		try {
			
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
			
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	/**
	 * �����������ص�����������Ϣ���浽SharedPreferences�ļ���
	 * @param context
	 * @param cityName
	 * @param weatherCode
	 * @param temp1
	 * @param temp2
	 * @param weatherDesp
	 * @param publishTime
	 */
	
	private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1,
			String temp2, String weatherDesp, String publishTime) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��", Locale.CHINA);
		
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
	
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		
		editor.commit();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
