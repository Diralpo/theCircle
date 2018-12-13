package com.demo.model;

import com.jfinal.plugin.activerecord.Model;

public class Announcement extends Model<Announcement> {
    public static final Announcement dao = new Announcement().dao();
}
