package com.example.rec;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AiJobDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AiJobDetailsFragment extends Fragment {

    private WebView webView; //댓글창 WebView

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ImageButton starBtn;
    public String starTag;
    public String jobID;
    public HashSet<String> fillStars;
    public int textCount = 0;
    TextView address;
    TextView employmentType;
    TextView hireType;
    TextView pay;
    TextView phoneNumber;
    TextView jobName;
    ImageButton backwardsBtn;

    ProgressBar progressBar1;
    TextView explainView1;
    ProgressBar progressBar2;
    TextView explainView2;

    public String fillStarList;
    public AiJobDetailsFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AiJobDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AiJobDetailsFragment newInstance(String param1, String param2) {
        AiJobDetailsFragment fragment = new AiJobDetailsFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_ai_job_details, container, false);
        address = view.findViewById(R.id.addressResult);               //사업장 주소
        employmentType = view.findViewById(R.id.employmentTypeResult); //모집직종
        hireType = view.findViewById(R.id.hireTypeResult);             //고용형태
        pay = view.findViewById(R.id.payResult);                       //임금
        phoneNumber = view.findViewById(R.id.phoneNumberResult);       //연락처
        jobName = view.findViewById(R.id.companyName);                 //사업장명

        progressBar1 = view.findViewById(R.id.circularProgressBar);    //직장상세정보 progressbar
        explainView1 = view.findViewById(R.id.explain);                //직장상세정보 explain
        progressBar2 = view.findViewById(R.id.circularProgressBar1);   //댓글 progressbar
        explainView2 = view.findViewById(R.id.explain1);               //댓글 explain

        progressBar1.setVisibility(View.VISIBLE);
        explainView1.setVisibility(View.VISIBLE);


        backwardsBtn = view.findViewById(R.id.backwards);
        backwardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AiResultHomeFragment aiResultHomeFragment = new AiResultHomeFragment();//항목 클릭하면 프래그먼트(aiResultHomeFragment) 전환
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                backwardsBtn.setImageResource(R.drawable.img_16);
                fragmentTransaction.replace(R.id.frame, aiResultHomeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        starBtn = view.findViewById(R.id.starButton);
        if(getArguments()!=null)
        {
            starTag = getArguments().getString("StarTag");
            jobID = getArguments().getString("JobID");
            int star = Integer.parseInt(starTag);
            starBtn.setTag(star);
            starBtn.setImageResource(star);
        }

        sendPost(); // job Details JSON 요청

        webView = view.findViewById(R.id.commentView);
        sendPostChat();

        return view;
    }

    public void sendPost()
    {
        //cookie 불러오기
        SharedPreferences preferences = getActivity().getSharedPreferences("pref_loginSession", getActivity().MODE_PRIVATE);
        String cookie = preferences.getString("current_login_session", null);

        String url = "https://pi.imdhson.com/jobs/" + jobID; //직장 아이디 추가한 job details url
        Log.e("@@@@@","@@@@json 요청 url "+url);
        //createpopUp(url);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("_id", "string");
            jsonBody.put("구인신청일자","string");
            jsonBody.put("사업장명","string");
            jsonBody.put("모집직종","string");
            jsonBody.put("고용형태","string");
            jsonBody.put("임금형태","string");
            jsonBody.put("임금","int");
            jsonBody.put("입사형태","string");
            jsonBody.put("요구경력","string");
            jsonBody.put("요구학력","string");
            jsonBody.put("전공계열","string");
            jsonBody.put("요구자격증","string");
            jsonBody.put("사업장 주소","string");
            jsonBody.put("기업형태","string");
            jsonBody.put("담당기관","string");
            jsonBody.put("등록일","string");
            jsonBody.put("연락처","string");
            jsonBody.put("필수부위","string");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("@@@@@@@jobDetail","@@@@@response> "+jsonBody.toString());
                        //createpopUp("JOB INFO > "+response.toString());
                        try
                        {
                            String job = response.getString("사업장명");
                            String jobAddress = response.getString("사업장 주소");
                            String tel = response.getString("연락처");
                            String payment = response.getString("임금");
                            String hiringType = response.getString("고용형태");
                            String employType = response.getString("모집직종");

                            Log.e("@@@@@@@","@@@@@ response result "+jobName+","+jobAddress+","+tel+","+payment+","+hiringType+","+employType);
                            //createpopUp("response result "+jobName+","+jobAddress+","+tel+","+payment+","+hiringType+","+employType);

                            String money1 = String.format("%,d",Integer.parseInt(payment));
                            address.setText(jobAddress);          //사업장 주소
                            employmentType.setText(employType);   //모집직종
                            hireType.setText(hiringType);         //고용형태
                            pay.setText(money1);                  //임금
                            phoneNumber.setText(tel);             //전화번호
                            jobName.setText(job);                 //사업장명
                            progressBar1.setVisibility(View.GONE);
                            explainView1.setVisibility(View.GONE);
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("@@@@@@@@@aiJobDetails","@@@error "+error.toString());//repair2
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
        // 요청을 큐에 추가
        Volley.newRequestQueue(this.getContext()).add(jsonObjectRequest);

    }

    public void createpopUp(String str){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("인사말").setMessage(str);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void sendPostChat()
    {
        //cookie 불러오기
        SharedPreferences preferences = getActivity().getSharedPreferences("pref_loginSession", getActivity().MODE_PRIVATE);
        String cookie = preferences.getString("current_login_session", null);

        String url = "https://pi.imdhson.com/articles/" + jobID; //직장 아이디 추가한 job details url
        Log.e("@@@@@","@@@@json 요청 url "+url);

        progressBar2.setVisibility(View.VISIBLE);
        explainView2.setVisibility(View.VISIBLE);

        webView.setWebChromeClient(new WebChromeClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // JavaScript 사용 설정

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true); //파일 엑세스
        webSettings.setLoadWithOverviewMode(true); // 메타태그
        webSettings.setUseWideViewPort(true); //화면 사이즈 맞추기
        webSettings.setSupportZoom(true); // 화면 줌 사용 여부
        webSettings.setBuiltInZoomControls(true); //화면 확대 축소 사용 여부
        webSettings.setDisplayZoomControls(true); //화면 확대 축소시, webview에서 확대/축소 컨트롤 표시 여부
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient()
        {
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                progressBar2.setVisibility(View.GONE);
                explainView2.setVisibility(View.GONE);
            }
        });

    }

}