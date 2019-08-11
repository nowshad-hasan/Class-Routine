package com.example.nowshad.project300.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nowshad.project300.R;
import com.example.nowshad.project300.another.StudentRoutineListAdapter;
import com.example.nowshad.project300.another.TeacherRoutineListAdapter;
import com.example.nowshad.project300.fragment.DailyClassFragment;
import com.example.nowshad.project300.fragment.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DailyClassActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TextView textViewDateName;
    private TextView textViewDayName;
    private String URL;
    SharedPreferences preferences;
    private String TAG=DailyClassActivity.class.getSimpleName();
    SharedPreferences.Editor editor;
    ListView myList;
    String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_class);

        textViewDateName=(TextView)findViewById(R.id.dateName);
        textViewDayName=(TextView)findViewById(R.id.dayName);

        preferences=getSharedPreferences("prefLogin",MODE_PRIVATE);

        System.out.println("sdfgsdfgsdfg");
         myList=(ListView)findViewById(R.id.list);
        Intent intent = getIntent();

        currentDate=intent.getStringExtra("currentDate");

        if(preferences.getString("userRole",null).equals("teacher"))
        {
            showTeacherRoutine(currentDate);
        }
        else if(preferences.getString("userRole",null).equals("student"))
        {
            showStudentRoutine(currentDate);
        }

        preferences=DailyClassActivity.this.getSharedPreferences("prefLogin",MODE_PRIVATE);
editor=preferences.edit();


    }


    private void hideDialogue(){
        if(progressDialog.isShowing())
            progressDialog.hide();
    }
    private void showDialogue() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void showTeacherRoutine(String currentDate)
    {
        progressDialog=new ProgressDialog(DailyClassActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        final ArrayList<Integer> arrayListRoutineID=new ArrayList<>();
        final ArrayList<String> arrayListSession=new ArrayList<>();
        final ArrayList<String> arrayListCourseName=new ArrayList<>();
        final ArrayList<String> arrayListCourseCode=new ArrayList<>();
        final ArrayList<String> arrayListTime=new ArrayList<>();
        final ArrayList<Integer> arrayListDuration=new ArrayList<>();
        final ArrayList<String> arrayListRoomNo=new ArrayList<>();
        final ArrayList<String> arrayListCourseStatus=new ArrayList<>();
        final ArrayList<String> arrayListRoutineStatus=new ArrayList<>();

        showDialogue();

        URL="http://"+preferences.getString("ipAddress",null)+":8080/ClassRoutine/SingleDayTeacherRoutineServlet?teacherID="+preferences.getInt("teacherID",0)+"&&singleDate="+currentDate;



        RequestQueue requestQueue= Volley.newRequestQueue(DailyClassActivity.this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //  Log.e(TAG,"OK from fragment");
                Log.e(TAG, response.toString());


                JSONArray jsonArray = null;



                try {

                    textViewDateName.setText(response.getString("dateName"));
                    textViewDayName.setText(response.getString("dayName"));

                    jsonArray=response.getJSONArray("daily_routine");

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        arrayListRoutineID.add(jsonObject.getInt("routineID"));
                        arrayListSession.add(jsonObject.getString("sessionName"));
                        arrayListCourseName.add(jsonObject.getString("courseName")) ;
                        arrayListCourseCode.add(jsonObject.getString("courseCode"));
                        arrayListTime.add(jsonObject.getString("timeName"));
                        arrayListDuration.add(jsonObject.getInt("timeDuration"));
                        arrayListRoomNo.add(jsonObject.getString("roomNo"));
                        arrayListCourseStatus.add(jsonObject.getString("roomStatus"));
                        arrayListRoutineStatus.add(jsonObject.getString("routineStatus"));


                    }


                    editor.putString("dateName",response.getString("dateName"));
                    editor.putString("dayName",response.getString("dayName"));
editor.commit();


                    TeacherRoutineListAdapter teacherRoutineListAdapter=new TeacherRoutineListAdapter(DailyClassActivity.this,arrayListRoutineID,arrayListSession,arrayListCourseName,arrayListCourseCode,arrayListTime,
                            arrayListDuration,arrayListRoomNo,arrayListCourseStatus,arrayListRoutineStatus);
                    myList.setAdapter(teacherRoutineListAdapter);
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

    public void showStudentRoutine(String currentDate)
    {

        progressDialog=new ProgressDialog(DailyClassActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        final ArrayList<String> arrayListTeacherName=new ArrayList<>();
        final ArrayList<String> arrayListCourseName=new ArrayList<>();
        final ArrayList<String> arrayListCourseCode=new ArrayList<>();
        final ArrayList<String> arrayListTime=new ArrayList<>();
        final ArrayList<Integer> arrayListDuration=new ArrayList<>();
        final ArrayList<String> arrayListRoomNo=new ArrayList<>();
        final ArrayList<String> arrayListCourseStatus=new ArrayList<>();
        final ArrayList<String> arrayListRoutineStatus=new ArrayList<>();

        showDialogue();

        URL="http://"+preferences.getString("ipAddress",null)+":8080/ClassRoutine/SingleDayStudentRoutineServlet?sessionID="+preferences.getInt("sessionID",0)+"&&singleDate="+currentDate;



        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //  Log.e(TAG,"OK from fragment");
                Log.e(TAG, response.toString());


                JSONArray jsonArray = null;



                try {

                    textViewDateName.setText(response.getString("dateName"));
                    textViewDayName.setText(response.getString("dayName"));

                    jsonArray=response.getJSONArray("daily_routine");

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                        arrayListTeacherName.add(jsonObject.getString("teacherName"));
                        arrayListCourseName.add(jsonObject.getString("courseName")) ;
                        arrayListCourseCode.add(jsonObject.getString("courseCode"));
                        arrayListTime.add(jsonObject.getString("timeName"));
                        arrayListDuration.add(jsonObject.getInt("timeDuration"));
                        arrayListRoomNo.add(jsonObject.getString("roomNo"));
                        arrayListCourseStatus.add(jsonObject.getString("roomStatus"));
                        arrayListRoutineStatus.add(jsonObject.getString("routineStatus"));


                    }



                    StudentRoutineListAdapter studentRoutineListAdapter=new StudentRoutineListAdapter(DailyClassActivity.this,arrayListTeacherName,arrayListCourseName,arrayListCourseCode,arrayListTime,
                            arrayListDuration,arrayListRoomNo,arrayListCourseStatus,arrayListRoutineStatus);


                    myList.setAdapter(studentRoutineListAdapter);
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
}
