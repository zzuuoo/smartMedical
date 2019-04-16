package com.zuovx.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;
import com.zuovx.Utils.CountDownTimerUtils;
import com.zuovx.Utils.Regex;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity
        implements View.OnClickListener {


    private Button sendVeri,resSure,resCancle;
    private EditText phone,name,id,code;
    EventHandler eventHandler;
    private static final String country = "86";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityCollector.addActivity(this);
        init();
        messageManage(phone.getText().toString(),country);

    }


    private void messageManage(String phone,String country)
    {
        // 在尝试读取通信录时以弹窗提示用户（可选功能）
        SMSSDK.setAskPermisionOnReadContact(true);

        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                new Handler(Looper.getMainLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        int event = msg.arg1;
                        int result = msg.arg2;
                        Object data = msg.obj;
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理成功得到验证码的结果
                                // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                            } else {
                                // TODO 处理错误的结果
                                ((Throwable) data).printStackTrace();
                            }
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理验证码验证通过的结果
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                startActivity(intent);
                                ActivityCollector.removeActivity(RegisterActivity.this);
                                finish();
//                                 Toast.makeText(RegisterActivity.this,"成功，进入信息完善界面",Toast.LENGTH_SHORT).show();
                            } else {
                                // TODO 处理错误的结果
                                Toast.makeText(RegisterActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                                ((Throwable) data).printStackTrace();
                            }
                        }
                        // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                        return false;
                    }
                }).sendMessage(msg);
            }
        };
// 注册一个事件回调，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eventHandler);

// 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
//        SMSSDK.getVerificationCode(country, phone);

// 提交验证码，其中的code表示验证码，如“1357”
//        SMSSDK.submitVerificationCode(country, phone, code);


    }

    private void init()
    {
        sendVeri = (Button)findViewById(R.id.sendVerification);
        resSure = (Button)findViewById(R.id.registerSure);
        resCancle = (Button)findViewById(R.id.registerCancel);
        sendVeri.setOnClickListener(this);
        resCancle.setOnClickListener(this);
        resSure.setOnClickListener(this);
        code = (EditText)findViewById(R.id.verification);

        phone = (EditText)findViewById(R.id.registerPhone);
        name = (EditText)findViewById(R.id.registerName);
        id = (EditText)findViewById(R.id.registerID);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sendVerification:
              sendVerification();
                break;
            case R.id.registerCancel:
                ActivityCollector.removeActivity(this);
                finish();
                break;
            case R.id.registerSure:
                SMSSDK.submitVerificationCode(country, phone.getText().toString(), code.getText().toString());
                Toast.makeText(this,"成功，进入信息完善界面",Toast.LENGTH_SHORT).show();
                break;
                default:
                    break;
        }
    }

    private void sendVerification()
    {
        if(phone.getText()==null){
            return;
         }
        String p;
        p = phone.getText().toString();
        if(!Regex.isMobileExact(p)){
           Toast.makeText(this,"手机号不合法",Toast.LENGTH_SHORT).show();
           phone.setError("此处不合法");
        }else {
            SMSSDK.getVerificationCode(country, p);
            CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(sendVeri, 60000, 1000); //倒计时1分钟
            mCountDownTimerUtils.start();
        }
    }
    // 使用完EventHandler需注销，否则可能出现内存泄漏
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
        ActivityCollector.removeActivity(this);
    }

}
