package com.example.moappfinal_jachuisochui;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SelectMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectmenu);

        String username;

        setTitle("자취소취할수있취");

        /***번들 받기 - 유저 아이디 받기 ***/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        username =bundle.get("name").toString();
//        Toast.makeText(getApplicationContext(), "메뉴선택 - "+username, Toast.LENGTH_LONG).show();


        /**유저 아이디 전달 -> 방구하기에서 찜할때 사용.*/
        Intent intent2= new Intent(SelectMenuActivity.this, FindingRoomActivity.class);
        intent2.putExtra("name",username);
        /**유저 아이디 사용 -> 찜 목록에서 디비 뒤질때 사용*/
        Intent intent4= new Intent(SelectMenuActivity.this, JjimRoomListActivity.class);
        intent4.putExtra("name",username);


        Button seek = (Button)findViewById(R.id.seek);
        Button offer = (Button)findViewById(R.id.offer);
        Button wish = (Button)findViewById(R.id.wish);


        //방 구하기 - 유저 아이디 전달
        seek.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                startActivity(intent2);
            }

        });

        //방 내놓기
        offer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent3 = new Intent(getApplicationContext(), RoomOfferActivity.class);
               startActivity(intent3);
            }
        });
        //내가 찜한 방
        //연결됏나 확인하고 ->확인
        //이름 넘겨주기
        wish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(intent4);
            }
        });


    }


}
