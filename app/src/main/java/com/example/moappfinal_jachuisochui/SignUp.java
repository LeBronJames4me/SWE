package com.example.moappfinal_jachuisochui;

import android.content.ContentValues;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    /** DB관련 변수 모음 */
    String dbName = "st_file.db";
    int dbVersion = 3;
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase db;
    String tag = "SQLite";
    String tableName = "people";

    EditText EditText_Name; //입력받은 아이디
    EditText EditText_Password; //입력받은 비밀번호

    Button Button_Join; // 가입신청 버튼
    Button Button_Login; //가입한 아이디로 로그인

//    TextView TextView_Result; //결과창

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /***view들 연결***/

        EditText_Name = (EditText)findViewById(R.id.EditText_Name);
        //입력한 아이디
        EditText_Password = (EditText)findViewById(R.id.EditText_Password);
        //입력한 비밀번호

        Button_Join = (Button)findViewById(R.id.Button_Join);
        //가입신청 버튼
        Button_Login = (Button)findViewById(R.id.Button_Login);
        //바로 로그인 버튼

//        TextView_Result = (TextView)findViewById(R.id.TextView_Result);
//        // 결과 창

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


        /***가입 신청 버튼 리스너***/
        Button_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //가입신청 버튼 누르면 이래된다 메소드
                String name = EditText_Name.getText().toString();
                //입력한 아뒤
                String password = EditText_Password.getText().toString();
                //입력한 비번


                if("".equals(name)||"".equals(password))
                {
//                    TextView_Result.setText("회원가입 실패 - 공백문자불가");
                    return;
                    //테이블 삽입 안하고 걍 넘김
                }

                //공백이 없다면 인서트 시도 -> 이미 가입한 회원인가 확인 과정 필요
                insert(name,password);

            }


        });
        /**가입한 아이디로 바로 로그인 하기 버튼 리스너 */

        Button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 버튼 누르면 이래된다 메소드

                String inputname = EditText_Name.getText().toString();
                //입력한 아뒤
                String inputpassword = EditText_Password.getText().toString();
                //입력한 비번

                //키보드 내리기
                InputMethodManager imm =
                        (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

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
    void insert(String name,String password)
    {
        //people 테이블에 사용자가 입력한 name과 password 를 삽입한다.
        //이미 존재하는 아뒤일 경우  삽입 안함.

        ContentValues values = new ContentValues();
        //여기에 데이터 입력해서 삽입한다긔

        //name이 존재하는지 커서로 일일이 확인
        Cursor c = db.query(tableName, null, null, null, null, null, null);

        while(c.moveToNext())
        {
            String recordname = c.getString(1);
            //아이디 불러오긔

            if(recordname.equals(name))
            {
                //해당되는 아뒤가 있으면 토스트 박스 띄우고 리턴
                //키보드 내리기
                InputMethodManager imm =
                        (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                Toast.makeText(getApplicationContext(), "이미 존재하는 아이디입니다.", Toast.LENGTH_LONG).show();
                return;
            }
        }
        //해당되는 아뒤 없으면 나옴->삽입 시작
        values.put("name",name);
        values.put("password",password);

        int myroomid = 0;
        values.put("myroomid",myroomid);
        //일단 방 아뒤는 없는 상태니까 0으로 초기화

        long result = db.insert(tableName,null,values);
        Toast.makeText(getApplicationContext(), "회원가입 완료 - "+name+"님, 환영합니다!", Toast.LENGTH_LONG).show();

//        TextView_Result.setText(result+"번째 회원가입 성공:");
        select();
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
                Intent intent=new Intent(SignUp.this,SelectMenuActivity.class);
                intent.putExtra("name",name);
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
    void select()
    {
        Cursor c = db.query(tableName,null,null,null,null,null,null);

        while(c.moveToNext())
        {
            int id = c.getInt(0);
            //회원번호
            String name = c.getString(1);
            //회원 아뒤
            String password = c.getString(2);
            //회원비번
            int myroomid = c.getInt(3);
            //방번호

            Log.d(tag,"id"+id+", name:"+name+"password:"+password+"myroomid"
            +myroomid);

            //결과창 업뎃
//            TextView_Result.append("\n"+"회원번호: "+id+"회원아뒤"+name+"회원비번"+password+"디폴트방"+myroomid);

            //키보드 내리기
            InputMethodManager imm =
                    (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
}
