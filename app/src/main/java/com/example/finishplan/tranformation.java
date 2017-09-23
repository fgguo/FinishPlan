package com.example.finishplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;;

import com.getbase.floatingactionbutton.FloatingActionButton;

/**
 * Created by dell-pc on 2017/3/26.
 */

public class tranformation extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tranformation);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.clock);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent turn = new Intent(tranformation.this, moodRecord.class);
                startActivity(turn);
                //跳转到下一页
            }
        });
        FloatingActionButton fa=(FloatingActionButton)findViewById(R.id.memoryconclosion);
        fa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tur = new Intent(tranformation.this, select.class);
                startActivity(tur);
                //跳转到下一页
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(tranformation.this, MainActivity.class);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home);
        }
        return true;
    }
}
