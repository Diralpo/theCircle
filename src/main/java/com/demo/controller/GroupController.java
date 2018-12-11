package com.demo.controller;

import com.demo.model.Group;
import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

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
}
