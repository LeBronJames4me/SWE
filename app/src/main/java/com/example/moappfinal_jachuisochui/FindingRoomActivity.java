package com.example.moappfinal_jachuisochui;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class FindingRoomActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapFragment mapFragment;
    Button Button_Next;
    String username;
    Spinner Spinner_Maptype;
    LatLng LatLng_Selected;
    Marker Marker_SelectedLocation;
    InfoWindow infoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findingroomd);
        setTitle("방 구하기");
        /***뷰 연결하기***/
        //여기에 보여질 것 - 인텐트로 받은 유저 아이디.
        Button Button_Next = (Button)findViewById(R.id.Button_Next);
        //누르면 이 유저 아이디를 전달하며 다음 액티비티로 이동.


        /***번들 받기 - 유저 아이디 받기 ***/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        username =bundle.get("name").toString();



        /**버튼 누르면 유저 이름 전달하면서 다음 액티비티로 넘어감. 이떄 토스트박스 띄워야 함.**/
        Button_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FindingRoomActivity.this, NearRoomActivity.class);
                intent.putExtra("username",username);
                double latitude = LatLng_Selected.latitude;
                double longitude = LatLng_Selected.longitude;
                //선택한 지점의 위경도 또한 넘겨준다.
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                Toast.makeText(getApplicationContext(), "300m 이내의 자취방을 탐색합니다.", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

        /**지도 프래그먼트***/
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
        Marker_SelectedLocation = new Marker();
        Marker_SelectedLocation.setTag("중심위치");
        //정보창 초기화
        infoWindow = new InfoWindow();


        //지도 줌 버튼 비활성화 -> 여기선 중심지점만 지정하므로
        //걍 한번 활성화 시켜봣는데 이건 폰으로 직접 확인해야 할듯..그래서 고치든말든 웅앵
        naverMap.getUiSettings().setZoomControlEnabled(true);


        //현위치 버튼 비활성화 -> 어차피 학교 중심으로 할 것임.
        naverMap.getUiSettings().setLocationButtonEnabled(false);
        //회전 불가.
        naverMap.getUiSettings().setRotateGesturesEnabled(false);

        //디폴트 -> 그냥 basic, 안되면 빼자!
        naverMap.setMapType(NaverMap.MapType.Basic);
        //카메라 위치 업데이트 -> 여기를 보여줄 것임: 경북대학교 중심(대학원동 쪽)이 떠야함.
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(35.8899242,128.610697));
        naverMap.moveCamera(cameraUpdate);


        /**스피너로 지도 종류 설정*/
        Spinner Spinner_Maptype = (Spinner)findViewById(R.id.Spinner_Maptype);

        Spinner_Maptype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //이걸로 지도 타입 바꿔죠잉
                CharSequence mapType = (CharSequence) adapterView.getItemAtPosition(i);
                naverMap.setMapType(NaverMap.MapType.valueOf(mapType.toString()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /**지도의 어떤 지점을 선택하면 마커가 띄워지고, 이동 버튼을 누르면
         * 다음 액티비티에 해당 지점의 위 경도 전달해줌*/
        //정보창 어댑터
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence)infoWindow.getMarker().getTag();
            }
        });

        //일단은 맵 클릭하면 선택한 지점의 위경도 토스트박스로 띄움 ->성공
        //성공시 그거 어디 라트랑 변수에 저장하기 ->성공인듯
        //성공시 그 지점에 마커 띄우기 ->이걸로 확인하긔 =>성공 맞음!
        //라트랑->화면 넘어갈때 넘겨줌 잊지말긔
        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF point, @NonNull LatLng coord) {
//                Toast.makeText(FindingRoomActivity.this, coord.latitude + "," + coord.longitude, Toast.LENGTH_SHORT).show();
                LatLng_Selected = coord;
                Marker_SelectedLocation.setWidth(115);
                Marker_SelectedLocation.setHeight(120);
                Marker_SelectedLocation.setPosition(coord);
                Marker_SelectedLocation.setMap(naverMap);
                infoWindow.open(Marker_SelectedLocation);

            }
        });
    }
}
