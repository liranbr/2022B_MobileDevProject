package com.example.mobiledevproject.Utility;

import android.content.Context;
import android.content.Intent;

public class UtilityMethods {

    public static void switchActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }
}
