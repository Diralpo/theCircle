package com.demo.controller;

import com.demo.model.Announcement;
import com.demo.model.Group;
import com.demo.model.User;
import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
import java.util.Map;

public class AnnounceController extends Controller {
    public void createAnnouncement() {
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        User current_user = getSessionAttr("current_user");
        if (current_user == null || !current_user.get("u_id").toString().equals(map.get("u_id").toString())) {
            System.out.println("ERROR createAnnouncement");
            renderJson("{\"code\":400}");
            return;
        }
        Group res_list = Group.dao.findFirst("select * from thegroup where gro_id =" + map.get("gro_id"));
        if (res_list != null) {
            if ((int) current_user.get("u_permissions") <= 255 && !res_list.get("gro_manager_id").toString().equals(
                    current_user.get("u_id").toString())) {
                // 若发送者不为小组管理者，则查找其在小组中权限位
                Record res = Db.findFirst("select * from user_group_relation " +
                        "where ugr_user_id =" + current_user.get("u_id") + " and ugr_gro_id=" + map.get("gro_id"));
                if (res == null || (int) res.get("ugr_permissions") <= 255) {
                    // 若检查其在小组中的权限不满足条件，结束程序
                    renderJson("{\"code\":400}");
                    return;
                }
            }
            // 进入此分支，则用户为小组管理员或权限高
            Record new_announce = new Record().set("ann_status", 0).set("ann_creator_id",
                    current_user.get("u_id")).set("ann_gro_id", map.get("gro_id")).set("ann_details",
                    map.get("details")).set("ann_title", map.get("title"));
            //System.out.println(map);
            boolean success = Db.save("announcement", "ann_id", new_announce);
            if (success) {
                renderJson("{\"code\":200}");
                return;
            }
        }
        renderJson("{\"code\":400}");
    }

    public void getAnnouncement() {
        String s = HttpKit.readData(getRequest());
        Map map = new Gson().fromJson(s, Map.class);
        User current_user = getSessionAttr("current_user");
        if (current_user == null || !current_user.get("u_id").toString().equals(map.get("u_id").toString())) {
            renderJson("{\"code\":400}");
            return;
        }
        //System.out.println("查询");
        String sql = "select ann_id, ann_create_time, ann_title, " +
                "ann_details, u_nickname from announcement,users where announcement.ann_creator_id=users.u_id and " +
                "ann_gro_id=" + map.get("gro_id") + " order by ann_create_time";
        //System.out.println(sql);
        List<Record> ann_res = Db.find(sql);
        renderJson(ann_res);
    }
}
