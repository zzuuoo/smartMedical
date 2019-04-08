package com.zuovx.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Model.Book;
import com.zuovx.R;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import java.util.List;

public class DoctorBookActivity extends AppCompatActivity {

    private int doctorId,scheduleId;
    private RequestQueue requestQueue;
    private LoadingDialog loadingDialog;
    private List<Book> books;
//    private BookAdapter bookAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_book);
        Intent intent = this.getIntent();
        doctorId = intent.getIntExtra("doctorId",0);
        scheduleId = intent.getIntExtra("scheduleId",0);
    }

    //每个book有个病人，根据这个book的病人id打开或创建病历
    public void getBookByScheduleId()
    {
        loadingDialog = new LoadingDialog(this,"数据读取中...");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求

        StringRequest stringRequest =new StringRequest(GlobalVar.url
                +"book/getBooKByScheduleId?scheduleId="+scheduleId, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                loadingDialog.close();
                try {
                    Gson gson = new Gson();
                    books = gson.fromJson(s, new TypeToken<List<Book>>() {}.getType());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(books==null||books.size()==0)
                {
//                    textView.setVisibility(View.VISIBLE);
                }else{
//                    textView.setVisibility(View.GONE);
//                    bookAdapter = new BookAdapter(DoctorBookActivity.this,
//                            R.layout.mybook_item,books);
//                    listView.setAdapter(bookAdapter);
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
