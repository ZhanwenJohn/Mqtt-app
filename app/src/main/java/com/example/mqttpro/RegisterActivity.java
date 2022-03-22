package com.example.mqttpro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mqttpro.analysis.RegisterAnalysis;
import com.example.mqttpro.user.UserDao;

public class RegisterActivity extends AppCompatActivity {

    /**************   定义变量    *************/
    private EditText et_stuid,et_stuname,et_username1,et_password1,et_password2,et_email;
    private Button bt_registered;
    private TextView tv_user_count;
    private String stu;
    private String studentName;
    private String userName;
    private String passWord1;
    private String passWord2;
    private String studentEmail;
    private int sFlag = 0;
    private Handler handler0,handlerAdd;

    DialogInterface.OnClickListener click1;
    DialogInterface.OnClickListener click2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

        /*
         * 注册信息提交按钮单击事件
         */
        bt_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInformation();
            }
        });

        DialogInterface.OnClickListener click1 = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                RegisterActivity.this.finish();
            }
        };
        DialogInterface.OnClickListener click2 = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.cancel();
            }
        };

        /*
         * 测试UI主线程是否工作，查询用户数量
         */
        handler0 = new Handler(Looper.myLooper()) {
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what==0) {
                    InitEditText();
                    tv_user_count.setText("数量为："+msg.obj.toString());
                    System.out.println("执行结束");
                }
            }
        };

        handlerAdd = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Toast.makeText(RegisterActivity.this,"请输入学号",Toast.LENGTH_SHORT).show();
                        System.out.println("请输入学号");
                        break;
                    case 2:
                        Toast.makeText(RegisterActivity.this,"请输入姓名",Toast.LENGTH_SHORT).show();
                        System.out.println("请输入姓名");
                        break;
                    case 3:
                        Toast.makeText(RegisterActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                        System.out.println("请输入用户名");
                        break;
                    case 4:
                        Toast.makeText(RegisterActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                        System.out.println("请输入密码");
                        break;
                    case 5:
                        Toast.makeText(RegisterActivity.this,"请输入确认密码",Toast.LENGTH_SHORT).show();
                        System.out.println("请输入确认密码");
                        break;
                    case 6:
                        Toast.makeText(RegisterActivity.this,"请输入电子邮箱",Toast.LENGTH_SHORT).show();
                        System.out.println("请输入电子邮箱");
                        break;
                    case 7:
                        Toast.makeText(RegisterActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                        System.out.println("两次密码输入不一致");
                        break;
                    case 8:
                        Toast.makeText(RegisterActivity.this,"用户名长度必须大于4个字符",Toast.LENGTH_SHORT).show();
                        System.out.println("用户名长度必须大于4个字符");
                        break;
                    case 9:
                        Toast.makeText(RegisterActivity.this,"密码长度必须大于4个字符",Toast.LENGTH_SHORT).show();
                        System.out.println("密码长度必须大于4个字符");
                        break;
                    case 10:
                        Toast.makeText(RegisterActivity.this,"输入的用户名已存在",Toast.LENGTH_SHORT).show();
                        System.out.println("输入的用户名已存在");
                        break;
                    case 11:/*注册成功弹出提示弹窗*/
                        Toast.makeText(RegisterActivity.this,"注册成功，前往登陆页面",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(RegisterActivity.this);
                        alertdialogbuilder.setMessage("注册成功，是否切换至登陆页面？");
                        alertdialogbuilder.setPositiveButton("确定", click1);
                        alertdialogbuilder.setNegativeButton("取消", click2);
                        AlertDialog alertdialog1=alertdialogbuilder.create();
                        alertdialog1.show();
                        sFlag = 1;
                        System.out.println("注册成功，前往登陆页面");
                        break;
                    default:
                        break;
                }
                System.out.println("执行结束");
            }
        };
    }

    /*
     * 控件ID绑定
     */
    private void initView(){
        et_stuid = findViewById(R.id.et_stuid);
        et_stuname = findViewById(R.id.et_stuname);
        et_username1 = findViewById(R.id.et_username1);
        et_password1 = findViewById(R.id.et_password1);
        et_password2 = findViewById(R.id.et_password2);
        et_email = findViewById(R.id.et_email);
        bt_registered = findViewById(R.id.bt_registered);
        tv_user_count = findViewById(R.id.tv_user_count);
    }
    /*
     * EditText中的文本提取出来
     * 获取用户填写的注册信息
     */
    private void InitEditText(){
        stu = et_stuid.getText().toString();
        studentName = et_stuname.getText().toString();
        userName = et_username1.getText().toString();
        passWord1 = et_password1.getText().toString();
        passWord2 = et_password2.getText().toString();
        studentEmail = et_email.getText().toString();
    }
    /*
     * 执行查询用户数量的方法
     */
    private void selectCount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserDao userDao = new UserDao();
                System.out.println("执行开始");
                Message msg = new Message();
                msg.what = 0;
                msg.obj = userDao.getUserSize();
                //像主线程发送数据
                handler0.sendMessage(msg);
            }
        }).start();
    }

    /*
     * 注册页面返回按键单击事件
     */
    public void returnLoginIngActivity(View view){

        startActivity(new Intent(this,LoginActivity.class));
        RegisterActivity.this.finish();
    }

    public void addInformation(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行开始");
                InitEditText();
                RegisterAnalysis registerAnalysis = new RegisterAnalysis();
                Message msg = new Message();
                msg.what = registerAnalysis.addInformation(stu,studentName,userName,passWord1,passWord2,studentEmail);
                handlerAdd.sendMessage(msg);
            }
        }).start();
    }
}
