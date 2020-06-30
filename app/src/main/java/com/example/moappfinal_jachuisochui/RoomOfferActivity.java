package com.example.moappfinal_jachuisochui;



import android.content.DialogInterface;
import android.content.Intent;
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

public class RoomOfferActivity extends AppCompatActivity {
    ArrayList<String> roomdata = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ArrayList<room> roomlist = new ArrayList<room>();
    ListView listView;
    //final int ROOM_INFO = 21;
    final int NEW_ROOM = 22;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomoffer);

        tv = (TextView)findViewById(R.id.tv);

        setTitle("내놓은 방 리스트");
        setListView();
    }

    public void onClick(View v)
    {
        Intent intent = new Intent(RoomOfferActivity.this,RoomSaleActivity.class);
        intent.putExtra("roomlist",roomdata);
        startActivityForResult(intent,NEW_ROOM);
    }

    public void setListView() {
        listView = (ListView) findViewById(R.id.listview);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomdata);
        listView.setAdapter(adapter);

        //꾹 눌렀을때 삭제
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                AlertDialog.Builder dlg = new AlertDialog.Builder(view.getContext());
                dlg.setTitle("삭제확인")
                        .setIcon(R.drawable.room)
                        .setMessage("내놓은 방을 삭제 하시겠습니까?")
                        .setNegativeButton("취소", null)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                roomdata.remove(position);
                                roomlist.remove(position);
                                adapter.notifyDataSetChanged();
                                tv.setText("내놓은 방 리스트(" + roomdata.size() + "개)");
                            }
                        })
                        .show();
                return true;
            }
        });


        //클릭시 상세정보가 나타남
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RoomOfferActivity.this, RoomSaleInfoAcitivy.class);
                room r = roomlist.get(position);
                intent.putExtra("roominfo", r);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == NEW_ROOM) {
            if (resultCode == RESULT_OK) {
                room r = data.getParcelableExtra("newroom");
                roomdata.add(r.getName());
                roomlist.add(r);
                adapter.notifyDataSetChanged();
                tv.setText("내놓은 방 리스트(" + roomdata.size() + "개)");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
