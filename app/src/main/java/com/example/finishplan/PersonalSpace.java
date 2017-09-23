package com.example.finishplan;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PersonalSpace extends Activity {
     private static final int PASSWORD_MIWEN = 0x81;
     private EditText editText;
     private Button button;
    dataBase data;
    Cursor cursor;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_personal_space);
         editText = (EditText) findViewById(R.id.et_password);
         data = new dataBase(this);
         cursor = data.select(1,1,1,1);
         cursor.moveToFirst();
         button = (Button) findViewById(R.id.btn_pwd);
         button.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 editText.setInputType(PASSWORD_MIWEN);//设置显示为密文
                 editText.setSelection(editText.length());//设置光标显示
                 String str = editText.getText().toString().trim();
                 if(cursor.getCount()>=1) {
                     String Q1 = cursor.getString(1);
                     int i = str.compareTo(Q1);
                     if (i == 0) {
                         Toast.makeText(PersonalSpace.this, "隐私空间", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(PersonalSpace.this, tranformation.class);
                         startActivity(intent);
                     } else {
                         Toast.makeText(PersonalSpace.this, "密码错误！请重新输入", Toast.LENGTH_SHORT).show();
                     }
                 }
                 else{
                     Toast.makeText(PersonalSpace.this, "请先设置初始密码", Toast.LENGTH_SHORT).show();
                 }
             }
     });
}
}