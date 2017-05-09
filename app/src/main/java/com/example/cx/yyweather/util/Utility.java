package com.example.cx.yyweather.util;

import android.text.TextUtils;

import com.example.cx.yyweather.database.WeatherDB;
import com.example.cx.yyweather.model.City;
import com.example.cx.yyweather.model.Country;
import com.example.cx.yyweather.model.Province;

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




}
