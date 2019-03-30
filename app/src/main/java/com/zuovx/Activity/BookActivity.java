package com.zuovx.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.zuovx.Adapter.SectionAdapter;
import com.zuovx.Model.Section;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 预约
 */
public class BookActivity extends AppCompatActivity {

    private List<Section> sections ;
    private List<Section> searchlist;
    private RequestQueue requestQueue;
    private LoadingDialog loadingDialog;
    private ListView listView;
    private SearchView searchView;
    private Boolean isSearch = false;
    private SectionAdapter sectionAdapter;
    private TextView textView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ActivityCollector.addActivity(this);
        getSection();
        init();

    }
    public void init()
    {

        //返回键
        toolbar = findViewById(R.id.book_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchView = (SearchView)findViewById(R.id.bookSearchView);

        textView = (TextView)findViewById(R.id.nothingBook);
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
                    sectionAdapter = new SectionAdapter(getApplicationContext(),
                            R.layout.book_item,searchlist);
                    listView.setAdapter(sectionAdapter);
                    isSearch=true;

                }
                if(newText.length()==0)
                {
                    sectionAdapter = new SectionAdapter(getApplicationContext(),
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
                Intent intent = new Intent(BookActivity.this,ChooseScheduleActivity.class);
                intent.putExtra("sectionId",section.getSectionId());
                startActivity(intent);
//                Toast.makeText(getApplicationContext(),section.getSectionName(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getSection(){
        //创建一个请求队列

        listView = (ListView)findViewById(R.id.book_section_list);
        loadingDialog = new LoadingDialog(this,"数据读取中");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
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
                sectionAdapter = new SectionAdapter(getApplicationContext(),
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
