package com.example.moappfinal_jachuisochui;



import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RoomSaleActivity extends AppCompatActivity {
    EditText name,location,description,tel;
    room r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomsale);

        setTitle("방 내놓기");

        name = (EditText)findViewById(R.id.name);
        location = (EditText)findViewById(R.id.location);
        description = (EditText)findViewById(R.id.description);
        tel = (EditText)findViewById(R.id.tel);


    }

    public void onClick(View v)
    {
        if (v.getId() == R.id.btnCancel)
        {
            finish();
        }
        else
        {
            r = new room(name.getText().toString(),
                    location.getText().toString(),
                    description.getText().toString(),
                    tel.getText().toString()
            );


            Intent intent = getIntent();
            intent.putExtra("newroom",r);
            setResult(RESULT_OK,intent);
            finish();
        }
    }



}
