package com.example.mqttpro.bean;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class IpBean {

    @SerializedName("ret")
    private String ret;

    @SerializedName("ip")
    private String ip;

    @SerializedName("data")
    private String[] data;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "IpBean{" +
                "ret='" + ret + '\'' +
                ", ip='" + ip + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}

/*
{
    "ret": "ok",        // ret 值为 ok 时 返回 data 数据 为err时返回msg数据
    "ip": "240e:398:1:90a0:585e:a0f6:97d3:bd5",     // IPv6
    "data": [
        "中国",            // 国家（极少为空）
        "四川",          // 省份/自治区/直辖市（少数为空）
        "成都",          // 地级市（部份为空）
        "锦江区",          // 区/县（部份为空）
        "电信",            // 运营商
        "610000",          // 邮政编码
        "028"              // 地区区号
    ]
}
 */
