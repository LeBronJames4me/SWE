package com.example.moappfinal_jachuisochui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    { //오픈헬퍼 생성자
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_people =
                //회원정보 table (people)
                // : id(정수)/이름(string)/비번(string)/찜방(정수)
                "CREATE TABLE people" +
                        "(id INTEGER PRIMARY KEY autoincrement,"+
                        "name TEXT,"+
                        "password TEXT,"+
                        "myroomid INTEGER);";

        db.execSQL(sql_people);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF exists people;";
        db.execSQL(sql);
    }
}
