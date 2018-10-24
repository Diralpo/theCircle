package com.demo.config;

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
    public void configPlugin(Plugins me) {
        DruidPlugin dp = new DruidPlugin("jdbc:mysql://localhost:3306/circle?useSSL=false",
                "root","123456");
        me.add(dp);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        me.add(arp);
        arp.addMapping("user","id",User.class);
    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }

}
