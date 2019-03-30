//package com.zuovx.DAO;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.View;
//
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.zuovx.Activity.AddDoctorActivity;
//import com.zuovx.Model.Section;
//import com.zuovx.Utils.GlobalVar;
//
//import java.util.List;
//
//public class AllDao {
//    public RequestQueue requestQueue;
//
//    public void getSection(Context context){
//
//        requestQueue = Volley.newRequestQueue(context);
//        //创建一个请求
//
//        StringRequest stringRequest =new StringRequest(GlobalVar.url +"section/getAllSection", new Response.Listener<String>() {
//            //正确接收数据回调
//            @Override
//            public void onResponse(String s) {
//                try {
//                    Gson gson = new Gson();
//                    sections = gson.fromJson(s, new TypeToken<List<Section>>() {}.getType());
//
//                    Log.d("sections:",sections.toString());
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                loadingDialog.close();
////                sectionAdapter = new SectionAdapter(getActivity(),
////                        R.layout.book_item,sections);
////                listView.setAdapter(sectionAdapter);
////                if(sections==null||sections.size()==0)
////                {
////                    textView.setVisibility(View.VISIBLE);
////                }else{
////                    textView.setVisibility(View.GONE);
////                }
//
//            }
//        }, new Response.ErrorListener() {//异常后的监听数据
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
////                volley_result.setText("加载错误"+volleyError);
//                loadingDialog.close();
////                textView.setVisibility(View.VISIBLE);
//            }
//        });
//        //将get请求添加到队列中
//        requestQueue.add(stringRequest);
//    }
//}
