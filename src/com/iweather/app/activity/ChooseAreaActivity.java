package com.iweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.iweather.app.R;
import com.iweather.app.db.IWeatherDB;
import com.iweather.app.model.City;
import com.iweather.app.model.County;
import com.iweather.app.model.Province;
import com.iweather.app.util.HttpCallbackListener;
import com.iweather.app.util.HttpUtil;
import com.iweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {

	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private IWeatherDB iWeatherDB;
	private List<String> dataList = new ArrayList<String>();

	// 省列表
	private List<Province> provinceList;

	// 城市列表
	private List<City> cityList;

	// 县列表
	private List<County> countyList;

	// 选中的省份
	private Province selectedProvince;

	// 选中的城市
	private City selectedCity;

	// 选中的级别
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);

		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);

		iWeatherDB = IWeatherDB.getInstance(this);
		queryProvinces();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(position);
					queryCities();

				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(position);
					queryCounties();
				}
			}
		});

		

	}

	private void queryProvinces() {

		provinceList = iWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province p : provinceList) {
				dataList.add(p.getProvinceName());
			}

			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromSever(null, "province");
		}

	}

	private void queryCities() {

		cityList = iWeatherDB.loadCities(selectedProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City p : cityList) {
				dataList.add(p.getCityName());
			}

			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromSever(selectedProvince.getProvinceCode(), "city");
		}

	}

	private void queryCounties() {

		countyList = iWeatherDB.loadCounties(selectedCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County p : countyList) {
				dataList.add(p.getCountyName());
			}

			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromSever(selectedCity.getCityCode(), "county");
		}

	}

	private void queryFromSever(final String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";

		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}

		showProgressDialog();

		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvincesRespone(iWeatherDB, response);
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesRespone(iWeatherDB, response, selectedProvince.getId());
				} else if ("county".equals(type)) {
					result = Utility.handleCountiesRespone(iWeatherDB, response, selectedCity.getId());
				}

				if (result) {
					// 通过runonUIThread（）方法回到主线程处理逻辑
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}
					});
				}

			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();

						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					}
				});
			}

		});
	}
	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog()
	{
		if(progressDialog == null)
		{
			progressDialog = new ProgressDialog(this);
			
			progressDialog.setMessage("正在加载。。。");
			progressDialog.setCanceledOnTouchOutside(false);
			
			
		}
		progressDialog.show();
	}
	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog()
	{
		if(progressDialog != null)
		{
			progressDialog.dismiss();
		}
	}
	/**
	 * 处理back键回退，根据当前级别做出相应的处理
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		if(currentLevel == LEVEL_COUNTY)
		{
			queryCities();
			
		}
		else if(currentLevel == LEVEL_CITY)
		{
			queryProvinces();
		}
		else
		{
			finish();
		}
	}

}
