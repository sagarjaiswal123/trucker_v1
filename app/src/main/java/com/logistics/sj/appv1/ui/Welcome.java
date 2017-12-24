package com.logistics.sj.appv1.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.logistics.sj.appv1.R;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainActivity=new Intent(Welcome.this,Primary.class);
                startActivity(mainActivity);
                finish();
            }
        },3000);

    }
}
