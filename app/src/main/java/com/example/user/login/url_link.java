package com.example.user.login;

/**
 * Created by user on 14/07/2017.
 */

public class url_link {
    public static String url_link = "http://192.168.1.226:8099/cms/api/";

    public String getUrl_link(String type){
        return url_link+type+".php";
    }
}
