package com.zuovx.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ActivityCollector.addActivity(this);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
