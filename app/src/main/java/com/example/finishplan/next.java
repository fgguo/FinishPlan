package com.example.finishplan;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class next extends Activity {
    /////////////////////////////////////////////////////////////////////////定义基本控件
    Button choosedate,choosetime,finish,back;
    EditText showtopic,showtime,showdate;
    Calendar c;
    /////////////////////////////////////////////////////////////////////////定义中间传递变量
    String gettopic="学习",getdate="1996年7月4日",gettime="5:40";
    /////////////////////////////////////////////////////////////////////////定义数据库
    dataBase data;
    Cursor cursor;
    int year,month,day,hour,minute;
    private final static int DATE_DIALOG = 0;
    private final static int TIME_DIALOG = 1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next);
        setTitle("制定计划");
        data=new dataBase(this);
        cursor=data.select(1);
        cursor.moveToFirst();
//        /////////////////////////////////////////////////////////////////////////选择日期
        choosedate=(Button)findViewById(R.id.choosedate);
        choosedate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(DATE_DIALOG);
            }
        });


        /////////////////////////////////////////////////////////////////////////选择时间
        choosetime=(Button)findViewById(R.id.choosetime);
        choosetime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
				Calendar c=Calendar.getInstance();  //show the time
				String st1=c.getTime().toString();
				showtime.setText(st1);
                showDialog(TIME_DIALOG);

            }
        });
        /////////////////////////////////////////////////////////////////////////获取控件上的内容 准备储存

        showtopic=(EditText)findViewById(R.id.edittopic);
        showtime=(EditText)findViewById(R.id.edittime);
        showdate=(EditText)findViewById(R.id.editdate);
        back=(Button)findViewById(R.id.cancel);
        /////////////////////////////////////////////////////////////////////////取消存储事件
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Builder cancelConfirm=new Builder(next.this);
                cancelConfirm.setTitle("确定回去么？还没保存呢");
                cancelConfirm.setPositiveButton("回去吧", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent turnback=new Intent(next.this,MainActivity.class);
                        startActivity(turnback);
                    }
                });
                cancelConfirm.setNegativeButton("继续填写", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                cancelConfirm.show();
            }
        });
        /////////////////////////////////////////////////////////////////////////存入数据库
        finish=(Button)findViewById(R.id.finish);
        finish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String finished="未完成!";
                gettopic=showtopic.getText().toString();                                   //主题
                getdate=showdate.getText().toString();                                    //日期
                gettime=showtime.getText().toString();//时间
                if (TextUtils.isEmpty(gettopic)) {
                    Toast.makeText(next.this,"请输入计划内容！",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(getdate)) {
                    Toast.makeText(next.this,"请输入计划日期！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(gettime)) {
                    Toast.makeText(next.this,"请输入计划时间！",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    add(gettopic, getdate, gettime, finished);
                    Toast.makeText(getApplicationContext(), "新计划已建立，加油完成吧！", Toast.LENGTH_SHORT).show();
                    Intent back = new Intent(next.this, MainActivity.class);   //准备返回首页
                    startActivity(back);
                    finish();
                }
            }
        });
    }
    public void add( String content,String date,String time,String finish)
    {
        {
            data.insert(content,date, time,finish);
            cursor.requery();
        }
    }
    /////////////////////////////////////////////////////////////////////////获取日期 时间的方法 重写消息框
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        c = Calendar.getInstance();
        switch (id) {
            case DATE_DIALOG:
                dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker dp, int Year, int Month, int dayOfMonth) {
                                showdate.setText( Year + "年" + (Month+1) + "月" + dayOfMonth + "日");
                                year=Year;
                                month=Month;
                                day=dayOfMonth;
                            }
                        },
                        c.get(Calendar.YEAR), // 传入年份
                        c.get(Calendar.MONTH), // 传入月份
                        c.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                dialog.setTitle(c.get(Calendar.YEAR)+","+c.get(Calendar.MONTH)+","+c.get(Calendar.DAY_OF_MONTH));
                break;
            case TIME_DIALOG:
                dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override public void onTimeSet(TimePicker view, int hourOfDay, int Minute)
                    {
                        if(Minute>=10)
                        { showtime.setText(hourOfDay+":"+Minute);}
                        else
                        {
                            showtime.setText(hourOfDay+":"+"0"+Minute);
                        }
                        hour=hourOfDay;
                        minute= Minute;}}, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true);
                dialog.setTitle(c.get(Calendar.HOUR_OF_DAY)+":" +c.get(Calendar.MINUTE));
                break;
        }
        return dialog;
    }
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}

