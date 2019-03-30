package com.zuovx.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Model.Section;
import com.zuovx.R;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import java.util.List;

public class AddDoctorActivity extends AppCompatActivity implements View.OnClickListener {

    private LoadingDialog loadingDialog;

    private List<Section> sections ;
    private RequestQueue requestQueue;
    private Handler handler;
    private Spinner spinnerSection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        init();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        if(sections!=null&&sections.size()>0){
                            String[] strings = new String[sections.size()];
                            for(int i=0;i<sections.size();i++){
                                strings[i] = sections.get(i).getSectionName();
                            }
                            spinnerSection.setAdapter(new ArrayAdapter<String>(AddDoctorActivity.this,android.R.layout.simple_spinner_item,strings));
                        }
                        break;
                }
            }
        };


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    private void init(){
        spinnerSection = (Spinner)findViewById(R.id.spinner_section);
        getSection();



    }

    public void getSection(){
        //创建一个请求队列

//        listView = (ListView)view.findViewById(R.id.admin_section_list);
        loadingDialog = new LoadingDialog(AddDoctorActivity.this,"数据读取中");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(AddDoctorActivity.this);
        //创建一个请求

        StringRequest stringRequest =new StringRequest(GlobalVar.url +"section/getAllSection", new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                try {
                    Gson gson = new Gson();
                    sections = gson.fromJson(s, new TypeToken<List<Section>>() {}.getType());
                    Message message = new Message();
                    message.what=1;
                    handler.sendMessage(message);
                    Log.d("sections:",sections.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadingDialog.close();


            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loadingDialog.close();
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }
}
