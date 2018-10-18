package com.demo.controller;


import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

import java.util.Map;

public class HelloController extends Controller {

    public void index() {
        redirect("index.html");
    }

    public void login() {
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        if ("test".equals(map.get("email")) && "123".equals(map.get("password"))) {
            setSessionAttr("username", "test");
            getSession().setAttribute("username", "test");
    }
        renderJson("{\"mes\":\"已经登录\"}");
    }

    public void logout() {
        String username = getSessionAttr("username");
        System.out.println(username);
        getSession().removeAttribute("username");
        renderJson("{\"mes\":\"退出成功\"}");
    }
}
