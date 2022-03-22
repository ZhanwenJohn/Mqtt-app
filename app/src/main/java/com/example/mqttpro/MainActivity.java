package com.example.mqttpro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.mqttpro.analysis.LoginAnalysis;
import com.example.mqttpro.bean.DataWeatherBean;
import com.example.mqttpro.bean.IpBean;
import com.example.mqttpro.bean.THdataBean;
import com.example.mqttpro.bean.ThLimiteBean;
import com.example.mqttpro.bean.WeatherBean;
import com.example.mqttpro.services.NetServices;
import com.example.mqttpro.services.ObtainMyPhoneInfo;
import com.example.mqttpro.user.ThDataUser;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    /********************变量的定义**********************/
    //控件初始化
    private TextView temp_val, hum_val, tv_tem, tv_weather, tem_low_high,tv_air,
            tv_win, tv_ipaddress, tv_time, tv_student_name;
    private ImageView led_switch, iv_weather, alarm_switch;
    private Button val_pub;
    private EditText temp_up, temp_low, hum_up, hum_low;
    private AppCompatSpinner mSpinner;
    private ArrayAdapter<String> mSpAdapter;
    private String[] mCtiies;
    //标记位
    private int led_intFlag , alarm_intFlag ;//LED开关 警报开关
    private int val_intFlag;//限定值发送标记位
    private String val_strFlag;
    //MQTT的数据部分
    private String host = "tcp://1.1.1.1:1883";
    private String mqtt_userName = "admin";
    private String mqtt_passWord = "public";
    private String mqtt_id;//生成客户端随机ID
    private String mqtt_pub_topic = "topic_Set_stm32";
    private String mqtt_sub_topic = "topic_Data_stm32";
    private String TAG = "MainActivity";
    public double Temperature = 0.0, Humidity = 0.0;
    private MqttClient client;
    private MqttConnectOptions options;
    private Handler handler, mHandler, mMsgHander;
    private ScheduledExecutorService scheduler;
    //变量
    private String studentAdminName = null, adminName;
    private String ipaddress, ipv4address;

    Timer timer = new Timer();
    /********************函数体************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*获取Login页面的用户名信息*/
        Intent intent = getIntent();
        try{
            if (intent != null){
                adminName = intent.getStringExtra("adminName");
                ipv4address = intent.getStringExtra("ipv4address");
                ipaddress = intent.getStringExtra("ipaddress");
                System.out.println("子线程获得登录用户名: " + adminName);
                System.out.println("子线程获得IPV4地址: "+ ipv4address);
                System.out.println("子线程获得IP地址: "+ ipaddress);
            }
        }catch (Exception exception){
            Log.d(TAG,"------无法获取相关信息，检查网络连接------");
            exception.printStackTrace();
        }
        /***************************函数调用********************************************/
        initView();//控件初始化!!!
        ObtainStudenNameInfo();
        IpAddressInfor();
        /**********************************功能区******************************/
        //单击提交开关
        val_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(("".equals(temp_up.getText().toString())) || ("".equals(temp_low.getText().toString())) || ("".equals(hum_up.getText().toString())) || ("".equals(hum_low.getText().toString()))){
                    Toast.makeText(MainActivity.this, "请输入完整限定值", Toast.LENGTH_SHORT).show();
                }else {
                    if((Integer.parseInt(temp_up.getText().toString()))>=100||(Integer.parseInt(temp_low.getText().toString()))<0||(Integer.parseInt(hum_up.getText().toString()))>=100||(Integer.parseInt(hum_low.getText().toString()))<0||(Integer.parseInt(hum_up.getText().toString()))<(Integer.parseInt(hum_low.getText().toString()))||(Integer.parseInt(temp_up.getText().toString()))<(Integer.parseInt(temp_low.getText().toString()))){
                        Toast.makeText(MainActivity.this, "输入数值不规范，请重新输入", Toast.LENGTH_SHORT).show();
                        //清除温湿度上下限设置值
                    }else {
                        ThLimiteBean th = new ThLimiteBean();
                        th.setTemp_up(Integer.parseInt(temp_up.getText().toString()));
                        th.setTemp_low(Integer.parseInt(temp_low.getText().toString()));
                        th.setHumi_up(Integer.parseInt(hum_up.getText().toString()));
                        th.setHumi_low(Integer.parseInt(hum_low.getText().toString()));
                        Gson gson = new Gson();
                        String json = gson.toJson(th);
                        Log.d(TAG,"--------  封装的数据  ------"+json);
                        publishmessageplus(mqtt_pub_topic,json);
                        if (val_intFlag == 1){
                            System.out.println(mqtt_id);
                            Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            //提取并发送上下限值
                            //清除温湿度上下限设置值
                            temp_up.setText("");hum_up.setText("");temp_low.setText("");hum_low.setText("");
                        }else if (val_intFlag == 0){
                            Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        //LED灯开关
        led_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (led_intFlag == 1) {
                    publishmessageplus(mqtt_pub_topic, "{\"led_set\":0}");
                } else {
                    publishmessageplus(mqtt_pub_topic, "{\"led_set\":1}");
                }
            }
        });

        //警报开关
        alarm_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    publishmessageplus(mqtt_pub_topic, "{\"beep_set\":0}");
            }
        });

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1://连接成功
                        System.out.println("连接成功");
                        Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_LONG).show();
                        try {
                            client.subscribe(mqtt_sub_topic, 1);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2://连接失败
                        System.out.println("连接失败");
                        Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_LONG).show();
                        break;
                    case 3://MQTT收到消息回传
                        String THdata = msg.obj.toString();
                        Log.d(TAG,"-----  单片机数据  -----"+mqtt_sub_topic+"\n"+THdata);
                        upDateTH(THdata);
                        break;
                    case 4://天气预报更新
                        String weather = msg.obj.toString();
                        Log.d("fan","--主线程收到了天气预报--Weather---"+ weather);
                        Gson gson = new Gson();
                        WeatherBean weatherBean = gson.fromJson(weather, WeatherBean.class);
                        Log.d("fan","---解析后的数据---" + weatherBean.toString());
                        updateUiOfWeather(weatherBean);
                    default:
                        break;
                }
            }
        };
        mHandler = new Handler(Looper.myLooper()) {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        studentAdminName = msg.obj.toString();
                        if(studentAdminName!=null){
                            tv_student_name.setText("欢迎" + studentAdminName + "登录");
                        }else{
                            tv_student_name.setText("请重新登录");
                        }
                        break;
                    case 1:
                        String ipinfo = msg.obj.toString();
                        System.out.println("主线程收到了IP地址信息" + ipinfo);
                        IpAddressAnalysis(ipinfo);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 更新UI界面的温湿度数据
     * @param tHdata JSON格式的数据字符串
     */
    @SuppressLint("SetTextI18n")
    private void upDateTH(String tHdata) {
        Gson gson = new Gson();
        THdataBean th = gson.fromJson(tHdata, THdataBean.class);
        if(th.getDoubleString(th.getTemperature())!=null&&th.getDoubleString(th.getHumidity())!=null){
            Temperature = th.getTemperature();
            Humidity = th.getHumidity();
            led_intFlag = th.getLedFlag();
            alarm_intFlag = th.getAlarmFlag();
            led_switch.setImageResource(getImgResOfLED(th.getLedFlag()));
            alarm_switch.setImageResource(getImgResOfAlarm(th.getAlarmFlag()));
            packData();
            temp_val.setText(th.getDoubleString(th.getTemperature())+" ℃");
            hum_val.setText(th.getDoubleString(th.getHumidity())+" H");
            Log.d(TAG,"/****  温度  ****/" + Double.toString(th.getTemperature()));
            Log.d(TAG,"/****  湿度  ****/" + Double.toString(th.getHumidity()));
        }else{
            temp_val.setText(0 + "℃");
            hum_val.setText(0 + "H");
            Temperature = 0.0;
            Humidity = 0.0;
        }
    }

    /**
     * 更新警报图标状态
     * @param alarmFlag 警报开关标记位
     * @return 图标
     */
    private int getImgResOfAlarm(int alarmFlag) {
        int result = 0;
        switch (alarmFlag) {
            case 0:
                result = R.drawable.onalarm;
                break;
            case 1:
                result = R.drawable.offalarm;
                break;
            default:
                result = R.drawable.offalarm;
                break;
        }
        return result;
    }

    /**
     * 更新LED图标状态
     * @param ledFlag LED开关标记位
     * @return  图标
     */
    private int getImgResOfLED(int ledFlag) {
        int result = 0;
        switch (ledFlag) {
            case 0:
                result = R.drawable.on_led;
                break;
            case 1:
                result = R.drawable.off_led;
                break;
            default:
                result = R.drawable.off_led;
                break;
        }
        return result;
    }

    public void packData(){
        Message msg = new Message();
        ThDataUser thDataUser = new ThDataUser();
        thDataUser.setTemperature(String.format("%.1f",Temperature));
        thDataUser.setHumidity(String.format("%.1f",Humidity));
        Gson gson = new Gson();
        String json = gson.toJson(thDataUser);
        msg.what = 0;
        msg.obj = json;
        mMsgHander=ChartActivity.mMsgHander;
        mMsgHander.sendMessage(msg);
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
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 获取IP地址子线程
     */
    public void  IpAddressInfor(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    ipv4address = ObtainMyPhoneInfo.getIPAddress(MainActivity.this).trim();
                    ipaddress = NetServices.ObtianIpAddress().trim();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = NetServices.ObtainIpv6Info(ipaddress);
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }catch (Exception pingException){
                    tv_ipaddress.setText("无法显示网络信息");
                }
            }
        },1000,300000);
    }

    /**
     * 解析IP地址信息
     */
    @SuppressLint("SetTextI18n")
    private void IpAddressAnalysis(String ipinfo) {
        Gson gson = new Gson();
        IpBean ipBean = gson.fromJson(ipinfo, IpBean.class);
        Log.d("fan", "---解析后的数据---" + ipBean.toString());
        if(ipBean.getRet().equals("ok")&&ipBean != null) {
            if (ipaddress.length() <= 15) {
                if (ipBean.getData()[1] != null && ipBean.getData()[2] == null) {
                    tv_ipaddress.setText("IP地址\n" + ipaddress + "\nIPV4地址\n" + ipv4address + "\n省份:" +
                            ipBean.getData()[1] + "\n运营商:中国" + ipBean.getData()[4]);
                } else if (ipBean.getData()[2] != null && ipBean.getData()[3] == null) {
                    tv_ipaddress.setText("IP地址\n" + ipaddress + "\nIPV4地址\n" + ipv4address + "\n省份:" +
                            ipBean.getData()[1] + "\n城市:" + ipBean.getData()[2] + "\n运营商:中国" + ipBean.getData()[4]);
                } else if (ipBean.getData()[3] != null) {
                    tv_ipaddress.setText("IP地址\n" + ipaddress + "\nIPV4地址\n" + ipv4address + "\n省份:" +
                            ipBean.getData()[1] + "\n城市:" + ipBean.getData()[2] + "\n县区:" +
                            ipBean.getData()[3] + "\n运营商:中国" + ipBean.getData()[4]);
                }
            } else {
                if (ipBean.getData()[1] != null && ipBean.getData()[2] == null) {
                    tv_ipaddress.setText("IP地址\n" + ipv4address + "\n省份:" +
                            ipBean.getData()[1] + "\n运营商:中国" + ipBean.getData()[4]);
                } else if (ipBean.getData()[2] != null && ipBean.getData()[3] == null) {
                    tv_ipaddress.setText("IP地址\n" + ipv4address + "\n省份:" + ipBean.getData()[1] +
                            "\n城市:" + ipBean.getData()[2] + "\n运营商:中国" + ipBean.getData()[4]);
                } else if (ipBean.getData()[3] != null) {
                    tv_ipaddress.setText("IP地址\n" + ipv4address + "\n省份:" +
                            ipBean.getData()[1] + "\n城市:" + ipBean.getData()[2] + "\n县区:" +
                            ipBean.getData()[3] + "\n运营商:中国" + ipBean.getData()[4]);
                }
            }
        }else{
            tv_ipaddress.setText("无法显示\n网络信息");
        }
    }

    /**
     * 天气信息更新到界面
     * @param weatherBean 天气信息
     */
    @SuppressLint("SetTextI18n")
    private void updateUiOfWeather(WeatherBean weatherBean) {
        if(weatherBean == null){
            return;
        }
        List<DataWeatherBean> dataWeatherBeanList =  weatherBean.getmDataWeatherBean();
        DataWeatherBean dataWeather = dataWeatherBeanList.get(0);
        if (dataWeather == null){
            return;
        }
        tv_tem.setText(dataWeather.getTem());
        tv_weather.setText(dataWeather.getWea());
        tv_time.setText(dataWeather.getDate() + "  " + dataWeather.getWeek());
        tem_low_high.setText(dataWeather.getTem2()+"~"+dataWeather.getTem1());
        tv_win.setText(dataWeather.getWin()[0]+ "  " + dataWeather.getWin()[1]
                + "\n" +dataWeather.getWin_speed());
        tv_air.setText("空气:" + dataWeather.getAir()+ " " + dataWeather.getAir_level() +
                "\n" + dataWeather.getAir_tips());
        iv_weather.setImageResource(getImgResOfWeather(dataWeather.getWea_img()));
        tv_student_name = findViewById(R.id.tv_Student_Name);
    }

    private int getImgResOfWeather(String werStr){
        int result = 0;
        switch (werStr){
            case "qing":
                result = R.drawable.biz_plugin_weather_qing;
                break;
            case "yin":
                result = R.drawable.biz_plugin_weather_yin;
                break;
            case "yu":
                result = R.drawable.midrain;
                break;
            case "xue":
                result = R.drawable.biz_plugin_weather_daxue;
                break;
            case "lei":
                result = R.drawable.shandiandalei;
                break;
            case "shachen":
                result = R.drawable.biz_plugin_weather_shachenbao;
                break;
            case "wu":
                result = R.drawable.biz_plugin_weather_wu;
                break;
            case "bingbao":
                result = R.drawable.biz_plugin_weather_leizhenyubingbao;
                break;
            case "yun":
                result = R.drawable.cloudy;
                break;
            default:
                result = R.drawable.biz_plugin_weather_qing;
                break;
        }
        return result;
    }
    /**
     * 返回单击事件，跳转到登录页面
     */
    public void returnActivity(View view){
        timer.cancel();
        startActivity(new Intent(this,LoginActivity.class));
        MainActivity.this.finish();
    }

    /**
     * 切换到图表页面单击事件
     */
    @SuppressLint("DefaultLocale")
    public void comeInChart(View view){
        Intent intent = new Intent(MainActivity.this,ChartActivity.class);
        intent.putExtra("adminName",adminName);
        Log.d(TAG,"------用户姓名--------: "+ adminName);
        startActivity(intent);
    }

    /**
     *控件初始化(initView)
     * Android Studio UI 控件id连接  findViewById
     */
    private void initView(){
        temp_val = findViewById(R.id.tv_temp);
        hum_val = findViewById(R.id.tv_hum);
        val_pub = findViewById(R.id.val_pub);
        temp_low = findViewById(R.id.temp_low);
        temp_up = findViewById(R.id.temp_up);
        hum_up = findViewById(R.id.hum_up);
        hum_low = findViewById(R.id.hum_low);
        led_switch = findViewById(R.id.led_switch);
        alarm_switch = findViewById(R.id.alarm_switch);
        mCtiies = getResources().getStringArray(R.array.cities);
        mSpinner = findViewById(R.id.sp_ciTy);
        mSpAdapter = new ArrayAdapter<>(this,R.layout.sp_item_layout,mCtiies);
        mSpinner.setAdapter(mSpAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
                String selectedCity = mCtiies[position];
                //监听选择的城市名称
                getweatherOfCity(selectedCity);
            }

            @Override
            public void onNothingSelected (AdapterView < ? > parent){

            }
        });
        iv_weather = findViewById(R.id.iv_weather);
        tv_tem = findViewById(R.id.tv_tem);
        tv_weather = findViewById(R.id.tv_weather);
        tem_low_high = findViewById(R.id.tem_low_high);
        tv_air = findViewById(R.id.tv_air);
        tv_win = findViewById(R.id.tv_win);
        tv_ipaddress = findViewById(R.id.tv_ipaddress);
        tv_time = findViewById(R.id.tv_time);
    }

    /**
     * 开启天气预报子线程
     * @param selectedCity 城市名称
     */
    private void getweatherOfCity(String selectedCity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求网络
                System.out.println("请求网络连接");
                String weatherOfCity = NetServices.getWeatherOfCity(selectedCity);
                Message msg = new Message();
                msg.what = 4;
                msg.obj = weatherOfCity;
                handler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 连接MQTT服务器Emqx
     * @param view 连接按钮监听
     */
    public void mqttConnectButton(View view){
        Mqtt_init();//MQTT初始化!!!
        startReconnect();//MQTT重连!!!
    }

    /**
     * MQTT连接初始化
     */
    private void Mqtt_init() {
        try {
            mqtt_id = studentAdminName + "_APP";
            //host为主机名，mqtt_id为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(host, mqtt_id,
                    new MemoryPersistence());
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接的用户名
            options.setUserName(mqtt_userName);
            //设置连接的密码
            options.setPassword(mqtt_passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(40);
            //设置回调
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    //连接丢失后，一般在这里面进行重连
                    System.out.println("connectionLost----------");
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish后会执行到这里
                    System.out.println("deliveryComplete---------"
                            + token.isComplete());
                }

                @Override
                public void messageArrived(String topicName, MqttMessage message)
                        throws Exception {
                    //subscribe后得到的消息会执行到这里面
                    System.out.println("messageArrived----------");
                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = message.toString();
                    handler.sendMessage(msg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * MQTT连接
     */
    private void Mqtt_connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.connect(options);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 2;
                    val_intFlag = 0;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    /**
     * MQTT重连
     */
    private void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!client.isConnected()) {
                    Mqtt_connect();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

    //MQTT自定义发布topic
    private void publishmessageplus(String topic, String message2) {
        if (client == null || !client.isConnected()) {
            val_strFlag = "客户端没有连接，无法发送";
            Log.d(TAG,val_strFlag);
            return;
        }
        MqttMessage message = new MqttMessage();
        message.setPayload(message2.getBytes());
        try {
            client.publish(topic, message);
            val_intFlag = 1;
            Log.d(TAG,"/*******  发送成功  *******/");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数字与大小写字母混编字符串
     * @param size 被编码字符串
     * @return 编码字符串
     */
    public static String getNumLargeSmallLetter(int size){
        StringBuilder buffer = new StringBuilder();
        Random random = new Random();
        for(int i=0; i<size;i++){
            if(random.nextInt(2) % 2 == 0){//字母
                if(random.nextInt(2) % 2 == 0){
                    buffer.append((char) (random.nextInt(27) + 'A'));
                }else{
                    buffer.append((char) (random.nextInt(27) + 'a'));
                }
            }else{//数字
                buffer.append(random.nextInt(10));
            }
        }
        return buffer.toString();
    }
}