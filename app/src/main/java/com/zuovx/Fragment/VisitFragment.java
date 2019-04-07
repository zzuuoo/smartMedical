package com.zuovx.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zuovx.Activity.BookActivity;
import com.zuovx.Activity.MyBookActivity;
import com.zuovx.R;

/**
 * 预约管理、预约增查删改
 * 功能：
 * 智能导诊
 * 预约挂号
 * 我的挂号
 * 就诊指引
 */
public class VisitFragment extends Fragment implements View.OnClickListener {

    private Button guide,appointment,mart,myregistration;

    public VisitFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_visit, container, false);
        myregistration = (Button) v.findViewById(R.id.myregistration);
        myregistration.setOnClickListener(this);
        guide = (Button)v.findViewById(R.id.guide);
        guide.setOnClickListener(this);
        appointment = (Button)v.findViewById(R.id.appointment);
        appointment.setOnClickListener(this);
        mart = (Button)v.findViewById(R.id.mart);
        mart.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.myregistration:
                //我的挂号
//                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//                dialog.setTitle("我的挂号");
//                dialog.setMessage("待完成");
//                dialog.setCancelable(true);
//                dialog.show();
                Intent intentm = new Intent(getActivity(),MyBookActivity.class);
                startActivity(intentm);
                break;
            case R.id.guide:
                //预约指引
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(getContext());
                dialog1.setTitle("预约指引");
                dialog1.setMessage("待完成");
                dialog1.setCancelable(true);
                dialog1.show();
                break;
            case R.id.mart:
                //智能导诊
                AlertDialog.Builder dialog12 = new AlertDialog.Builder(getContext());
                dialog12.setTitle("智能导诊");
                dialog12.setMessage("待完成");
                dialog12.setCancelable(true);
                dialog12.show();
                break;
            case R.id.appointment:
                //预约挂号
                Intent intent  = new Intent(getContext(),BookActivity.class);
                startActivity(intent);
//                AlertDialog.Builder dialog122 = new AlertDialog.Builder(getContext());
//                dialog122.setTitle("预约挂号");
//                dialog122.setMessage("待完成");
//                dialog122.setCancelable(true);
//                dialog122.show();
                break;
                default:
                    break;
        }
    }



}
