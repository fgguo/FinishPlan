package com.example.finishplan;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dell-pc on 2017/4/8.
 */

public class AlarmReceiver3 extends BroadcastReceiver {
    public void onReceive(final Context context, Intent intent) {
        Intent i = new Intent(context, MyService.class);
        context.startService(i);
    }
}