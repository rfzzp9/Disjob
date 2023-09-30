package com.example.rec;

import static com.example.rec.AiSelectHomeFragment.sharedKey;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AiResultHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AiResultHomeFragment extends Fragment implements View.OnClickListener {

    public RecyclerView recyclerView;
    public Adapter adapter;
    public ArrayList<JobListData> items = new ArrayList<>();
    public LinearLayoutManager layoutManager;
    //public Sorting sorting;

    public ProgressBar progressBar;
    public TextView explainView;
    public ImageButton settingBtn;
    public TextView jobCnt;
    public SearchView searchView;
    public TextView resultArea;
    public String resultDisabled1;
    public String resultDisabled2;
    public String resultDisabled3;
    public TextView resultDis;
    public static ArrayList<Integer> arrayList = new ArrayList<>();
    HashSet<Integer> starPos;
    static public String key;
    static HashMap<String, ArrayList<Integer>> integerHashMap = new HashMap<String, ArrayList<Integer>>();
    HashMap<String, String> jobList = new HashMap<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static ImageButton star;
    public static boolean i = true;
    public static ArrayList<Integer> arrayList_remove = new ArrayList<>();

    public AiResultHomeFragment() {

        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AiResultHoemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AiResultHomeFragment newInstance(String param1, String param2) {
        AiResultHomeFragment fragment = new AiResultHomeFragment();
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
    public void onPause() {
        super.onPause();
        Log.e("@@@@@@@@@@@@@@hello","onPause");
        SharedPreferences preferences = getActivity().getSharedPreferences("kbs", getActivity().MODE_PRIVATE); //repair1
        SharedPreferences.Editor editor = preferences.edit();

        String area = AiSelectHomeFragment.editArea.getText().toString();
        String dis1 = AiSelectHomeFragment.spinner_disabled1.getSelectedItem().toString();
        String dis2 = AiSelectHomeFragment.spinner_disabled2.getSelectedItem().toString();
        String dis3 = AiSelectHomeFragment.spinner_disabled3.getSelectedItem().toString();
        key = area + "_" + dis1 + "_" + dis2 + "_" + dis3; //key

        if(arrayList.size() > 0 || arrayList_remove.size() > 0)
        {
            ArrayList<Integer> _arrayList = new ArrayList<>(); //중복 제거한 arraylist
            for(int i : arrayList)
            {
                if(!_arrayList.contains(i))
                {
                    _arrayList.add(i); //중복 제거된 변수 -> _arrayList
                }
            }

            Log.e("@@@@", "@@@@  _arrayList 중복제거 후-> "+_arrayList);
            //여기까지 잘 됨

            if(integerHashMap.containsKey(key))
            {
                Log.e("@@@@value List ", "@@@@ value List1 ->"+key);
                ArrayList<Integer> valueList = integerHashMap.get(key); //key가 존재하는list가져옴
                Log.e("@@@@value List ", "@@@@ value List ->"+valueList);
                for(int i : _arrayList)
                {
                    valueList.add(i); //key 의 기존 list 에 _arraylist 추가 대입
                }Log.e("@@@@value List ", "@@@@ value List 대입 후 ->"+valueList);

                if(arrayList_remove.size() > 0)
                {
                    valueList.removeAll(arrayList_remove);
                }
                arrayList_remove.clear();

                integerHashMap.put(key, valueList);
                Log.e("@@@@value List ", "중복 제거 완전히 한 후 ->"+valueList);
                // createpopUp("valueList "+valueList);
            }
            else //hashmap에 key가 없으면 새로 key, value 대입
            {
                Log.e("@@@@value List ", "@@@@ value List2 ->"+_arrayList); //_arraylist 제대로 들어옴
                integerHashMap.put(key, _arrayList); //key와 중복 제거된 _arraylist를 hashmap 대입
                Log.e("arraylist @@@@","@@@@new key value"+integerHashMap); //제대로 들어옴

            } //잘됨
            Log.e("integerhashmap@@@@@", "test");
            Log.e("integerhashmap@@@@@","@@@@"+integerHashMap); //잘 들어감
            arrayList.clear(); //arraylist clear

            Log.e("@@@@@","@@@@!!! last map"+integerHashMap);

            ArrayList<Integer> arr = integerHashMap.get(key); //value
            ArrayList<Integer> arr2 = new ArrayList<>();      //중복 제거할 새 value
            for(int i : arr)
            {
                if(!arr2.contains(i))
                {
                    arr2.add(i);
                }
            }
            integerHashMap.put(key, arr2); //최종 대입.
            Log.e("arr2","@@@@"+arr2); //중복 제거 한 arr2
            Log.e("@@@@","@@@@"+integerHashMap);

            JSONObject jsonObject = new JSONObject(integerHashMap);
            String jsonString = jsonObject.toString();
            editor.putString("key", jsonString);
            editor.commit();
        }
        Log.e("@@@@@@@@","onPause");
    }

    @Override
    public void onResume() {     //AiJobDetailsFragment 에서 뒤로가기하여 다시 재로드됐을 때 and 시작할 때
        super.onResume();
        Log.e("@@@@@@@@@@@@@@hello","onResume");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("@@@@@@@@@@@@@@hello","1");

        View view = inflater.inflate(R.layout.fragment_ai_result_home, container, false);

        settingBtn = view.findViewById(R.id.setting);
        settingBtn.setOnClickListener(this); //설정버튼 누르면 다시 카테고리 선택 화면 넘어감

        progressBar = view.findViewById(R.id.circularProgressBar);
        explainView = view.findViewById(R.id.explain);
        progressBar.setVisibility(View.VISIBLE);
        explainView.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        Log.e("@@@@@@@@@@@@@@starPos","@@@@@@@@@@@"+starPos);
        Log.e("@@@@@@@@@@@@@@hello","2");

        //items.add(new JobListData(R.drawable.img_1,"한국장애인고용공단", "11150","경상북도 포항시","상용직"));
//        items.add(new JobListData("우체국", "13160","경상북도 포항시","상용직"));
//        items.add(new JobListData("kcc건설현장", "21160","대구광역시 남구","계약직"));
//        items.add(new JobListData("이마트", "11160","대구광역시 남구"));
//        items.add(new JobListData("파출소", "9160","대구광역시 남구"));
//        items.add(new JobListData("세븐일레븐", "10000","대구광역시 남구"));

        //createpopUp("@@@@@@@@!!!2222211");
        //login cookie 불러오기
        SharedPreferences preferences = this.getActivity().getSharedPreferences("pref_loginSession", this.getActivity().MODE_PRIVATE);
        String cookie = preferences.getString("current_login_session", null);


        sendPost(cookie);

//        Log.e("@@@@@@@@@@@@@","@@@sendPost result -> "+sendPost(cookie).get(0));
//        Log.e("@@@@@@@@@@@@@","@@@sendPost result -> "+sendPost(cookie).get(1));
        //items.addAll(sendPost(cookie));

        adapter = new Adapter(items);  //count 유동적으로 되도록.
        recyclerView.setAdapter(adapter);


        Log.e("@@@@@@@@@@@@@","@@@sendPost result -> "+items); //repair
        //createpopUp("@@@!!!!!!!!!!!!!!!!!!sendPost result -> "+items);
        Log.e("@@@@@@@@@@@@@@hello","3");

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.recyclerview_divider));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(dividerItemDecoration); //구분선 추가

        //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        Log.e("@@@@@@@@@@@@@@hello","4");

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                v.setBackgroundColor(Color.rgb(211,211,211)); //클릭된 항목 강조
                //v.findViewById(R.id.layout).setBackgroundColor(R.drawable.round_shape3);
                //v.findViewById(R.id.starBtn).setBackgroundColor(Color.rgb(211,211,211)); //수정하기 (이미지버튼 배경 회색 적용해야함)
                Log.e("@@@@@@@@@@@@@", "!!!!!!!!!!!!!!!!!"+v.findViewById(R.id.starBtn).getTag());

                String id = null;
                TextView textView_jobName = v.findViewById(R.id.jobName);
                for(String key : jobList.keySet()) //key - jobID  value - jobName
                {
                    String name = jobList.get(key); //value
                    if(textView_jobName.getText().equals(name))
                    {
                        id = key;
                        Log.e("@@@@@@@ job id","@@@@"+id+", "+name);
                        //createpopUp("@@@@"+id+", "+name);
                    }
                }

                ImageButton imgBtn = v.findViewById(R.id.starBtn);
                Integer resource = (Integer)imgBtn.getTag();
                Bundle bundle = new Bundle();
                bundle.putString("StarTag",resource.toString()); //star id 넘기기
                bundle.putString("JobID",id); //job ID 넘기기
                AiJobDetailsFragment jobDetailsFragment = new AiJobDetailsFragment();//항목 클릭하면 프래그먼트(AiJobDetailsFragment) 전환
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                jobDetailsFragment.setArguments(bundle); //bundle(star id) 넘기기
                fragmentTransaction.replace(R.id.frame, jobDetailsFragment);
                fragmentTransaction.commit();
            }

            @SuppressLint("ResourceType")
            @Override
            public void onButtonClick(View v, int pos) {
                Log.e("@@@@@@@@@@@@@", "!!!!!!!!!!!!!!!!!"+v.findViewById(R.id.starBtn).getTag());
                //v.setBackgroundColor(Color.rgb(211,211,211)); //클릭된 항목 강조
                Log.e("@@@@@@@@@@@@@", "!!!!!!!!!!!!!!!!!");
                //v.findViewById(R.id.starBtn).setBackgroundColor(Color.rgb(211,211,211)); //수정하기 (이미지버튼 배경 회색 적용해야함)
                ImageButton img = v.findViewById(R.id.starBtn);
                TextView textView_jobName = v.findViewById(R.id.jobName);
                if(i == true)
                {
                    img.setTag(R.drawable.img_1);
                    i = false;
                }
                //img.setTag(R.drawable.img_fillstar);
                Integer resource = (Integer)img.getTag();
                Log.e("aaaaaaaaaaa", "img tag : "+resource); //88
                //Integer resource_current = (Integer) v.getTag();
                //createpopUp(String.valueOf(resource+"!"));
                if(resource==2131230835 || resource == 2131230828)                          //if(img.getId()==2131230942)
                {
                    //createpopUp(String.valueOf(pos));
                    Log.e("aaaaa 1", "@@@@@@@@@@@@@@@@@@@@@@@@@@"+pos); //들어옴
                    arrayList.add(pos); //1차로 arraylist에 fillStar pos 대입
                    //createpopUp("pos1:"+pos);
                    //createpopUp("pos2:"+arrayList);
                    img.setImageResource(R.drawable.img_2);
                    img.setTag(R.drawable.img_2);
                   // adapter.notifyItemChanged(pos);
                    String id = null; //job id
                    TextView textViewJobName = recyclerView.getLayoutManager().findViewByPosition(pos).findViewById(R.id.jobName); //star눌러진 job name

                    for(String key : jobList.keySet()) //key - jobID  value - jobName
                    {
                        String name = jobList.get(key); //value
                        if(textViewJobName.getText().equals(name))
                        {
                            id = key;
                            //createpopUp("id:"+id+","+"jobName:"+name);
                        }
                    }
                    sendRequest("https://pi.imdhson.com/scrap/add", id);
                }
                else
                {
                    //createpopUp(String.valueOf(pos+"@"));
                    //TODO  arrayList.add(pos); pos빼기 and size<0 코드에서 arraylist clear
                    //arrayList.indexOf(pos);
                    //Log.e("index of @@@@@@@@@@@","@@@@@"+arrayList.indexOf(pos));
                    Log.e("aaaaa2", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+arrayList);
                    Log.e("aaaaa2", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+pos); //들어옴
                    for(int i = 0; i < arrayList.size(); ++i) //arraylist에서 pos된 값 remove (중복 포함)
                    {
                        if(arrayList.get(i)==pos)
                        {
                            arrayList.remove(i); //pos와 동일한 index 값 삭제
                        }
                    }

                    arrayList_remove.add(pos); //remove할 pos 추가
                    //createpopUp(arrayList_remove.toString());
                    Log.e("aaaaaaaaaaaaaaarraylist", "!!!!!!!!!!!"+arrayList);
                    //Log.e("aaaaaaaaaaaaaaarraylist", "!!!!!!!!!!!"+arrayList_remove);

                    Log.e("@@@@@@@@@@@@@@","!!!!!"+starPos);
                    img.setImageResource(R.drawable.img_1);
                    img.setTag(R.drawable.img_1);
                   // adapter.notifyItemChanged(pos);
                    String id = null; //job id
                    TextView textViewJobName = recyclerView.getLayoutManager().findViewByPosition(pos).findViewById(R.id.jobName); //star눌러진 job name

                    for(String key : jobList.keySet()) //key - jobID  value - jobName
                    {
                        String name = jobList.get(key); //value
                        if(textViewJobName.getText().equals(name))
                        {
                            id = key;
                        }
                    }
                    sendRequest("https://pi.imdhson.com/scrap/del", id);
                }
            }
        });


        jobCnt = view.findViewById(R.id.jobCnt);
        // jobCnt.setText(Integer.toString(items.size()));
        Log.e("@@@@@@@@@@@@@@hello","5");
        searchView = view.findViewById(R.id.searchText);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //직장 search
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                filterList(items, s);
                jobCnt.setText(Integer.toString(items.size()-1)); //todo jobCnt 코드 수정해야함
                return true;
            }
        });

          resultArea = view.findViewById(R.id.resultAnswer_area);
