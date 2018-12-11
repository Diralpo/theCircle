package com.demo.model;

import com.jfinal.plugin.activerecord.Model;

public class University extends Model<University> {

    public static final University dao = new University().dao();
}
