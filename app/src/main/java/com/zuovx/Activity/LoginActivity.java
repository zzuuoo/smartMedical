package com.zuovx.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zuovx.Model.User;
import com.zuovx.R;
import com.zuovx.Utils.ActivityCollector;
import com.zuovx.Utils.GlobalVar;
import com.zuovx.Utils.Regex;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
        implements LoaderCallbacks<Cursor> ,OnClickListener{

    public Handler handler=null;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private RequestQueue requestQueue;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button register;
    private RadioGroup radioGroup;
    private CheckBox remeberAccount;
//    private CheckBox autoLogin;
    private RadioButton adminRadio;
    private RadioButton patientRadio;
    private RadioButton doctorRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        init();
//        ActivityCollector.addActivity(this);
        populateAutoComplete();
        setCircleLogo();
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        readAccount();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        showProgress(true);
                        break;
                    case 2:
                        showProgress(false);
                        break;
                    case 3:
                        finish();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                        case 4:
                            showProgress(false);
                            mPasswordView.setError("账号或密码错误");
                            mPasswordView.requestFocus();
                            break;
                    case 5:
                        showProgress(false);
                        mPasswordView.setError("网络请求失败");
                        mPasswordView.requestFocus();
                        break;
                }
            }
        };
    }



    private void init()
    {
        register = (Button)findViewById(R.id.register_button);
        register.setOnClickListener(this);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        radioGroup = (RadioGroup)findViewById(R.id.status);
        remeberAccount = (CheckBox) findViewById(R.id.saveUser);
        adminRadio = (RadioButton)findViewById(R.id.admin);
        patientRadio = (RadioButton)findViewById(R.id.patient);
        doctorRadio = (RadioButton)findViewById(R.id.doctor);
    }


    private void setCircleLogo()
    {
        //圆形图
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_login);
        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.logo_login));
        circleDrawable.getPaint().setAntiAlias(true);
        circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
        ImageView image3 = (ImageView) findViewById(R.id.logo_login_form_1);
        image3.setImageDrawable(circleDrawable);

    }
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            mEmailView.setError(getString(R.string.error_invalid_phone));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            int status = 1;
            switch (radioGroup.getCheckedRadioButtonId())
            {
                case R.id.doctor:
                    status=2;
                    break;
                case R.id.patient:
                    status=1;
                    break;
                case R.id.admin:
                    status=0;
                    break;
                    default:
                        break;
            }
            if(remeberAccount.isChecked())
            {
                //创建sharedPreference对象，info表示文件名，MODE_PRIVATE表示访问权限为私有的
                SharedPreferences sp = getSharedPreferences("smartMedicalUser", MODE_PRIVATE);
                //获得sp的编辑器
                SharedPreferences.Editor ed = sp.edit();
                //以键值对的显示将用户名和密码保存到sp中
                ed.putString("account", phone);
                ed.putString("password", password);
                ed.putInt("status",status);
                ed.putBoolean("remeber",true);
//                            Toast.makeText(getApplication(),"保存密码成功",Toast.LENGTH_SHORT).show();
//                提交用户名和密码
//                if(autoLogin.isChecked())
//                {
//                    ed.putBoolean("autoLogin",true);
//                }else{
//                    ed.putBoolean("autoLogin",false);
//                }
                ed.commit();
            }else{
                SharedPreferences sp = getSharedPreferences("smartMedicalUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("account");
                editor.remove("password");
                editor.remove("status");
                editor.putBoolean("remeber",false);
//                editor.putBoolean("autoLogin",false);

                editor.commit();
//                            Toast.makeText(getApplication(),"取消密码成功",Toast.LENGTH_SHORT).show();
            }

            loginWithVolley(phone,password,String.valueOf(status));

        }
    }

    //读取保存在本地的用户名和密码
    public void readAccount() {

        //创建SharedPreferences对象
        SharedPreferences sp = getSharedPreferences("smartMedicalUser", MODE_PRIVATE);
        //获得保存在SharedPredPreferences中的用户名和密码
        if(sp.getBoolean("remeber",false))
        {
            String account = sp.getString("account", "");
            String password = sp.getString("password", "");
            //在用户名和密码的输入框中显示用户名和密码
            mEmailView.setText(account);
            mPasswordView.setText(password);
            remeberAccount.setChecked(true);
            int status = sp.getInt("status",-1);
            switch (status)
            {
                case 0:
                    adminRadio.setChecked(true);
                    break;
                case 1:
                    patientRadio.setChecked(true);
                    break;
                case 2:
                    doctorRadio.setChecked(true);
                break;
            }
//            if(sp.getBoolean("autoLogin",false))
//            {
//                autoLogin.setChecked(true);
//            }
        }
        else{
            remeberAccount.setChecked(false);
//            autoLogin.setChecked(false);
        }
    }


    /**
     * 使用volley框架进行登录请求，并返回用户基本信息存于MainActivity的user中
     * @param account
     * @param password
     * @param status
     */
    private void loginWithVolley(final String account, final String password, final String status)
    {
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                GlobalVar.url + "login",
                new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s==null||s.equals(""))
                {
                    //登录失败操作
                    MainActivity.isLogin = false;
                    GlobalVar.isLogin = false;
                    Log.d("登录","登录失败");

                }else{
                    User user =null;
                    try {
                        Log.e("成功接受信息：",s);
                        Gson gson = new Gson();
                        user= gson.fromJson(s, User.class);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(user!=null)
                    {
                        Log.e("成功登录账号：",user.getAccount());
                        MainActivity.user = user;
                        //接着登录成功操作
                        MainActivity.isLogin = true;
                        GlobalVar.user = user;
                        GlobalVar.isLogin = true;

                    }
                }
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);


                if (MainActivity.isLogin) {
                    Message message1 = new Message();
                    message1.what = 3;
                    handler.sendMessage(message1);
                } else {
                    Message message1 = new Message();
                    message1.what = 4;
                    handler.sendMessage(message1);
                }
