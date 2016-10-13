package com.iweather.app.util;

import com.iweather.app.db.IWeatherDB;
import com.iweather.app.model.City;
import com.iweather.app.model.County;
import com.iweather.app.model.Province;

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

}
