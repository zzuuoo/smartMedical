package com.zuovx.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Model.Doctor;
import com.zuovx.Model.Section;
import com.zuovx.R;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;
import com.zuovx.datePicker.CustomDatePicker;
import com.zuovx.datePicker.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvSelectedDate;
    private TextView scheduleRemainder;
    private CustomDatePicker mDatePicker;
    private ArrayAdapter<Section> arrayAdapterSection;
    private RequestQueue requestQueue;
    private LoadingDialog loadingDialog;
    private List<Section> sections;
    private Spinner spinnerSection;
    private Spinner spinnerTime;
    private Doctor doctor;
    private Button cancle,sure;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_manager);
        init();
        Intent intent = this.getIntent();
        doctor = (Doctor) intent.getSerializableExtra("doctor");
        initDatePicker();
//        getSection();
    }
    public void init(){
        //返回键
        toolbar = findViewById(R.id.arrange_schedule_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.select_date_shedule).setOnClickListener(this);
        scheduleRemainder = (TextView)findViewById(R.id.schedule_remainder);
        sure = findViewById(R.id.sure_editschedule);
        cancle = findViewById(R.id.cancel_editschedule);
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);
        mTvSelectedDate = findViewById(R.id.tv_selected_date_shedule);
//        spinnerSection = findViewById(R.id.session_schedule_spinner);
        spinnerTime = findViewById(R.id.schedule_what_time);
        String[] time ={"全天","上午","下午"};
        spinnerTime.setAdapter(new ArrayAdapter<String>(ScheduleManagerActivity.this,android.R.layout.simple_spinner_item,time));
    }
    public void getSection(){
        //创建一个请求队列

//        listView = (ListView)findViewById(R.id.book_section_list);
        loadingDialog = new LoadingDialog(this,"数据读取中");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求

        StringRequest stringRequest =new StringRequest(GlobalVar.url +"section/getAllSection", new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                try {
                    Gson gson = new Gson();
                    sections = gson.fromJson(s, new TypeToken<List<Section>>() {}.getType());

                    Log.d("sections:",sections.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                arrayAdapterSection = new ArrayAdapter<Section>(ScheduleManagerActivity.this,android.R.layout.simple_spinner_item,sections);
                spinnerSection.setAdapter(arrayAdapterSection);
                SpinnerAdapter spinnerPAdapter = spinnerSection.getAdapter();
                int k = spinnerPAdapter.getCount();
                for(int i=0;i<k;i++)
                {
                    if(doctor.getSectionId()==((Section)spinnerPAdapter.getItem(i)).getSectionId()){
                        spinnerSection.setSelection(i,true);
                        break;
                    }
                }
                loadingDialog.close();


            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                volley_result.setText("加载错误"+volleyError);
                loadingDialog.close();
//                textView.setVisibility(View.VISIBLE);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
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
            case R.id.sure_editschedule:
//                MakeSchedule();
                try {
                    PostWithVolley();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                finish();
                break;
            case R.id.cancel_editschedule:
                finish();
                break;
        }
    }

    private void MakeSchedule(){
        String t = mTvSelectedDate.getText().toString();
        String s = spinnerTime.getSelectedItem().toString();
        String remainder = scheduleRemainder.getText().toString();
        Toast.makeText(this,s+t+remainder,Toast.LENGTH_SHORT).show();
    }
    private void PostWithVolley() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String date = mTvSelectedDate.getText().toString();
        Date d = format.parse(date);
        final long ld = d.getTime();
        String when = spinnerTime.getSelectedItem().toString();
        final String remainder = scheduleRemainder.getText().toString();
        final  String w ;
        if(when.equals("全天")){
            w="0";
        }else if(when.equals("上午")){
            w = "1";
        }else{
            w = "2";
        }
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                GlobalVar.url + "schedule/arrangeSchedule",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);

//                Message message1 = new Message();
//                message1.what = 3;
//                handler.sendMessage(message1);

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

//                Message message1 = new Message();
//                message1.what = 3;
//                handler.sendMessage(message1);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("doctorId", String.valueOf(doctor.getDoctorId()));
                map.put("w", w);
                map.put("remainder",remainder);
                map.put("workTimeStart",String.valueOf(ld));
                map.put("isCancle","false");
                return map;
            }

        };

        requestQueue.add(stringRequest);
    }
}
