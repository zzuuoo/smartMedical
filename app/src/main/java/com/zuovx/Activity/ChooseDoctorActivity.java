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
import com.zuovx.Adapter.DoctorAdapter;
import com.zuovx.Model.Doctor;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseDoctorActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private LoadingDialog loadingDialog;
    private List<Doctor> doctors;
    private List<Doctor> searchDoctors;
    private ListView listView;
    private SearchView searchView;
    private Integer sectionId;
    private Boolean isSearch = false;
    private DoctorAdapter doctorAdapter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_doctor);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        sectionId = intent.getIntExtra("sectionId",0);
        getDoctorBySectionId(sectionId);
        init();


    }

    public void init()
    {
        searchView = (SearchView)findViewById(R.id.all_doctor_searchView);

        textView = (TextView)findViewById(R.id.nothingDoctor);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getApplicationContext(),newText, Toast.LENGTH_SHORT).show();
                searchDoctors = new ArrayList<>();
                for(int i=0;i<doctors.size();i++){
                    if(doctors.get(i).getName().contains(newText)){
                        searchDoctors.add(doctors.get(i));
//                        System.out.println("添加一个"+doctors.get(i).getName());
                    }
                }
                if(searchDoctors.size()>0&&newText.length()>0){
                    doctorAdapter = new DoctorAdapter(getApplicationContext(),
                            R.layout.doctor_item,searchDoctors);
                    listView.setAdapter(doctorAdapter);
                    isSearch=true;

                }
                if(newText.length()==0)
                {
                    doctorAdapter = new DoctorAdapter(getApplicationContext(),
                            R.layout.doctor_item,doctors);
                    listView.setAdapter(doctorAdapter);
                    isSearch=false;
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                System.out.println("点击了医生");
                Doctor doctor =null;
                if(isSearch)
                {
                    doctor = searchDoctors.get(i);
                }else{
                    doctor = doctors.get(i);
                }
//                Intent intent = new Intent(BookActivity.this,ChooseDoctorActivity.class);
//                intent.putExtra("sectionId",section.getSectionId());
//                startActivity(intent);
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChooseDoctorActivity.this);
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
    public void getDoctorBySectionId(Integer sectionId)
    {
        listView = (ListView)findViewById(R.id.all_doctor_list);
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
                        System.out.println(d.getName());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadingDialog.close();
                doctorAdapter = new DoctorAdapter(getApplicationContext(),
                        R.layout.doctor_item,doctors);
                listView.setAdapter(doctorAdapter);
                if(doctors==null||doctors.size()==0)
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
