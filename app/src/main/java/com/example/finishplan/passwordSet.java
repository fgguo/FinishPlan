package com.example.finishplan;

import android.app.Activity;
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

public class passwordSet extends Activity {
    private static final int PASSWORD_MINGWEN = 0x90;
    private static final int PASSWORD_MIWEN = 0x81;
    private EditText editText;
    private Button button;
    Cursor cursor;
    dataBase data;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passwordset);
        editText = (EditText) findViewById(R.id.et_password);
        button = (Button) findViewById(R.id.btn_pwd);
        data = new dataBase(this);
        cursor = data.select(1,1,1,1);
        cursor.moveToFirst();
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editText.setInputType(PASSWORD_MIWEN);//设置显示为密文
                editText.setSelection(editText.length());//设置光标显示
                String str = editText.getText().toString().trim();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(passwordSet.this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    if (cursor.getCount() == 0) {
                        data.insert(str);
                    } else {
                        data.modifyState("1", str, 1);
                    }
                    Toast.makeText(passwordSet.this, "设置成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(passwordSet.this, PersonalSetings.class);
                    startActivity(intent);
                }
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {//重写返回事件
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(passwordSet.this,PersonalSetings.class);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
