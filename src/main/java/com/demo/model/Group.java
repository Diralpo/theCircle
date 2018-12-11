package com.demo.model;

import com.jfinal.plugin.activerecord.Model;

public class Group extends Model<Group>{
    public  static  final Group dao = new Group().dao();
}
