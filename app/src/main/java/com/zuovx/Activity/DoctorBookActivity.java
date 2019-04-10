package com.zuovx.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Adapter.PatientBookAdapter;
import com.zuovx.Model.BookPatientSche;
import com.zuovx.Model.Schedule;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import java.util.List;

public class DoctorBookActivity extends AppCompatActivity {

    private int doctorId,scheduleId;
    private RequestQueue requestQueue;
    private LoadingDialog loadingDialog;
    private List<BookPatientSche> books;
    private Toolbar toolbar;
    private SearchView searchView;
    private TextView textView;
    private ListView listView;
    private Schedule schedule;
//    private BookAdapter bookAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_book);
        ActivityCollector.addActivity(this);
        Intent intent = this.getIntent();
        doctorId = intent.getIntExtra("doctorId",0);
        scheduleId = intent.getIntExtra("scheduleId",0);
        schedule = (Schedule)intent.getSerializableExtra("schedule");
        init();
    }

    public void init(){
        //返回键
        toolbar = findViewById(R.id.doctor_book_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchView = findViewById(R.id.doctor_book_searchView);
        textView = findViewById(R.id.nothing_doctor_book);
        listView = findViewById(R.id.doctor_book_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookPatientSche bookPatientSche = books.get(i);
                Intent intent = new Intent(DoctorBookActivity.this,AddPatientRecordActivity.class);
//                intent.putSerializableExtra()
                intent.putExtra("bookPatientSche",bookPatientSche);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(),"我是book",Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                return true;
            }
        });

        getBookByScheduleId();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    //每个book有个病人，根据这个book的病人id打开或创建病历
    public void getBookByScheduleId()
    {
        loadingDialog = new LoadingDialog(this,"数据读取中...");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求

        StringRequest stringRequest =new StringRequest(GlobalVar.url
                +"book/showBookforDoctor?scheduleId="+scheduleId, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                loadingDialog.close();
                try {
                    Gson gson = new Gson();
                    books = gson.fromJson(s, new TypeToken<List<BookPatientSche>>() {}.getType());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(books==null||books.size()==0)
                {
                    textView.setVisibility(View.VISIBLE);
                }else{
                    textView.setVisibility(View.GONE);
                    PatientBookAdapter patientBookAdapter =
                            new PatientBookAdapter(DoctorBookActivity.this,
                            R.layout.book_patient_item,books);
                    listView.setAdapter(patientBookAdapter);
                }

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
}
