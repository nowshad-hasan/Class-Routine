package com.example.nowshad.project300.fragment;

import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.nowshad.project300.another.StudentRoutineListAdapter;
import com.example.nowshad.project300.another.TeacherRoutineListAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class WeeklyClassFragment extends ListFragment {

    MaterialSpinner materialSpinner;
    private String URL;
    private ProgressDialog progressDialog;
    SharedPreferences preferences;
    private String TAG=WeeklyClassFragment.class.getSimpleName();

    private TextView textViewDateName;
    private TextView textViewDayName;

    SharedPreferences.Editor editor;

    public WeeklyClassFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences=getActivity().getSharedPreferences("prefLogin",MODE_PRIVATE);
        editor=preferences.edit();

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_weekly_class, container, false);

        textViewDateName=(TextView)view.findViewById(R.id.dateName);
        textViewDayName=(TextView)view.findViewById(R.id.dayName);

        materialSpinner=(MaterialSpinner)view.findViewById(R.id.spinnerView);
        materialSpinner.setItems("Sunday","Monday","Tuesday","Wednesday","Thursday");
        materialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                Toast.makeText(getActivity(), item+" "+position,
//                        Toast.LENGTH_SHORT).show();
//                bloodGroup=item;
//                showTeacherWeeklyRoutine(position+1);

                if(preferences.getString("userRole",null).equals("teacher"))
                {
                    showTeacherWeeklyRoutine(position+1);
                }
                else if(preferences.getString("userRole",null).equals("student"))
                {
                    showStudentWeeklyRoutine(position+1);
                }

            }
        });

        return view;
    }

    private void hideDialogue(){
        if(progressDialog.isShowing())
            progressDialog.hide();
    }
    private void showDialogue(){
        if(!progressDialog.isShowing())
            progressDialog.show();
    }

    public void showTeacherWeeklyRoutine(int dayID)
    {
        progressDialog=new ProgressDialog(getActivity());
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

        URL="http://"+preferences.getString("ipAddress",null)+":8080/ClassRoutine/ShowTeacherRoutineServlet?teacherID="+preferences.getInt("teacherID",0)+"&&dayID="+dayID;



        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
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

                    TeacherRoutineListAdapter teacherRoutineListAdapter=new TeacherRoutineListAdapter(getActivity(),arrayListRoutineID,arrayListSession,arrayListCourseName,arrayListCourseCode,arrayListTime,
                            arrayListDuration,arrayListRoomNo,arrayListCourseStatus,arrayListRoutineStatus);
                    setListAdapter(teacherRoutineListAdapter);
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

    public void showStudentWeeklyRoutine(int dayID)
    {
        progressDialog=new ProgressDialog(getActivity());
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

        URL="http://"+preferences.getString("ipAddress",null)+":8080/ClassRoutine/ShowStudentRoutineServlet?sessionID="+preferences.getInt("sessionID",0)+"&&dayID="+dayID;



        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
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



                    StudentRoutineListAdapter studentRoutineListAdapter=new StudentRoutineListAdapter(getActivity(),arrayListTeacherName,arrayListCourseName,arrayListCourseCode,arrayListTime,
                            arrayListDuration,arrayListRoomNo,arrayListCourseStatus,arrayListRoutineStatus);
                    setListAdapter(studentRoutineListAdapter);
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
