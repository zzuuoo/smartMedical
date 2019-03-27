package com.zuovx.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zuovx.Model.DoctorSche;
import com.zuovx.R;

import java.util.List;

public class DoctorSchAdapter extends ArrayAdapter<DoctorSche> {
    static class ViewHolder{
        ImageView doctoeHead;
        TextView doctorName;
        TextView appointCount;
        TextView doctorPosition;
        TextView doctorForteAndIntroduction;
        TextView doctorWorkTime;

    }

    private int resourceId;
    public DoctorSchAdapter(@NonNull Context context, int resource, @NonNull List<DoctorSche> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        DoctorSche doctorSche = getItem(position);
        View view;
        if(convertView==null)
        {
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            convertView=view;
            holder = new ViewHolder();
            holder.doctorName = view.findViewById(R.id.doctorName);
            holder.doctoeHead = view.findViewById(R.id.doctorHead);
            holder.appointCount = view.findViewById(R.id.appointCount);
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
        holder.doctorName.setText(doctorSche.getDoctor().getName());
        holder.doctorPosition.setText(doctorSche.getDoctor().getHonour());
        holder.doctorForteAndIntroduction.setText(
                "医生特长："+doctorSche.getDoctor().getForte()+"。医生简介："+doctorSche.getDoctor().getIntroduction());
        holder.appointCount.setText("余号："+doctorSche.getSchedule().getRemainder());
        int w = doctorSche.getSchedule().getW();
        String wh ;
        if(w==1){
            wh = "上午";
        }else{
            wh = "下午";
        }
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        holder.doctorWorkTime.setText("    "+format.format(doctorSche.getSchedule().getWorkTimeStart())+" "+wh);
        holder.doctorWorkTime.setText("    "+doctorSche.getSchedule().getWorkTimeStart()+"    "+wh);
        return convertView;
    }
}
