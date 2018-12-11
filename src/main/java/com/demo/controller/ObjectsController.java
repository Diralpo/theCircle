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
                "from object where obj_status=1 order by obj_last_modify_time desc");
        renderJson(objects);
    }

    public void showFocus(){
        User current_user = getSessionAttr("current_user");
        if(current_user==null){
            renderJson("{\"code\":400}");
            return;
        }
        List<Record> reslist = Db.find("select obj_id,obj_name,obj_href,obj_img_href,obj_type " +
                "from object where exists(select * from users, user_object_relation where users.u_id = " +
                "user_object_relation.uor_user_id and user_object_relation.uor_obj_id=object.obj_id and users.u_id="
                +current_user.get("u_id")+
                ")");
        renderJson(reslist);
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
        System.out.println(map);
        User current_user = getSessionAttr("current_user");
        if(current_user!=null && current_user.get("u_id").toString().equals( map.get("u_id").toString())) {
            Record new_comment = new Record().set("com_status", 1).set("com_creator_id", map.get("u_id"))
                    .set("com_obj_id", map.get("obj_id")).set("com_details", map.get("text"));
            boolean success = Db.save("comment", "com_id", new_comment);
            if (!success) {
                renderJson("{\"code\":400}");
            } else {
                renderJson("{\"code\":200}");
            }
        }
        else {
            renderJson("{\"code\":400}");
        }
    }

    public void getObjectComments(){
        String s=HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s,Map.class);
        //List<Comment> comments= Comment.dao.find("select * from comment where com_obj_id="+map.get("obj_id"));
        List<Record> comments_info = Db.find("select com_create_time, com_last_modify_time, com_obj_id, " +
                "com_href, com_details, u_nickname from comment, users where comment.com_creator_id=users.u_id " +
                "and com_obj_id="+map.get("obj_id"));
        renderJson(comments_info);
    }

    public void createObject(){
        boolean success = false;
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s,Map.class);
        Record new_object = new Record().set("obj_type",map.get("object_type")).set("obj_name",map.get("object_name"))
                .set("obj_distribution_company",map.get("object_writer")).set("obj_distribution_time",map.get("object_time"))
                .set("obj_href",map.get("object_get_link")).set("obj_img_href",map.get("object_image_link"))
                .set("obj_details",map.get("object_introduction")).set("obj_creator_id",map.get("object_creator_id"))
                .set("obj_status",0);
        try {
            success = Db.save("object", "obj_id", new_object);
        }catch (Exception e){
            System.out.println(e);
        }
        if(success==true){
            renderJson("{\"code\":200}");
        }
        else {
            renderJson("{\"code\":400}");
        }
    }

    public void addFocus(){
        boolean success = false;
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s,Map.class);
        User current_user = getSessionAttr("current_user");
        if(current_user==null){
            renderJson("{\"code\":400}");
            return;
        }
        Record new_uor = new Record().set("uor_obj_id", map.get("obj_id")).set("uor_user_id",
                current_user.get("u_id"));
        try {
            success = Db.save("user_object_relation", "uor_obj_id",new_uor);
        }catch (Exception e){
            System.out.println(e);
        }
        if(success==true){
            renderJson("{\"code\":200}");
        }
        else {
            renderJson("{\"code\":400}");
        }
    }

    public void query_has_focused(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s,Map.class);
        User current_user = getSessionAttr("current_user");
        if(current_user==null){
            renderJson("{\"code\":400}");
            return;
        }
        List<Record> res = Db.find("select * from user_object_relation where uor_user_id =" +
                current_user.get("u_id")+" and uor_obj_id="+map.get("obj_id"));
        if(res.size() > 0){
            renderJson("{\"code\":200,\"result\":1}");
        }
        else{
            renderJson("{\"code\":200,\"result\":0}");
        }
    }

    public void removeFocus(){
        boolean success = false;
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s,Map.class);
        User current_user = getSessionAttr("current_user");
        if(current_user==null){
            renderJson("{\"code\":400}");
            return;
        }
        Db.delete("delete from user_object_relation where uor_user_id =" +
                current_user.get("u_id")+" and uor_obj_id="+map.get("obj_id"));
        renderJson("{\"code\":200}");
    }


}
