package com.zuovx.Utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.zuovx.Model.User;

public class GlobalVar {
//    public static final String url = "http://192.168.43.8:8080/";8080
    public static String url = "http://192.168.43.8:8080/";
//    public static String url = "http://10.101.11.69:8080/";
    public static boolean isLogin = false;
    public static User user = null;
    /**
     * 或取本机的ip地址
     */

    public static String getlocalip(Context context){
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        //  Log.d(Tag, "int ip "+ipAddress);
        if(ipAddress==0)return null;
        return ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."
                +(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
    }



}
