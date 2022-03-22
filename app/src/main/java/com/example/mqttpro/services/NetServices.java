package com.example.mqttpro.services;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetServices {

    private static final String URL_WEATHER_WITH_FUTURE = "https://yiketianqi.com/api?unescape=1&" + "version=v1&appid=59255594&appsecret=PUnkAHX8";
    private static final String URL_IPADDRESS_WITH_GET = "https://www.taobao.com/help/getip.php";
    private static final String URL_IPADDRESS_INFO_GET = "http://apis.juhe.cn/ip/ipNewV3";
    private static final String URL_IPADDRESS_INFO_KEY = "7cbe672035411f50c8e0bcf17003fbcc";
    private static final String URL_IPV6ADDRESS_INFO_GET = "http://api.ip138.com/ip/";
    private static final String URL_IPV6ADDRESS_INFO_KEY = "68fd984a3db980b5a6df2e7eb7b19c3b";

    /**
     * 网络连接请求
     * @param urlStr 链接地址
     * @return 读取API内容字符串
     */
    public static String doGet(String urlStr) {
        String result = "";
        HttpURLConnection connection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        //连接网络
        try {
            URL urL = new URL(urlStr);
            connection = (HttpURLConnection) urL.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            System.out.println("网络连接成功！！！");
            //从连接读取数据(二进制)
            InputStream inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            //二进制流送入缓冲区
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //一行一行取出
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            result = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络连接异常！！！");
        } finally {
            //使用完释放资源，关闭连接
            if (connection != null) {
                connection.disconnect();
                System.out.println("断开connection连接");
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                    System.out.println("关闭inputStreamReader连接");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("关闭inputStreamReader异常");
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    System.out.println("关闭bufferedReader连接");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("关闭bufferedReader异常");
                }
            }
        }
        System.out.println("成功返回结果字符串result");
        return result;
    }

    /**
     * 中文URL编码
     *
     * @param url 中文
     * @return 编码值
     */
    private static String urlEncodeChinese(String url) {
        Matcher matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(url);
        String tmp = "";
        while (matcher.find()) {
            tmp = matcher.group();
            try {
                url = url.replaceAll(tmp, URLEncoder.encode(tmp, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return url.replace(" ", "%20");
    }

    /**
     * 获取天气预报
     * @param cityName 城市名称
     */
    public static String getWeatherOfCity(String cityName) {
        String URL = URL_WEATHER_WITH_FUTURE + "&city=" + cityName;
        System.out.println("天气预报链接地址为:"+ URL);
        return doGet(URL);
    }

    /**
     * 获取外网的IP(必须放到子线程里处理)
     */
    public static String getNetIp(String url) {
        String ip = "";
        InputStream inStream = null;
        try {
            URL infoUrl = new URL(url);
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "gb2312"));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    //builder.append(line).append("\n");
                }
                inStream.close();
                int start = builder.indexOf(":");
                int end = builder.indexOf("}");
                ip = builder.substring(start + 1, end);
                return ip;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取IP地址
     * @return IP地址
     */
    public static String ObtianIpAddress(){
        String ipaddress = "";
        System.out.println("IP地址查询链接为:"+ URL_IPADDRESS_WITH_GET);
        ipaddress = getNetIp(URL_IPADDRESS_WITH_GET).replace("\"","");
        return ipaddress;
    }

    /**
     * 获取IP地址相关信息
     * @param ip IP地址
     * @return IP地址的相关信息
     */
    public static String ObtainIpInformation(String ip){
        String URL = URL_IPADDRESS_INFO_GET + "?ip=" + ip + "&key=" + URL_IPADDRESS_INFO_KEY;
        System.out.println("IP地址信息查询链接:" + URL);
        return doGet(URL);
    }

    public static String ObtainIpv6Info(String ip){
        String URL = URL_IPV6ADDRESS_INFO_GET + "?ip=" + ip + "&datatype=jsonp&token=" + URL_IPV6ADDRESS_INFO_KEY;
        System.out.println("IPV6地址信息查询链接:" + URL);
        return doGet(URL);
    }
}
