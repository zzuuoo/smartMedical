package com.zuovx.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Model.BookPatientSche;
import com.zuovx.Model.PatientRecord;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddPatientRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private BookPatientSche bookPatientSche;
    private TextView pname;
    private EditText chief,nowHistory,physicalExamination,therapeuticExamination,diagnosis;
    private Button sure,cancle,edit;
    private LoadingDialog loadingDialog;
    private RequestQueue requestQueue;
    private Handler handler;
    private Toolbar toolbar;
    private PatientRecord patientRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_record);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        bookPatientSche = (BookPatientSche)intent.getSerializableExtra("bookPatientSche");
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
                        finish();
                        break;
                    case 3:
                        initPatintRecord();
                        loadingDialog.close();
                        break;
                    case 4:
                        loadingDialog.close();
                        break;
                }
            }
        };
    }
    public void init(){
        //返回键
        toolbar = findViewById(R.id.add_doctor_book_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chief = findViewById(R.id.dchief);
        nowHistory = findViewById(R.id.dnowHistory);
        physicalExamination = findViewById(R.id.dphysicalExamination);
        therapeuticExamination = findViewById(R.id.dtherapeuticExamination);
        diagnosis = findViewById(R.id.ddiagnosis);
        pname = findViewById(R.id.patientRecord_name);
        pname.setText(bookPatientSche.getPatient().getName());
        sure = findViewById(R.id.sure_dp);
        cancle = findViewById(R.id.cancel_dp);
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);
        edit = findViewById(R.id.doctor_add_patient_record);
        edit.setOnClickListener(this);
        getPatientRecord();
        setUnEditAble();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sure_dp:
                PostWithVolley();
                break;
            case R.id.cancel_dp:
                finish();
                break;
            case R.id.doctor_add_patient_record:
                setEditAble();
                break;
                default:
                    break;
        }

    }

    private void initPatintRecord(){
        if(patientRecord!=null&&patientRecord.getDoctorId()!=0){
            chief.setText(patientRecord.getChief());
            nowHistory.setText(patientRecord.getNowHistory());
            physicalExamination.setText(patientRecord.getPhysicalExamination());
            therapeuticExamination.setText(patientRecord.getTherapeuticExamination());
            diagnosis.setText(patientRecord.getDiagnosis());
        }
    }

    private void setUnEditAble(){
        chief.setCursorVisible(false);
        chief.setFocusable(false);
        chief.setFocusableInTouchMode(false);

        nowHistory.setCursorVisible(false);
        nowHistory.setFocusable(false);
        nowHistory.setFocusableInTouchMode(false);

        physicalExamination.setCursorVisible(false);
        physicalExamination.setFocusable(false);
        physicalExamination.setFocusableInTouchMode(false);

        therapeuticExamination.setCursorVisible(false);
        therapeuticExamination.setFocusable(false);
        therapeuticExamination.setFocusableInTouchMode(false);

        diagnosis.setCursorVisible(false);
        diagnosis.setFocusable(false);
        diagnosis.setFocusableInTouchMode(false);
    }

    private void setEditAble(){
        chief.setCursorVisible(true);
        chief.setFocusable(true);
        chief.setFocusableInTouchMode(true);

        nowHistory.setCursorVisible(true);
        nowHistory.setFocusable(true);
        nowHistory.setFocusableInTouchMode(true);

        physicalExamination.setCursorVisible(true);
        physicalExamination.setFocusable(true);
        physicalExamination.setFocusableInTouchMode(true);

        therapeuticExamination.setCursorVisible(true);
        therapeuticExamination.setFocusable(true);
        therapeuticExamination.setFocusableInTouchMode(true);

        diagnosis.setCursorVisible(true);
        diagnosis.setFocusable(true);
        diagnosis.setFocusableInTouchMode(true);

        Toast.makeText(AddPatientRecordActivity.this,"请进行修改",Toast.LENGTH_SHORT).show();
    }


    private void PostWithVolley()  {

        if(TextUtils.isEmpty(chief.getText())||TextUtils.isEmpty(nowHistory.getText())||
                TextUtils.isEmpty(physicalExamination.getText())||
                TextUtils.isEmpty(therapeuticExamination.getText())||
                TextUtils.isEmpty(diagnosis.getText())){
            Toast.makeText(AddPatientRecordActivity.this,"数据不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        loadingDialog = new LoadingDialog(AddPatientRecordActivity.this,"加载中,,,");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                GlobalVar.url + "medicalRecord/insertPatientRecord",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        if(s.equals("1"))
                        {
                            AddPatientRecordActivity.this.setResult(1);
                            Toast.makeText(AddPatientRecordActivity.this,"succeed",Toast.LENGTH_SHORT).show();
                        }else{
                            AddPatientRecordActivity.this.setResult(0);
                            Toast.makeText(AddPatientRecordActivity.this,"faild",Toast.LENGTH_SHORT).show();

                        }
                        Message message1 = new Message();
                        message1.what = 2;
                        handler.sendMessage(message1);


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                AddPatientRecordActivity.this.setResult(0);
                Message message1 = new Message();
                message1.what = 2;
                handler.sendMessage(message1);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("patientId",String.valueOf(bookPatientSche.getPatient().getPatientId()));
                map.put("doctorId",String.valueOf(bookPatientSche.getSchedule().getDoctorId()));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String date = bookPatientSche.getSchedule().getWorkTimeStart();
                Date d = new Date();
                try {
                     d = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                map.put("admissionTime",String.valueOf(d.getTime()));
                map.put("chief",chief.getText().toString());
                map.put("nowHistory",nowHistory.getText().toString());
                map.put("physicalExamination",physicalExamination.getText().toString());
                map.put("therapeuticExamination",therapeuticExamination.getText().toString());
                map.put("diagnosis",diagnosis.getText().toString());
                return map;
            }

        };

        requestQueue.add(stringRequest);
    }

    private void getPatientRecord()  {

        loadingDialog = new LoadingDialog(AddPatientRecordActivity.this,"加载中,,,");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                GlobalVar.url + "medicalRecord/editPatientRecord",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            Gson gson = new Gson();
                            patientRecord = gson.fromJson(s, new TypeToken<PatientRecord>() {}.getType());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message message1 = new Message();
                        message1.what = 3;
                        handler.sendMessage(message1);

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                AddPatientRecordActivity.this.setResult(0);
                Message message1 = new Message();
                message1.what = 4;
                handler.sendMessage(message1);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("patientId",String.valueOf(bookPatientSche.getPatient().getPatientId()));
                map.put("doctorId",String.valueOf(bookPatientSche.getSchedule().getDoctorId()));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String date = bookPatientSche.getSchedule().getWorkTimeStart();
                Date d = new Date();
                try {
                    d = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                map.put("admissionTime",String.valueOf(d.getTime()));
                return map;
            }

        };

        requestQueue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
