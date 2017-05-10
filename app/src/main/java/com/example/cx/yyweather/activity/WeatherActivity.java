package com.example.cx.yyweather.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cx.yyweather.R;
import com.example.cx.yyweather.service.AutoUpdateService;
import com.example.cx.yyweather.util.HttpCallbackListener;
import com.example.cx.yyweather.util.HttpUtil;
import com.example.cx.yyweather.util.Utility;

/**
 * Created by cx on 2017/5/10.
 */

public class WeatherActivity extends Activity {
    private LinearLayout weatherInfoLy;
    private TextView cityNameT;
    private TextView publishTimeT;
    private TextView weatherDespT;
    private TextView temp1T;
    private TextView temp2T;
    private TextView currentDateT;
    private Button switchCity;
    private Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weatherly);

        weatherInfoLy=(LinearLayout)findViewById(R.id.weather_info_layout);
        cityNameT=(TextView)findViewById(R.id.city_name);
        publishTimeT=(TextView)findViewById(R.id.publish_text);
        weatherDespT=(TextView)findViewById(R.id.weather_desp);
        temp1T=(TextView)findViewById(R.id.temp1);
        temp2T=(TextView)findViewById(R.id.temp2);
        currentDateT=(TextView)findViewById(R.id.current_date);
        switchCity=(Button)findViewById(R.id.switch_city);
        refresh=(Button) findViewById(R.id.refresh_weather);
        switchCity.setText("选择");
        refresh.setText("刷新");
        switchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(WeatherActivity.this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishTimeT.setText("loading.....");
                SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherCode=pref.getString("weather_code","");
                if (!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
            }
        });
        String countryCode=getIntent().getStringExtra("country_code");
        if(!TextUtils.isEmpty(countryCode)){
            publishTimeT.setText("loading");
            weatherInfoLy.setVisibility(View.INVISIBLE);
            cityNameT.setVisibility(View.INVISIBLE);
            queryWeatherCode(countryCode);
        }else{
            showWeather();
        }
    }

    private void queryWeatherCode(String countryCode){
        String address="http://www.weather.com.cn/data/list3/city"+countryCode+".xml";
        queryFromeServer(address,"countryCode");
    }

    private void queryWeatherInfo(String weatherCode){
        String address="http:weather.com.cn/data/cityinfo/"+weatherCode+".html";
        queryFromeServer(address,"weatherCode");
    }

    private void queryFromeServer(final String address,final String type){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("countryCode".equals(type)){
                    if (!TextUtils.isEmpty(response)){
                        String[] array=response.split("\\|");
                        if (array!=null&&array.length==2){
                            String weatherCode=array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                }else if ("weatherCode".equals(type)){
                    Utility.handleWeatherResponse(WeatherActivity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishTimeT.setText("加载失败");
                    }
                });

            }
        });
    }
    /**
     *
     */
    private void showWeather(){
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        cityNameT.setText(pref.getString("city_name",""));
        temp1T.setText(pref.getString("temp1",""));
        temp2T.setText(pref.getString("temp2",""));
        weatherDespT.setText(pref.getString("weather_desp",""));
        publishTimeT.setText("今天"+pref.getString("publish_time","发布"));
        currentDateT.setText(pref.getString("current_date",""));
        weatherInfoLy.setVisibility(View.VISIBLE);
        cityNameT.setVisibility(View.VISIBLE);
        Intent intent=new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

}
