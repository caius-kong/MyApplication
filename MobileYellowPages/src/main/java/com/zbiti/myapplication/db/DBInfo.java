package com.zbiti.myapplication.db;

/**
 * Created by admin on 2016/3/13.
 */
public class DBInfo {
    /**
     * 数据库信息
     */
    public static class DB {
        public static final String DB_NAME = "myp.db";
        public static final int VERSION = 1;
    }

    /**
     * 电话表信息
     */
        public static class MOBILE_TABLE {
        public static final String TABLE_NAME = "tmobile";
        // 编号，图标，名称，电话，网址，地址，类型(快递、交通出行...)，是否热门(0/1)
        public static final String ID = "_id";
        public static final String ICON = "_icon";
        public static final String NAME = "_name";
        public static final String PHONE = "_phone";
        public static final String WEBSITE = "_website";
        public static final String ADDRESS = "_address";
        public static final String TYPE = "_type";
        public static final String ISHOT = "_isHot";

        public static final String CREATE_TABLE = "create table if not exists "
                + TABLE_NAME + "("
                + ID + " integer primary key autoincrement,"
                + ICON + " integer not null, "
                + NAME + " varchar(64) not null, "
                + PHONE+ " varchar(64) not null, "
                + WEBSITE+ " varchar(64), "
                + ADDRESS+ " varchar(64), "
                + TYPE+ " varchar(64) not null, "
                + ISHOT+ " varchar(64) not null default '0'"
                + ")";

        public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;
    }
}
