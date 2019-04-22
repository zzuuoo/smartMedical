package com.zuovx.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zuovx.Activity.EditMyselfctivity;
import com.zuovx.Activity.LoginActivity;
import com.zuovx.Activity.MainActivity;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;

/**
 * 功能：
 * 建卡绑卡
 * 就医反馈
 * 我的账单
 * 我的排队
 * 门诊充值
 */

public class PersonalFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView userNickName;
    private ImageView head_image;
    private RelativeLayout personalData;
    private ListMenuItemView userOrder;
    private ListMenuItemView logout;
    private ListMenuItemView outpatientRecharge;
    private ListMenuItemView medicalFeedBack;
    private ListMenuItemView myqueue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_personal,container,false);
        userNickName= view.findViewById(R.id.me_layout_name_tips);
        head_image = view.findViewById(R.id.me_layout_head_image);
        personalData = view.findViewById(R.id.personalData);
        outpatientRecharge = view.findViewById(R.id.outpatientRecharge);
        medicalFeedBack = view.findViewById(R.id.Medical_feedback);
        myqueue = view.findViewById(R.id.MyQueue);
        myqueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("我的就医队列");
                dialog.setMessage("待完成");
                dialog.setCancelable(true);
                dialog.show();
            }
        });
        medicalFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("就医反馈");
                dialog.setMessage("请把问题发送至邮箱：zuovx@foxmail.com");
                dialog.setCancelable(true);
                dialog.show();
            }
        });
        outpatientRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("门诊充值");
                dialog.setMessage("待完成");
                dialog.setCancelable(true);
                dialog.show();

//                Intent intent = new Intent(getContext(), TestActivity.class);
//                startActivity(intent);
            }
        });
        logout = view.findViewById(R.id.me_layout_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.isLogin)
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("警告");
                    dialog.setMessage("确认注销吗？");
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.isLogin=false;
                            MainActivity.user=null;
                            userNickName.setText("点击登录");
                            head_image.setImageResource(R.drawable.home_people);
//                            userNickName.setClickable(true);
//                            head_image.setClickable(true);
                            getActivity().finish();
                            startActivity(new Intent(getContext(), LoginActivity.class));

                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getContext(), "取消", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();


                }else{
                    Toast.makeText(getContext(), "还没登录，点击头像即可", Toast.LENGTH_SHORT).show();
                }
            }
        });
        userOrder= view.findViewById(R.id.MyOrder);
        userOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.isLogin)
                {
                    Toast.makeText(getContext(),"未登录，请先登录",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getContext(),"订单信息",Toast.LENGTH_LONG).show();
                }
//                Toast.makeText(getContext(),"订单信息",Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getContext(), ShowSaleActivity.class));
            }
        });
        personalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.isLogin)
                {
//                    getActivity().finish();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }else{
                    startActivity(new Intent(getActivity(),EditMyselfctivity.class));
                    //此处应该跳进个人资料，然后可以修改、完善个人资料
//                    Toast.makeText(getContext(),"进入个人资料",Toast.LENGTH_LONG).show();
                }
            }
        });
        view.findViewById(R.id.me_layout_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("警告");
                dialog.setMessage("确认退出吗？");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.isLogin=false;
//                        MainActivity.user=null;
                        userNickName.setText("点击登录");
                        head_image.setImageResource(R.drawable.home_people);
                        userNickName.setClickable(true);
                        head_image.setClickable(true);
                        getActivity().finish();
                        ActivityCollector.quitAll();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if(MainActivity.user!=null&&MainActivity.isLogin)
        {

            userNickName.setText(MainActivity.user.getAccount());
            head_image.setImageResource(R.drawable.logo_login);
        }


    }

    @Override
    public void onClick(View view) {

    }

}
