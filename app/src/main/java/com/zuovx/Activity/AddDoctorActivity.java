package com.zuovx.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Model.Section;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AddDoctorActivity extends AppCompatActivity implements View.OnClickListener {

    private LoadingDialog loadingDialog;

    private List<Section> sections ;
    private RequestQueue requestQueue;
    private Handler handler;
    private Spinner spinnerSection;
    private Button sure,cancle;

    RadioGroup radioGroup;

    private EditText name,password,phone,idNumber,honour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        init();
        ActivityCollector.addActivity(this);
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
                    case 2:
                        loadingDialog.close();
                        finish();
                        break;
                    case 3:
                        break;
                }
            }
        };


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sure_addDoctor:
                //读取数据，发生添加数据
//                postDataWithParame();
                PostWithVolley();
                break;
            case R.id.cancel_addDoctor:
                AddDoctorActivity.this.setResult(0);
                finish();
                break;
        }
    }

    private void init(){
        spinnerSection = (Spinner)findViewById(R.id.spinner_section);
//        name,password,phone,idNumber,honour
        name = (findViewById(R.id.add_doctor_name));
        password = findViewById(R.id.add_doctor_pass);
        phone = findViewById(R.id.add_doctor_phone);
        idNumber = findViewById(R.id.add_doctor_id);
        honour = findViewById(R.id.add_doctor_honour);
        radioGroup = findViewById(R.id.add_doctor_radiogroup_user);
        sure = findViewById(R.id.sure_addDoctor);
        sure.setOnClickListener(this);
        cancle = findViewById(R.id.cancel_addDoctor);
        cancle.setOnClickListener(this);
        getSection();



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
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

    private void postDataWithParame() {
        String url = GlobalVar.url+"user/addDoctor";
        loadingDialog = new LoadingDialog(AddDoctorActivity.this,"数据读取中");
        loadingDialog.show();
        final Map<String,String> map = new HashMap<>();
        map.put("name",name.getText().toString());//传入参数
        map.put("phone",phone.getText().toString());//传入参数
        map.put("password",password.getText().toString());//传入参数
        map.put("idNumber",idNumber.getText().toString());//传入参数
        map.put("honour",honour.getText().toString());//传入参数
        map.put("sectionId",sections.get(spinnerSection.getSelectedItemPosition()).getSectionId()+"");//传入参数
//        spinnerSection.getSelectedItemPosition();
        RadioButton rb = (RadioButton)AddDoctorActivity.this.findViewById(radioGroup.getCheckedRadioButtonId());
        map.put("sex",rb.getText().toString());//传入参数
        System.out.println("addDoctor:"+map.get("phone"));

        JSONObject jsonObject = new JSONObject(map);

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
//        String jsonStr = "{\"username\":\"lisi\",\"nickname\":\"李四\"}";//json数据.
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                AddDoctorActivity.this.setResult(-1);
//                loadingDialog.close();
                Message message = new Message();
                message.what=2;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if(response.body().string().equals("1"))
                {
                    AddDoctorActivity.this.setResult(1);
                }else{
                    AddDoctorActivity.this.setResult(0);
                }

                Message message = new Message();
                message.what=2;
                handler.sendMessage(message);
            }


        });
    }

    private void PostWithVolley()  {

        loadingDialog = new LoadingDialog(AddDoctorActivity.this,"加载中,,,");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                GlobalVar.url + "user/addDoctor",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("1")){
                            AddDoctorActivity.this.setResult(1);
                        }else{
                            AddDoctorActivity.this.setResult(0);
                        }
                        Message message1 = new Message();
                        message1.what = 2;
                        handler.sendMessage(message1);


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                AddDoctorActivity.this.setResult(0);
                Message message1 = new Message();
                message1.what = 2;
                handler.sendMessage(message1);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name",name.getText().toString());//传入参数
                map.put("phone",phone.getText().toString());//传入参数
                map.put("password",password.getText().toString());//传入参数
                map.put("idNumber",idNumber.getText().toString());//传入参数
                map.put("honour",honour.getText().toString());//传入参数
                map.put("sectionId",sections.get(spinnerSection.getSelectedItemPosition()).getSectionId()+"");//传入参数
//        spinnerSection.getSelectedItemPosition();
                RadioButton rb = (RadioButton)AddDoctorActivity.this.findViewById(radioGroup.getCheckedRadioButtonId());
                String sex = "1";
                if(rb.getText().toString().equals("女")){
                    sex = "0";
                }
                map.put("sex",sex);//传入参数
                return map;
            }

        };

        requestQueue.add(stringRequest);
    }
}
