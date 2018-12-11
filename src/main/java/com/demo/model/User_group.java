package com.demo.model;

import com.jfinal.plugin.activerecord.Model;

public class User_group extends Model<User_group> {
    public static final User_group dao = new User_group().dao();
}
