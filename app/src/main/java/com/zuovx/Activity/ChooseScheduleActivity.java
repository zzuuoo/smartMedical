package com.zuovx.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.zuovx.Adapter.DoctorSchAdapter;
import com.zuovx.Model.Doctor;
import com.zuovx.Model.DoctorSche;
import com.zuovx.Model.Schedule;
import com.zuovx.Model.ScheduleT;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseScheduleActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private LoadingDialog loadingDialog;
    private List<Doctor> doctors;
    private List<Schedule> schedules;
    private List<DoctorSche> doctorSches;
    private List<DoctorSche> searchDoctorSches;
    private ListView listView;
    private SearchView searchView;
    private Integer sectionId;
    private Boolean isSearch = false;
//    private DoctorAdapter doctorAdapter;
    private DoctorSchAdapter doctorSchAdapter;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_schedule);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        sectionId = intent.getIntExtra("sectionId",0);
        getDoctorBySectionId(sectionId);
        init();

    }
    public void init()
    {
        searchView = (SearchView)findViewById(R.id.all_schedule_searchView);

        textView = (TextView)findViewById(R.id.nothingSchedule);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getApplicationContext(),newText, Toast.LENGTH_SHORT).show();
                searchDoctorSches = new ArrayList<>();
                for(int i=0;i<doctorSches.size();i++){
                    if(doctorSches.get(i).getDoctor().getName().contains(newText)){
                        searchDoctorSches.add(doctorSches.get(i));
//                        System.out.println("添加一个"+doctors.get(i).getName());
                    }
                }
                if(searchDoctorSches.size()>0&&newText.length()>0){
                    doctorSchAdapter = new DoctorSchAdapter(getApplicationContext(),
                            R.layout.doctor_item,searchDoctorSches);
                    listView.setAdapter(doctorSchAdapter);
                    isSearch=true;

                }
                if(newText.length()==0)
                {
                    doctorSchAdapter = new DoctorSchAdapter(getApplicationContext(),
                            R.layout.doctor_item,doctorSches);
                    listView.setAdapter(doctorSchAdapter);
                    isSearch=false;
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                System.out.println("点击了医生");
                DoctorSche doctorSche =null;
                if(isSearch)
                {
                    doctorSche = searchDoctorSches.get(i);
                }else{
                    doctorSche = doctorSches.get(i);
                }
//                Intent intent = new Intent(BookActivity.this,ChooseDoctorActivity.class);
//                intent.putExtra("sectionId",section.getSectionId());
//                startActivity(intent);
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChooseScheduleActivity.this);
                dialog.setTitle("确认预约吗？");
                dialog.setMessage("预约须知：同一天你只能预约两次," +
                        "预约成功而不赴约的，超过两次则拉入黑名单，确认预约吗？。");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(getApplicationContext(),"预约成功",Toast.LENGTH_SHORT).show();

                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
//                Toast.makeText(getApplicationContext(),doctor.getName(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void getDoctorBySectionId(final Integer sectionId)
    {
        listView = (ListView)findViewById(R.id.all_schedule_list);
        loadingDialog = new LoadingDialog(this,"数据读取中...");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求

        StringRequest stringRequest =new StringRequest(GlobalVar.url +"user/getDoctorBySection?sectionId="+sectionId, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                try {
                    Gson gson = new Gson();
                    doctors = gson.fromJson(s, new TypeToken<List<Doctor>>() {}.getType());
//                    Log.d("doctorsBySectionId:",doctors.toString());
                    for(Doctor d:doctors){
                        System.out.println(d.getName()+d.getHonour());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadingDialog.close();
                getSchedule(sectionId);


            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                volley_result.setText("加载错误"+volleyError);
                loadingDialog.close();
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }

    public void getSchedule(Integer sectionId)
    {
        loadingDialog = new LoadingDialog(this,"数据读取中...");
        loadingDialog.show();
//        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求

        StringRequest stringRequest =new StringRequest(GlobalVar.url
                +"schedule/getScheduleBySectionId?sectionId="+sectionId, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                try {
                    Gson gson = new Gson();
                    List<ScheduleT> scheduleTS = new ArrayList<>();
                    scheduleTS = gson.fromJson(s, new TypeToken<List<ScheduleT>>() {}.getType());
                    schedules = new ArrayList<>();
                    for(ScheduleT t :scheduleTS){
                        Schedule schedule = new Schedule();
                        schedule.setDoctorId(t.getDoctorId());
                        schedule.setIsCancle(t.getIsCancle());
                        schedule.setRemainder(t.getRemainder());
                        schedule.setScheduleId(t.getScheduleId());
                        schedule.setWorkTimeStart(new Date(t.getWorkTimeStart()));
                        schedule.setW(t.getW());
                        schedules.add(schedule);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (schedules!=null&&schedules.size()>0){
                    doctorSches = new ArrayList<>();
                    for(Schedule sc :schedules){
                        for(Doctor d : doctors){
                            if(sc.getDoctorId()==d.getDoctorId()){
                                DoctorSche doctorSche = new DoctorSche();
                                doctorSche.setDoctor(d);
                                doctorSche.setSchedule(sc);
                                doctorSches.add(doctorSche);
                                break;
                            }
                        }
                    }
                    doctorSchAdapter = new DoctorSchAdapter(getApplicationContext(),
                            R.layout.doctor_item,doctorSches);
                    listView.setAdapter(doctorSchAdapter);
                }
                loadingDialog.close();
                if(doctorSches==null||doctorSches.size()==0)
                {
                    textView.setVisibility(View.VISIBLE);
                }else{
                    textView.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                volley_result.setText("加载错误"+volleyError);
                loadingDialog.close();
                textView.setVisibility(View.VISIBLE);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    /**
     * 使用volley框架进行登录请求，并返回用户基本信息存于MainActivity的user中
     * @param account
     * @param password
     * @param status
     */
    private void bookWithVolley(final String account, final String password, final String status)
    {
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                GlobalVar.url + "login",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s==null||s.equals(""))
                        {

                        }else{

                        }
//                        Message message = new Message();
//                        message.what = 2;
//                        handler.sendMessage(message);


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
                map.put("account", account);
                map.put("password", password);
                map.put("userStatus",status);
                return map;
            }

        };

        requestQueue.add(stringRequest);
    }
}
