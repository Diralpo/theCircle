package com.demo.controller;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
import java.util.Map;

public class AnnouncementController extends Controller {
    public void get_announce_list(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        String gro_id = map.get("gro_id").toString();

        List<Record> announce_list_info;

        try{
            announce_list_info = Db.find("select announcement.*,users.u_nickname from announcement,users where ann_gro_id="+gro_id+
            " and ann_creator_id=u_id");
            renderJson(announce_list_info);
        }catch (Exception e){
            System.out.println("Exception: AnnouncementController.get_announce_list ");
            System.out.println(e);
        }
    }
    public void search_announce(){
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        String ann_id = map.get("ann_id").toString();

        Record announce_info;

        try{
            announce_info = Db.findFirst("select * from announcement where ann_id="+ann_id);
            renderJson(announce_info);
        }catch (Exception e){
            System.out.println("Exception: AnnouncementController.search_announce ");
            System.out.println(e);
        }
    }
}
