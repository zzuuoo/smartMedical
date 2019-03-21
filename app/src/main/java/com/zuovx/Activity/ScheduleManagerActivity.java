package com.zuovx.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zuovx.R;
import com.zuovx.datePicker.CustomDatePicker;
import com.zuovx.datePicker.DateFormatUtils;

public class ScheduleManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvSelectedDate;
    private CustomDatePicker mDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_manager);
        findViewById(R.id.select_date_shedule).setOnClickListener(this);
        mTvSelectedDate = findViewById(R.id.tv_selected_date_shedule);
        initDatePicker();
    }
    private void initDatePicker() {

//        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long beginTimestamp = System.currentTimeMillis();
        long mon = 2592000000l;
        long endTimestamp = System.currentTimeMillis()+mon;

        mTvSelectedDate.setText(DateFormatUtils.long2Str(beginTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedDate.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatePicker.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select_date_shedule:
                mDatePicker.show(mTvSelectedDate.getText().toString());
                break;
        }
    }
}
