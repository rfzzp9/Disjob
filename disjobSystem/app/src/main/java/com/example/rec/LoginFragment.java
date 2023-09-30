package com.example.rec;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    WebView loginView;
    WebSettings webSettings;
    ProgressBar progressBar;
    TextView textView;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        SharedPreferences preferences = getActivity().getSharedPreferences("pr", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        progressBar = view.findViewById(R.id.circularProgressBar);
        textView = view.findViewById(R.id.explain);
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

        loginView = (WebView) view.findViewById(R.id.LoginWeb);
        loginView.setWebViewClient(new WebViewClient()); //새창 띄우지않기

        loginView.setWebViewClient(new WebViewClient());
        webSettings = loginView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true); //파일 엑세스
        webSettings.setLoadWithOverviewMode(true); // 메타태그
        webSettings.setUseWideViewPort(true); //화면 사이즈 맞추기
        webSettings.setSupportZoom(true); // 화면 줌 사용 여부
        webSettings.setBuiltInZoomControls(true); //화면 확대 축소 사용 여부
        webSettings.setDisplayZoomControls(true); //화면 확대 축소시, webview에서 확대/축소 컨트롤 표시 여부
        webSettings.setJavaScriptEnabled(true);
        loginView.loadUrl("https://pi.imdhson.com/login"); //연결할 url



        loginView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 로그인이 완료된 페이지 URL 패턴을 확인
                if (url.contains("exit")) {        // 로그인이 완료됨
                    progressBar.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    CookieManager cookieManager = CookieManager.getInstance(); //webview와 연결된 쿠키 관리 역할
                    cookieManager.setAcceptCookie(true);
                    String myCookies = cookieManager.getCookie(url);
                    //createpopUp("myCookies @@@@@@@@"+myCookies);
                    String[] _myCookies = myCookies.split(";");
                    String str = _myCookies[_myCookies.length -1]; //마지막 키 값 TODO COOKIE(마지막키값(str)) -> sharedPreferences에 저장하기->그리고 main activity에서 불러오기

                    SharedPreferences preferences = getActivity().getSharedPreferences("pref_loginSession", getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("current_login_session",str); //로그인한 cookie를 -> shared preferences에 저장
                    editor.commit();
                    
                    //createpopUp("@@@@@ login shared preferences-> "+ preferences.getAll());
                    Log.e("@@@@@@@@@@@@@","@@@@cookie 마지막값 "+str);
                    Log.e("@@@@@@@@@@@@@","@@@@shared preferences save "+preferences.getAll());
                    //createpopUp(str);

                    sendPost(str);



                    //webview 죽이기
//                    loginView.destroy();
                    //webview fragment 죽이기
//                    FragmentManager manager = getActivity().getSupportFragmentManager();
//                    manager.beginTransaction().remove(LoginFragment.this).commit();
//                    manager.popBackStack();
                    //aiSelectHomeFragment 띄우기


                }
            }
        });

        return view;
    }

    public void onDestroy() {
        super.onDestroy();
        loginView.destroy();
    }

    public void createpopUp(String str){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("로그인 완료").setMessage(str);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void sendPost(String cookie)
    {
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
                        //Log.e("@@@@@@@@@@@@","@@@@@ response >> "+response.toString());
                        //createpopUp("Response"+response.toString());
                        try {
                            String email = response.getString("Email");
                            //createpopUp(email+"님 환영합니다!");

                            AiSelectHomeFragment aiSelectHomeFragment = new AiSelectHomeFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            progressBar.setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                            fragmentTransaction.replace(R.id.frame, aiSelectHomeFragment);
                            fragmentTransaction.commit();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("@@@@@@@@@@@@","@@@@@ error "+error.toString());
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
        Volley.newRequestQueue(this.getContext()).add(jsonObjectRequest);
    }

}