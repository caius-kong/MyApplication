package com.zbiti.myapplication.entity;

import java.io.Serializable;

/**
 * Created by admin on 2016/5/25.
 */
public class User implements Serializable{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
