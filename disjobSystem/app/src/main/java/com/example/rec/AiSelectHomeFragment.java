package com.example.rec;

import static android.content.Context.LOCATION_SERVICE;
import static android.support.constraint.motion.Debug.getLocation;
import static android.support.v4.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AiSelectHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AiSelectHomeFragment extends Fragment implements View.OnClickListener {

    ImageButton nextBtn;
    public static Spinner spinner_disabled1;
    public static Spinner spinner_disabled2;
    public static Spinner spinner_disabled3;
    private List<String> list;  // 데이터를 넣은 리스트변수

    public LinearLayout layout;

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    private GPSTracker gps;



    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //public static EditText editArea;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ImageButton searchBtn;
    public ImageButton resetBtn;
    static AutoCompleteTextView editArea;
    public ImageView setLocation;




    public AiSelectHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AiSelectHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AiSelectHomeFragment newInstance(String param1, String param2) {
        AiSelectHomeFragment fragment = new AiSelectHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("@@@@@@@@@@@@@","1212");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_ai_select_home, container, false);


        //editArea = view.findViewById(R.id.edit_area);

        layout = view.findViewById(R.id.layout);
        setLocation = view.findViewById(R.id.input_area2); //현재 위치 설정

        setLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        setLocation.setImageResource(R.drawable.img_44);
                        setLocation.invalidate();
                        //editArea.setText("경상북도 구미시");

                        if (!isPermission) {
                            callPermission();
                        }
                        gps = new GPSTracker(getContext());
                        if (gps.isGetLocation()) {
                            //GPSInfo를 통해 알아낸 위도값과 경도값

                            double latitude = gps.getLatitude();

                            double longitude = gps.getLongitude();


                            //Geocoder

                            Geocoder gCoder = new Geocoder(getContext(), Locale.getDefault());

                            List<android.location.Address> addr = null;
                            try {

                                addr = gCoder.getFromLocation(latitude, longitude, 1);

                                Address a = addr.get(0);


                                for (int i = 0; i <= a.getMaxAddressLineIndex(); ++i) {
                                    //여기서 변환된 주소 확인할  수 있음
                                    Log.v("알림", "AddressLine(" + i + ")" + a.getAddressLine(i) + "\n");
                                }
                                String degree = a.getAdminArea(); // Degree
                                String city = a.getLocality(); // City
                                //String phrase = a.getThoroughfare(); // Phrase
                                ArrayList<String> address = new ArrayList<>();
                                address.add(degree); //도
                                address.add(city);   //시, 구
                                //address.add(phrase); //동

                                String result = "";
                                for(int i = 0; i <2; ++i)
                                {
                                    if(address.get(i)==null)
                                    { }
                                    else {
                                      result += address.get(i);
                                      if(i != 1)
                                      {
                                          result += " ";
                                      }
                                    }
                                }
                                editArea.setText(result);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (addr != null) {

                                if (addr.size() == 0) {
                                    Log.e("@@@@@@@@@@@@@","@@@@주소정보없음");
                                }

                            }

                        } else {

                            // GPS 를 사용할수 없으므로

                            gps.showSettingsAlert();

                        }
                        callPermission();

                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        setLocation.setImageResource(R.drawable.img_43);
                        setLocation.invalidate();
                        break;
                    }
                }

                return false;
            }
        });

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });

        list = new ArrayList<String>();
        settingList();

        editArea = (AutoCompleteTextView) view.findViewById(R.id.edit_area);
        editArea.setAdapter(new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, list));
        nextBtn = view.findViewById(R.id.next_btn);
        spinner_disabled1 = view.findViewById(R.id.spinner_disabled1);
        spinner_disabled2 = view.findViewById(R.id.spinner_disabled2);
        spinner_disabled3 = view.findViewById(R.id.spinner_disabled3);

        resetBtn = view.findViewById(R.id.reset_btn);
        searchBtn = view.findViewById(R.id.searchBtn);
        searchBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                    //hideKeyboard();
                     showKeyboard();
                     editArea.requestFocus();
                return false;
            }
        });


        resetBtn.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        resetBtn.setImageResource(R.drawable.img_40);
                        resetBtn.invalidate();
                        //v.invalidate();
                        editArea.setText("");
                        spinner_disabled1.setSelection(0); //repair1
                        spinner_disabled2.setSelection(0);
                        spinner_disabled3.setSelection(0);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        //v.getBackground().clearColorFilter();
                        //v.invalidate();
                        resetBtn.setImageResource(R.drawable.img_6);
                        resetBtn.invalidate();
                        break;
                    }
                }
                return false;
            }
        });


        nextBtn.setOnClickListener(this);
        spinner_disabled1.setSelection(0);
        spinner_disabled2.setSelection(0);
        spinner_disabled3.setSelection(0);




        //editArea = view.findViewById(R.id.edit_area);


        return view;
    }
    static String sharedKey = null;
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.next_btn:
                Log.e("selectHome", "nextbtn click@@@@@@@@@@@@@@");
                String dis1="", dis2="", dis3="";
                if(spinner_disabled1.getSelectedItem().toString().equals("선택"))
                {
                    dis1 = "";
                }
                else
                {
                    dis1 = spinner_disabled1.getSelectedItem().toString();
                }
                if(spinner_disabled2.getSelectedItem().toString().equals("선택"))
                {
                    dis2 = "";
                }
                else {
                    dis2 = spinner_disabled2.getSelectedItem().toString();
                }
                if(spinner_disabled3.getSelectedItem().toString().equals("선택"))
                {
                    dis3 = "";
                }
                else {
                    dis3 = spinner_disabled3.getSelectedItem().toString();
                }
                sendRequest(editArea.getText().toString(), dis1, dis2, dis3);

                AiResultHomeFragment aiResultFragment = new AiResultHomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                nextBtn.setImageResource(R.drawable.img_20);
                fragmentTransaction.replace(R.id.frame, aiResultFragment);
                fragmentTransaction.commit();
                break;

        }
    }

    private void hideKeyboard()
    {
        if (getActivity() != null && getActivity().getCurrentFocus() != null)
        {
            // 프래그먼트기 때문에 getActivity() 사용
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void showKeyboard()
    {
            // 프래그먼트기 때문에 getActivity() 사용
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editArea, 0);
    }

    public void sendRequest(String _area, String _dis1, String _dis2, String _dis3)
    {
        String url = "https://pi.imdhson.com/users/settings/submit/";

        SharedPreferences preferences = getActivity().getSharedPreferences("pref_loginSession", getActivity().MODE_PRIVATE);
        String cookie = preferences.getString("current_login_session", null);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loc", _area);
            jsonObject.put("type1",_dis1);
            jsonObject.put("type2", _dis2);
            jsonObject.put("type3", _dis3);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("@@@@@@@@@@@@","@@@@@ response >> "+response.toString());
                        //createpopUp("Response"+response.toString());
                        //createpopUp(response.toString());
                        Log.e("@@@@","response"+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //createpopUp("@@@@@@@@@ error "+error.toString());
                        Log.e("@@@@@@@@@@@@","@@@@@@@@@ error "+error.toString()); //repair2
                        // 요청에 대한 오류 처리
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // 쿠키를 헤더에 추가
                Log.e("@@@@","response1");
                headers.put("Cookie",cookie);
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        // 요청을 큐에 추가
        Volley.newRequestQueue(this.getContext()).add(jsonObjectRequest);
    }

    public void createpopUp(String str){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("인사말").setMessage(str);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void settingList()
    {
        list.add("강원도");
        list.add("강원도 강릉시");
        list.add("강릉시");
        list.add("강원도 동해시");
        list.add("동해시");
        list.add("강원도 삼척시");
        list.add("삼척시");
        list.add("양양군");
        list.add("강원도 양양군");
        list.add("강원도 원주시");
        list.add("원주시");
        list.add("강원도 정선군");
        list.add("정선군");
        list.add("강원도 춘천시");
        list.add("춘천시");
        list.add("강원도 평창군");
        list.add("경기도 고양시");
        list.add("고양시");
        list.add("경기도 과천시");
        list.add("과천시");
        list.add("경기도 광주시");
        list.add("경기도 군포시");
        list.add("경기도 김포시");
        list.add("김포시");
        list.add("경기도 남양주시");
        list.add("남양주시");
        list.add("경기도 부천시");
        list.add("부천시");
        list.add("경기도 성남시");
        list.add("성남시");
        list.add("경기도 수원시");
        list.add("수원시");
        list.add("경기도 시흥시");
        list.add("시흥시");
        list.add("경기도 안산시");
        list.add("안산시");
        list.add("안성시");
        list.add("경기도 안성시");
        list.add("경기도 안양시");
        list.add("안양시");
        list.add("경기도 양주시");
        list.add("양주시");
        list.add("경기도 오산시");
        list.add("오산시");
        list.add("경기도 용인시");
        list.add("용인시");
        list.add("경기도 의정부시");
        list.add("의정부시");
        list.add("이천시");
        list.add("경기도 이천시");
        list.add("경기도 파주시");
        list.add("파주시");
        list.add("경기도 평택시");
        list.add("평택시");
        list.add("포천시");
        list.add("경기도 포천시");
        list.add("경기도 하남시");
        list.add("하남시");
        list.add("화성시");
        list.add("경기도 화성시");
        list.add("경상남도 거제시");
        list.add("거제시");
        list.add("경상남도 김해시");
        list.add("김해시");
        list.add("경상남도 밀양시");
        list.add("밀양시");
        list.add("경상남도 사천시");
        list.add("사천시");
        list.add("경상남도 양산시");
        list.add("양산시");
        list.add("경상남도 진주시");
        list.add("진주시");
        list.add("경상남도 창녕군");
        list.add("경상남도 창원시");
        list.add("창원시");
        list.add("경상남도 통영시");
        list.add("통영시");
        list.add("경상남도 합천군");
        list.add("경상북도 경산시");
        list.add("경산시");
        list.add("경상북도 경주시");
        list.add("경주시");
        list.add("경상북도 구미시");
        list.add("구미시");
        list.add("경상북도 김천시");
        list.add("김천시");
        list.add("문경시");
        list.add("경상북도 문경시");
        list.add("경상북도 상주시");
        list.add("상주시");
        list.add("경상북도 안동시");
        list.add("안동시");
        list.add("영천시");
        list.add("경상북도 영천시");
        list.add("경상북도 의성군");
        list.add("경상북도 칠곡군");
        list.add("칠곡군");
        list.add("포항시");
        list.add("경상북도 포항시");
        list.add("광주광역시");
        list.add("대구광역시");
        list.add("대구");
        list.add("대전");
        list.add("대전광역시");
        list.add("부산광역시");
        list.add("부산");
        list.add("서울");
        list.add("서울특별시");
        list.add("세종");
        list.add("세종특별자치시");
        list.add("울산광역시");
        list.add("울산");
        list.add("인천광역시");
        list.add("인천");
        list.add("전라남도");
        list.add("경기도");
        list.add("경상북도");
        list.add("경상남도");
        list.add("고흥군");
        list.add("전라남도 고흥군");
        list.add("전라남도 광양시");
        list.add("광양시");
        list.add("나주시");
        list.add("전라남도 나주시");
        list.add("전라남도 목포시");
        list.add("목포시");
        list.add("전라남도 무안군");
        list.add("무안군");
        list.add("순천시");
        list.add("전라남도 순천시");
        list.add("전라남도 여수시");
        list.add("여수시");
        list.add("전라남도 영암군");
        list.add("영암군");
        list.add("전라남도 장흥군");
        list.add("장흥군");
        list.add("전라남도 함평군");
        list.add("함평군");
        list.add("전라북도");
        list.add("군산시");
        list.add("전라북도 군산시");
        list.add("전라북도 김제시");
        list.add("김제시");
        list.add("전라북도 남원시");
        list.add("남원시");
        list.add("전라북도 순창군");
        list.add("순창군");
        list.add("전라북도 완주군");
        list.add("완주군");
        list.add("전라북도 익산시");
        list.add("익산시");
        list.add("전라북도 전주시");
        list.add("전주시");
        list.add("전라북도 진안군");
        list.add("진안군");
        list.add("제주특별자치도");
        list.add("제주특별자치도 제주시");
        list.add("제주시");
        list.add("충청남도");
        list.add("충청남도 계룡시");
        list.add("계룡시");
        list.add("충청남도 공주시");
        list.add("공주시");
        list.add("충청남도 금산군");
        list.add("금산군");
        list.add("충청남도 논산시");
        list.add("논산시");
        list.add("충청남도 당진시");
        list.add("당진시");
        list.add("충청남도 서산시");
        list.add("서산시");
        list.add("충청남도 서천군");
        list.add("서천군");
        list.add("충청남도 아산시");
        list.add("아산시");
        list.add("충청남도 천안시");
        list.add("천안시");
        list.add("충청남도 청양군");
        list.add("충청남도 태안군");
        list.add("충청북도");
        list.add("충청북도 음성군");
        list.add("음성군");
        list.add("충청북도 제천시");
        list.add("제천시");
        list.add("충청북도 증평군");
        list.add("증평군");
        list.add("충청북도 진천군");
        list.add("진천군");
        list.add("충청북도 청주시");
        list.add("청주시");
        list.add("충청북도 충주시");
        list.add("충주시");
    }

    private void callPermission() {

        // Check the SDK version and whether the permission is already granted or not.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

                && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)

                != PackageManager.PERMISSION_GRANTED) {



            requestPermissions(

                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},

                    PERMISSIONS_ACCESS_FINE_LOCATION);


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

                && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)

                != PackageManager.PERMISSION_GRANTED){


            requestPermissions(

                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},

                    PERMISSIONS_ACCESS_COARSE_LOCATION);

        } else {

            isPermission = true;

        }

    }

    @Override

    public void onRequestPermissionsResult(int requestCode, String[] permissions,

                                           int[] grantResults) {

        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION

                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            isAccessFineLocation = true;


        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION

                && grantResults[0] == PackageManager.PERMISSION_GRANTED){


            isAccessCoarseLocation = true;

        }


        if (isAccessFineLocation && isAccessCoarseLocation) {

            isPermission = true;

        }

    }

}