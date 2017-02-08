package com.zbiti.myapplication;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by kongyunhui on 2017/1/23.
 */

/**
 * 注解的方式运行多个测试类，执行空类MathTestSuite可以了！
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ExampleUnitTest.class
})
public class MathTestSuite {
}
