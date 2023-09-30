package com.example.rec;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public WebView webView;
    public ProgressBar progressBar;
    public TextView explainView;

    public BoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoardFragment newInstance(String param1, String param2) {
        BoardFragment fragment = new BoardFragment();
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

        View view = inflater.inflate(R.layout.fragment_board, container, false);

        progressBar = view.findViewById(R.id.circularProgressBar);
        explainView = view.findViewById(R.id.explain);

        progressBar.setVisibility(View.VISIBLE);
        explainView.setVisibility(View.VISIBLE);

        webView = view.findViewById(R.id.BoardWeb);
        sendPostBoard();

        return view;
    }


    public void sendPostBoard()
    {
        //cookie 불러오기
        SharedPreferences preferences = getActivity().getSharedPreferences("pref_loginSession", getActivity().MODE_PRIVATE);
        String cookie = preferences.getString("current_login_session", null);

        String url = "https://pi.imdhson.com/articles";
        Log.e("@@@@@","@@@@json 요청 url "+url);
        //createpopUp(url);

        webView.setWebViewClient(new WebViewClient()
        {
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                explainView.setVisibility(View.GONE);
            }
        });

        webView.requestFocus(View.FOCUS_DOWN);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // JavaScript 사용 설정
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true); //파일 엑세스
        webSettings.setLoadWithOverviewMode(true); // 메타태그
        webSettings.setUseWideViewPort(true); //화면 사이즈 맞추기
        webSettings.setSupportZoom(false); // 화면 줌 사용 여부
        webSettings.setBuiltInZoomControls(false); //화면 확대 축소 사용 여부
        webSettings.setDisplayZoomControls(false); //화면 확대 축소시, webview에서 확대/축소 컨트롤 표시 여부
        webSettings.setJavaScriptEnabled(true);

        // 댓글창 로드
        webView.loadUrl(url);
    }

}