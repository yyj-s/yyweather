package com.example.cx.yyweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.cx.yyweather.receiver.AutoUpdateReceiver;
import com.example.cx.yyweather.util.HttpCallbackListener;
import com.example.cx.yyweather.util.HttpUtil;
import com.example.cx.yyweather.util.Utility;

/**
 * Created by cx on 2017/5/10.
 */

public class AutoUpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startID){
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour=60*60*1000;
        long triggerAtTime= SystemClock.elapsedRealtime()+anHour;
        Intent intent1=new Intent(this,AutoUpdateReceiver.class);
        PendingIntent pendingintent=PendingIntent.getBroadcast(this,0,intent1,0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingintent);
        return super.onStartCommand(intent,flags,startID);
    }
    /**
     *
     */
    private void updateWeather(){
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCode=pref.getString("weather_code","");
        String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(AutoUpdateService.this,response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();

            }
        });
    }
}
