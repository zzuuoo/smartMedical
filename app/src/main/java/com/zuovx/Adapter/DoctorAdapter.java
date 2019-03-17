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

public class DoctorAdapter extends ArrayAdapter<Doctor> {
    static class ViewHolder{
        ImageView doctoeHead;
        TextView doctorName;
//        TextView appointCount;
        TextView doctorPosition;
        TextView doctorForteAndIntroduction;
        TextView doctorWorkTime;

    }

    private int resourceId;
    public DoctorAdapter(@NonNull Context context, int resource, @NonNull List<Doctor> objects) {
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
            holder.doctorName = view.findViewById(R.id.doctorName);
            holder.doctoeHead = view.findViewById(R.id.doctorHead);
//            holder.appointCount = view.findViewById(R.id.appointCount);
            holder.doctorForteAndIntroduction = view.findViewById(R.id.doctorForte);
            holder.doctorPosition = view.findViewById(R.id.doctorPosition);
            holder.doctorWorkTime = view.findViewById(R.id.doctorWorkTime);
//            holder.sectionIntroduction = view.find
            convertView.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder)convertView.getTag();
        }
        holder.doctorName.setText(doctor.getName());
//        doctor.getSex()
        holder.doctorForteAndIntroduction.setText(
                "医生特长："+doctor.getForte()+"。医生简介："+doctor.getIntroduction());
        return convertView;
    }
}
