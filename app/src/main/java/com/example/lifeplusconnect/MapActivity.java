package com.example.lifeplusconnect;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private Geocoder geocoder;
    EditText editText;
    ImageButton imageButton;
    Marker marker = new Marker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        editText = findViewById(R.id.editText);
        imageButton = findViewById(R.id.search);

        //지도 사용권한을 받아옴
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        //getMapAsync를 호출하여 비동기로 onMapReady콜백 메서드 호출
        //onMapReady에서 NaverMap객체를 받음
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { //권한 거부됨

            }
            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setMapType(NaverMap.MapType.Terrain);

        this.naverMap = naverMap;
        geocoder = new Geocoder(this);
        //네이버 맵에 locationSource를 set하면 위치 추적 기능을 사용할 수 있다.
        naverMap.setLocationSource(locationSource);
        //위치 추적 모드 지정 가능 내 위치로 이동
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        //현재위치 버튼 사용가능
        naverMap.getUiSettings().setLocationButtonEnabled(true);
        LatLng initialPosition = new LatLng(37.506855, 127.066242);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initialPosition);
        naverMap.moveCamera(cameraUpdate);

        markersPosition = new Vector<LatLng>();


        // 카메라 이동 되면 호출 되는 이벤트
        naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(int reason, boolean animated) {
                //freeActiveMarkers();
                // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
                LatLng currentPosition = getCurrentPosition(naverMap);
                for (LatLng markerPosition : markersPosition) {
                    if (!withinSightMarker(currentPosition, markerPosition))
                        continue;
                    Marker marker = new Marker();
                    marker.setPosition(markerPosition);
                    marker.setMap(naverMap);
                    activeMarkers.add(marker);
                }
            }
        });


        // 버튼 이벤트
        imageButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                if (str.length() > 1) {
                    List<Address> addressList = null;
                    try {
                        // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                        addressList = geocoder.getFromLocationName(
                                str, // 주소
                                10); // 최대 검색 결과 개수
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println(addressList.get(0).toString());
                    // 콤마를 기준으로 split
                    String[] splitStr = addressList.get(0).toString().split(",");
                    String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
                    System.out.println(address);

                    String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                    String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                    System.out.println(latitude);
                    System.out.println(longitude);

                    // 좌표(위도, 경도) 생성
                    LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    marker.setMap(null);
                    marker.setPosition(point);
                    // 마커 추가
                    marker.setMap(naverMap);


                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(point);
                    naverMap.moveCamera(cameraUpdate);
                } else {
                    //Context context = getApplicationContext();
                    Toast.makeText(getApplicationContext(), "2글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    // 마커 정보 저장시킬 변수들 선언
    private Vector<LatLng> markersPosition;
    private Vector<Marker> activeMarkers;

    // 현재 카메라가 보고있는 위치
    public LatLng getCurrentPosition(NaverMap naverMap) {
        CameraPosition cameraPosition = naverMap.getCameraPosition();
        return new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
    }

    // 선택한 마커의 위치가 가시거리(카메라가 보고있는 위치 반경 3km 내)에 있는지 확인
    //public final static double REFERANCE_LAT = 1 / 109.958489129649955;
    //public final static double REFERANCE_LNG = 1 / 88.74;
    public final static double REFERANCE_LAT_X3 = 3 / 109.958489129649955;
    public final static double REFERANCE_LNG_X3 = 3 / 88.74;

    public boolean withinSightMarker(LatLng currentPosition, LatLng markerPosition) {
        boolean withinSightMarkerLat = Math.abs(currentPosition.latitude - markerPosition.latitude) <= REFERANCE_LAT_X3;
        boolean withinSightMarkerLng = Math.abs(currentPosition.longitude - markerPosition.longitude) <= REFERANCE_LNG_X3;
        return withinSightMarkerLat && withinSightMarkerLng;
    }

}



