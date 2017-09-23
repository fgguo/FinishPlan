package com.example.finishplan;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.os.Vibrator;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Alarm extends Activity {
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        notification();
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {1000, 2000, 1000, 2000};// 停止 开启 停止 开启   
        vibrator.vibrate(pattern, 2);//重复两次上面的pattern 如果只想震动一次，index设为-1   
        AlertDialog.Builder builder = new AlertDialog.Builder(Alarm.this);
        //    设置Title的图标
        builder.setIcon(R.drawable.app_icon);
        //    设置Title的内容
        builder.setTitle("计划提醒");
        builder.setCancelable(false);
        //    设置Content来显示一个信息
        builder.setMessage("主人~该去完成计划了呢~");
        //    设置一个PositiveButton
        builder.setPositiveButton("知道啦", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Alarm.this, MainActivity.class);
                vibrator.cancel();
                finish();
                Alarm.this.startActivity(intent);
            }
        });
        //    设置一个NeutralButton
        builder.setNeutralButton("一会吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent inten = new Intent(Alarm.this, AlarmReceiver2.class);
                PendingIntent pi = PendingIntent.getBroadcast(Alarm.this, 0, inten, 0);
////设置一个PendingIntent对象，发送广播
                AlarmManager amm = (AlarmManager) getSystemService(ALARM_SERVICE);
                long triggerTime = SystemClock.elapsedRealtime() + 5 * 60 * 1000;
////获取AlarmManager对象
                amm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
                finish();
                vibrator.cancel();
            }
        });
        //    显示出该对话框
        builder.show();
    }
    private void notification() {
        dataBase data;
        data = new dataBase(this);
        Cursor cursor;
        cursor = data.select(1);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String daTe = cursor.getString(2) + cursor.getString(3);
            String content=cursor.getString(1);
            int  id=cursor.getInt(0);
            Log.d("id",""+id);
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            String Date = new String(" ");
            if (hour < 10 && minute < 10)//调整时间格式
            {
                Date = (year + "年" + month + "月" + day + "日" + "0" + hour + ":" + "0" + minute).toString();
            }
            if (hour < 10 && minute >= 10) {
                Date = (year + "年" + month + "月" + day + "日" + "0" + hour + ":" + minute).toString();
            }
            if (hour >= 10 && minute >= 10) {
                Date = (year + "年" + month + "月" + day + "日" + hour + ":" + minute).toString();
            }
            if (hour >= 10 && minute < 10) {
                Date = (year + "年" + month + "月" + day + "日" + hour + ":" + "0" + minute).toString();
            }
            Pattern p = Pattern.compile("\\d{1,}");//连续数字的最少个数
            String u = daTe;
            Matcher m = p.matcher(u);
            List<String> digitList = new ArrayList<String>();//提取数字
            while (m.find()) {
                digitList.add(m.group());
            }
            int Hour = Integer.valueOf(digitList.get(3));
            if (Hour < 10) {
                daTe = cursor.getString(2) + "0" + cursor.getString(3);
            }
            int result = daTe.compareTo(Date);
            if(result==0){             //设置通知
                Intent intent1=new Intent(this,MainActivity.class);
                PendingIntent pi=PendingIntent.getActivity(this,0,intent1,0);
                Bitmap abcd =  BitmapFactory.decodeResource(getResources(), R.mipmap.applauncher);
                NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE) ;
                Notification notification=new NotificationCompat.Builder(this)
                        .setContentTitle("计划提醒")
                        .setContentText("您的当前计划是："+content)
                        .setSmallIcon(R.mipmap.applauncher)
                        .setLargeIcon(abcd)
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pi)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();
                manager.notify(id,notification);
            }
            cursor.moveToNext();
        }
    }
}
