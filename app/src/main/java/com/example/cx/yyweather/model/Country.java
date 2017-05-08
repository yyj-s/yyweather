package com.example.cx.yyweather.model;

/**
 * Created by cx on 2017/5/8.
 */

public class Country {
    private int id;
    private String countryName;
    private String countryCode;
    private int cityID;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getCountryName(){
        return countryName;
    }
    public void setCountryName(String countryName){
        this.countryName=countryName;
    }
    public String getCountryCode(){
        return countryCode;
    }
    public void setCountryCode(String countryCode){
        this.countryCode=countryCode;
    }
    public int getCityID(){
        return cityID;
    }
    public void setCityID(int cityID){
        this.cityID=cityID;
    }
}
