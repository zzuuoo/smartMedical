package com.zuovx.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zuovx.Model.Section;
import com.zuovx.R;

import java.util.List;

public class SectionAdapter  extends ArrayAdapter<Section> {

    static class ViewHolder{
        TextView sectionName;
        TextView sectionIntroduction;

    }
    private int resourceid;

    public SectionAdapter(@NonNull Context context, int resource, @NonNull List<Section> objects) {
        super(context, resource, objects);
        this.resourceid=resource;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        Section section = getItem(position);
        View view;
        if(convertView==null)
        {
            view= LayoutInflater.from(getContext()).inflate(resourceid,parent,false);
            convertView=view;
            holder = new ViewHolder();
            holder.sectionName = view.findViewById(R.id.sectionName);
//            holder.sectionIntroduction = view.find
            convertView.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder)convertView.getTag();
        }
        holder.sectionName.setText(section.getSectionName());
        return convertView;
    }
}
