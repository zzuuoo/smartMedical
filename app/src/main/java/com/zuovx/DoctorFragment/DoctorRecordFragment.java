package com.zuovx.DoctorFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Activity.MainActivity;
import com.zuovx.Activity.ShowPatientRecordActivity;
import com.zuovx.Adapter.PatientRecordAdapter;
import com.zuovx.Model.PPatientRecord;
import com.zuovx.R;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 病历管理，病历增查删改
 * 功能：
 * 我的病历
 * 检验检查
 * 健康咨讯
 * 使用说明
 *
 */
public class DoctorRecordFragment extends Fragment {

    private ListView listView;
    private SearchView medical_record_searchview;
    private PatientRecordAdapter adapter;
    private List<PPatientRecord> list,searchlist;
    private TextView textView;
    private LoadingDialog loadingDialog;
    private RequestQueue requestQueue;
    private Handler handler;
    private boolean isSearch = false;
//    private List<PPatientRecord> list1;

    public DoctorRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onResume() {
        super.onResume();
        getPatientRecord();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_record,
                container, false);
        init(view);
        return view;
    }

    /**
     * listview内容适配器
     * 监听
     */
    public void init(View view)
    {
        listView = view.findViewById(R.id.medical_record_list);
        textView=(TextView)view.findViewById(R.id.nothingMedicalRcord);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PPatientRecord pPatientRecord;
                if(isSearch){
                    pPatientRecord = searchlist.get(i);
                }else{
                    pPatientRecord = list.get(i);
                }
                Intent intent = new Intent(getActivity(),ShowPatientRecordActivity.class);
                intent.putExtra("PPatientRecord",pPatientRecord);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               PPatientRecord pPatientRecord;
                if(isSearch){
                    pPatientRecord = searchlist.get(i);
                }else{
                    pPatientRecord = list.get(i);
                }
//                AlertDialog.Builder dialog = new AlertDialog.Builder(PlayManageActivity.this);
//                dialog.setTitle("警告");
//                dialog.setMessage("确认删除吗？");
//                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
////                        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
////                        sqLiteDatabase.delete("play","play_id  =  ?",new String[]{String.valueOf(p.getPlay_id())});
////                        Toast.makeText(getApplicationContext(), "删除成功"+p.getPlay_id(), Toast.LENGTH_SHORT).show();
////                        setData();
////                        adapter=new PlayAdapter(getApplicationContext(),R.layout.play_item,list);
////                        listView.setAdapter(adapter);
//                        delete(p.getPlay_id());
//                    }
//                });
//                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                dialog.show();
                return true;
            }
        });
//        play_toolbar = findViewById(R.id.play_toolbar);
//        play_toolbar.setTitle("");
//        setSupportActionBar(play_toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        play_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        play_add = findViewById(R.id.add_play);
//
//        play_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(PlayManageActivity.this, AddPlayActivity.class);
//                startActivityForResult(intent,1);//到时候重写那个返回调用函数
//            }
//        });

        medical_record_searchview = view.findViewById(R.id.medical_recordSearchView);

        medical_record_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getApplicationContext(),newText, Toast.LENGTH_SHORT).show();
                searchlist = new ArrayList<PPatientRecord>();
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getPatientRecord().getAdmissionTime().contains(newText)
                            ||list.get(i).getPatient().getAccount().contains(newText)
                            ||list.get(i).getPatient().getName().contains(newText)){
                        searchlist.add(list.get(i));
                    }
                }
                if(searchlist.size()>0&&newText.length()>0){
                    adapter = new PatientRecordAdapter(getContext(),R.layout.patient_record_item,searchlist);
                    listView.setAdapter(adapter);
                    isSearch=true;

                }
                if(newText.length()==0)
                {
                    adapter = new PatientRecordAdapter(getContext(),R.layout.patient_record_item,list);
                    listView.setAdapter(adapter);
                    isSearch=false;
                }
                return false;
            }
        });

    }
//    public void getRecord(){
//        //创建一个请求队列
//
//        loadingDialog = new LoadingDialog(getActivity(),"数据读取中");
//        loadingDialog.show();
//        requestQueue = Volley.newRequestQueue(getActivity());
//        //创建一个请求
//
//        StringRequest stringRequest =new StringRequest(GlobalVar.url +"medicalRecord/getBookPatientByDoctorAccount?account="+MainActivity.user.getAccount(), new Response.Listener<String>() {
//            //正确接收数据回调
//            @Override
//            public void onResponse(String s) {
//                try {
//                    System.out.println(s);
//                    Gson gson = new Gson();
//                    list = gson.fromJson(s, new TypeToken<List<PPatientRecord>>() {}.getType());
//
//                    Log.d("PatientRecord:",list.toString());
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                loadingDialog.close();
//                if(list==null||list.size()==0)
//                {
//                    textView.setVisibility(View.VISIBLE);
//                }else{
//                    textView.setVisibility(View.GONE);
//                    PatientRecordAdapter adapter =
//                            new PatientRecordAdapter(getActivity(),
//                                    R.layout.patient_record_item,list);
//                    listView.setAdapter(adapter);
//                }
//
//
//            }
//        }, new Response.ErrorListener() {//异常后的监听数据
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
////                volley_result.setText("加载错误"+volleyError);
//                loadingDialog.close();
//                textView.setVisibility(View.VISIBLE);
//            }
//        });
//        //将get请求添加到队列中
//        requestQueue.add(stringRequest);
//    }
    private void getPatientRecord()  {

        loadingDialog = new LoadingDialog(getContext(),"加载中,,,");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                GlobalVar.url + "medicalRecord/getBookPatientByDoctorAccount",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loadingDialog.close();
                        try {
                            Gson gson = new Gson();
                            list = gson.fromJson(s, new TypeToken<List<PPatientRecord>>() {}.getType());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(list!=null&&list.size()>0){
                            adapter = new PatientRecordAdapter(getContext(),R.layout.patient_record_item,list);
                            listView.setAdapter(adapter);
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loadingDialog.close();
//                Message message1 = new Message();
//                message1.what = 4;
//                handler.sendMessage(message1);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
//                map.put("patientId",String.valueOf(bookPatientSche.getPatient().getPatientId()));
//                map.put("doctorId",String.valueOf(bookPatientSche.getSchedule().getDoctorId()));
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                String date = bookPatientSche.getSchedule().getWorkTimeStart();
//                Date d = new Date();
//                try {
//                    d = format.parse(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                map.put("account",MainActivity.user.getAccount());
                return map;
            }

        };

        requestQueue.add(stringRequest);
    }

}
