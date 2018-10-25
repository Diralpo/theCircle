package com.demo.config;

import java.io.FileInputStream;
import java.util.Properties;

import com.demo.controller.UserController;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.demo.model.User;

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
    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me){
        Properties property = new Properties();
        try {
            property.load(new FileInputStream("/config.properties"));
            String db_url = property.getProperty("DB_URL");
            String db_username = property.getProperty("DB_USERNAME");
            String db_pwd = property.getProperty("DB_PASSWORD");

            DruidPlugin dp = new DruidPlugin(db_url, db_username, db_pwd);
            me.add(dp);
            ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
            me.add(arp);
            arp.addMapping("users","id",User.class);
        }
        catch (Exception e){
            System.out.println("读取配置文件及登录数据库时出现错误");
            System.exit(-1);
        }
    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }


}