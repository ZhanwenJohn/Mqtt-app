package com.example.mqttpro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mqttpro.analysis.LoginAnalysis;
import com.example.mqttpro.services.NetServices;
import com.example.mqttpro.services.ObtainMyPhoneInfo;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {
    private Handler mHandler, handler;
    private EditText et_adminUsername;
    private EditText et_adminPassword;
    private String adminName;
    private String adminPassword;
    private String ipv4address ,ipaddress;
    private String TAG = "LoginActivity";

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        //获取IP地址和IPV4地址（本地地址）
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                if (LoginAnalysis.getPing(LoginActivity.this)){
                    ipv4address = ObtainMyPhoneInfo.getIPAddress(LoginActivity.this).trim();
                    ipaddress = NetServices.ObtianIpAddress().trim();
                    msg.obj = "---------获取到网络连接--------------";
                }else {
                    ipv4address = "err";
                    ipaddress = "err";
                    msg.obj = "--------无网络连接-------------";
                }
                mHandler.sendMessage(msg);
            }
        },100,2000);


        mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        Log.d(TAG,msg.obj.toString());
                        break;
                    case 1:
                        Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                        System.out.println("请输入用户名");
                        break;
                    case 2:
                        Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                        System.out.println("请输入密码");
                        break;
                    case 3:
                        Toast.makeText(LoginActivity.this,"输入密码不正确",Toast.LENGTH_SHORT).show();
                        et_adminPassword.setText("");
                        System.out.println("输入密码不正确");
                        break;
                    case 4:/*发送用户名信息到主页面*/
                        InitEditText();
                        timer.cancel();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("adminName",adminName);
                        intent.putExtra("ipv4address",ipv4address);
                        intent.putExtra("ipaddress",ipaddress);
                        Log.d(TAG,"------IPV4地址--------: "+ ipv4address);
                        Log.d(TAG,"-------IP地址---------: "+ ipaddress);
                        startActivity(intent);
                        LoginActivity.this.finish();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 控件初始化，ID绑定
     */
    private void initView(){
        et_adminUsername = findViewById(R.id.et_username);
        et_adminPassword = findViewById(R.id.et_password);
    }

    /**
     * 提取用户输入的用户名喝密码
     */
    private void InitEditText(){
        adminName = et_adminUsername.getText().toString();
        adminPassword = et_adminPassword.getText().toString();
    }

    /**
     * 登录开关单击事件(loginActivity)
     * @param view 登录开关监听
     */
    public void loginActivity(View view){
        Log.d(TAG,"登陆按键按下！！！");
        Log.d(TAG,"开始登录操作");
        loginAdmin();
    }

    /**
     * 登录子线程
     */
    private void loginAdmin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InitEditText();
                Message msg = new Message();
                msg.what = LoginAnalysis.findByNameAndPassword(adminName,adminPassword);
                mHandler.sendMessage(msg);
            }
        }).start();
    }


    /**
     * 注册开关点击事件(registerActivity)
     * @param view 注册开关监听
     */
    public void registerActivity(View view){
        Intent it = new Intent(this,RegisterActivity.class);
        startActivity(it);
        System.out.println("切换到注册页面");
    }
}
