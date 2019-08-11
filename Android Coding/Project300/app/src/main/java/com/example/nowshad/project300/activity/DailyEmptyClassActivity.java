package com.example.nowshad.project300.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nowshad.project300.R;
import com.example.nowshad.project300.another.EmptyClassListAdapter;
import com.example.nowshad.project300.another.RoomInfo;
import com.example.nowshad.project300.another.TeacherRoutineListAdapter;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DailyEmptyClassActivity extends AppCompatActivity {


    SharedPreferences preferences;
    private String URL;
    private String TAG=DailyEmptyClassActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private TextView textViewDayName,textViewDateName;
    ListView myList;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_empty_class);
        preferences=DailyEmptyClassActivity.this.getSharedPreferences("prefLogin",MODE_PRIVATE);
editor=preferences.edit();

        Intent intent = getIntent();

        Toast.makeText(getApplicationContext(),intent.getStringExtra("currentDate")+" "+preferences.getInt("timeDuration",0)+" "+preferences.getString("roomStatus",null),Toast.LENGTH_SHORT).show();

        String currentDate=intent.getStringExtra("currentDate");
        int timeDuration=preferences.getInt("timeDuration",0);
        String roomStatus=preferences.getString("roomStatus",null);

        textViewDateName=(TextView)findViewById(R.id.dateNameEmptyClass);
        textViewDayName=(TextView)findViewById(R.id.dayNameEmptyClass);

        myList=(ListView)findViewById(R.id.listDailyEmptyClass);

        editor.putString("rescheduleDate",currentDate);
editor.commit();
        showEmptyClass(currentDate,timeDuration,roomStatus);


    }

    private void showEmptyClass(String currentDate, int timeDuration, String roomStatus) {

        progressDialog=new ProgressDialog(DailyEmptyClassActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        final ArrayList<String> arrayListTimeName=new ArrayList<>();
        final ArrayList<Integer> arrayListTimeID=new ArrayList<>();
        final ArrayList<ArrayList> arrayListEmptyRoomInfo=new ArrayList<>();

        final ArrayList<RoomInfo> roomInfoArrayList=new ArrayList<>();

        showDialogue();

        URL="http://"+preferences.getString("ipAddress",null)+":8080/ClassRoutine/SingleDayEmptyClass?singleDate="+currentDate+"&&timeDuration="+timeDuration+"&&roomStatus="+roomStatus;



        RequestQueue requestQueue= Volley.newRequestQueue(DailyEmptyClassActivity.this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //  Log.e(TAG,"OK from fragment");
                Log.e(TAG, response.toString());


                JSONArray jsonArray ;



                try {

                    textViewDateName.setText(response.getString("dateName"));
                    textViewDayName.setText(response.getString("dayName"));

                    jsonArray=response.getJSONArray("daily_routine");

                    for (int i = 0; i < jsonArray.length(); ++i) {

                        RoomInfo timeInfo=new RoomInfo();


                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        arrayListTimeName.add(jsonObject.getString("timeName"));
//                        arrayListTimeID.add(jsonObject.getInt("timeID"));

                        if(jsonObject.getJSONArray("rooms").length()>0)
                        {
                            timeInfo.setTimeName(jsonObject.getString("timeName"));
                            timeInfo.setTimeID(jsonObject.getInt("timeID"));
                            timeInfo.setInfoType(0);
                            roomInfoArrayList.add(timeInfo);
                        }



                        JSONArray jArray=jsonObject.getJSONArray("rooms");

//                        ArrayList<String> arrayListEmptyRoomNo=new ArrayList<>();
//                        ArrayList<Integer> arrayListEmptyRoomID=new ArrayList<>();


//                        ArrayList<RoomInfo> roomInfos=new ArrayList<>();
                        for(int j=0;j<jArray.length();++j)
                        {
                            RoomInfo roomInfo=new RoomInfo();

                             JSONObject jObject=jArray.getJSONObject(j);

                             roomInfo.setRoomNo(jObject.getString("roomNo"));
                             roomInfo.setRoomID(jObject.getInt("roomID"));
                             roomInfo.setTimeID(jsonObject.getInt("timeID"));
                             roomInfo.setTimeName(jsonObject.getString("timeName"));
                             roomInfo.setInfoType(1);
                             roomInfoArrayList.add(roomInfo);

//                             arrayListEmptyRoomNo.add(jObject.getString("roomNo"));
//                             arrayListEmptyRoomID.add(jObject.getInt("roomID"));
//                             roomInfo.setRoomNo(jObject.getString("roomNo"));
//                             roomInfo.setRoomID(jObject.getInt("roomID"));
//                             roomInfos.add(roomInfo);
                        }
//                        arrayListEmptyRoomInfo.add(roomInfos);







                    }

//                    for(int i=0;i<arrayListEmptyRoomInfo.size();++i)
//                    {
//                        ArrayList<RoomInfo> roomInfos=arrayListEmptyRoomInfo.get(i);
//
//                        Log.e(TAG,"Header is called && i value is "+i);
//
//                        for(int j=0;j<roomInfos.size();++j)
//                        {
//                            Log.e(TAG,"From JSON request class "+roomInfos.get(j).getRoomNo()+" "+roomInfos.get(j).getRoomID());
//                        }
//                    }

                    for(int i=0;i<roomInfoArrayList.size();++i)
                    {
                        RoomInfo roomInfo=roomInfoArrayList.get(i);

                        Log.e(TAG,"From JSON request class "+roomInfo.getInfoType()+" "+roomInfo.getTimeName()+" "+roomInfo.getTimeID()+" "+roomInfo.getRoomNo()+" "+roomInfo.getRoomID());
                    }


//                    EmptyClassListAdapter emptyClassListAdapter=new EmptyClassListAdapter(DailyEmptyClassActivity.this,arrayListTimeName,arrayListTimeID,arrayListEmptyRoomInfo);

                    EmptyClassListAdapter emptyClassListAdapter=new EmptyClassListAdapter(DailyEmptyClassActivity.this,roomInfoArrayList);
                    myList.setAdapter(emptyClassListAdapter);
                    hideDialogue();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.e(TAG,"OK from fragment");
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                hideDialogue();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);


    }

    private void hideDialogue(){
        if(progressDialog.isShowing())
            progressDialog.hide();
    }
    private void showDialogue() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
}
