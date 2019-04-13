package com.zuovx.DoctorFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Activity.DoctorBookActivity;
import com.zuovx.Activity.MainActivity;
import com.zuovx.Adapter.ScheduleAdapter;
import com.zuovx.Model.Doctor;
import com.zuovx.Model.DoctorSche;
import com.zuovx.Model.Schedule;
import com.zuovx.R;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 功能：
 * 个人值班管理
 */
public class DoctorMainFragment extends Fragment implements View.OnClickListener {

    private List<Schedule> schedules ;
    private List<Doctor> doctors;
    private RequestQueue requestQueue;
    //    private LoadingDialog loadingDialog;
    private ListView listView;
    private SearchView searchView;
    private Boolean isSearch = false;
    private ScheduleAdapter scheduleAdapter;
    private TextView textView;
    private Button addSchedule;
    private Handler handler;

    private List<DoctorSche> doctorSches;
    private List<DoctorSche> doctorSchesSeach;

    LoadingDialog dialog;

    public DoctorMainFragment() {
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
        View v = inflater.inflate(R.layout.fragment_doctor_main, container, false);
        init(v);
        getSchedule();
        return v;
    }
    public void init(View view){
        listView = (ListView)view.findViewById(R.id.doctor_schedule_list);
//        addSchedule = (Button)view.findViewById(R.id.doctor_add_schedule);
//        addSchedule.setOnClickListener(this);
        searchView = (SearchView)view.findViewById(R.id.doctor_schedule_searchView);

        textView = (TextView)view.findViewById(R.id.nothing_doctor_schedule);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getApplicationContext(),newText, Toast.LENGTH_SHORT).show();
                doctorSchesSeach = new ArrayList<>();
                if(doctorSches==null){
                    return false;
                }
                for(int i=0;i<doctorSches.size();i++){
                    if(doctorSches.get(i).getSchedule().getWorkTimeStart().contains(newText)||
                            doctorSches.get(i).getDoctor().getName().contains(newText)){
                        doctorSchesSeach.add(doctorSches.get(i));
                    }
                }
                if(doctorSchesSeach.size()>0&&newText.length()>0){
                    scheduleAdapter = new ScheduleAdapter(getActivity(),
                            R.layout.schedule_item,doctorSchesSeach);
                    listView.setAdapter(scheduleAdapter);
                    isSearch=true;

                }
                if(newText.length()==0)
                {
                    scheduleAdapter = new ScheduleAdapter(getActivity(),
                            R.layout.schedule_item,doctorSches);
                    listView.setAdapter(scheduleAdapter);
                    isSearch=false;
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DoctorSche doctorSche =null;
                if(isSearch)
                {
                    doctorSche = doctorSchesSeach.get(i);
                }else{
                    doctorSche = doctorSches.get(i);
                }
//                deleteSchedule(doctorSche.getSchedule().getScheduleId());
                Intent intent = new Intent(getActivity(),DoctorBookActivity.class);
                intent.putExtra("scheduleId",doctorSche.getSchedule().getScheduleId());
                intent.putExtra("doctorId",doctorSche.getDoctor().getDoctorId());
                intent.putExtra("schedule",doctorSche.getSchedule());
                startActivity(intent);
//                Toast.makeText(getApplicationContext(),section.getSectionName(),Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //删除
                DoctorSche doctorSche =null;
                if(isSearch)
                {
                    doctorSche = doctorSchesSeach.get(i);
                }else{
                    doctorSche = doctorSches.get(i);
                }
                final int id = doctorSche.getSchedule().getScheduleId();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("警告");
                dialog.setMessage("确认删除吗？请慎重，会影响用户挂号订单！！！");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        deleteSchedule(id);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
//                deleteSchedule(doctorSche.getSchedule().getScheduleId());
                return true;
            }
        });
    }
    public void getSchedule(){
        //创建一个请求队列

//        loadingDialog = new LoadingDialog(getActivity(),"数据读取中");
//        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(getActivity());
        //创建一个请求

        StringRequest stringRequest =new StringRequest(GlobalVar.url +"schedule/getScheduleByDoctorAccount?account="+MainActivity.user.getAccount(), new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                try {
                    Gson gson = new Gson();
                    schedules = new ArrayList<>();
                    schedules = gson.fromJson(s, new TypeToken<List<Schedule>>() {}.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                loadingDialog.close();
//                Message message = new Message();
//                message.what=1;
//                handler.sendMessage(message);
//                sectionAdapter = new SectionAdapter(getActivity(),
//                        R.layout.book_item,schedules);
//                listView.setAdapter(sectionAdapter);
                if(schedules==null||schedules.size()==0)
                {
                    textView.setVisibility(View.VISIBLE);
                }else{
                    textView.setVisibility(View.GONE);
                    getDoctor();
                }

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
////                volley_result.setText("加载错误"+volleyError);
//                Message message = new Message();
//                message.what=1;
//                handler.sendMessage(message);
                textView.setVisibility(View.VISIBLE);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }
    public void getDoctor(){
//        loadingDialog = new LoadingDialog(getActivity(),"数据读取中");
//        loadingDialog.show();
//        requestQueue = Volley.newRequestQueue(getActivity());
        //创建一个请求
        doctorSches= new ArrayList<>();

        StringRequest stringRequest =new StringRequest(GlobalVar.url +"user/getDoctorByAccount?account="+MainActivity.user.getAccount(), new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                Doctor d = new Doctor();
                try {
                    Gson gson = new Gson();
//                    doctors = new ArrayList<>();
//                    doctors =
                     d  = gson.fromJson(s, new TypeToken<Doctor>() {}.getType());

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                loadingDialog.close();
//                sectionAdapter = new SectionAdapter(getActivity(),
//                        R.layout.book_item,schedules);
//                listView.setAdapter(sectionAdapter);
                if(schedules==null||schedules.size()==0)
                {
                    textView.setVisibility(View.VISIBLE);
                }else{
                    textView.setVisibility(View.GONE);
                    doctorSches.clear();
                    for(Schedule schedule:schedules){
                        DoctorSche doctorSche = new DoctorSche();
                        doctorSche.setSchedule(schedule);
                        doctorSche.setDoctor(d);
                        doctorSches.add(doctorSche);

                    }
                    scheduleAdapter = new ScheduleAdapter(getActivity(),R.layout.schedule_item,doctorSches);
                    listView.setAdapter(scheduleAdapter);
                }

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                volley_result.setText("加载错误"+volleyError);
//                loadingDialog.close();
                textView.setVisibility(View.VISIBLE);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
                default:
                    break;
        }
    }



}
