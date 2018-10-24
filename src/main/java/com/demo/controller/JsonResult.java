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

    public Object getCode() {
        return code;
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
