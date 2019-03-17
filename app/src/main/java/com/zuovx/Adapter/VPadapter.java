package com.zuovx.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zuovx.R;

import java.util.ArrayList;

/**
 * Created by zuo on 2018.12.12.
 */

public class VPadapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list;
    private ArrayList<String> title;
    private ArrayList<Integer> imageList;
    private Context context;
    public VPadapter(FragmentManager fm, ArrayList list, ArrayList title, ArrayList pic, Context context) {
        super(fm);
        this.list=list;
        this.title=title;
        this.context=context;
        imageList=pic;
    }



    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }



    public View getTabView(int position)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.tab_item,null,false);
        TextView titl= view.findViewById(R.id.tab_title);
        titl.setText(title.get(position));
        ImageView imageView= view.findViewById(R.id.tab_icon);
        imageView.setImageResource(imageList.get(position));
        return view;
    }
}
