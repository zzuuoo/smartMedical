package com.zuovx.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zuovx.Model.BookPatientSche;
import com.zuovx.R;

import java.util.List;

public class PatientBookAdapter extends ArrayAdapter<BookPatientSche> {

    static class ViewHolder{
        TextView name;
        TextView sex;
        TextView when;

    }
    private int resourceid;

    public PatientBookAdapter(@NonNull Context context, int resource, @NonNull List<BookPatientSche> objects) {
        super(context, resource, objects);
        this.resourceid=resource;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        PatientBookAdapter.ViewHolder holder;
        BookPatientSche bookDocSche = getItem(position);
        View view;
        if(convertView == null) {
            view= LayoutInflater.from(getContext()).inflate(resourceid,parent,false);
            convertView=view;
            holder = new PatientBookAdapter.ViewHolder();
            holder.name = view.findViewById(R.id.book_patient_name);
            holder.sex = view.findViewById(R.id.book_patient_sex);
            holder.when = view.findViewById(R.id.book_patient_item_when);
            convertView.setTag(holder);
        }else{
            view = convertView;
            holder = (PatientBookAdapter.ViewHolder)convertView.getTag();
        }
        holder.name.setText(bookDocSche.getPatient().getName());
        if(bookDocSche.getPatient()==null||bookDocSche.getPatient().getSex()==null){
            holder.sex.setText("未设置");
        } else if(bookDocSche.getPatient().getSex()==0){
            holder.sex.setText("女");
        }else{
            holder.sex.setText("男");
        }
        String w = "  上午";
        if(bookDocSche.getSchedule().getW() == 2){
            w = "  下午";
        }
        if(bookDocSche.getBook().isAvaliablity()){
            holder.when.setText(bookDocSche.getSchedule().getWorkTimeStart()+w+"    未处理");
            System.out.println("未处理");
        }else{
            System.out.println("已处理");
            holder.when.setText(bookDocSche.getSchedule().getWorkTimeStart()+w+"    已处理");
        }

        return convertView;
    }
}