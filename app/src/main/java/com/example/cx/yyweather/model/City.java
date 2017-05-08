package com.example.cx.yyweather.model;

import java.lang.ref.PhantomReference;

/**
 * Created by cx on 2017/5/8.
 */

public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceID;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getCityName(){
        return cityName;
    }
    public void setCityName(String cityName){
        this.cityName=cityName;
    }
    public String getCityCode(){
        return cityCode;
    }
    public void setCityCode(String cityCode){
        this.cityCode=cityCode;
    }
    public int getProvinceID(){
        return provinceID;
    }
    public void setProvinceID(int provinceID){
        this.provinceID=provinceID;
    }
}
