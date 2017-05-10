package com.example.cx.yyweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.cx.yyweather.service.AutoUpdateService;

/**
 * Created by cx on 2017/5/10.
 */

public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Intent intent1=new Intent(context,AutoUpdateService.class);
        context.startService(intent1);
    }
}
