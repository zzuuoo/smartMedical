package com.zuovx.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zuovx.Model.BookPatientSche;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;

public class AddPatientRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private BookPatientSche bookPatientSche;
    private TextView pname;
    private EditText chief,nowHistory,physicalExamination,therapeuticExamination,diagnosis;
    private Button sure,cancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_record);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        bookPatientSche = (BookPatientSche)intent.getSerializableExtra("bookPatientSche");
        init();
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
                break;
            case R.id.cancel_dp:
                finish();
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
