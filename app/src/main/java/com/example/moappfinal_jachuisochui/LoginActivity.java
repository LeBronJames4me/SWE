package com.example.moappfinal_jachuisochui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    String dbName = "st_file.db";
    int dbVersion = 3;
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase db;
    String tag = "SQLite";
    String tableName = "people";

    EditText EditText_InputName; //로그인시도 아이디
    EditText EditText_InputPassword; //로그인시도 비밀번호

    Button Button_Join; // 가입신청 버튼 ->회원가입 항목으로 이동
    Button Button_Login; //로그인 버튼 -> 조회함서 비교 후 temp로 이동


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /***view들 연결***/

        EditText_InputName = (EditText)findViewById(R.id.EditText_InputName);
        //로그인시도 아이디
        EditText_InputPassword = (EditText)findViewById(R.id.EditText_InputPassword);
        //로그인시도 비밀번호

        Button_Join = (Button)findViewById(R.id.Button_Join);
        //가입신청 버튼
        Button_Login = (Button)findViewById(R.id.Button_Login);
        //로그인 버튼

        /***sqlite open helper 등장***/
        helper = new MySQLiteOpenHelper(
                this, //현재 제어권자
                dbName, //st_file.db
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


        /***회원가입 신청 버튼 누르면 회원가입창으로 이동ㅇ한다.***/
        Button_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUp.class);
                startActivity(intent);
            }

        });

        /***로그인 버튼 누르면 search로 비교한다:
        회원정보 있으면 다음화면(temp -> 성ㅅ공시 회원정보 번들로 전달까지 ,)
                없으면 토스트 박스 띄우면서 반려***/

        Button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 버튼 누르면 이래된다 메소드

                String inputname = EditText_InputName.getText().toString();
                //입력한 아뒤
                String inputpassword = EditText_InputPassword.getText().toString();
                //입력한 비번


                if("".equals(inputname))
                {
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_LONG).show();
                    return;
                    //비교 안하고 걍 넘김
                }
                if("".equals(inputpassword))
                {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_LONG).show();
                    return;
                    //비교 안하고 걍 넘김
                }

                //공백이 없다면 비교 시도 -> 로그인 성공시 다음화면, 실패시 토스트박스.
                search(inputname,inputpassword);
            }
        });

    }

    void search(String inputname,String inputpassword)
    {
        //해당되는 아뒤와 비번이 잇다면 다음 액티비티로 이동, 없다면 토스트박스 띄우며 리턴.
        Cursor c = db.query(tableName,null,null,null,null,null,null);

        while(c.moveToNext())
        {
            int id = c.getInt(0);
            //회원번호
            String name = c.getString(1);
            //회원 아뒤
            String password = c.getString(2);
            //회원비번

            Log.d(tag,"id"+id+", name:"+name+"password:"+password);

            if(name.equals(inputname)&&password.equals(inputpassword))
            {

                //이름과 아뒤 둘다 똑같으면 다음 액티비티 이동->아뒤 전달; 디비에서 찾기 위함.
                Intent intent=new Intent(LoginActivity.this,SelectMenuActivity.class);
                intent.putExtra("name",name);
                Toast.makeText(getApplicationContext(), "로그인성공 - 유저"+name, Toast.LENGTH_LONG).show();
                startActivity(intent);
                return;
            }
            //키보드 내리기
            InputMethodManager imm =
                    (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        //회원정보 없는디?->토스트박스 띄우며 리턴.
        Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 다시 확인하세요.", Toast.LENGTH_LONG).show();
        return;

    }
}
