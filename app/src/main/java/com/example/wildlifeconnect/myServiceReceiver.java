package com.example.wildlifeconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class myServiceReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE=12345;


    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent intent1=new Intent(context,NewFlagService.class);
        context.startService(intent1);
        //checkNewFlags(context);

    }

}
