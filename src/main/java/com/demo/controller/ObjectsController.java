package com.demo.controller;

import java.util.List;
import java.util.Map;

import com.jfinal.kit.HttpKit;
import com.google.gson.Gson;
import com.jfinal.core.Controller;

import com.demo.model.TheObject;
import com.demo.model.User;

public class ObjectsController extends Controller {

    public void seekObject() {
        List<TheObject> objects = TheObject.dao.find("select obj_id,obj_name,obj_href,obj_img_href,obj_type " +
                "from object where obj_status=1 order by obj_last_modify_time");
        renderJson(objects);
    }

    public void search(){
        System.out.println("执行搜索操作");
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        User current_user = getSessionAttr("current_user");
        String keyword = map.get("keyword").toString();
        if(current_user!=null && (int)current_user.get("u_permissions")>255 &&
                map.get("email").toString().equals(current_user.get("u_email"))){
            List<TheObject> objects = TheObject.dao.find("select obj_id,obj_name,obj_href,obj_img_href," +
                    "obj_type, obj_status from object where obj_name like \"%" + keyword+
                    "%\"order by obj_last_modify_time");
            renderJson(objects);
        }
        else{
            List<TheObject> objects = TheObject.dao.find("select obj_id,obj_name,obj_href,obj_img_href, " +
                    "obj_type from object where obj_status=1 and obj_name like \"%" + keyword+
                    "%\"order by obj_last_modify_time");
            renderJson(objects);
        }
    }

    public void query() {
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        System.out.println(map.get("obj_id").toString());
        renderJson("{\"code\":400, \"detail\": \"这里是"+ map.get("obj_name").toString() + "  的介绍\"}");
    }
}
