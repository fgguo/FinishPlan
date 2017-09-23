package com.example.finishplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by dell-pc on 2017/3/25.
 */

public class passwordproCheck extends Activity {
    EditText q1,q2,q3;
    String getq1="付国",getq2="平凡的世界",getq3="肉";
    Button finish,back;
    dataBase data;
    Cursor cursor;
    int a=1,b=1,c=1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passwordprocheck);
        data = new dataBase(this);
        cursor = data.select(1,1,1);
        cursor.moveToFirst();
        q1 = (EditText) findViewById(R.id.q11);
        q2 = (EditText) findViewById(R.id.q22);
        q3 = (EditText) findViewById(R.id.q33);
            final String Q1 = cursor.getString(1);
            final String Q2 = cursor.getString(2);
            final String Q3 = cursor.getString(3);
        back=(Button) findViewById(R.id.cancel);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder cancelConfirm=new AlertDialog.Builder(passwordproCheck.this);
                cancelConfirm.setTitle("确定回去么？还没答完呢");
                cancelConfirm.setPositiveButton("回去吧", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent turnback=new Intent(passwordproCheck.this,PersonalSetings.class);
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
        finish=(Button)findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getq1=q1.getText().toString();
                getq2=q2.getText().toString();
                getq3=q3.getText().toString();
                a = getq1.compareTo(Q1);
                b=getq2.compareTo(Q2);
                c=getq3.compareTo(Q3);
                if(a==0&&b==0&&c==0) {
                    Intent next = new Intent(passwordproCheck.this, passwordSet.class);
                    startActivity(next);
                }
                else{
                    Toast.makeText(passwordproCheck.this,"回答错误！请重新回答！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(passwordproCheck.this, MainActivity.class);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home);
        }
        return true;
    }
}
