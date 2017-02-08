package com.zbiti.myapplication;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/3/31.
 */
public class TestClass extends InstrumentationTestCase {
    public void test1(){
        Gson gson = new Gson();
        List<User> users = new ArrayList<>();
        for(int i=0; i<3; i++){
            User user = new User(i,"jack" + i,"123" + i, "演员" + i);
            users.add(user);
        }
        // list --> string
        String json = gson.toJson(users);
        System.out.println("-json-->" + json);

        // string --> list
        List<User> list = gson.fromJson(json, new TypeToken<List<User>>() {}.getType());
        System.out.println("-list-->" + list);

        // user --> string
        String str = gson.toJson(users.get(0));
        System.out.println("-str-->" + str);

        // string --> User
        User user = gson.fromJson(str, User.class);
        System.out.println("-user-->" + user);

        String ss = "12345";
        System.out.println(ss.substring(1, ss.length()-1));
    }
}
