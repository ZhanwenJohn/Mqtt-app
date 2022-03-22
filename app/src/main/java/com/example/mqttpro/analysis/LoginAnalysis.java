package com.example.mqttpro.analysis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.mqttpro.user.User;
import com.example.mqttpro.user.UserDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginAnalysis {

    public static int findByNameAndPassword(String adminName, String adminPassword) {
        int analysis = 0;
        User user = new User();;
        UserDao userDao = new UserDao();
        user = userDao.findByNameAndPassword(adminName);
        if (adminName.equals("")){
            analysis = 1;
            System.out.println("请输入用户名");
        }else if(adminPassword.equals("")){
            analysis = 2;
            System.out.println("请输入密码");
        }else if (!user.getPassword().equals(adminPassword)){
            analysis = 3;
            System.out.println("输入密码不正确");
        }else{
            analysis = 4;
        }
        return analysis;
    }

    /**
     * 查找登录用户对应的学生姓名
     * @param adminName 用户名
     * @return 学生姓名
     */
    public static String findStudentName(String adminName) {
        User user = new User();
        UserDao userDao = new UserDao();
        user = userDao.findByNameAndPassword(adminName);
        return user.getStudentName();
    }

    /**
     * 判断网络是否可用
     * @return boolean
     */
    public static boolean getPing(Context mContext) {
        if (mContext!=null){
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) mContext.
                            getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean connected = networkInfo.isConnected();
            if (networkInfo!=null&&connected){
                if (networkInfo.getState()== NetworkInfo.State.CONNECTED){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
//        String result = null;
//        try {
//            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
//            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
//            // 读取ping的内容，可以不加
//            InputStream input = p.getInputStream();
//            BufferedReader in = new BufferedReader(new InputStreamReader(input));
//            StringBuffer stringBuffer = new StringBuffer();
//            String content = "";
//            while ((content = in.readLine()) != null) {
//                stringBuffer.append(content);
//            }
//            Log.d("------ping-----", "result content : " + stringBuffer.toString());
//            // ping的状态
//            int status = p.waitFor();
//            if (status == 0) {
//                result = "success";
//                return 1;
//            } else {
//                result = "failed";
//            }
//        } catch (IOException e) {
//            result = "IOException";
//            } catch (InterruptedException e) {
//                result = "InterruptedException";
//            } finally {
//                Log.d("----result---", "result = " + result);
//            }
//        return 0;
    }
}
