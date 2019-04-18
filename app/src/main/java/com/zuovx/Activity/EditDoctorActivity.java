package com.zuovx.Activity;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditDoctorActivity extends AppCompatActivity implements View.OnClickListener {

    private LoadingDialog loadingDialog;

    private List<Section> sections ;
    private RequestQueue requestQueue;
    private Handler handler;
    private Spinner spinnerSection;
    private Button sure,cancle;
    private Doctor doctor;

    RadioGroup radioGroup;
    RadioButton girl,boy;
//    int sectionId;

    private EditText name,password,phone,idNumber,honour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor);
        Intent intent = getIntent();
//        sectionId = intent.getIntExtra("sectionId",-1);
        doctor = (Doctor) intent.getSerializableExtra("doctor");
        ActivityCollector.addActivity(this);
        init();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        if(sections!=null&&sections.size()>0){
                            int sid = 0;
                            String[] strings = new String[sections.size()];
                            for(int i=0;i<sections.size();i++){
                                strings[i] = sections.get(i).getSectionName();
                                if(doctor!=null){
                                    if(doctor.getSectionId()==sections.get(i).getSectionId()){
                                        sid = i;
                                    }
                                }

                            }
                            spinnerSection.setAdapter(new ArrayAdapter<String>(EditDoctorActivity.this,android.R.layout.simple_spinner_item,strings));
                            spinnerSection.setSelection(sid);
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
            case R.id.sure_editDoctor:
                //读取数据，发生添加数据
//                postDataWithParame();
                if(doctor!=null){
                    PostWithVolley();
                }else{
                    Toast.makeText(this,"something was wrong",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancel_editDoctor:
//                EditDoctorActivity.this.setResult(0);
                finish();
                break;
        }
    }

    private void init(){
        spinnerSection = (Spinner)findViewById(R.id.spinner_section);
//        name,password,phone,idNumber,honour
        name = (findViewById(R.id.edit_doctor_name));
        password = findViewById(R.id.edit_doctor_pass);
        phone = findViewById(R.id.edit_doctor_phone);
        idNumber = findViewById(R.id.edit_doctor_id);
        honour = findViewById(R.id.edit_doctor_honour);
        radioGroup = findViewById(R.id.edit_doctor_radiogroup);
        girl = findViewById(R.id.edit_doctor_girl);
        boy = findViewById(R.id.edit_doctor_boy);
        sure = findViewById(R.id.sure_editDoctor);
        sure.setOnClickListener(this);
        cancle = findViewById(R.id.cancel_editDoctor);
        cancle.setOnClickListener(this);
        getSection();
        if(doctor!=null){
            name.setText(doctor.getName());
            phone.setText(doctor.getPhone());
            idNumber.setText(doctor.getIdNumber());
            honour.setText(doctor.getHonour());
            if(doctor.getSex()==0){
                girl.setChecked(true);
            }else{
                boy.setChecked(true);
            }
        }



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    public void getSection(){
        //创建一个请求队列

//        listView = (ListView)view.findViewById(R.id.admin_section_list);
        loadingDialog = new LoadingDialog(EditDoctorActivity.this,"数据读取中");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(EditDoctorActivity.this);
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


    private void PostWithVolley()  {

        loadingDialog = new LoadingDialog(EditDoctorActivity.this,"加载中,,,");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                GlobalVar.url + "user/updateDoctor",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        if(s.equals("1"))
                        {
                            EditDoctorActivity.this.setResult(1);
                            Toast.makeText(EditDoctorActivity.this,"succeed",Toast.LENGTH_SHORT).show();
                        }else{
                            EditDoctorActivity.this.setResult(0);
                            Toast.makeText(EditDoctorActivity.this,"faild",Toast.LENGTH_SHORT).show();

                        }
                        Message message1 = new Message();
                        message1.what = 2;
                        handler.sendMessage(message1);


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                EditDoctorActivity.this.setResult(0);
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
                if(doctor!=null){
                    map.put("account",doctor.getAccount());//传入参数
                    map.put("doctorId",String.valueOf(doctor.getDoctorId()));
                }
                if (password.getText() != null &&
                        password.getText().toString() != null &&
                        password.getText().toString() != "") {
                    map.put("password",password.getText().toString());//传入参数

                }
                map.put("idNumber",idNumber.getText().toString());//传入参数
                map.put("honour",honour.getText().toString());//传入参数
                map.put("sectionId",sections.get(spinnerSection.getSelectedItemPosition()).getSectionId()+"");//传入参数
//        spinnerSection.getSelectedItemPosition();
                RadioButton rb = (RadioButton)EditDoctorActivity.this.findViewById(radioGroup.getCheckedRadioButtonId());
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
