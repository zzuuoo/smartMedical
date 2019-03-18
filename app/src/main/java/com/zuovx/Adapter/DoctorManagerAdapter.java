package com.zuovx.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zuovx.Model.Doctor;
import com.zuovx.R;

import java.util.List;

public class DoctorManagerAdapter extends ArrayAdapter<Doctor> {
    static class ViewHolder{
        ImageView doctoeHead;
        TextView doctorName;
        //        TextView appointCount;
        TextView account;


    }

    private int resourceId;
    public DoctorManagerAdapter(@NonNull Context context, int resource, @NonNull List<Doctor> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        Doctor doctor = getItem(position);
        View view;
        if(convertView==null)
        {
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            convertView=view;
            holder = new ViewHolder();
            holder.doctorName = view.findViewById(R.id.doctor_manager_name);
            holder.doctoeHead = view.findViewById(R.id.doctor_manager_head);
//            holder.appointCount = view.findViewById(R.id.appointCount);
            holder.account = view.findViewById(R.id.doctor_manager_account);
            convertView.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder)convertView.getTag();
        }
        holder.doctorName.setText(doctor.getName());
        holder.account.setText(doctor.getAccount());
//        doctor.getSex()
        return convertView;
    }
}