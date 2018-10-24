package com.demo.controller;

public class JsonResult {
    public int code;
    public String email;
    public String password;
    public String nickname;
    public int school_id;

    public JsonResult(int code,String email,String password,String nickname,int school_id){
        this.code = code;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.school_id = school_id;

    }
}
