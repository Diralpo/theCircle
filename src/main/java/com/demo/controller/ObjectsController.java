package com.demo.controller;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.demo.model.Comment;
import com.jfinal.kit.HttpKit;
import com.google.gson.Gson;
import com.jfinal.core.Controller;

import com.demo.model.TheObject;
import com.demo.model.User;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class ObjectsController extends Controller {

    public void seekObject() {
        List<TheObject> objects = TheObject.dao.find("select obj_id,obj_name,obj_href,obj_img_href,obj_type " +
                "from object where obj_status=1 order by obj_last_modify_time");
        renderJson(objects);
    }

    public void search_by_likeName(){
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

    public void search_by_id(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        User current_user = getSessionAttr("current_user");
        String id = map.get("object_id").toString();
        if(current_user!=null && (int)current_user.get("u_permissions")>255 &&
                map.get("email").toString().equals(current_user.get("u_email"))){
            List<TheObject> objects = TheObject.dao.find("select obj_id,obj_name,obj_href,obj_img_href," +
                    "obj_type, obj_status from object where obj_id =" + id+
                    " order by obj_last_modify_time");
            renderJson(objects);
        }
        else{
            List<TheObject> objects = TheObject.dao.find("select obj_id,obj_name,obj_href,obj_img_href, " +
                    "obj_type from object where obj_status=1 and obj_id =" + id +
                    " order by obj_last_modify_time");
            renderJson(objects);
        }
    }

    public void addComment(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        Date date=new Date();
        Record new_commnet = new Record().set("com_status",1).set("com_creator_id",map.get("u_id"))
                .set("com_obj_id",map.get("obj_id")).set("com_details",map.get("text"));
        boolean success = Db.save("comment","com_id",new_commnet);

        if(!success){
            renderJson("{\"code\":400}");
        }else {

            renderJson("{\"code\":200}");
        }
    }

    public void getObjectComments(){
        String s=HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s,Map.class);
        List<Comment> comments= Comment.dao.find("select * from comment where com_obj_id="+map.get("obj_id"));
        renderJson(comments);
    }

}
