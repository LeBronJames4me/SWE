package com.example.moappfinal_jachuisochui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class NearRoomActivity extends AppCompatActivity implements OnMapReadyCallback {

    /** DB관련 변수 모음 */
    String dbName = "rm_file.db";
    int dbVersion = 3;
    private MySQLiteOpenHelper2 helper;
    private SQLiteDatabase db;
    String tag = "SQLite";
    String tableName = "rooms";

    /**뷰 변수 모음**/
    //전달받은 유저명
    String username;

    //전달받은 중심지정
    LatLng LatLng_Center;
    Marker Marker_Center;

    String centerlat;
    double centerlatitude;

    String centerlon;
    double centerlongitude;

    //지도
    MapFragment mapFragment;

    //다음화면 이동
    Button Button_Next;

    //마커가 설정한 위경도 넘겨줄 것.
    LatLng LatLng_Selected;
    Marker Marker_SelectedLocation;
    InfoWindow infoWindow;

    //전체 방 정보.
    LatLng LatLng_Record;
    Marker Marker_Record;
    Marker Marker_Record_blue;
    Marker Marker_Record_gray;
    int recordNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearroom);
        setTitle("방 구하기");
        /***뷰 연결하기***/
        //여기에 보여질 것 - 인텐트로 받은 유저 아이디.
        Button Button_Next = (Button)findViewById(R.id.Button_Next);
        //누르면 이 유저 아이디를 전달하며 다음 액티비티로 이동.
        int dbflag = 0;

        /**인텐트 관련 **/

        /***번들 받기 - 유저 아이디 받기 ***/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        username =bundle.get("username").toString();

        /**번들받기 - 중심지점 받기 -> LatLng_Center 초기화.*/
        //위도 (from string to double)
       centerlat = bundle.get("latitude").toString();
       centerlatitude= Double.parseDouble(centerlat);

       //경도 - from string to double
        centerlon= bundle.get("longitude").toString();
        centerlongitude = Double.parseDouble(centerlon);

        LatLng_Center = new LatLng(centerlatitude,centerlongitude);


        /**버튼 누르면 유저 이름과 마커의 위경도 전달하면서 다음 액티비티로 넘어감. 이떄 토스트박스 띄워야 함.**/
        //일단은 템프로 (오버레이 성공 및 디비 성공시 룸인포액티비티로 넘어가유)
        Button_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NearRoomActivity.this, RoomInfoActivity.class);
                intent.putExtra("username",username);
                double latitude = LatLng_Selected.latitude;
                double longitude = LatLng_Selected.longitude;
                //선택한 지점의 위경도 또한 넘겨준다.
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                if(latitude==0.0 ||longitude==0.0) {
                    Toast.makeText(getApplicationContext(), "선택된 방이 없습니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
//                    Toast.makeText(getApplicationContext(), "위도: "+latitude+" 경도: "+longitude,Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            }
        });


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

        /**데이터 삽입**/
        /**db에 방 정보 저장하고 한번 띄워보기 시도...-> 현재 디비에 하나라도 없다면 초기화 됏다는 뜻이니까 다시 해줌.*/
        Cursor c = db.query(tableName,null,null,null,null,null,null);

        while(c.moveToNext())
        {
            if ((c.getDouble(4)==35.8921912)&&(c.getDouble(5)==128.608295))
            {
//                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                dbflag = 1;
                break;
            }
            else
            {
                dbflag = 0;
            }


        }

        if(dbflag==0) {
            //초기화댐 ㅠ
//            Toast.makeText(getApplicationContext(), "디비 손실 - 재 삽입 시작", Toast.LENGTH_LONG).show();

            insert("산격빌", "북구 산격동 1120-58번지", "산격초앞", 35.8942833, 128.606843, "유명부동산",
                    "null", "0538620419");
            insert("명성빌", "북구 침산동 118번지", "이스라엘사람 많고 시끄러움", 35.8917326, 128.605529, "유명부동산",
                    "null", "0538620419");
            insert("가람빌", "북구 가람동 1158번지", "조용하고 맛집많음", 35.8885678, 128.602287, "유명부동산",
                    "null", "0538620419");
            insert("애니빌", "북구 대현동 1120-38번지", "비둘기 많음", 35.8860223, 128.603287, "유명부동산",
                    "null", "0538620419");
            insert("충현빌", "북구 복현동 1148-8번지", "술집 많음, 지하철역 근처", 35.8859412, 128.605988, "유명부동산",
                    "null", "0538620419");
            insert("청구빌", "동구 신암동 115-8번지", "중국인 많음, 복층구조", 35.8612527, 128.601737, "유명부동산",
                    "null", "0538620419");
            insert("경대빌", "북구 침산동 111-10번지", "공과금 면제, 복도에 정수기 있음", 35.8861565, 128.6101, "준희부동산",
                    "null", "0535453535");
            insert("대흥빌", "북구 산격동 1-120번지", "낮에 시끄럽고 밤에 조용함, 보증금 협의 가능", 35.92532, 128.551864, "준희부동산",
                    "null", "0535453535");
            insert("신화빌", "북구 산격동 2-156번지", "차가 많이 다니고 시끄러움", 35.884784, 128.616515, "준희부동산",
                    "null", "0535453535");
            insert("치안빌", "북구 대현동 18-164번지", "개 많이 있고 고양이 많음", 35.885773, 128.616515, "준희부동산",
                    "null", "0535453535");
            insert("행복빌", "북구 산격동 17-120번지", "방특집", 35.8880619, 128.616517, "준희부동산",
                    "null", "0535453535");
            insert("신암빌", "북구 대현동 84-4번지", "넓음 다섯명 수용 가능", 35.8840709, 128.612417, "현희부동산",
                    "null", "0537724444");
            insert("태권빌", "북구 산격동 2302번지", "투룸, 두명이 살기 적당", 35.8951267, 128.605088, "현희부동산",
                    "null", "0537724444");
            insert("신성빌", "북구 산격동 2230-1번지", "채광 좋아서 빨래 잘마름", 35.8789887, 128.613742, "현희부동산",
                    "null", "0537724444");
            insert("파바빌", "북구 산격동 126-53번지", "동향 구조, 이층침대 있음", 35.8921912, 128.608295, "현희부동산",
                    "null", "0537724444");
            insert("경대아파트", "북구 대현동 5-3330번지", "북향 구조, 적당히 시원함", 35.8895567, 128.617268, "유빈부동산",
                    "null", "0538650429");
            insert("경진빌", "북구 대현동 15-112번지", "쓰리룸 반전세", 35.8918805, 128.617348, "유빈부동산",
                    "null", "0538650429");
            insert("복현빌", "북구 침산동 17-25번지", "친구소개시 월세 10% 감면", 35.8910743, 128.618328, "유빈부동산",
                    "null", "0538650429");
            insert("경원빌", "북구 전민동 1120-58번지", "파키스탄 사람 많음, 술집 많음, 원룸", 35.885738, 128.611066, "유빈부동산",
                    "null", "0538650429");
            insert("정문빌", "동구 신암동 1128번지", "냉장고 없음", 35.8850331, 128.613909, "유빈부동산",
                    "null", "0538650429");
            insert("텍문빌", "북구 복현동 11-25번지", "길거리에 개똥많음, 인도인 많음, 방넓음", 35.8895567, 128.617268, "유빈부동산",
                    "null", "0538650429");
            insert("동양빌", "북구 대현동 11211번지", "깨끗하고 아늑함", 35.8947685, 128.615679, "유빈부동산",
                    "null", "0538650429");
            insert("복우빌", "동구 신암동 12-8번지", "이전에 5년 살았음", 35.895874, 128.613885, "유빈부동산",
                    "null", "0538650429");

        }



        /**지도 관련***/
        //지도 프래그먼트
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.FrameLayout_Map);
        if(mapFragment==null)
        {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.FrameLayout_Map,mapFragment).commit();
        }

        mapFragment.getMapAsync(this); //비동기적 객체 획득



        /**
         * MapFragment 는 지도에 대한 뷰 역할만을 담당하므로 API를 호출하려면
         * 인터페이스 역할을 하는 NaverMap객체가 필요하다.
         * 이때 MapFragment의 getMapAsync()메서드로 OnMapReacyCallback을 등록하면
         * 비동기로 NaverMap 객체를 얻을수 있다.
         * NaverMap객체가 준비되면 onMapReady/()콜백 메서드가 호출된다.
         */
    }

    /**NaverMap객체가 준비되면 호출되는 메서드**/
    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {

        //마커 초기화
        Marker_Center = new Marker();


//        Marker_Center.setTag("중심위치");
        //정보창 초기화
        infoWindow = new InfoWindow();


        //지도 줌 버튼 비활성화 -> 여기선 중심지점만 지정하므로
        //걍 한번 활성화 시켜봣는데 이건 폰으로 직접 확인해야 할듯..그래서 고치든말든 웅앵
        naverMap.getUiSettings().setZoomControlEnabled(true);

        //못움직이게 해야함.
        naverMap.getUiSettings().setScrollGesturesEnabled(false);

        //현위치 버튼 비활성화 -> 어차피 학교 중심으로 할 것임.
        naverMap.getUiSettings().setLocationButtonEnabled(false);
        //회전 불가.
        naverMap.getUiSettings().setRotateGesturesEnabled(false);

        //디폴트 -> 그냥 basic, 안되면 빼자!
        naverMap.setMapType(NaverMap.MapType.Basic);
        //카메라 위치 업데이트 -> 여기를 보여줄 것임: 선택지점.
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(LatLng_Center.latitude,LatLng_Center.longitude));
        naverMap.moveCamera(cameraUpdate);

        //받아온 중심위치를 띄워준다. - 마커 옵션 지정
        Marker_Center.setPosition(LatLng_Center);
        Marker_Center.setWidth(115);
        Marker_Center.setHeight(120);
        Marker_Center.setCaptionText("선택지점");
        Marker_Center.setCaptionColor(Color.MAGENTA);
        Marker_Center.setCaptionHaloColor(Color.WHITE);
        Marker_Center.setIcon(MarkerIcons.BLACK);
        Marker_Center.setIconTintColor(Color.DKGRAY);
        Marker_Center.setMap(naverMap);

        //방 디비를 뒤져서 관련된 곳 다 마커로 찍기
        Cursor c = db.query(tableName,null,null,null,null,null,null);
        LatLng_Selected = new LatLng(0.0,0.0);

        HashMap<Integer, Marker> markerHashMap = new HashMap<Integer, Marker>();
        recordNum = 0;
        while(c.moveToNext())
        {
            LatLng_Record = new LatLng(c.getDouble(4),c.getDouble(5));
            Integer index = c.getInt(0);

            Marker_Record = new Marker();
            Marker_Record.setWidth(115);
            Marker_Record.setHeight(120);
            Marker_Record.setIcon(MarkerIcons.BLACK);
            Marker_Record.setPosition(LatLng_Record);
            Marker_Record.setTag(index);

            markerHashMap.put(index, Marker_Record);

            //가까우면 파란색, 멀리있으면 밝은 회색으로표현.
            //중심지점과 그 리코드 간 거리.
            double distance  = distance(LatLng_Record.latitude,LatLng_Record.longitude,LatLng_Center.latitude,LatLng_Center.longitude);
            if(distance<=0.3)
                Marker_Record.setIconTintColor(Color.BLUE);
            else
                Marker_Record.setIconTintColor(Color.GRAY);

            Marker_Record.setOnClickListener(overlay->{
               //디비속 위치에 마커를 찍어 주고, 그 마커를 클릭햇을 경우 선택된 위경도로 디비속 마커의 위경도를 설정해 해준다.
               //->LatLng_Selcted 디폴트값 0.0, 0.0
                Marker eventMarker = markerHashMap.get(Integer.parseInt(overlay.getTag().toString()));
                LatLng_Selected = eventMarker.getPosition();
                Toast.makeText(NearRoomActivity.this,"방이 선택되었습니다. ",Toast.LENGTH_LONG).show();
                return true;
            });
            Marker_Record.setMap(naverMap);
            recordNum++;
        }
