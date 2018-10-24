package com.demo.controller;

public class JsonResult {
    public Object code;
    public Object email;
    public Object password;
    public Object nickname;
    public Object school_id;

    public JsonResult(Object code,Object email,Object password,Object nickname,Object school_id){
        this.code = code;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.school_id = school_id;

    }
}
