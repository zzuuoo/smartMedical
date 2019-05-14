package com.zuovx.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zuovx.Model.DoctorSche;
import com.zuovx.R;

import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<DoctorSche> {

    static class ViewHolder{
        TextView name;
        TextView count;
        TextView when;
        TextView f;

    }
    private int resourceid;

    public ScheduleAdapter(@NonNull Context context, int resource, @NonNull List<DoctorSche> objects) {
        super(context, resource, objects);
        this.resourceid=resource;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ScheduleAdapter.ViewHolder holder;
        DoctorSche schedule = getItem(position);
        View view;
        if(convertView == null) {
            view= LayoutInflater.from(getContext()).inflate(resourceid,parent,false);
            convertView=view;
            holder = new ScheduleAdapter.ViewHolder();
            holder.name = view.findViewById(R.id.schedule_doctor_name);
            holder.count = view.findViewById(R.id.schedule_appoint_count);
            holder.when = view.findViewById(R.id.schedule_item_when);
            holder.f = view.findViewById(R.id.saflag);
            convertView.setTag(holder);
        }else{
            view = convertView;
            holder = (ScheduleAdapter.ViewHolder)convertView.getTag();
        }
        holder.name.setText(schedule.getDoctor().getName());
        holder.count.setText("余号:"+schedule.getSchedule().getRemainder());
        String w = "  上午";
        if(schedule.getSchedule().getW() == 2){
            w = "  下午";
        }
        holder.when.setText(schedule.getSchedule().getWorkTimeStart()+w);
        if(schedule.getSchedule().getIsCancle()==true){
            holder.f.setText("已取消");
        }
        return convertView;
    }
}