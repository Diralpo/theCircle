package com.demo.model;

import com.jfinal.plugin.activerecord.Model;

public class TheMessage extends Model<TheMessage> {

    public static final TheMessage dao = new TheMessage().dao();
}
