package com.zuovx.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Adapter.BookAdapter;
import com.zuovx.Model.Book;
import com.zuovx.Model.BookDocSche;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBookActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private SearchView searchView;
    private Toolbar toolbar;
    private Boolean isSearch = false;
    private RequestQueue requestQueue;
    private LoadingDialog loadingDialog;
    private List<BookDocSche> books,searchBooks;
    private TextView textView;
    private BookAdapter bookAdapter;
    private Button addBook;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book);
        ActivityCollector.addActivity(this);
        init();
        getBookSD();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        loadingDialog.close();
                        Toast.makeText(MyBookActivity.this,"取消成功",Toast.LENGTH_SHORT).show();
                        getBookSD();
                        break;
                    case 2:
                        loadingDialog.close();
                        Toast.makeText(MyBookActivity.this,"取消失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        break;
                }
            }
        };
    }
    public void init(){
        listView = findViewById(R.id.mybook_list);
        searchView = findViewById(R.id.mybookSearchView);
        textView = findViewById(R.id.nothingmyBook);

        addBook = findViewById(R.id.mybook_add);
        addBook.setOnClickListener(this);
        //返回键
        toolbar = findViewById(R.id.mybook_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
              return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchBooks = new ArrayList<>();
                for(int i=0;i<books.size();i++){
                    if(books.get(i).getBook().getBookTime().contains(s)
                            ||books.get(i).getDoctor().getName().contains(s)
                            ||books.get(i).getSchedule().getWorkTimeStart().contains(s)
                            ||books.get(i).getDoctor().getHonour().contains(s)){
                        searchBooks.add(books.get(i));
//                        System.out.println("添加一个"+doctors.get(i).getName());
                    }
                }
                if(searchBooks.size()>0&&s.length()>0){
                    bookAdapter = new BookAdapter(getApplicationContext(),
                           R.layout.mybook_item,searchBooks);
                   listView.setAdapter(bookAdapter);
                    isSearch=true;

                }else {
                    Toast.makeText(getApplicationContext(),"没有你要找的",Toast.LENGTH_SHORT).show();
                }

                if(s.length()==0)
                {
                    bookAdapter = new BookAdapter(getApplicationContext(),
                            R.layout.mybook_item,books);
                    listView.setAdapter(bookAdapter);
                    isSearch=false;
                }
                return false;
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookDocSche bookDocSche;
                if(isSearch){
                    bookDocSche=searchBooks.get(i);
                }else {
                    bookDocSche=books.get(i);
                }
                final Book book = bookDocSche.getBook();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                Date bd=null;
                try {
                    bd = simpleDateFormat.parse(bookDocSche.getSchedule().getWorkTimeStart());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println(bookDocSche.getSchedule().getWorkTimeStart());
                if(bd!=null){
                    System.out.println(bd.getTime());
                    if(bd.getTime()>new Date().getTime()&&book.isAvaliablity()){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MyBookActivity.this);
                        dialog.setTitle("警告");
                        dialog.setMessage("确认取消吗？");
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cancleBook(book);
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.show();
                    }else{
                        Toast.makeText(MyBookActivity.this,"已失效,不可取消",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MyBookActivity.this,"已失效,不可取消",Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public void cancleBook(final Book book){
        loadingDialog = new LoadingDialog(this,"取消中...");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                GlobalVar.url + "book/cancleBook",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s!=null&&s.equals("1"))
                        {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }else{
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                        }



                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("bookId", book.getBookId()+"");
                map.put("isAvaliablity", "0");
                map.put("isCancle", "1");
                map.put("scheduleId", book.getScheduleId()+"");
                return map;
            }

        };

        requestQueue.add(stringRequest);
    }

    public void getBookSD()
    {
        loadingDialog = new LoadingDialog(this,"数据读取中...");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求

        StringRequest stringRequest =new StringRequest(GlobalVar.url
                +"book/showBookByAccount?account="+MainActivity.user.getAccount(), new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                loadingDialog.close();
                try {
                    Gson gson = new Gson();
                    books = gson.fromJson(s, new TypeToken<List<BookDocSche>>() {}.getType());

                } catch (Exception e) {
                    e.printStackTrace();
                }

               if(books==null||books.size()==0)
                {
                    textView.setVisibility(View.VISIBLE);
                }else{
                    textView.setVisibility(View.GONE);
                   bookAdapter = new BookAdapter(getApplicationContext(),
                           R.layout.mybook_item,books);
                   listView.setAdapter(bookAdapter);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mybook_add:
                Intent intent  = new Intent(MyBookActivity.this,BookActivity.class);
                startActivity(intent);
                break;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
