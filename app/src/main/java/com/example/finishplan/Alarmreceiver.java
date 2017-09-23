package com.example.finishplan;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * Created by dell-pc on 2017/2/19.
 */
public class Alarmreceiver extends BroadcastReceiver {
    @Override
    public void onReceive( final Context context, Intent intent) {
        // TODO Auto-generated method stub
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {
        Intent playIntent = new Intent(context, Alarm.class);
        playIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//这一句很重要，不然会出错
        context.startActivity(playIntent);
        }
        else {
            Intent playIntent = new Intent(context, Alarm.class);
            playIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//这一句很重要，不然会出错
            context.startActivity(playIntent);
        }
    }
}


