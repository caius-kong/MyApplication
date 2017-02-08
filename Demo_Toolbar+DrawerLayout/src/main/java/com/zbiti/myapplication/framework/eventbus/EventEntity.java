package com.zbiti.myapplication.framework.eventbus;

import java.io.Serializable;

/**
 * Created by admin on 2016/6/17.
 */
public class EventEntity implements Serializable{
    private String type;
    private String arg1;
    private String arg2;
    private Object obj;

    public EventEntity(String type) {
        this.type = type;
    }

    public EventEntity(String type, String arg1) {
        this.type = type;
        this.arg1 = arg1;
    }

    public EventEntity(String type, String arg1, Object obj) {
        this.type = type;
        this.arg1 = arg1;
        this.obj = obj;
    }

    public EventEntity(String type, String arg1, String arg2, Object obj) {
        this.type = type;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.obj = obj;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }
}
