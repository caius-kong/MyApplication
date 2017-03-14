package com.zbiti.myapplication;

/**
 * Created by kongyunhui on 2017/2/13.
 */

public class Test {
    static{
        i=2;
//        System.out.print(i);// 非法向前引用(静态语句块中只能访问到定义在静态语句之前的变量,定义它之后的变量,可以赋值,但不能访问)
    }
    static int i=1;

    @org.junit.Test
    public void test1(){
        System.out.println(Test.i);
    }
}
