package com.zbiti.myapplication.db;

/**
 * Created by kongyunhui on 2017/1/12.
 */

public class DBInfos {
    /**
     * 数据库信息
     */
    public static class DB {
        public static final String DB_NAME = "sqlitedemo.db";
        public static final int VERSION = 1;
    }


    /**
     * 用户表信息
     */
    public static class USER_TABLE {
        public static final String TABLE_NAME = "user";

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String PHONE = "phone";

        public static final String CREATE_TABLE = "create table if not exists "
                + TABLE_NAME + "("
                + ID + " integer primary key autoincrement,"
                + NAME + " varchar(20) not null, "
                + PHONE+ " varchar(11) not null "
                + ")";

        public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;

        // 删除表中某个字段：1、创建临时表；2、把旧表数据insert到临时表；3、删除旧表；重命名临时表
    }
}
