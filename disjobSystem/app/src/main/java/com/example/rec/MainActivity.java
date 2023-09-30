package com.example.rec;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    Fragment aiFragment;
    Fragment boardFragment;
    Fragment scrapFragment;
    ProgressBar progressBar;
    static String[] i={""};
    ImageButton mypageButton;
    TextView explainView;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_title_layout);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mypageButton = findViewById(R.id.mypageBtn);

        mypageButton.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        mypageButton.setImageResource(R.drawable.img_42);
                        mypageButton.invalidate();
                        MyPageFragment myPageFragment = new MyPageFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.frame, myPageFragment).commit();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        mypageButton.setImageResource(R.drawable.img_41);
                        mypageButton.invalidate();
                        break;
                    }
                }
                return false;
            }
        });



        setContentView(R.layout.activity_main);
        Log.i("MainActivity@@", "1111");
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_view); //하단바
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        bottomNav.setItemIconTintList(null);

        progressBar = findViewById(R.id.circularProgressBar);
        explainView = findViewById(R.id.explain);

        progressBar.setVisibility(View.VISIBLE); // 보이기


        DoOrNotLogin();
    }

    //navigation bar 클릭 시 프래그먼트 전환
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.aiMenu:
                    selectedFragment = new AiSelectHomeFragment();
                    break;
                case R.id.boardMenu:
                    selectedFragment = new BoardFragment(); //화면 바꿔야함
                    break;
                case R.id.scrapMenu:
                    selectedFragment = new ScrapFragment(); //화면 바꿔야함
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, selectedFragment).commit();
            return true;
        }
    };

    public void DoOrNotLogin()   //받은 cookie로 /session에서 json 받아오기 -> "Not LOGIN"이면 해당 cookie를 db(shared)// 에서 삭제
    {
        SharedPreferences preferences = this.getSharedPreferences("pref_loginSession", this.MODE_PRIVATE);
        String cookie = preferences.getString("current_login_session", null);
        //createpopUp("@@@@@ confirm shared preferences -> "+ preferences.getAll());

        String url = "https://pi.imdhson.com/session";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("ID", "string");
            jsonBody.put("Email","string");
            jsonBody.put("LastLogin","string");
            jsonBody.put("ScrapList","string");
            JSONObject settings = new JSONObject();
            settings.put("Loc", "");
            settings.put("Type1", "");
            settings.put("Type2", "");
            settings.put("Type3", "");
            jsonBody.put("Settings",settings);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("@@@@@@@@@@@@","@@@@@ response >> "+response.toString());
                        //createpopUp("Response "+response.toString());
                        if(response.toString().contains("Not LOGIN")) //Not LOGIN 이면 Login Fragment 띄움
                        {
                            Log.e("@@@@@@@@@@@@","loginFragment >> "+response.toString());
                            LoginFragment loginFragment = new LoginFragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.frame, loginFragment).commit();
                            progressBar.setVisibility(View.GONE); // 숨기기
                            explainView.setVisibility(View.GONE);
                            createpopUp2();
                        }
                        else { //Not LOGIN 이 아니면 이전에 로그인된 cookie가 살아있기 때문에 aiSelectHomeFragment 띄움
                            Log.e("@@@@@@@@@@@@","aiSelectHomeFragment >> "+response.toString());
                            AiSelectHomeFragment aiSelectHomeFragment = new AiSelectHomeFragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.frame, aiSelectHomeFragment).commit();
                            progressBar.setVisibility(View.GONE); // 숨기기
                            explainView.setVisibility(View.GONE);
                            String email = null;
                            try {
                                email = response.getString("Email");
                                createpopUp1(email);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("@@@@@@@@@@@@","@@@@@ error "+error.toString());//repair2
                        // 요청에 대한 오류 처리
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // 쿠키를 헤더에 추가
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }


    public void createpopUp1(String str){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.show_dialog, (LinearLayout)findViewById(R.id.layoutDialog));

        //dialog text 지정
        builder.setView(view);
        ((TextView)view.findViewById(R.id.textTitle)).setText(" DisJob");
        ((TextView)view.findViewById(R.id.textMessage_id)).setText(str);

        AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow()!=null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }

    public void createpopUp2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.show_dialog2, (LinearLayout)findViewById(R.id.layoutDialog));

        //dialog text 지정
        builder.setView(view);
        ((TextView)view.findViewById(R.id.textTitle)).setText(" DisJob");
        //((TextView)view.findViewById(R.id.textMessage_id)).setText("로그인 화면으로 이동합니다.");

        AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow()!=null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }

}