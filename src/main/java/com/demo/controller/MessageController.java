package com.demo.controller;

import com.demo.model.User;
import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
import java.util.Map;

public class MessageController  extends Controller {
    public void sending(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        String receiver_nickname = map.get("receiver").toString();
        String details = map.get("details").toString();
        User current_user = getSessionAttr("current_user");
        // 将用户对个人资料的修改放到后台验证
        if(current_user!=null){
            System.out.println("信息: " + details + "\n发送给: "+receiver_nickname);
            String receiver_id = User.dao.findFirst("select * from users where u_nickname='"+receiver_nickname+"'").get("u_id").toString();
            if(!receiver_id.equals(current_user.get("u_id"))){
                Record new_message = new Record().set("pm_details", details).set(
                        "pm_sender_id", current_user.get("u_id")).set(
                        "pm_receiver_id", receiver_id);
                boolean success = Db.save("message","pm_id", new_message);
                if(success){
                    renderJson("{\"code\":200}");
                }
                else {
                    renderJson("{\"code\":400}");
                }
            }
        }
    }
}
