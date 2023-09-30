package com.example.rec;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScrapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScrapFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public RecyclerView recyclerView;
    public LinearLayoutManager layoutManager;
    public Adapter_scrap adapter;
    public ProgressBar progressBar;
    public TextView explainView;
    public TextView cntView;
    public TextView explain;
    public ArrayList<JobListData> items_scrap = new ArrayList<>();

    public ScrapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScrapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScrapFragment newInstance(String param1, String param2) {
        ScrapFragment fragment = new ScrapFragment();
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
        View view = inflater.inflate(R.layout.fragment_scrap, container, false);

        cntView = view.findViewById(R.id.cnt);
        cntView.setVisibility(View.GONE);
        explain = view.findViewById(R.id.text);
        explain.setVisibility(View.GONE);

        recyclerView = (RecyclerView) view.findViewById(R.id.jobScrapList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        progressBar = view.findViewById(R.id.circularProgressBar);
        explainView = view.findViewById(R.id.explain);
        progressBar.setVisibility(View.VISIBLE);
        explainView.setVisibility(View.VISIBLE);

        sendPost();

        adapter = new Adapter_scrap(items_scrap);
        //cntView.setText(items_scrap.size());

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        return view;
    }

    public void sendPost()
    {
        String url = "https://pi.imdhson.com/scrap";
        //cookie 불러오기
        SharedPreferences preferences = getActivity().getSharedPreferences("pref_loginSession", getActivity().MODE_PRIVATE);
        String cookie = preferences.getString("current_login_session", null);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("AI_List_num", "int");
            jsonBody.put("_id","string");
            jsonBody.put("사업장명","string");
            jsonBody.put("임금형태","string");
            jsonBody.put("임금","int");
            jsonBody.put("사업장 주소","string");
            jsonBody.put("고용형태","string");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("@@@@@@@","@@@@@ response1 >> "+response.toString());
                        //key - jobName / value - jobID

                        //createpopUp("Response"+response.toString());
                        try {
                            for(int i = 0; i < response.length(); ++i)
                            {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String jobId = jsonObject.getString("_id");
                                String name = jsonObject.getString("사업장명");
                                String money = jsonObject.getString("임금");
                                String address = jsonObject.getString("사업장 주소");
                                String hireType = jsonObject.getString("고용형태");
                                Log.e("@@@@@@@","@@@@@ response2 >> "+name+","+money+","+address+","+hireType);
                                //createpopUp("Response  "+name+","+money+","+address+","+hireType);
                                String money1 = String.format("%,d",Integer.parseInt(money));
                                items_scrap.add(new JobListData(R.drawable.img_2, name, money1, address, hireType));
                            }
                            adapter.notifyDataSetChanged();//repair
                            //cntView.setText(items_scrap.size()); //scrap list size
                            cntView.setText(Integer.toString(items_scrap.size()));
                            progressBar.setVisibility(View.GONE);
                            explainView.setVisibility(View.GONE);
                            cntView.setVisibility(View.VISIBLE); //스크랩리스트 갯수
                            explain.setVisibility(View.VISIBLE); //부연설명


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        createpopUp("@@@@@@@@@@@@@@@ error1 "+error.toString());
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
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 60000;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        // 요청을 큐에 추가
        Volley.newRequestQueue(this.getContext()).add(jsonArrayRequest);

    }

    public void createpopUp(String str){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("인사말").setMessage(str);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}