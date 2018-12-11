package com.demo.controller;

import com.demo.model.TheObject;
import com.demo.model.University;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

public class UniversityController extends Controller {
    public void getUniversityList(){
        List<University> universities = University.dao.find("select * from university");
        renderJson(universities);
    }
}
