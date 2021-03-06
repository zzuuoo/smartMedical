package com.zuovx.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Model.Patient;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

public class EditMyselfctivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name,phone,address,forte,introduction,age;
    private RadioGroup sexs;
    private RadioButton boy,girl;
    private Button sure,cancle;
    private Patient patient;

    private RequestQueue requestQueue;
    private LoadingDialog loadingDialog;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_myselfctivity);
        ActivityCollector.addActivity(this);
        init();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        loadingDialog.close();
                        finish();
                        break;
                    case 2:
                        loadingDialog.close();
                        initshow();
//                        Toast.makeText(MyBookActivity.this,"取消失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        break;
                }
            }
        };
    }
    public void init(){
        name = findViewById(R.id.edit_patient_name);
        phone = findViewById(R.id.edit_patient_phone);
        address = findViewById(R.id.edit_patient_address);
        age = findViewById(R.id.edit_patient_age);
        boy = findViewById(R.id.edit_patient_boy);
        girl = findViewById(R.id.edit_patient_girl);
        sure = findViewById(R.id.sure_edit_patient);
        cancle = findViewById(R.id.cancel_edit_patient);
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);
        getPatient();

    }

    public void initshow(){
        if(patient!=null){
            name.setText(patient.getName());
            age.setText(String.valueOf(patient.getAge()));
            phone.setText(patient.getPhone());
            address.setText(patient.getAddress());
            if(patient.getSex()==0){
                girl.setChecked(true);
            }else{
                boy.setChecked(true);
            }
        }
    }

    public void getPatient(){
        loadingDialog = new LoadingDialog(this,"数据读取中...");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求

        StringRequest stringRequest =new StringRequest(GlobalVar.url
                +"user/getPatientByAccount?account="+MainActivity.user.getAccount(), new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                loadingDialog.close();
                try {
                    Gson gson = new Gson();
                    patient = gson.fromJson(s, new TypeToken<Patient>() {}.getType());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);

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


    private void savePatient()  {

        loadingDialog = new LoadingDialog(EditMyselfctivity.this,"加载中,,,");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                GlobalVar.url + "user/updatePatient",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        if(s.equals("1"))
                        {
//                            AddDoctorActivity.this.setResult(1);
                            Toast.makeText(EditMyselfctivity.this,"succeed",Toast.LENGTH_SHORT).show();
                        }else{
//                            AddDoctorActivity.this.setResult(0);
                            Toast.makeText(EditMyselfctivity.this,"faild",Toast.LENGTH_SHORT).show();

                        }
                        Message message1 = new Message();
                        message1.what = 1;
                        handler.sendMessage(message1);


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

//                AddDoctorActivity.this.setResult(0);
                Message message1 = new Message();
                message1.what = 1;
                handler.sendMessage(message1);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name",name.getText().toString());//传入参数
                map.put("phone",phone.getText().toString());//传入参数
                map.put("address",address.getText().toString());
                map.put("patientId",patient.getPatientId().toString());//传入参数
                map.put("age",age.getText().toString());//传入参数
                map.put("account",MainActivity.user.getAccount());
                String sex = "0";
                if(boy.isChecked()){
                    sex = "1";
                }
                map.put("sex",sex);//传入参数
                return map;
            }

        };

        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_edit_patient:
                finish();
                break;
            case R.id.sure_edit_patient:
                savePatient();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
