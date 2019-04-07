package com.zuovx.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Adapter.DoctorManagerAdapter;
import com.zuovx.Model.Doctor;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class DoctorManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Doctor> doctors = new ArrayList<>();
//    private List<User> userList = new ArrayList<>();
    //    private List<Employee> empSearchList = new ArrayList<>();
    private List<Doctor> doctorSearchList = new ArrayList<>();
    private ListView ls = null;
    private Button addUser;
    private Toolbar user_toolbar;
    private boolean isSearch =false;
    private SearchView user_searchview;
    private DoctorManagerAdapter doctorAdapter;
    private RequestQueue requestQueue;
    Handler handler;
    LoadingDialog dialog;
    int c = 0;
    int sectionId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_manager);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        sectionId = intent.getIntExtra("sectionId",-1);
        setData();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        dialog.close();
                        init();
                        break;
                    case 2:
                        Toast.makeText(DoctorManagerActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                        dialog.close();
                        break;
                    case 3:
                        dialog.close();
                        setData();
                        doctorAdapter = new DoctorManagerAdapter(getApplication(),R.layout.doctor_manager_item,doctors);
                        ls.setAdapter(doctorAdapter);
                        Toast.makeText(DoctorManagerActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };
//                init();

    }
    public void init()
    {

        //返回键
        user_toolbar = findViewById(R.id.doctor_user_toolbar);
        user_toolbar.setTitle("");
        setSupportActionBar(user_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ls = findViewById(R.id.doctor_user_list);
        addUser  = findViewById(R.id.doctor_add_user);
        addUser.setOnClickListener(this);
        doctorAdapter = new DoctorManagerAdapter(this,R.layout.doctor_manager_item,doctors);
        ls.setAdapter(doctorAdapter);
        ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee e;
//                if(isSearch){
//                    e = empSearchList.get(i);
//                }else{
//                    e = empList.get(i);
//                }
                final Doctor d;
                if(isSearch){
                    d=doctorSearchList.get(i);
                }else {
                    d=doctors.get(i);
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(DoctorManagerActivity.this);
                dialog.setTitle("警告");
                dialog.setMessage("确认删除吗？");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDoctor(d.getAccount());
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                return true;
            }
        });
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Doctor d;
                if(isSearch){
                    d=doctorSearchList.get(i);
                }else{
                    d=doctors.get(i);
                }
                Intent intent = new Intent(DoctorManagerActivity.this,ScheduleManagerActivity.class);
                intent.putExtra("doctor",d);
                startActivityForResult(intent,0);
                Toast.makeText(getApplicationContext(),d.getName(),Toast.LENGTH_SHORT).show();
            }
        });


        user_searchview = findViewById(R.id.doctor_userSearchView);

        user_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doctorSearchList.clear();
                for (int i = 0; i < doctors.size(); i++) {
                    if (doctors.get(i).getName().contains(newText)) {
                        doctorSearchList.add(doctors.get(i));
                    }
                }
                if (doctorSearchList.size() > 0 && newText.length() > 0) {
                    doctorAdapter= new DoctorManagerAdapter(getApplicationContext(),R.layout.doctor_manager_item,doctorSearchList);
                    ls.setAdapter(doctorAdapter);
                    isSearch = true;

                }
                else if (newText.length() == 0) {
                    doctorAdapter = new DoctorManagerAdapter(getApplication(),R.layout.doctor_manager_item,doctors);
                    ls.setAdapter(doctorAdapter);
                    isSearch = false;
                }
                return false;
            }
        });
    }

    public void setData()
    {

        doctors.clear();
//        userList.clear();
        c=0;
        dialog = new LoadingDialog(DoctorManagerActivity.this,"数据请求中...");
        dialog.show();
        getDoctor();
//        getUser();

    }

    public void deleteDoctor(String account){
        dialog = new LoadingDialog(DoctorManagerActivity.this,"数据请求中...");
        dialog.show();
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求
        StringRequest stringRequest =new StringRequest(GlobalVar.url+
                "user/deleteDoctor?account="+account
                , new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                if(s.equals("1")){
                    Message message = new Message();
                    message.what =3;
                    handler.sendMessage(message);
                }else{
                    Message message = new Message();
                    message.what =2;
                    handler.sendMessage(message);
                }


            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("LoginUser","加载错误"+volleyError);
                Message message = new Message();
                message.what =2;
                handler.sendMessage(message);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }
    public void getDoctor(){
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://128.0.0.241:8080/mobileUser/login?name=000001&password=123456";

        StringRequest stringRequest =new StringRequest(GlobalVar.url+
                "user/getDoctorBySection?sectionId="+sectionId, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {

                doctors = new Gson().fromJson(s, new TypeToken<List<Doctor>>() {}.getType());
                Message message = new Message();
                message.what =1;
                handler.sendMessage(message);

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("LoginUser","加载错误"+volleyError);
                Message message = new Message();
                message.what =1;
                handler.sendMessage(message);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        setResult(1);
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.doctor_add_user:
                Intent i = new Intent(DoctorManagerActivity.this,
                        AddDoctorActivity.class);
                startActivityForResult(i,1);
                break;
            default:
                break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(requestCode==0){
            switch (resultCode)
            {
                case -1:

                    Toast.makeText(getApplication(),"添加值班失败",Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                    Toast.makeText(getApplication(),"添加值班成功",Toast.LENGTH_SHORT).show();;

                    break;
                default:
                    break;
            }
        }else if(requestCode==1){
            switch (resultCode){
                case 1:
                    Toast.makeText(getApplication(),"添加医生成功",Toast.LENGTH_SHORT).show();;
                    setData();
                    break;
                case 0:
                    Toast.makeText(getApplication(),"添加医生失败",Toast.LENGTH_SHORT).show();;

                    break;
            }
        }

    }
}