//          if(AiSelectHomeFragment.editArea.getText().toString().equals("선택"))
//          {
//              resultArea.setText("해당없음");
//          }
//          else {
              resultArea.setText(AiSelectHomeFragment.editArea.getText().toString());
//          }


          resultDis = view.findViewById(R.id.resultAnswer_disabled);
        if(AiSelectHomeFragment.spinner_disabled1.getSelectedItem().toString().equals("선택")) {
            resultDisabled1 = ""; }
        else {
            resultDisabled1 = AiSelectHomeFragment.spinner_disabled1.getSelectedItem().toString();
        }
        if(AiSelectHomeFragment.spinner_disabled2.getSelectedItem().toString().equals("선택")) {
            resultDisabled2 = "";}
        else {resultDisabled2 = AiSelectHomeFragment.spinner_disabled2.getSelectedItem().toString();}
        if(AiSelectHomeFragment.spinner_disabled3.getSelectedItem().toString().equals("선택")) {
            resultDisabled3 = "";}
        else {resultDisabled3 = AiSelectHomeFragment.spinner_disabled3.getSelectedItem().toString();}

        if(AiSelectHomeFragment.spinner_disabled1.getSelectedItem().toString().equals("선택") &&
                AiSelectHomeFragment.spinner_disabled3.getSelectedItem().toString().equals("선택") &&
                AiSelectHomeFragment.spinner_disabled3.getSelectedItem().toString().equals("선택"))
        {
            resultDis.setText("해당없음");
        }
        else {
            resultDis.setText(resultDisabled1 + " "+ resultDisabled2+ " " + resultDisabled3);
        }
        Log.e("@@@@@@@@@@@@@@hello","6");
        return view;
    }

    public void onClick(View view) {
        Log.e("@@@@@@@@@@@@@@hello","onclick");
        switch (view.getId())
        {
            case R.id.setting:
                view.setBackgroundColor(Color.rgb(211,211,211)); //수정하기
                AiSelectHomeFragment aiSelectHomeFragment = new AiSelectHomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                settingBtn.setImageResource(R.drawable.img_29);
                fragmentTransaction.replace(R.id.frame, aiSelectHomeFragment);
                fragmentTransaction.commit();
                break;
        }
    }


    public void filterList(ArrayList<JobListData> _sortedItems, String text)
    {
        Log.e("@@@@@@@@@@@@@@hello","filterList");
        Log.w("hello","filterList");
        ArrayList<JobListData> filteredList = new ArrayList<>();
        for(JobListData item : _sortedItems)
        {
            if(item.getJob_name().contains(text))
            {
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty())
        {
            Log.e("filtering : ","no filtering");
        }
        else {
            adapter.setFilteredList(filteredList);
        }
    }

    public void createpopUp(String str){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("인사말").setMessage(str);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void sendPost(String cookie)
    {
        Log.e("@@@@@@@@@@@@@@hello","sendPost");
        String url = "https://pi.imdhson.com/ailist";
        ArrayList<JobListData> arr = new ArrayList<>();

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

                                //arr.add(new JobListData(R.drawable.img_1, name, money1, address, hireType));


                                items.add(new JobListData(R.drawable.img_1, name, money1, address, hireType));
                                jobList.put(jobId, name); //key - jobName, value - jobID
                            }
                            adapter.notifyDataSetChanged();//repair
                            progressBar.setVisibility(View.GONE);
                            explainView.setVisibility(View.GONE);

                            String area = AiSelectHomeFragment.editArea.getText().toString();
                            String dis1 = AiSelectHomeFragment.spinner_disabled1.getSelectedItem().toString();
                            String dis2 = AiSelectHomeFragment.spinner_disabled2.getSelectedItem().toString();
                            String dis3 = AiSelectHomeFragment.spinner_disabled3.getSelectedItem().toString();
                            String _key = area + "_" + dis1 + "_" + dis2 + "_" + dis3; //key
                            key = area + "_" + dis1 + "_" + dis2 + "_" + dis3; //key

                            Log.e("@@@@@@@@@@", "@@@@key" + key); //onPause -> key
                            Log.e("@@@@@@@@@@", "@@@@_key" + _key);
                            //if(_key.equals()) -> equals 내에 shared preferences 에서 key for문 돌려서 가져오기(i)

                            SharedPreferences preferences =getActivity().getSharedPreferences("kbs", getActivity().MODE_PRIVATE);//repair1
                            HashMap<String, ArrayList<Integer>> retrievedHashMap = new HashMap<>();
                            Log.e("@@@@@@@@@@@@@@hello","onResume1");

                            try
                            {
                                if(retrievedHashMap != null)
                                {
                                    String jsonString = preferences.getString("key",(new JSONObject()).toString());
                                    JSONObject jsonObject = new JSONObject(jsonString);

                                    Log.e("@@@@1","@@@jsonString :"+jsonString.toString());
                                    //createpopUp(jsonString);

                                    Iterator<String> keys = jsonObject.keys();

                                    while(keys.hasNext())
                                    {
                                        String key = keys.next();
                                        Log.e("@@@@2","@@@ key"+key); //잘 나옴

                                        JSONArray jsonArray = jsonObject.getJSONArray(key);
                                        ArrayList<Integer> arrayList = new ArrayList<>();

                                        Log.e("@@@@3","@@@ key"+key); //잘 나옴

                                        for (int i = 0; i < jsonArray.length(); i++)
                                        {
                                            int value = jsonArray.getInt(i);
                                            arrayList.add(value);
                                        }
                                        retrievedHashMap.put(key, arrayList);
                                        Log.e("@@@@4","@@@ key"+key); //잘 나옴
                                    }

                                    Log.e("@@@@","@@@hashmap to string"+retrievedHashMap); //잘나옴

                                    ArrayList<Integer> key_list = new ArrayList<>();
                                    Set set = retrievedHashMap.keySet();
                                    Iterator iterator = set.iterator();

                                    while(iterator.hasNext())  //key에 해당하는 arraylist 가져옴 -> key_list
                                    {
                                        String key = (String) iterator.next();
                                        Log.e("@@@@@@@@@@","@@@@@@@@@@@@"+key);
                                        Log.e("@@@@@@@@@@","@@@@@@@@@@@@"+_key);
                                        if(key.equals(_key)) //현재 _key 랑 같은 key 인지 비교
                                        {
                                            JSONArray jsonArray = jsonObject.getJSONArray(key);
                                            for (int i = 0; i < jsonArray.length(); i++)
                                            {
                                                int value = jsonArray.getInt(i);
                                                key_list.add(value);
                                            }
                                        }
                                    }
                                    Log.e("@@@@","@@ key list"+key_list);


                                    Log.e("@@@@@@@@@@@@@@hello","onResume2");
                                    if(key_list.size() > 0) //key_list 에 따라 별 채우기
                                    {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                for(int i : key_list)
                                                {
                                                    ImageButton img = recyclerView.getLayoutManager().findViewByPosition(i).findViewById(R.id.starBtn);
                                                    Log.e("@@@","i"+i);
                                                    int resource = (int) img.getTag();
                                                    img.setImageResource(R.drawable.img_2);
                                                    img.setTag(R.drawable.img_2);
                                                    Log.e("get tag", "@@@@@@@@@@@@" + resource);

                                                }
                                            }
                                        }, 500);
                                    }
                                }
                            }
                            catch(Exception e)
                            {
                            }
                            Log.e("@@@@@@@@@@@@@@hello","onResume3");

                            jobCnt.setText(Integer.toString(items.size()-1));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("@@@@@@@@@@@@","@@@@@ error1 "+error.toString());
                        // 요청에 대한 오류 처리
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // 쿠키를 헤더에 추가
                Log.e("@@@@@@@@@@@@@@hello","getHeaders");
                headers.put("Cookie",cookie);
                return headers;
            }
        };

        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
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
        Volley.newRequestQueue(this.getContext()).add(jsonArrayRequest);


        //return arr;
    }


    public void sendRequest(String url, String jobID)
    {
        SharedPreferences preferences = getActivity().getSharedPreferences("pref_loginSession", getActivity().MODE_PRIVATE);
        String cookie = preferences.getString("current_login_session", null);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", jobID);
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
        // 요청을 큐에 추가
        Volley.newRequestQueue(this.getContext()).add(jsonObjectRequest);
    }


}