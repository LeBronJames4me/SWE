package com.example.moappfinal_jachuisochui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper2 extends SQLiteOpenHelper {

    public MySQLiteOpenHelper2(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    { //오픈헬퍼 생성자
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql_rooms =
                //방정보 테이블
                // : id(정수,순차적 증가,PK)/방이름(string)/방위치(string)/방설명(string)/위도(double)/경도(double)/
                // : 관리자(string)/찜한 유저 이름(string)/관리자연락처(string)/사진(blob)
                "CREATE TABLE rooms" +
                        "(roomid INTEGER PRIMARY KEY autoincrement," +
                        "roomname TEXT," +
                        "roomposition TEXT," +
                        "roomdesc TEXT," +
                        "roomlatitude DOUBLE," +
                        "roomlongitude DOUBLE," +
                        "ownername TEXT," +
                        "jjimusername TEXT," +
                        "phonenumber TEXT);" ;
        db.execSQL(sql_rooms);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF exists rooms";
        db.execSQL(sql);
    }
}
