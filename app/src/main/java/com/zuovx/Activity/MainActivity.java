package com.zuovx.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.zuovx.Adapter.VPadapter;
import com.zuovx.AdminFragment.AdminMainFragment;
import com.zuovx.AdminFragment.AdminPersionalFragment;
import com.zuovx.AdminFragment.AdminScheduleFragment;
import com.zuovx.DoctorFragment.DoctorMainFragment;
import com.zuovx.DoctorFragment.DoctorPersionalFragment;
import com.zuovx.DoctorFragment.DoctorRecordFragment;
import com.zuovx.Fragment.PatientRecordFragment;
import com.zuovx.Fragment.PersonalFragment;
import com.zuovx.Fragment.VisitFragment;
import com.zuovx.Model.User;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    private ArrayList<Fragment> fragmentArrayList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Integer> piclist;
    private ArrayList<String> title;
    public static boolean isLogin = false;
    public static final int PATIENT = 1;
    public static final int DOCTOR = 2;
    public static final int MANAGER = 0;
    //    public static int role = -1;
    public static User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        title = new ArrayList<>();
        piclist = new ArrayList<>();
        if(user!=null) {
            if (user.getStatus() == 1) {
                title.add("就诊");
                title.add("病历");
                title.add("个人信息");
                piclist.add(R.mipmap.tab_movie_a);
                piclist.add(R.mipmap.cinema);
                piclist.add(R.mipmap.personal);

            } else if (user.getStatus() == 0) {
                title.add("值班管理");
                title.add("医生管理");
                title.add("个人信息");
                piclist.add(R.mipmap.tab_movie_a);
                piclist.add(R.mipmap.cinema);
                piclist.add(R.mipmap.personal);
            } else {
                title.add("处理预约");
                title.add("患者病历");
                title.add("个人信息");
                piclist.add(R.mipmap.tab_movie_a);
                piclist.add(R.mipmap.cinema);
                piclist.add(R.mipmap.personal);
            }
        }
        fragmentArrayList = new ArrayList<>();

        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        viewPager = findViewById(R.id.viewpager);
        initViewPager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.quitAll();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                ActivityCollector.quitAll();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initViewPager()
    {
        if(fragmentArrayList!=null)
        {
            fragmentArrayList.clear();
        }
        if (user == null || user.getStatus() == 1) {
            fragmentArrayList.add(new VisitFragment());
            fragmentArrayList.add(new PatientRecordFragment());
            fragmentArrayList.add(new PersonalFragment());
        } else if (user.getStatus() == 0) {
            fragmentArrayList.add(new AdminScheduleFragment());
            fragmentArrayList.add(new AdminMainFragment());
            fragmentArrayList.add(new AdminPersionalFragment());
        } else {
            fragmentArrayList.add(new DoctorMainFragment());
            fragmentArrayList.add(new DoctorRecordFragment());
            fragmentArrayList.add(new DoctorPersionalFragment());

        }

        VPadapter vPadapter = new VPadapter(getSupportFragmentManager(), fragmentArrayList, title, piclist, this);
        viewPager.setAdapter(vPadapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(vPadapter.getTabView(i));
        }
    }
}