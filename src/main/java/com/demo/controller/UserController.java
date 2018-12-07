package com.demo.controller;


import com.demo.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;


import java.util.List;
import java.util.Map;

public class UserController extends Controller {

    public void index() {
        redirect("static/login.html");
    }


    public void logIn() {
        //login返回状态码 code:200->成功，400，没有该用户或账号密码错误，401该用户已登录
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        User current_user = getSessionAttr("current_user");
        User user = User.dao.findFirst("select * from users where u_email="+"\""+map.get("email").toString()+"\"");
        if(current_user!=null && user!=null && current_user.equals(user)){
            renderJson(new JsonResult(401,current_user.get("u_id"),current_user.get("u_email"),current_user.get("u_password"),current_user.get("u_nickname"),current_user.get("u_uni_id")));
        }
        else {
            if(user==null || !user.get("u_password").equals(map.get("password"))){
                renderJson(new JsonResult(400,null,null,null,null,0));
            }
            else {
                setSessionAttr("current_user",user);
                //renderJson("{\"code\":200}");
                renderJson(new JsonResult(200,user.get("u_id"),user.get("u_email"),user.get("u_password"),user.get("u_nickname"),user.get("u_uni_id")));
            }
        }
        return;
    }


    public void logOut() {
        //logout返回状态码：200登出成功，400系统记录用户不一致，出错
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        User current_user = getSessionAttr("current_user");
        if(current_user!=null && current_user.get("u_email").equals(map.get("email"))){
            getSession().removeAttribute("current_user");
            renderJson("{\"code\":200}");
        }
        else {
            renderJson("{\"code\":400}");
        }
        return;
    }

    public void change_user_info(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        String nickname = map.get("u_nickname").toString();
        User current_user = getSessionAttr("current_user");

        // 将用户对个人资料的修改放到后台验证
        if(current_user!=null && nickname.equals(current_user.get("u_nickname"))){
            Db.update("update users set u_sex = '" +
                    map.get("u_sex").toString()+
                    "' where u_nickname='" + nickname+
                    "'");
            renderJson("{\"code\":200}");
        }
        else{
            // 用户和想修改的不匹配
            renderJson("{\"code\":400}");
        }
    }

    public void user(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);

        String nickname = map.get("nickname").toString();

        User current_user = getSessionAttr("current_user");

        List<Record> userinfo;
        // 将用户对个人资料的修改放到后台验证
        if(current_user!=null && nickname.equals(current_user.get("u_nickname"))){
            System.out.println("find1 "+nickname);
            userinfo = Db.find("select u_nickname,u_photo,u_sex,u_permissions,u_status," +
                    "u_email, u_create_time, uni_name " +
                    "from users, university where u_nickname=\"" + nickname+
                    "\"and users.u_uni_id=university.uni_id");
            renderJson(userinfo);
        }
        else{
            System.out.println("find2 "+nickname);
            userinfo = Db.find("select u_nickname,u_photo,u_sex,uni_name " +
                    "from users, university where u_nickname=\"" + nickname+
                    "\"and users.u_uni_id=university.uni_id");
            renderJson(userinfo);
        }
        System.out.println(userinfo);
    }

    public void signUp(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        Record new_user  = new Record().set("u_nickname",map.get("nickname")).set(
                "u_password",map.get("password")).set(
                "u_sex",map.get("sex")).set("u_permission",1).set("u_status",1)
                .set("u_email",map.get("email"));
        boolean success = Db.save("users","u_id",new_user);
        if(success==true){
            User user = User.dao.findFirst("select * from users where u_email="+"\""+map.get("email").toString()+"\"");
            setSessionAttr("current_user",user);
            renderJson(new JsonResult(200,user.get("u_id"),user.get("u_email"),user.get("u_password"),user.get("u_nickname"),user.get("u_uni_id")));
        }
        else {
            renderJson("{\"code\":400}");
        }
        return;

    }


}
