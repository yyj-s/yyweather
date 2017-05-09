package com.example.cx.yyweather.util;

/**
 * Created by cx on 2017/5/9.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
