package com.example.cx.yyweather.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cx.yyweather.model.City;
import com.example.cx.yyweather.model.Country;
import com.example.cx.yyweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cx on 2017/5/8.
 */

public class WeatherDB {
    public static final String DB_NAME="weather";

    public  static  final int VERSION=1;

    private static WeatherDB weatherDB;

    private SQLiteDatabase db;

    private WeatherDB(Context context){
        WeatherOpenHelper dbHelper=new WeatherOpenHelper(context,DB_NAME,null,VERSION);
        db=dbHelper.getWritableDatabase();
    }

    public synchronized static WeatherDB getInstance(Context context){
        if(weatherDB==null)
            weatherDB=new WeatherDB(context);
        return weatherDB;
    }

    public void saveProvince(Province province){
        if (province!=null){
            ContentValues values=new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("Province",null,values);
        }
    }

    public List<Province> loadProvince(){
        List<Province> list=new ArrayList<Province>();
        Cursor cursor=db.query("Province",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                Province province=new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while(cursor.moveToNext());
        }
        if (cursor!=null)
            cursor.close();
        return list;
    }

    public void saveCity(City city){
        if (city!=null){
            ContentValues values=new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceID());
            db.insert("City",null,values);
        }
    }

    public List<City> loadCity(int provinceID){
        List<City> list=new ArrayList<City>();
        Cursor cursor=db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceID)},null,null,null);
        if (cursor.moveToFirst()){
            do{
                City city=new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceID(provinceID);
                list.add(city);
            }while(cursor.moveToNext());
        }
        if (cursor!=null)
            cursor.close();
        return list;
    }

    public void saveCountry(Country country){
        if (country!=null){
            ContentValues values=new ContentValues();
            values.put("country_name",country.getCountryName());
            values.put("country_code",country.getCountryCode());
            values.put("city_id",country.getCityID());
            db.insert("Country",null,values);
        }
    }

    public List<Country> loadCountry(int cityID){
        List<Country> list=new ArrayList<Country>();
        Cursor cursor=db.query("Country",null,"city_id=?",new String[]{String.valueOf(cityID)},null,null,null);
        if (cursor.moveToFirst()){
            do{
                Country country=new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCityID(cityID);
                list.add(country);
            }while(cursor.moveToNext());
        }
        if (cursor!=null)
            cursor.close();
        return list;
    }
}
