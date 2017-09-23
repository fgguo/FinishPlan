package com.example.finishplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by dell-pc on 2017/3/27.
 */

public class select extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        Button conslusion=(Button)findViewById(R.id.conclusion);
        conslusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tur = new Intent(select.this, Dailyconclusion.class);
                startActivity(tur);
            }
        });
        Button mood=(Button)findViewById(R.id.mood);
        mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tur = new Intent(select.this,moodMemory.class);
                startActivity(tur);
            }
        });
}
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(select.this, tranformation.class);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home);
        }
        return true;
    }
}
