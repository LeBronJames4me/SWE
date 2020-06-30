package com.example.moappfinal_jachuisochui;



import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class JjimRoomListActivity extends AppCompatActivity {
    ArrayList<String> roomdata = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ArrayList<room> roomlist = new ArrayList<room>();
    ListView listView;
    //final int ROOM_INFO = 21;
    final int NEW_ROOM = 22;
    TextView tv;
    String username;
    double latitude,longitude;

    /** DB관련 변수 모음 */
    String dbName = "rm_file.db";
    int dbVersion = 3;
    private MySQLiteOpenHelper2 helper;
    private SQLiteDatabase db;
    String tag = "SQLite";
    String tableName = "rooms";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jjimroomlist);

        tv = (TextView)findViewById(R.id.tv);

        //  username으로 인텐트 받음

        /***번들 받기 - 유저 아이디 받기 ***/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        username =bundle.get("name").toString();
        Toast.makeText(getApplicationContext(), username+"가 찜한 방 목록", Toast.LENGTH_LONG).show();

        /**DB관련**/
        // sqlite open helper 등장
        helper = new MySQLiteOpenHelper2(
                this, //현재 제어권자
                dbName, //rm_file.db
                null, //커서 웅앵웅
                dbVersion // 버전: 3
        );

        //db 불러오기***/
        try{
            db = helper.getWritableDatabase();
            //db이름 - 저장된 db를 불러온다.(SQLiteDatabase db)
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            Log.e(tag,"데이터 베이스를 열 수 없음ㅠ"); //tag: SQLite
            finish();
        }


        setTitle("내가 찜한 방 보기");
        setListView();



    }

    /**리스트 뷰 관련*/
    public void setListView() {
        listView = (ListView) findViewById(R.id.listview);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomdata);
        listView.setAdapter(adapter);


        /**디비 뒤져서 해당 유저가 찜한 방 있는지 보고 있으면 add -> user name 받아야 함*/
        /**선택한 방의 정보(마커로 선택해야 하니깐 위경도)를 DB에서 불러와 jjimuser를 업데이트한다*/


            Cursor c = db.query(tableName,null,null,null,null,null,null);

            while(c.moveToNext())
            {
                //찜한 방이 있다면~
                if (c.getString(7).equals(username))
                {
//                //해당되는 찜방이 있다면~
                    /**새 방 추가해서 그거 리스트 뷰에 보이는 것까지만. 해보기*/
                    room r = new room(c.getString(1),c.getString(2),c.getString(3),c.getString(8));
                    roomdata.add(r.getName());
                    roomlist.add(r);
                    adapter.notifyDataSetChanged();
                }
            }
        listView.setAdapter(adapter);

        tv.setText("내가 찜한 방 보기(" + roomdata.size() + "개)");

        //클릭시 상세정보가 나타남
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(JjimRoomListActivity.this, JjimInfoActivity.class);
                //해당 방의 위경도 찾아서 latitude랑 longitude에 저장
                room r = roomlist.get(position);
                String roomname = new String( r.getName());
                searchandupdate(roomname);
                intent.putExtra("username",username);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivity(intent);
            }
        });

    }
    void searchandupdate(String roomname)
    {

        Cursor c = db.query(tableName,null,null,null,null,null,null);

        while(c.moveToNext())
        {
            //방고유번호 - c.getInt(0)
            if(c.getString(1).equals(roomname))
            {
                latitude = c.getDouble(4);
                longitude=c.getDouble(5);
                return;
            }
        }
    }


}
