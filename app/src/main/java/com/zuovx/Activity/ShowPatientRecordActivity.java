package com.zuovx.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zuovx.Model.PPatientRecord;
import com.zuovx.R;

public class ShowPatientRecordActivity extends AppCompatActivity {
    private PPatientRecord pPatientRecord;
    private EditText chief,nowHistory,physicalExamination,therapeuticExamination,diagnosis;
    private TextView pname;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_patient_record);
        Intent intent = getIntent();
        pPatientRecord = (PPatientRecord)intent.getSerializableExtra("PPatientRecord");
        if(pPatientRecord!=null){
            System.out.println("0000000000000000000");
            if(pPatientRecord.getPatientRecord()!=null){
                System.out.println("..............."+pPatientRecord.getPatientRecord().getChief());
            }
        }
        init();
    }
    public void init(){
        chief = findViewById(R.id.show_patient_record_chief);
        nowHistory = findViewById(R.id.show_patient_record_nowHistory);
        physicalExamination = findViewById(R.id.show_patient_record_physicalExamination);
        therapeuticExamination = findViewById(R.id.show_patient_record_therapeuticExamination);
        diagnosis = findViewById(R.id.show_patient_record_diagnosis);

        pname = findViewById(R.id.show_patient_record_name);
        if(pPatientRecord!=null){
            if(pPatientRecord.getPatientRecord()!=null){
                chief.setText(pPatientRecord.getPatientRecord().getChief());
                nowHistory.setText(pPatientRecord.getPatientRecord().getNowHistory());
                physicalExamination.setText(pPatientRecord.getPatientRecord().getPhysicalExamination());
                therapeuticExamination.setText(pPatientRecord.getPatientRecord().getTherapeuticExamination());
                diagnosis.setText(pPatientRecord.getPatientRecord().getDiagnosis());
            }
            if(pPatientRecord.getPatient()!=null){
                if(pPatientRecord.getPatient().getName()!=null&&!pPatientRecord.getPatient().getName().equals("")){
                    pname.setText(pPatientRecord.getPatient().getName());
                }else{
                    pname.setText(pPatientRecord.getPatient().getAccount());
                }

            }


        }
        setUnEditAble();

        //返回键
        toolbar = findViewById(R.id.show_patient_record_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        searchView = (SearchView)findViewById(R.id.all_schedule_searchView);
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
}
