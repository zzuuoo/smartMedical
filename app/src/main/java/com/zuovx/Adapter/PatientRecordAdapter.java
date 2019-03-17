package com.zuovx.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zuovx.Activity.MainActivity;
import com.zuovx.Model.PatientRecord;
import com.zuovx.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PatientRecordAdapter extends ArrayAdapter<PatientRecord> {


    static class ViewHolder{
        TextView medical_record_patient_name ;
        TextView time_of_create ;
        TextView medical_record_item_type ;
        TextView medical_record_item_introduction;
        TextView medical_record_status ;
    }

    private int resourceid;
    private Context context;
    public PatientRecordAdapter(@NonNull Context context, int resource, @NonNull List<PatientRecord> objects) {
        super(context, resource, objects);
        resourceid = resource;
        this.context=context;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        PatientRecord medicalRecord = getItem(position);
        View view;
        if(convertView==null)
        {
            view= LayoutInflater.from(getContext()).inflate(resourceid,parent,false);
            convertView=view;
            holder = new ViewHolder();
            holder.medical_record_item_introduction = view.findViewById(R.id.medical_record_item_introduction);
            holder.medical_record_item_type=view.findViewById(R.id.medical_record_item_type);
            holder.medical_record_patient_name=view.findViewById(R.id.medical_record_patient_name);
            holder.medical_record_status = view.findViewById(R.id.medical_record_status);
            holder.time_of_create = view.findViewById(R.id.time_of_create);
            convertView.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder)convertView.getTag();
        }
        //设置信息
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date(medicalRecord.getAdmissionTime()));// new Date()为获取当前系统时间，也可使用当前时间戳
        holder.time_of_create.setText(date);
        holder.medical_record_patient_name.setText(MainActivity.user.getAccount());
        holder.medical_record_status.setText(medicalRecord.getDiagnosis());
        holder.medical_record_item_type.setText("住院病历");
        holder.medical_record_item_introduction.setText( medicalRecord.getChief());

        return view;
    }
}
