package com.demo.controller;

public class JsonResult {
    public Object code;
    public Object u_id;
    public Object email;
    public Object password;
    public Object nickname;
    public Object school_id;

    public JsonResult(Object code,Object u_id,Object email,Object password,Object nickname,Object school_id){
        this.code = code;
        this.u_id = u_id;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.school_id = school_id;

    }

    public Object getCode() {
        return code;
    }

    public Object getU_id() {
        return u_id;
    }

    public void setU_id(Object u_id) {
        this.u_id = u_id;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public Object getNickname() {
        return nickname;
    }

    public void setNickname(Object nickname) {
        this.nickname = nickname;
    }

    public Object getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Object school_id) {
        this.school_id = school_id;
    }
}