//        markerHashMap


        /**지도에 사용자가 선택한 중심 지점이 띄워지고
         * 그 근방 100m? 로 원(오버레이)가 그려진다(->포기;반투명 불가)
         * 일단은 디비에 저장된 방 정보 다 불러와서 마커 다 띄우고,
         * 부족하면 디비에 방정보 조낸 많이 저장 (이곳 저곳 다양하게 한 15개정도?)
         * 성공하면 근방 300m 원 에 포함된 마커만 파란색으로 바꾸고, -> distance 함수 -> 거리를 미터로 반환
         * 마커 클릭 이벤트 -> 정보창 띄우기 (이전 액티비티와 비슷)
         * 다음 액티비티에 선택 지점의 위경도 전달해줌*/

        //정보창 어댑터
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence)infoWindow.getMarker().getTag();
            }
        });

        //마커 클릭 이벤트 설정-> Marker_Record.
        //다른곳을 누르면 정보창을 닫음.

    }

    //디비에 정보 넣어야함 -> 그래야 보여줌.
    void insert(String roomname,String roomposition,String roomdesc,Double roomlatitude,Double roomlongitude,String ownername,String jjimusername,String phonenumber)
    {


        ContentValues values = new ContentValues();
        //여기에 데이터 입력해서 삽입한다긔

        //해당되는 아뒤 없으면 나옴->삽입 시작
        values.put("roomname",roomname);
        values.put("roomposition",roomposition);
        values.put("roomdesc",roomdesc);
        values.put("roomlatitude",roomlatitude);
        values.put("roomlongitude",roomlongitude);
        values.put("ownername",ownername);
        values.put("jjimusername",jjimusername);
        values.put("phonenumber",phonenumber);


        long result = db.insert(tableName,null,values);
    }



    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
