package com.example.finishplan;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent1=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent1,0);
        Notification notification=new NotificationCompat.Builder(this)//通知
                .setContentTitle("圆计划")
                .setContentText("正在监督你的计划！加油哦~~~")
                .setSmallIcon(R.mipmap.applauncher)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi)
                .build();
        startForeground(999999999,notification);//前台服务
        alarmset();
        Intent inten = new Intent(MyService.this, AlarmReceiver3.class);
        PendingIntent p = PendingIntent.getBroadcast(MyService.this, 0, inten, 0);
////设置一个PendingIntent对象，发送广播
        AlarmManager amm = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerTime = SystemClock.elapsedRealtime() + 60* 60 * 1000;
////获取AlarmManager对象
        amm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, p);
    }
    @Override
    public void onStart(Intent intent, int startId) {
        Intent intent1=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent1,0);
        Bitmap abcd =  BitmapFactory.decodeResource(getResources(), R.mipmap.applauncher);
        Notification notification=new NotificationCompat.Builder(this)
                .setContentTitle("圆计划")
                .setContentText("正在监督你的计划！加油哦~~~")
                .setSmallIcon(R.mipmap.applauncher)
                .setLargeIcon(abcd)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi)
                .build();
        startForeground(999999999,notification);
        Intent inten = new Intent(MyService.this, AlarmReceiver3.class);
        PendingIntent p = PendingIntent.getBroadcast(MyService.this, 0, inten, 0);
////设置一个PendingIntent对象，发送广播
        AlarmManager amm = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerTime = SystemClock.elapsedRealtime() + 60* 60 * 1000;
////获取AlarmManager对象
        amm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, p);
        alarmset();
    }
        @Override
        public void onDestroy () {
            super.onDestroy();
        }

        @Override
        public IBinder onBind (Intent intent){
            // TODO Auto-generated method stub
            return null;
        }
      private void alarmset(){
    dataBase data;
    data = new dataBase(this);
    Cursor cursor;
    cursor = data.select(1);
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
        String daTe=cursor.getString(2)+cursor.getString(3);
        Calendar calendar=Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        String Date=new String(" ");
        if(hour<10&&minute<10)//调整时间格式
        {
            Date=(year+"年"+month+"月"+day+"日"+"0"+hour+":"+"0"+minute).toString();
        }
        if(hour<10&&minute>=10){
            Date =(year+"年"+month+"月"+day+"日"+"0"+hour+":"+minute).toString();
        }
        if(hour>=10&&minute>=10){
            Date =(year+"年"+month+"月"+day+"日"+hour+":"+minute).toString();
        }
        if(hour>=10&&minute<10) {
            Date=(year+"年"+month+"月"+day+"日"+hour+":"+"0"+minute).toString();
        }
        Pattern p=Pattern.compile("\\d{1,}");//连续数字的最少个数
        String u = daTe;
        Matcher m =p.matcher(u);
        List<String> digitList = new ArrayList<String>();
        while(m.find()){
            digitList.add( m.group());
        }
        int Year=Integer.valueOf(digitList.get(0));
        int Month=Integer.valueOf(digitList.get(1));
        int Day=Integer.valueOf(digitList.get(2));
        int Hour=Integer.valueOf(digitList.get(3));
        int Minute=Integer.valueOf(digitList.get(4));
        if(Hour<10){
            daTe=cursor.getString(2)+"0"+cursor.getString(3);
        }
        int result= daTe.compareTo(Date);
        if(result>0) {                   //计划时间比当前时间大就设置闹钟
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, Year);
            c.set(Calendar.MONTH, (Month-1));//也可以填数字，0-11,一月为0
            c.set(Calendar.DAY_OF_MONTH, Day);
            c.set(Calendar.HOUR_OF_DAY, Hour);
            c.set(Calendar.MINUTE, Minute);
            c.set(Calendar.SECOND, 0);
//也可以写在一行里
            Intent inten=new Intent(this,Alarmreceiver.class);
            PendingIntent pii=PendingIntent.getBroadcast(this, 0, inten,0);
////设置一个PendingIntent对象，发送广播
            AlarmManager amm=(AlarmManager)getSystemService(ALARM_SERVICE);
////获取AlarmManager对象
            amm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pii);
//时间到时，执行PendingIntent，只执行一次
//AlarmManager.RTC_WAKEUP休眠时会运行，如果是AlarmManager.RTC,在休眠时不会运行
//如果需要重复执行，使用上面一行的setRepeating方法，倒数第二参数为间隔时间,单位为毫秒
        }
        cursor.moveToNext();
    }
}
}