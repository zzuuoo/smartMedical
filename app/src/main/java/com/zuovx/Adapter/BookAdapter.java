package com.zuovx.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zuovx.Model.BookDocSche;
import com.zuovx.R;

import java.util.List;

public class BookAdapter extends ArrayAdapter<BookDocSche> {

    static class ViewHolder{
        TextView name;
        TextView honour;
        TextView when;

    }
    private int resourceid;

    public BookAdapter(@NonNull Context context, int resource, @NonNull List<BookDocSche> objects) {
        super(context, resource, objects);
        this.resourceid=resource;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        BookAdapter.ViewHolder holder;
        BookDocSche bookDocSche = getItem(position);
        View view;
        if(convertView == null) {
            view= LayoutInflater.from(getContext()).inflate(resourceid,parent,false);
            convertView=view;
            holder = new BookAdapter.ViewHolder();
            holder.name = view.findViewById(R.id.book_schedule_doctor_name);
            holder.honour = view.findViewById(R.id.book_schedule_honour);
            holder.when = view.findViewById(R.id.book_schedule_item_when);
            convertView.setTag(holder);
        }else{
            view = convertView;
            holder = (BookAdapter.ViewHolder)convertView.getTag();
        }
        holder.name.setText(bookDocSche.getDoctor().getName());
        holder.honour.setText(bookDocSche.getDoctor().getHonour());
        String w = "  上午";
        if(bookDocSche.getSchedule().getW() == 2){
            w = "  下午";
        }

//        if()
        if(bookDocSche.getBook().isAvaliablity()){
            holder.when.setText(bookDocSche.getSchedule().getWorkTimeStart()+w);
        }else{
            holder.when.setText(bookDocSche.getSchedule().getWorkTimeStart()+w+"    已完成");
        }
        return convertView;
    }
}