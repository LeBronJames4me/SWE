package com.example.moappfinal_jachuisochui;



import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RoomSaleInfoAcitivy extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomsaleinfo);

        setTitle("내놓은 방 정보 보기");

        TextView name = (TextView)findViewById(R.id.name);
        TextView location = (TextView)findViewById(R.id.location);
        TextView description = (TextView)findViewById(R.id.description);
        TextView tel = (TextView)findViewById(R.id.tel);
        Button back = (Button)findViewById(R.id.btnback) ;
        Intent intent = getIntent();
        room r = intent.getParcelableExtra("roominfo");

        name.setText(r.getName());
        location.setText(r.getLocation());
        description.setText(r.getDescription());
        tel.setText(r.getTel());
    }

    public void onClick(View v)
    {
        Intent intent = getIntent();
        room r = intent.getParcelableExtra("roominfo");
        switch (v.getId())
        {
            case R.id.btnback:
                finish();
                break;

        }
    }


}
