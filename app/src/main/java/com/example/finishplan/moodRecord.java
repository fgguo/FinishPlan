package com.example.finishplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;

/**
 * Created by dell-pc on 2017/3/26.
 */

public class moodRecord extends Activity{
    EditText memory;
    String getdate="1996年7月4日",Memory="好好学习";
    Button finish,back;
    dataBase data;
    Cursor cursor;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noodrecord);
        Toast.makeText(moodRecord.this,"写下你的心情吧！",Toast.LENGTH_SHORT).show();
        data=new dataBase(this);
        memory=(EditText)findViewById(R.id.memoryy);
        back=(Button)findViewById(R.id.cancel);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder cancelConfirm=new AlertDialog.Builder(moodRecord.this);
                cancelConfirm.setTitle("确定回去么？还没保存呢");
                cancelConfirm.setPositiveButton("回去吧", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent turnback=new Intent(moodRecord.this,MainActivity.class);
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
        finish=(Button)findViewById(R.id.finishing);
        Calendar calendar=Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        String Date=new String(" ");
        Date=(year+"年"+month+"月"+day+"日").toString();
        getdate=Date ;                               //日期
        finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Memory=memory.getText().toString();
                if (TextUtils.isEmpty(Memory)) {
                    Toast.makeText(moodRecord.this,"输入的内容为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                add( getdate,Memory,1 );
                Intent back=new Intent(moodRecord.this,MainActivity.class);   //准备返回首页;
                startActivity(back);
                finish();
            }
        });
    }
    public void add( String date,String journal,int i)
    {
        {
            data.insert(date,journal,i);
        }
    }
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
