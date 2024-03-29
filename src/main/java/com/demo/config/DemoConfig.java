package com.demo.config;

import com.demo.controller.*;
import com.demo.model.*;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class DemoConfig extends JFinalConfig {

    public static void main(String args[]) {
        JFinal.start("src/main/webapp", 80, "/");
    }

    @Override
    public void configConstant(Constants me) {
        me.setDevMode(true);
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", UserController.class);
        me.add("/object", ObjectsController.class);
        me.add("/message", MessageController.class);
        me.add("/university", UniversityController.class);
        me.add("/group",GroupController.class);
        me.add("/announcement", AnnounceController.class);
    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {
        Properties property = new Properties();
        try {
            property.load(new FileInputStream(new File("src/main/WEB-INF\\classes\\config.properties")));
            String db_url = property.getProperty("DB_URL");
            String db_username = property.getProperty("DB_USERNAME");
            String db_pwd = property.getProperty("DB_PASSWORD");

            DruidPlugin dp = new DruidPlugin(db_url, db_username, db_pwd);
            me.add(dp);
            ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
            me.add(arp);
            arp.addMapping("users", "u_id", User.class);
            arp.addMapping("object", "obj_id", TheObject.class);
            arp.addMapping("comment","com_id",Comment.class);
            arp.addMapping("message","pm_id", TheMessage.class);
            arp.addMapping("university","uni_id", University.class);
            arp.addMapping("thegroup","gro_id",Group.class);
            arp.addMapping("user_group_relation","ugr_user_id",User_group.class);
            arp.addMapping("announcement","ann_id",Announcement.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }


}