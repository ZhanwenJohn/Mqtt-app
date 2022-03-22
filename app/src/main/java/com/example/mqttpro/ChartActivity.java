package com.example.mqttpro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mqttpro.analysis.LoginAnalysis;
import com.example.mqttpro.bean.THdataBean;
import com.example.mqttpro.user.ThDataUser;
import com.example.mqttpro.utils.ThDataLineChartUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    private TextView set_test,student_name;
    ThDataLineChartUtil thDataLineChartUtil;
    LineChart lineChart_Th;
    List<Float> list = new ArrayList<>(); //数据集合
    List<String> names = new ArrayList<>(); //折线名字集合
    List<Integer> colour = new ArrayList<>();//折线颜色集合
    private final String TAG = "ChartActivity";
    private String adminName, studentName, Data;
    private Handler handler;
    private static double Tempe_Chart = 0.0,Humi_Chart = 0.0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        initViewv();

        Intent intent = getIntent();
        if (intent != null){
            adminName = intent.getStringExtra("adminName");
            Log.d(TAG,"子线程获得登录用户名: " + adminName);
        }else{
            Log.d(TAG,"------无法获取相关信息，检查网络连接------");
        }

        ObtainStudenNameInfo();

        //设置折线图
        //折线名字
        names.add("温度/℃");
        names.add("湿度/%");
        //折线颜色
        colour.add(Color.BLUE);
        colour.add(Color.GREEN);
        thDataLineChartUtil = new ThDataLineChartUtil(lineChart_Th,names,colour);
        thDataLineChartUtil.setYAxis(100,0,10000);
        //死循环添加数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void run() {
                            list.add((float) Tempe_Chart);
                            list.add((float) Humi_Chart);
                            Log.d(TAG,"/****  获得的List中数据  ****/"+list.get(0)+"\t"+list.get(1));
                            //list.add();
                            thDataLineChartUtil.addEntry(list);
                            list.clear();
                        }
                    });
                }
            }
        }).start();

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        studentName = msg.obj.toString();
                        student_name.setText("欢迎" + studentName + "登录");
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public static Handler mMsgHander =new Handler(Looper.myLooper()){
        public void handleMessage(android.os.Message msg) {
            switch(msg.what){
                case 0:
                    String data = msg.obj.toString();
                    upDateThChart(data);
                    Log.d("ChartActivity","MainActivity传输数据到ChartActivity"+data);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 解析MainActivity中的温湿度数据
     * @param data 字符串
     */
    private static void upDateThChart(String data) {
        Gson gson = new Gson();
        THdataBean tHdataBean = gson.fromJson(data,THdataBean.class);
        Tempe_Chart = tHdataBean.getTemperature();
        Humi_Chart = tHdataBean.getHumidity();
        Log.d("ChartActivity","温度:"+Tempe_Chart+"℃\t湿度:"+Humi_Chart+"H\n");
    }

    /**
     * 初始化控件，绑定
     */
    private void initViewv(){
        set_test = findViewById(R.id.set_test);
        student_name = findViewById(R.id.tv_Student_Name1);
        lineChart_Th = findViewById(R.id.ThDataLineChart);
    }
    /**
     * 获取姓名子线程
     */
    private void ObtainStudenNameInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = LoginAnalysis.findStudentName(adminName);
                handler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 返回MainActivity页面的单击事件
     */
    public void goBackMainActivity(View view){
        Intent intent = new Intent(ChartActivity.this,MainActivity.class);
        startActivity(intent);
    }

}
