package com.example.rec;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ImageButton logoutBtn;
    ProgressBar progressBar;
    ProgressBar progressBar1;
    TextView textView1;
    TextView textView;
    TextView myemail;
    TextView disabled;
    TextView location;
    TextView loginTime;

    public MyPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPageFragment newInstance(String param1, String param2) {
        MyPageFragment fragment = new MyPageFragment();
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

        //cookie 가져오기
        SharedPreferences preferences = this.getActivity().getSharedPreferences("pref_loginSession", this.getActivity().MODE_PRIVATE);
        String cookie = preferences.getString("current_login_session", null);

        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        myemail = view.findViewById(R.id.myEmail);
        disabled = view.findViewById(R.id.myDisAnswer);
        location = view.findViewById(R.id.myLocAnswer);
        loginTime = view.findViewById(R.id.lastLoginAnswer);

        logoutBtn = view.findViewById(R.id.logout_btn); //로그아웃 버튼

        progressBar = view.findViewById(R.id.circularProgressBar);
        textView = view.findViewById(R.id.explain);
        progressBar1 = view.findViewById(R.id.circularProgressBar1);
        textView1 = view.findViewById(R.id.explain1);

        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

        progressBar1.setVisibility(View.VISIBLE);
        textView1.setVisibility(View.VISIBLE);

        sendPost(cookie);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(view.VISIBLE);
                textView.setVisibility(view.VISIBLE);
                preferences.edit().remove("current_login_session").commit(); //login cookie 삭제
                //sharedPreferences.edit().clear().apply();

                //login fragment로 이동
                LoginFragment loginFragment = new LoginFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                logoutBtn.setImageResource(R.drawable.img_25);
                fragmentTransaction.replace(R.id.frame, loginFragment);
                fragmentTransaction.commit();

            }
        });
        return view;
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
                        Log.e("@@@@@@@@@@@@","@@@@@ response >> "+response.toString());
                        //createpopUp("Response"+response.toString());
                        try {
                            String email = response.getString("Email");
                            String lastLogin = response.getString("LastLogin");
                            String strings = response.getString("Settings");
                            JSONObject json = new JSONObject(strings);
                            String loc = json.getString("Loc");
                            String dis = json.getString("Type1");
                            String[] timeS = lastLogin.split("T");
                            String time = timeS[0];
                            myemail.setText(email);
                            disabled.setText(dis);
                            location.setText(loc);
                            loginTime.setText(time);

                            progressBar1.setVisibility(View.GONE);
                            textView1.setVisibility(View.GONE);

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