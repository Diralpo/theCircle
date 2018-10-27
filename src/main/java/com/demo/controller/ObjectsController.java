package com.demo.controller;

import com.jfinal.core.Controller;

import com.demo.model.TheObject;

import java.util.List;

public class ObjectsController extends Controller {

    public void seekObject() {
        System.out.println("begin");
        List<TheObject> objects = TheObject.dao.find("select obj_name,obj_href,obj_img_href,obj_type from object where obj_status=1 order by obj_last_modify_time");
        System.out.println(objects);
        renderJson(objects);
    }
}
