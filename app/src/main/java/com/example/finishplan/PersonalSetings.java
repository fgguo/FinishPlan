package com.example.finishplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by dell-pc on 2017/3/20.
 */

public class PersonalSetings extends Activity {
    Button information,security;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_settings);
        information=(Button)findViewById(R.id.Information);
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonalSetings.this,userHelper.class);
                startActivity(intent);
                //跳转到下一页
            }
        });

        security=(Button)findViewById(R.id.Security);
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonalSetings.this,editPassword.class);
                startActivity(intent);
                //跳转到下一页
            }
        });
    }
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
