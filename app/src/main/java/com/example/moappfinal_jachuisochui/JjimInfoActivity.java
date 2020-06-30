package com.example.moappfinal_jachuisochui;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class JjimInfoActivity extends AppCompatActivity  {

    /** DB관련 변수 모음 */
    String dbName = "rm_file.db";
    int dbVersion = 3;
    private MySQLiteOpenHelper2 helper;
    private SQLiteDatabase db;
    String tag = "SQLite";
    String tableName = "rooms";


    /**뷰들 모음*/

    //이전 액티비티에서 받아온 사용자 이름,
    // 사용자 위도, 사용자 경도 관련 변수들
    //ooo님 환영함니다 - 선택 위도 , 선택 경도
    String username;
    TextView TextView_Longitude;
    double latitude,longitude;
    TextView TextView_Latitude;
    Button Button_call;

    //rooms 테이블 뷰 관련 변수들
    TextView TextView_Roomname; //방이름
    TextView TextView_Roomposition; //방위치
    TextView TextView_Roomdesc; //방설명
    TextView TextView_Ownername; //소유자 아뒤
    TextView TextView_Phonenumber; //관리자연락처
    ImageView ImageView_Roomphoto; //방 사진 (imageview, blob)





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jjiminfo);
        setTitle("내가 찜한 방");

        /**뷰들 연결*/
        //이전 액티비티에서 받아온 유저명, 유저선택 위도, 유저 선택 경도, 버튼
        Button_call = (Button)findViewById(R.id.Button_call);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        String username = intent.getStringExtra("username");
        Log.d("intent", username);
        latitude = intent.getDoubleExtra("latitude", 0.0);
        Log.d("intent", "" + latitude);
        longitude = intent.getDoubleExtra("longitude", 0.0);
        Log.d("intent", "" + longitude);
//
//
//        username=bundle.get("username").toString();
//        String lat = bundle.get("latitude").toString();
//        latitude = Double.parseDouble(lat);
//        String lon = bundle.get("longitude").toString();
//        longitude = Double.parseDouble(lon);

//        TextView_Latitude.setText(lat);
//        TextView_Longitude.setText(lon);

        //방 정보 보여줄때 사용되는 것들->roomstable에서 삽입하고 그걸 보여준다.
         TextView_Roomname = (TextView)findViewById(R.id.TextView_Roomname); //방이름
         TextView_Roomposition = (TextView)findViewById(R.id.TextView_Roomposition);//방위치
         TextView_Roomdesc = (TextView)findViewById(R.id.TextView_Roomdesc);//방설명

         TextView_Ownername = (TextView)findViewById(R.id.TextView_Ownername); //소유자 아뒤
         TextView_Phonenumber = (TextView)findViewById(R.id.TextView_Phonenumber); //관리자연락처
         ImageView_Roomphoto = (ImageView)findViewById(R.id.ImageView_RoomPhoto); //방 사진 (imageview, blob)

        /***sqlite open helper 등장***/
        helper = new MySQLiteOpenHelper2(
                this, //현재 제어권자
                dbName, //rm_file.db
                null, //커서 웅앵웅
                dbVersion // 버전: 3
        );

        /***db 불러오기***/
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

        //보여주기 -> 사진은 아직 디폴트 =>성공!
        select(latitude, longitude);

        /**버튼 누르면 부동산에 전화하기*/
        //받은 전화번호 tel에 저장
        String tel = new String("tel:" + TextView_Phonenumber.getText().toString());

        Button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });

        /**버튼 누르면 방 디비 업데이트 하기 -> 위도 경도로 찾은 다음에 username으로 jjimuser 업데이트 한다.*/
        String finalUsername = username;
    }


    /**선택한 방의 정보(마커로 선택해야 하니깐 위경도)를 DB에서 불러와 보여준다.*/
    void select(double latitude,double longitude)

    {

        Cursor c = db.query(tableName,null,null,null,null,null,null);

        while(c.moveToNext())
        {
            //방고유번호 - c.getInt(0)
            if ((c.getDouble(4)==latitude)&&(c.getDouble(5)==longitude))
            {
//                Toast.makeText(getApplicationContext(), "탐색성공!", Toast.LENGTH_LONG).show();

                //해당되는 방이 있다면~

                //방이름
                String roomname = c.getString(1);
                TextView_Roomname.setText(roomname);

                //방위치
                String roomposition = c.getString(2);
                TextView_Roomposition.setText(roomposition);

                //방설명
                String roomdesc = c.getString(3);
                TextView_Roomdesc.setText(roomdesc);

                //방 위도,경도 ->텍스트 세팅을 위하여 string으로 변환
                String roomlatitude = Double.toString(latitude);
                String roomlongitude = Double.toString(longitude);

                //소유자 이름
                String ownername = c.getString(6);
                TextView_Ownername.setText(ownername);
                //찜한 유저 이름
                String jjimusername = c.getString(7);

                //관리자 연락처
                String phonenumber = c.getString(8);
                TextView_Phonenumber.setText(phonenumber);
                //사진은 담에...

                /****방 아이디에 따라서 다른 사진 넣기***/
                //방 id %10 +1
                //1~10
                switch (((c.getInt(0))%10)+1)
                {
                    case (1):
                        ImageView_Roomphoto.setImageResource(R.drawable.one);
                        break;
                    case 2:
                        ImageView_Roomphoto.setImageResource(R.drawable.two);
                        break;
                    case 3:
                        ImageView_Roomphoto.setImageResource(R.drawable.three);
                        break;
                    case 4:
                        ImageView_Roomphoto.setImageResource(R.drawable.four);
                        break;
                    case 5:
                        ImageView_Roomphoto.setImageResource(R.drawable.five);
                        break;
                    case 6:
                        ImageView_Roomphoto.setImageResource(R.drawable.six);
                        break;
                    case 7:
                        ImageView_Roomphoto.setImageResource(R.drawable.seven);
                        break;
                    case 8:
                        ImageView_Roomphoto.setImageResource(R.drawable.eight);
                        break;
                    case 9:
                        ImageView_Roomphoto.setImageResource(R.drawable.nine);
                        break;
                    case 10:
                        ImageView_Roomphoto.setImageResource(R.drawable.ten);
                        break;
                    default:
                        break;
                }
                return;
            }


        }
    }

}
