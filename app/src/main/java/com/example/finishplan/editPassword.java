package com.example.finishplan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by dell-pc on 2017/3/25.
 */

public class editPassword extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next);
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun)
        {
            editor.putBoolean("isFirstRun", false);
            editor.commit();
            Intent intent=new Intent(this,passwordProset.class);
            startActivity(intent);
        } else
        {
            Intent inten=new Intent(this,passwordproCheck.class);
            startActivity(inten);
        }
    }
}
