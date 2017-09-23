package com.example.finishplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by dell-pc on 2017/3/25.
 */

public class passwordProset extends Activity{
    EditText q1,q2,q3;
    String getq1="付国",getq2="平凡的世界",getq3="肉";
    Button finish,back;
    dataBase data;
    Cursor cursor;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passwordproset);
        data=new dataBase(this);
        q1=(EditText)findViewById(R.id.q1);
        q2=(EditText)findViewById(R.id.q2);
        q3=(EditText)findViewById(R.id.q3);
        back=(Button)findViewById(R.id.cancel);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder cancelConfirm=new AlertDialog.Builder(passwordProset.this);
                cancelConfirm.setTitle("还没填写完呢");
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
        finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getq1=q1.getText().toString();
                getq2=q2.getText().toString();
                getq3=q3.getText().toString();
                if (TextUtils.isEmpty(getq1)) {
                    Toast.makeText(passwordProset.this,"请为问题1设置答案！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getq2)) {
                    Toast.makeText(passwordProset.this,"请为问题2设置答案！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getq3)) {
                    Toast.makeText(passwordProset.this,"请为问题3设置答案！",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    add(getq1, getq2, getq3);
                    Toast.makeText(getApplicationContext(), "密保设置完毕！", Toast.LENGTH_SHORT).show();
                    Intent back = new Intent(passwordProset.this, passwordproCheck.class);   //准备返回首页;
                    startActivity(back);
                }
            }
        });
    }
    public void add( String getq1,String getq2,String getq3)
    {
            data.insert(getq1,getq2,getq3);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder cancelConfirm=new AlertDialog.Builder(passwordProset.this);
            cancelConfirm.setTitle("还没填写完呢");
            cancelConfirm.setNegativeButton("继续填写", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            cancelConfirm.show();
        }
        return true;
    }
}

