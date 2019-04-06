package com.zuovx.AdminFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Activity.DoctorManagerActivity;
import com.zuovx.Adapter.SectionAdapter;
import com.zuovx.Model.Section;
import com.zuovx.R;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 功能：
 * 科室和 医生的增查删改
 * 若删除科室，对应科室的医生也一并删除
 *
 */
public class AdminMainFragment extends Fragment implements View.OnClickListener {

    private List<Section> sections ;
    private List<Section> searchlist;
    private RequestQueue requestQueue;
    private LoadingDialog loadingDialog;
    private ListView listView;
    private SearchView searchView;
    private Boolean isSearch = false;
    private SectionAdapter sectionAdapter;
    private TextView textView;
//    private Button addSection;

    public AdminMainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void init(View view){

//        addSection = (Button)view.findViewById(R.id.admin_add_section);
//        addSection.setOnClickListener(this);
        searchView = (SearchView)view.findViewById(R.id.adminSectionSearchView);

        textView = (TextView)view.findViewById(R.id.nothingAdminSection);
        searchlist  = new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getApplicationContext(),newText, Toast.LENGTH_SHORT).show();
                searchlist = new ArrayList<Section>();
                for(int i=0;i<sections.size();i++){
                    if(sections.get(i).getSectionName().contains(newText)){
                        searchlist.add(sections.get(i));
                    }
                }
                if(searchlist.size()>0&&newText.length()>0){
                    sectionAdapter = new SectionAdapter(getActivity(),
                            R.layout.book_item,searchlist);
                    listView.setAdapter(sectionAdapter);
                    isSearch=true;

                }
                if(newText.length()==0)
                {
                    sectionAdapter = new SectionAdapter(getActivity(),
                            R.layout.book_item,sections);
                    listView.setAdapter(sectionAdapter);
                    isSearch=false;
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Section section =null;
                if(isSearch)
                {
                    section = searchlist.get(i);
                }else{
                    section = sections.get(i);
                }
                Intent intent = new Intent(getActivity(),DoctorManagerActivity.class);
                intent.putExtra("sectionId",section.getSectionId());
                startActivity(intent);
//                Toast.makeText(getApplicationContext(),section.getSectionName(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin, container, false);
        getSection(v);
        init(v);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
//            case R.id.admin_add_section:
//                //我的挂号
//                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//                dialog.setTitle("添加section");
//                dialog.setMessage("待完成");
//                dialog.setCancelable(true);
//                dialog.show();
//                break;

            default:
                break;
        }
    }
    public void getSection(View view){
        //创建一个请求队列

        listView = (ListView)view.findViewById(R.id.admin_section_list);
        loadingDialog = new LoadingDialog(getActivity(),"数据读取中");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(getActivity());
        //创建一个请求

        StringRequest stringRequest =new StringRequest(GlobalVar.url +"section/getAllSection", new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                try {
                    Gson gson = new Gson();
                    sections = gson.fromJson(s, new TypeToken<List<Section>>() {}.getType());

                    Log.d("sections:",sections.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadingDialog.close();
                sectionAdapter = new SectionAdapter(getActivity(),
                        R.layout.book_item,sections);
                listView.setAdapter(sectionAdapter);
                if(sections==null||sections.size()==0)
                {
                    textView.setVisibility(View.VISIBLE);
                }else{
                    textView.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                volley_result.setText("加载错误"+volleyError);
                loadingDialog.close();
                textView.setVisibility(View.VISIBLE);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }


}
