package com.zuovx.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
import com.zuovx.Model.BookPatientSche;
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
    private Button sure,cancle;
    private LoadingDialog loadingDialog;
    private RequestQueue requestQueue;
    private Handler handler;
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
                        break;
                }
            }
        };
    }
    public void init(){
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
                default:
                    break;
        }

    }


    private void PostWithVolley()  {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
