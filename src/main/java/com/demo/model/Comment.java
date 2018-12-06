package com.demo.model;
import com.jfinal.plugin.activerecord.Model;

public class Comment extends Model<Comment>{
    public static final Comment dao = new Comment().dao();
}
