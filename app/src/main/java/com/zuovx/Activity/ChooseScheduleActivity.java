package com.zuovx.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Adapter.DoctorSchAdapter;
import com.zuovx.Model.Doctor;
import com.zuovx.Model.DoctorSche;
import com.zuovx.Model.Schedule;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;
import com.zuovx.Utils.AuthResult;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.LoadingDialog;
import com.zuovx.Utils.OrderInfoUtil2_0;
import com.zuovx.Utils.PayResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseScheduleActivity extends AppCompatActivity {

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(ChooseScheduleActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ChooseScheduleActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(ChooseScheduleActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(ChooseScheduleActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };




    private RequestQueue requestQueue;
    private LoadingDialog loadingDialog;
    private List<Doctor> doctors;
    private List<Schedule> schedules;
    private List<DoctorSche> doctorSches;
    private List<DoctorSche> searchDoctorSches;
    private ListView listView;
    private SearchView searchView;
    private Integer sectionId;
    private Toolbar toolbar;
    private Boolean isSearch = false;
//    private DoctorAdapter doctorAdapter;
    private DoctorSchAdapter doctorSchAdapter;
    private TextView textView;

    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_schedule);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        sectionId = intent.getIntExtra("sectionId",0);
        getDoctorBySectionId(sectionId);
        init();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        loadingDialog.close();
                        Toast.makeText(ChooseScheduleActivity.this,"预约成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChooseScheduleActivity.this,MyBookActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        loadingDialog.close();
                        Toast.makeText(ChooseScheduleActivity.this,"预约失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        loadingDialog.close();
                        Toast.makeText(ChooseScheduleActivity.this,"预约失败，已经预约过或者跟已预约的时间段冲突",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

    }
    public void init()
    {
        //返回键
        toolbar = findViewById(R.id.choose_schedule_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchView = (SearchView)findViewById(R.id.all_schedule_searchView);

        textView = (TextView)findViewById(R.id.nothingSchedule);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getApplicationContext(),newText, Toast.LENGTH_SHORT).show();
                searchDoctorSches = new ArrayList<>();
                for(int i=0;i<doctorSches.size();i++){
                    if(doctorSches.get(i).getDoctor().getName().contains(newText)
                            ||doctorSches.get(i).getSchedule()
                            .getWorkTimeStart().contains(newText)
                            ||doctorSches.get(i).getDoctor().getHonour().contains(newText)){
                        searchDoctorSches.add(doctorSches.get(i));
//                        System.out.println("添加一个"+doctors.get(i).getName());
                    }
                }
                if(searchDoctorSches.size()>0&&newText.length()>0){
                    doctorSchAdapter = new DoctorSchAdapter(getApplicationContext(),
                            R.layout.doctor_item,searchDoctorSches);
                    listView.setAdapter(doctorSchAdapter);
                    isSearch=true;

                }
                if(newText.length()==0)
                {
                    doctorSchAdapter = new DoctorSchAdapter(getApplicationContext(),
                            R.layout.doctor_item,doctorSches);
                    listView.setAdapter(doctorSchAdapter);
                    isSearch=false;
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                System.out.println("点击了医生");
                DoctorSche doctorSche =null;
                if(isSearch)
                {
                    doctorSche = searchDoctorSches.get(i);
                }else{
                    doctorSche = doctorSches.get(i);
                }
                final String did = doctorSche.getSchedule().getScheduleId()+"";
//                Intent intent = new Intent(BookActivity.this,ChooseDoctorActivity.class);
//                intent.putExtra("sectionId",section.getSectionId());
//                startActivity(intent);
                if(doctorSche.getSchedule().getRemainder()>0) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(ChooseScheduleActivity.this);
                    dialog.setTitle("确认预约吗？");
                    dialog.setMessage("预约须知：同一天你只能预约两次," +
                            "预约成功而不赴约的，超过两次则拉入黑名单，确认预约吗？。");
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            bookWithVolley(MainActivity.user.getAccount(), did);
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
                    Toast.makeText(getApplicationContext(), "已被预约满", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(getApplicationContext(),doctor.getName(),Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                payV2();
                return true;
            }
        });

    }
    public void getDoctorBySectionId(final Integer sectionId)
    {
        listView = (ListView)findViewById(R.id.all_schedule_list);
        loadingDialog = new LoadingDialog(this,"数据读取中...");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求

        StringRequest stringRequest =new StringRequest(GlobalVar.url +"user/getDoctorBySection?sectionId="+sectionId, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                try {
                    Gson gson = new Gson();
                    doctors = gson.fromJson(s, new TypeToken<List<Doctor>>() {}.getType());
//                    Log.d("doctorsBySectionId:",doctors.toString());
                    for(Doctor d:doctors){
                        System.out.println(d.getName()+d.getHonour());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadingDialog.close();
                getSchedule(sectionId);


            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                volley_result.setText("加载错误"+volleyError);
                loadingDialog.close();
//                textView.setText("网络错误");
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }

    public void getSchedule(Integer sectionId)
    {
        loadingDialog = new LoadingDialog(this,"数据读取中...");
        loadingDialog.show();
//        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求

        StringRequest stringRequest =new StringRequest(GlobalVar.url
                +"schedule/getScheduleBySectionId?sectionId="+sectionId, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                try {
                    Gson gson = new Gson();
                    schedules = gson.fromJson(s, new TypeToken<List<Schedule>>() {}.getType());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (schedules!=null&&schedules.size()>0){
                    doctorSches = new ArrayList<>();
                    for(Schedule sc :schedules){
                        if(sc.getIsCancle()){
                            continue;
                        }
                        for(Doctor d : doctors){
                            if(sc.getDoctorId()==d.getDoctorId()){
                                DoctorSche doctorSche = new DoctorSche();
                                doctorSche.setDoctor(d);
                                doctorSche.setSchedule(sc);
                                doctorSches.add(doctorSche);
                                break;
                            }
                        }
                    }
                    sortScheDoctor(doctorSches);
                    doctorSchAdapter = new DoctorSchAdapter(getApplicationContext(),
                            R.layout.doctor_item,doctorSches);
                    listView.setAdapter(doctorSchAdapter);
                }
                loadingDialog.close();
                if(doctorSches==null||doctorSches.size()==0)
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
                textView.setText("网络错误");
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }

    public void sortScheDoctor(List<DoctorSche> ds){
        Collections.sort(ds, new Comparator<DoctorSche>() {
            @Override
            public int compare(DoctorSche o1, DoctorSche o2) {
                // TODO Auto-generated method stub
                Schedule s1,s2;
                s1 = o1.getSchedule();
                s2 = o2.getSchedule();
                long p1,p2;
                p1 = 0;
                p2 = 0;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    p1 = format.parse(s1.getWorkTimeStart()).getTime();
                    p2 = format.parse(s2.getWorkTimeStart()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                p1 = p1 +s1.getW();
                p2 = p2 +s2.getW();
                if(p1>p2){
                    return 1;
                }else if(p1<p2){
                    return -1;
                }
                return 0;

            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
//                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ChooseScheduleActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * 使用volley框架进行登录请求，并返回用户基本信息存于MainActivity的user中
     * @param account
     */
    private void bookWithVolley(final String account, final String scheduleId)
    {
        loadingDialog = new LoadingDialog(this,"预约中...");
        loadingDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                GlobalVar.url + "book/createBook",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s!=null&&s.equals("1"))
                        {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        }else if(s!=null&&s.equals("2")){
                            Message message = new Message();
                            message.what = 3;
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
                map.put("account", account);
                map.put("scheduleId", scheduleId);
                return map;
            }

        };

        requestQueue.add(stringRequest);
    }
}
