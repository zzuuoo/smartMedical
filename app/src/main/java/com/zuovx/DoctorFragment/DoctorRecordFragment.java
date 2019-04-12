package com.zuovx.DoctorFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.zuovx.Adapter.PatientRecordAdapter;
import com.zuovx.Model.PatientRecord;
import com.zuovx.R;

import java.util.ArrayList;
import java.util.List;

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
    private List<PatientRecord> list = new ArrayList<>();
    private TextView textView;

    public DoctorRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        PatientRecord m = new PatientRecord();
//        m.setAdmissionTime(new Date().getTime());
//        m.setChief("woshizhu数");
//        list.add(m);
//        PatientRecord m1 = new PatientRecord();
//        m1.setAdmissionTime(new Date().getTime());
//        m1.setChief("woshizhu");
//        list.add(m1);

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
        adapter = new PatientRecordAdapter(view.getContext(),R.layout.patient_record_item,list);
        listView = view.findViewById(R.id.medical_record_list);
        textView=(TextView)view.findViewById(R.id.nothingMedicalRcord);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Play p;
//                if(isSearch){
//                    p = searchlist.get(i);
//                }else{
//                    p = list.get(i);
//                }
//                Intent intent = new Intent(PlayManageActivity.this, PlayEditActivity.class);
//                intent.putExtra("play", p);
//                startActivityForResult(intent,2);//到时候重写那个返回调用函数
//                Toast.makeText(getApplication(),p.getPlay_name(),Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Play p;
//                if(isSearch){
//                    p = searchlist.get(i);
//                }else{
//                    p = list.get(i);
//                }
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
//                searchlist = new ArrayList<Play>();
//                for(int i=0;i<list.size();i++){
//                    if(list.get(i).getPlay_name().contains(newText)){
//                        searchlist.add(list.get(i));
//                    }
//                }
//                if(searchlist.size()>0&&newText.length()>0){
//                    adapter = new PlayAdapter(MyApplication.getContext(),R.layout.play_item,searchlist);
//                    listView.setAdapter(adapter);
//                    isSearch=true;
//
//                }
//                if(newText.length()==0)
//                {
//                    adapter = new PlayAdapter(MyApplication.getContext(),R.layout.play_item,list);
//                    listView.setAdapter(adapter);
//                    isSearch=false;
//                }
                return false;
            }
        });

    }

}