//                User u = new User("123","123",1);
//                MainActivity.user = u;
//                //接着登录成功操作
//                MainActivity.isLogin = true;
//                GlobalVar.user = u;
//                GlobalVar.isLogin = true;
//                Message message1 = new Message();
//                message1.what = 3;
//                handler.sendMessage(message1);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message message1 = new Message();
                message1.what = 5;
                handler.sendMessage(message1);
//                Message message1 = new Message();
//                message1.what = 3;
//                handler.sendMessage(message1);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("account", account);
                map.put("password", password);
                map.put("userStatus",status);
                return map;
            }

        };

        requestQueue.add(stringRequest);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
       return Regex.isMobileExact(phone);
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                                                                     .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.register_button:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String phone;
        private final String mPassword;
        private final Integer status;


        UserLoginTask(String phone, String password,Integer status) {
            this.phone = phone;
            this.mPassword = password;
            this.status=status;
            Log.d("登录信息：","账号:"+phone+" 密码："+password+" 状态："+status);
        }



        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

//            User p = new User(phone,mPassword);

            try {

                // Simulate network access.
                //根据phone,mPassword获取一个user
                //在此访问数据库接受反馈，如果验证为真，判断身份，获取头像路径，然后登录
                Map<String,String> map = new HashMap<>();
                map.put("account",phone);
                map.put("password",mPassword);
                map.put("status",String.valueOf(status));
                map.put("headPath","1");
                map.put("userId","0");
                JSONObject jsonObject = new JSONObject(map);

                OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();//创建OkHttpClient对象。
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
//        String jsonStr = "{\"username\":\"lisi\",\"nickname\":\"李四\"}";//json数据.
                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                Request request = new Request.Builder()
                        .url(GlobalVar.url)
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                Thread.sleep(5000);
                if(response.isSuccessful())
                {
                    String s = response.body().string();
                    Log.d("login请求：",s+"信息");
                    if(s==null||s.equals(""))
                        {
                            //登录失败操作
                            MainActivity.isLogin = false;
                            Log.d("登录了","但是登录失败");

                        }else{
                            try {
                                Gson gson = new Gson();
                                User user =null;
                                user= gson.fromJson(s, new TypeToken<User>() {}.getType());
                                Log.e("成功登录账号：",user.getAccount());
                                MainActivity.user = user;
                                //接着登录成功操作
                                MainActivity.isLogin = true;

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                }else{
                    Log.d("回答失败","failed");
                }

            } catch (Exception e) {
                Log.e("LoginError",e.toString());
                e.printStackTrace();
            }


            if(MainActivity.isLogin )
            {
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            } else {
                mPasswordView.setError("登录失败");
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
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
                finish();
                ActivityCollector.quitAll();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        ActivityCollector.removeActivity(this);
    }
}

