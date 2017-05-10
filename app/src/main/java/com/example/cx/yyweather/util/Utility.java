package com.example.cx.yyweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.icu.text.SimpleDateFormat;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.cx.yyweather.database.WeatherDB;
import com.example.cx.yyweather.model.City;
import com.example.cx.yyweather.model.Country;
import com.example.cx.yyweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

/**
 * Created by cx on 2017/5/9.
 */

public class Utility {
    /**
     * 省
     */
    public synchronized static boolean handleProvinceResponse(WeatherDB weatherDB,String response){
        if (!TextUtils.isEmpty(response)){
            String[] allProvince=response.split(",");
            if (allProvince!=null&&allProvince.length>0){
                for (String p : allProvince){
                    String[] array=p.split("\\|");
                    Province province=new Province();
                    province.setProvinceName(array[1]);
                    province.setProvinceCode(array[0]);
                    weatherDB.saveProvince(province);
                }
                return  true;
            }
        }
        return false;
    }
    /**
     * 市
     */
    public synchronized static boolean handleCityResponse(WeatherDB weatherDB,String response,int provinceID){
        if (!TextUtils.isEmpty(response)){
            String[] allCity=response.split(",");
            if (allCity!=null&&allCity.length>0){
                for (String p : allCity){
                    String[] array=p.split("\\|");
                    City city=new City();
                    city.setCityName(array[1]);
                    city.setCityCode(array[0]);
                    city.setProvinceID(provinceID);
                    weatherDB.saveCity(city);
                }
                return  true;
            }
        }
        return false;
    }
    /**
     * country
     */
    public synchronized static boolean handleCountryResponse(WeatherDB weatherDB,String response,int cityID){
        if (!TextUtils.isEmpty(response)){
            String[] allCountry=response.split(",");
            if (allCountry!=null&&allCountry.length>0){
                for (String p : allCountry){
                    String[] array=p.split("\\|");
                    Country country=new Country();
                    country.setCountryName(array[1]);
                    country.setCountryCode(array[0]);
                    country.setCityID(cityID);
                    weatherDB.saveCountry(country);
                }
                return  true;
            }
        }
        return false;
    }
    /**
     * 解析数据
     */
    public static void handleWeatherResponse(Context context,String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONObject weatherInfo=jsonObject.getJSONObject("weatherinfo");
            String cityName=weatherInfo.getString("city");
            String weatherCode=weatherInfo.getString("cityid");
            String temp1=weatherInfo.getString("temp1");
            String temp2=weatherInfo.getString("temp2");
            String weatherDesp=weatherInfo.getString("weather");
            String publishTime=weatherInfo.getString("ptime");
            saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public  static void saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,String weatherDesp,String publishTime ){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_delected",true);
        editor.putString("city_name",cityName);
        editor.putString("weather_code",weatherCode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherDesp);
        editor.putString("publish_time",publishTime);
        editor.putString("current_date",simpleDateFormat.format(new Date()));
        editor.commit();
    }




}
