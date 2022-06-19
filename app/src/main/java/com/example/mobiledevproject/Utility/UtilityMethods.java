package com.example.mobiledevproject.Utility;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.mobiledevproject.GPS_Arrival_Activity;

public class UtilityMethods {

    public static void switchActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void switchActivityWithData(Context context, Class<?> activity, String...data) {
        Intent intent = new Intent(context, activity);
        for(int i = 0; i < data.length; i++) {
            intent.putExtra("key" + i, data[i]);
        }
        context.startActivity(intent);
    }

    public static void closeKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
