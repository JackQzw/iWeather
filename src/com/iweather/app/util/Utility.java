package com.iweather.app.util;

import com.iweather.app.db.IWeatherDB;
import com.iweather.app.model.City;
import com.iweather.app.model.County;
import com.iweather.app.model.Province;

import android.text.TextUtils;

public class Utility {
	
	/**
	 * 解析和处理服务器返回的省级数据
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
					//将解析出的数据保存到Province表中
					iWeatherDB.saveProvince(province);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * 解析和处理服务器返回的城市数据
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
					//将解析出的数据保存到City表中
					iWeatherDB.saveCity(city);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的县级数据
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
					//将解析出的数据保存到County表中
					iWeatherDB.saveCounty(county);
				}
				
				return true;
			}
		}
		
		return false;
	}

}
