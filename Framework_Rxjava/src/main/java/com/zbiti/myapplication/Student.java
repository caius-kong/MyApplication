package com.zbiti.myapplication;

import java.util.Set;

/**
 * Created by admin on 2016/5/24.
 */
public class Student {
    private String name;
    private Set<Course> courses;

    public Student(String name, Set<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}
