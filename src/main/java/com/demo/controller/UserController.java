package com.demo.controller;


import com.demo.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

import java.util.Map;

public class UserController extends Controller {

    public void index() {
        redirect("static/login.html");
    }

    public void login() {
        //login返回状态码 code:200->成功，400，没有该用户或账号密码错误，401该用户已登录
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        String current_user = getSessionAttr("current_user");
        User user = User.dao.findFirst("select * from user where email="+map.get("email").toString());
        if(current_user.equals(user)){
            renderJson(new JsonResult(401,user.get("email").toString(),user.get("password").toString(),user.get("nickname").toString(),(int) user.get("school_id")));
        }
        else {
            if(user==null || !user.get("password").equals(map.get("password"))){
                renderJson(new JsonResult(400,null,null,null,0));
            }
            else {
                setSessionAttr("current_user",user);
                renderJson(new JsonResult(200,user.get("email").toString(),user.get("password").toString(),user.get("nickname").toString(),(int) user.get("school_id")));
            }

        }
        return;
    }

    public void logout() {
        String username = getSessionAttr("username");
        System.out.println(username);
        getSession().removeAttribute("username");
        renderJson("{\"mes\":\"退出成功\"}");
    }
}
