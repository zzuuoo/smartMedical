package com.zuovx.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.zuovx.Utils.ActivityCollector;
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
    private ArrayAdapter<Doctor> arrayAdapterDoctor;
    private RequestQueue requestQueue;
//    private LoadingDialog loadingDialog;
    private List<Section> sections;
    private List<Doctor> doctors;
//    private Spinner spinnerSection;
    private Spinner spinnerTime,spinnerSection,spinnerDoctor;
//    private Doctor doctor;
    private Button cancle,sure;
    private Toolbar toolbar;
    Handler handler;
    private LoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_manager);
        ActivityCollector.addActivity(this);
        init();
//        Intent intent = this.getIntent();
//        doctor = (Doctor) intent.getSerializableExtra("doctor");
        initDatePicker();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        ScheduleManagerActivity.this.setResult(1);
                        dialog.close();
//                        Toast.makeText(EditScheduleActivity.this,"生成票成功",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 2:
                        ScheduleManagerActivity.this.setResult(0);
                        dialog.close();
                        finish();
                    case 3:
                        dialog.close();
                        break;
                }
            }
        };
//        getSection();
        getSection();
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
        spinnerSection = findViewById(R.id.add_schedule_section);
        spinnerDoctor = findViewById(R.id.add_schedule_doctor);
        String[] time ={"全天","上午","下午"};
        spinnerTime.setAdapter(new ArrayAdapter<String>(ScheduleManagerActivity.this,android.R.layout.simple_spinner_item,time));
        spinnerSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(ScheduleManagerActivity.this,i,Toast.LENGTH_SHORT).show();
                System.out.println(i);
                getDoctor(sections.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void getSection(){
        //创建一个请求队列

//        listView = (ListView)findViewById(R.id.book_section_list);
        dialog = new LoadingDialog(this,"数据读取中");
        dialog.show();
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
                Message message = new Message();
                message.what=3;
                handler.sendMessage(message);
                if(k>0){
                    Section section = (Section) spinnerSection.getSelectedItem();
                    getDoctor(section);
                }


            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message message = new Message();
                message.what=3;
                handler.sendMessage(message);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }
    private void getDoctor(Section section){
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest =new StringRequest(GlobalVar.url+
                "user/getDoctorBySection?sectionId="+section.getSectionId(), new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {

                doctors = new Gson().fromJson(s, new TypeToken<List<Doctor>>() {}.getType());
//                if(doctors!=null&&doctors.size()>0){
//                    arrayAdapterDoctor = new ArrayAdapter<>(ScheduleManagerActivity.this,android.R.layout.simple_spinner_item,doctors);
//                    spinnerDoctor.setAdapter(arrayAdapterDoctor);
//                }
                arrayAdapterDoctor = new ArrayAdapter<>(ScheduleManagerActivity.this,android.R.layout.simple_spinner_item,doctors);
                spinnerDoctor.setAdapter(arrayAdapterDoctor);


            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("LoginUser","加载错误"+volleyError);
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
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select_date_shedule:
                mDatePicker.show(mTvSelectedDate.getText().toString());
                break;
            case R.id.sure_editschedule:
//                MakeSchedule();
                if (doctors==null||doctors.size()<1||sections==null
                        ||sections.size()<1||Integer.valueOf(scheduleRemainder.getText().toString())<1){
                    Toast.makeText(this,"请填合法数据",Toast.LENGTH_SHORT).show();
                    break;
                }
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
        dialog = new LoadingDialog(ScheduleManagerActivity.this,"加载中,,,");
        dialog.show();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                GlobalVar.url + "schedule/arrangeSchedule",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("1")){
                            Message message1 = new Message();
                            message1.what = 1;
                            handler.sendMessage(message1);
                        }else{
                            Message message1 = new Message();
                            message1.what = 2;
                            handler.sendMessage(message1);
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Message message1 = new Message();
                message1.what = 2;
                handler.sendMessage(message1);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                Doctor d = (Doctor) spinnerDoctor.getSelectedItem();
                if(d==null){
                    return map;
                }
                map.put("doctorId", String.valueOf(d.getDoctorId()));
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
