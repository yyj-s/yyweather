package com.example.cx.yyweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cx.yyweather.R;
import com.example.cx.yyweather.database.WeatherDB;
import com.example.cx.yyweather.model.City;
import com.example.cx.yyweather.model.Country;
import com.example.cx.yyweather.model.Province;
import com.example.cx.yyweather.util.HttpCallbackListener;
import com.example.cx.yyweather.util.HttpUtil;
import com.example.cx.yyweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cx on 2017/5/9.
 */

public class ChooseAreaActivity extends Activity {

    public static final int LEVEL_PROVINCE=0;

    public static final int LEVEL_CITY=1;

    public static final int LEVEL_COUNTRY=2;

    private boolean isFromeWeatherActivity;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private WeatherDB weatherDB;
    private List<String> datalist = new ArrayList<String>();

    private List<Province> provinceList;

    private List<City> cityList;

    private List<Country> countryList;

    private Province selectedProvince;
    private City selectedCity;
    private Country selectedCountry;

    private int currentLevel;

    @Override
    protected void onCreate(final Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        isFromeWeatherActivity=getIntent().getBooleanExtra("from_weather_activity",false);
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("city_selected",false)&&!isFromeWeatherActivity){
            Intent intent=new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView=(ListView)findViewById(R.id.list_view);
        titleText=(TextView)findViewById(R.id.title_text);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,datalist);
        listView.setAdapter(adapter);
        weatherDB=WeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(position);
                    queryCity();
                }else if (currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(position);
                    queryCountry();
                }else if (currentLevel==LEVEL_COUNTRY){
                    String countryCode=countryList.get(position).getCountryCode();
                    Intent intent=new Intent(ChooseAreaActivity.this,WeatherActivity.class);
                    intent.putExtra("country_code",countryCode);
                    startActivity(intent);
                    finish();
                }
            }
        });
        queryProvince();
    }
    /**
     *
     */
    private void queryProvince(){
        provinceList=weatherDB.loadProvince();
        if (provinceList.size()>0){
            datalist.clear();
            for (Province province:provinceList){
                datalist.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("China");
            currentLevel=LEVEL_PROVINCE;
        }else{
            queryFromServer(null,"province");
        }
    }
    /**
     *
     */
    private void queryCity(){
        cityList=weatherDB.loadCity(selectedProvince.getId());
        if (cityList.size()>0){
            datalist.clear();
            for (City city:cityList){
                datalist.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel=LEVEL_CITY;
        }else{
            queryFromServer(selectedProvince.getProvinceCode(),"city");
        }
    }
    /**
     *
     */
    private void queryCountry(){
        countryList=weatherDB.loadCountry(selectedCity.getId());
        if (countryList.size()>0){
            datalist.clear();
            for (Country country:countryList){
                datalist.add(country.getCountryName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel=LEVEL_COUNTRY;
        }else{
            queryFromServer(selectedCity.getCityCode(),"country");
        }
    }
    /**
     *
     */
    private void queryFromServer(final String code,final String type){
        String address;
        if (!TextUtils.isEmpty(code)){
            address="http://www.weather.com.cn/data/list3/city"+code+".xml";
        }else{
            address="http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result=false;
                if ("province".equals(type)){
                    result= Utility.handleProvinceResponse(weatherDB,response);
                }else if ("city".equals(type)){
                    result=Utility.handleCityResponse(weatherDB,response,selectedProvince.getId());
                }
                else if ("country".equals(type)){
                    result=Utility.handleCountryResponse(weatherDB,response,selectedCity.getId());
                }
                if (result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvince();
                            }
                            if ("city".equals(type)){
                                queryCity();
                            }
                            if ("country".equals(type)){
                                queryCountry();
                            }
                        }
                    });
                }

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void showProgressDialog(){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("正在努力加载>.<");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed(){
        if (currentLevel==LEVEL_COUNTRY){
            queryCity();
        }else if (currentLevel==LEVEL_CITY){
            queryProvince();
        }else{
            if (isFromeWeatherActivity){
                Intent intent=new Intent(this,WeatherActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }


}
