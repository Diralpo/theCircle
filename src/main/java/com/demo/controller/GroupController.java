package com.demo.controller;

import com.demo.model.Group;
import com.demo.model.TheObject;
import com.demo.model.University;
import com.demo.model.User;
import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.Deque;
import java.util.List;
import java.util.Map;

public class GroupController extends Controller {
    public void getGroupList(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        String obj_id = map.get("obj_id").toString();

        List<Record> group_list_info;

        try{
            group_list_info = Db.find("select gro_id,gro_name,uni_name from thegroup,university where " +
                    "thegroup.gro_obj_id="+obj_id+"  and thegroup.gro_uni_id=university.uni_id");
            renderJson(group_list_info);
        }catch (Exception e){
            System.out.println("Exception: GroupController.getGroupList ");
            System.out.println(e);
        }
    }

    public void search_group_by_uni(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s,Map.class);
        String obj_id = map.get("obj_id").toString();
        List<Record> group_list_info;
        try{
            group_list_info = Db.find("select gro_id,gro_name,uni_name from thegroup,university where " +
                    "thegroup.gro_obj_id="+obj_id+"  and thegroup.gro_uni_id=university.uni_id and university.uni_name like \"%" + map.get("uni_name").toString()+"%\"");
            renderJson(group_list_info);
        }catch (Exception e){
            System.out.println("Exception: GroupController.getGroupList ");
            System.out.println(e);
        }
    }

    public void create_group(){
        String s =HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s,Map.class);
        User current_user = getSessionAttr("current_user");

        if(current_user!=null && current_user.get("u_id").toString().equals(map.get("u_id"))){
            try {
                University university = University.dao.findFirst("select * from university where uni_name= \"" + map.get("gro_uni_name").toString()+"\"");
                TheObject theObject = TheObject.dao.findFirst("select * from object where obj_name=\"" + map.get("gro_obj_name").toString()+"\"");
                Record new_group = new Record().set("gro_name", map.get("gro_name").toString()).set("gro_status", 0)
                        .set("gro_manager_id", map.get("gro_manager_id")).set("gro_uni_id", university.get("uni_id")).set("gro_obj_id", theObject.get("obj_id"))
                        .set("gro_details",map.get("gro_detail").toString());

                Db.save("thegroup", "gro_id", new_group);
                Group last_create_group = Group.dao.findFirst("select * from thegroup where gro_name=\""+map.get("gro_name").toString()+"\"");
                Record new_ugr = new Record().set("ugr_user_id",map.get("gro_manager_id")).set("ugr_gro_id",last_create_group.get("gro_id")).set("ugr_permissions",0)
                        .set("ugr_status",1);
                Db.save("user_group_relation",new_ugr);
                renderJson("{\"code\":200}");
            }catch (Exception e){
                renderJson("{\"code\":400}");
            }
        }else{
            renderJson("{\"code\":404}");
        }

    }

    public void get_group_info(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s,Map.class);
        String gro_id = map.get("gro_id").toString();
        Record group_info;
        int user_num;
        try{
            group_info = Db.findFirst("select thegroup.*,university.uni_name,users.u_nickname,users.u_photo,users.u_id,object.obj_name from thegroup,university,object,users where " +
                    "thegroup.gro_id="+gro_id+" and thegroup.gro_uni_id = university.uni_id and thegroup.gro_obj_id = object.obj_id and " +
                    "thegroup.gro_manager_id = users.u_id");
            user_num = Db.queryInt("select count(*) from user_group_relation where ugr_gro_id="+gro_id);
            group_info.set("gro_user_num",user_num);
            renderJson(group_info);
        }catch (Exception e){
            System.out.println("Exception: GroupController.getGroupList ");
            System.out.println(e);
        }
    }

    public void add_group_focus(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s,Map.class);
        User current_user = getSessionAttr("current_user");
        if(current_user==null||!current_user.get("u_id").toString().equals(map.get("u_id").toString())){
            renderJson("{\"code\":400}");
            return;
        }
        try {
            Record new_ugr = new Record().set("ugr_user_id", map.get("u_id").toString())
                    .set("ugr_gro_id", map.get("gro_id")).set("ugr_permissions", 1).set("ugr_status", 1);
            Db.save("user_group_relation", new_ugr);
            renderJson("{\"code\":200}");
        }catch (Exception e){
            System.out.println(e);
            renderJson("{\"code\":400}");
        }
    }

    public void remove_group_focus(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s,Map.class);
        User current_user = getSessionAttr("current_user");
        if(current_user==null||!current_user.get("u_id").toString().equals(map.get("u_id").toString())){
            renderJson("{\"code\":400}");
            return;
        }
        try {
            Db.delete("delete from user_group_relation where ugr_user_id =" +
                    map.get("u_id").toString() + " and ugr_gro_id=" + map.get("gro_id").toString());
            renderJson("{\"code\":200}");
        }catch (Exception e){
            System.out.println(e);
            renderJson("{\"code\":400}");
        }
    }

    public void if_in_group(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s,Map.class);
        User current_user = getSessionAttr("current_user");
        if(current_user==null||!current_user.get("u_id").toString().equals(map.get("u_id").toString())){
            renderJson("{\"code\":400}");
            return;
        }
        try {
            int num;
            num = Db.queryInt("select count(*) from user_group_relation where ugr_gro_id="+map.get("gro_id").toString()+" and ugr_user_id="+map.get("u_id").toString());
            if(num!=0){
                renderJson("{\"code\":200}");
            }
            else{
                renderJson("{\"code\":400}");
            }
        }catch (Exception e){
            System.out.println(e);
            renderJson("{\"code\":400}");
        }

    }

    public void get_group_users(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s,Map.class);
        String gro_id = map.get("gro_id").toString();
        List<Record> user_list_info;
        try{
            user_list_info = Db.find("select u_id,u_nickname,u_photo from users,user_group_relation where " +
                    "ugr_gro_id ="+gro_id+" and ugr_user_id=u_id");
            renderJson(user_list_info);
        }catch (Exception e){
            System.out.println("Exception: GroupController.get_group_users ");
            System.out.println(e);
        }
    }
}
