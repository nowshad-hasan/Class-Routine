package com.example.nowshad.project300.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.nowshad.project300.another.SingleChoiceClass;
import com.example.nowshad.project300.another.TeacherRoutineListAdapter;
import com.example.nowshad.project300.fragment.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SingleClassActivity extends FragmentActivity {

    TextView sessionName, courseName, courseCode, classTime, classDuration, classRoomNo, courseStatus, routineStatus;
    Button buttonCancel, buttonReschedule;
    String URL;
    SharedPreferences preferences;
    private String TAG = SingleClassActivity.class.getSimpleName();
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_class);

        preferences = SingleClassActivity.this.getSharedPreferences("prefLogin", MODE_PRIVATE);
        editor = preferences.edit();
        final Intent intent = getIntent();


//        Toast.makeText(SingleClassActivity.this,Integer.toString(intent.getIntExtra("routineID",0)) ,Toast.LENGTH_SHORT).show();

        sessionName = (TextView) findViewById(R.id.sessionName);
        courseName = (TextView) findViewById(R.id.courseName);
        courseCode = (TextView) findViewById(R.id.courseCode);
        classTime = (TextView) findViewById(R.id.timeName);
        classDuration = (TextView) findViewById(R.id.duration);
        classRoomNo = (TextView) findViewById(R.id.roomNo);
        courseStatus = (TextView) findViewById(R.id.courseStatus);
        routineStatus = (TextView) findViewById(R.id.routineStatus);


        buttonCancel = (Button) findViewById(R.id.buttonCancelClass);
        buttonReschedule = (Button) findViewById(R.id.buttonRescheduleClass);

        sessionName.setText(intent.getStringExtra("sessionName"));
        courseName.setText(intent.getStringExtra("courseName"));
        courseCode.setText(intent.getStringExtra("courseCode"));
        classTime.setText(intent.getStringExtra("classTime"));
        classDuration.setText(Integer.toString(intent.getIntExtra("classDuration", 0)));
        classRoomNo.setText(intent.getStringExtra("classRoom"));
        courseStatus.setText(intent.getStringExtra("courseStatus"));
        routineStatus.setText(intent.getStringExtra("routineStatus"));

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                URL = "http://" + preferences.getString("ipAddress", null) + ":8080/ClassRoutine/CancelClassServlet?routineID=" + intent.getIntExtra("routineID", 0) + "&&cancelDate=" + preferences.getString("dateName", null);


                RequestQueue requestQueue = Volley.newRequestQueue(SingleClassActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //  Log.e(TAG,"OK from fragment");
                        Log.e(TAG, response.toString());


                        JSONArray jsonArray = null;


                        try {

                            if (response.getString("cancelStatus").equals("successful")) {

                                Toast.makeText(SingleClassActivity.this, "Cancellation successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SingleClassActivity.this, NavigationDrawerActivity.class);
                                SingleClassActivity.this.startActivity(intent);
                                SingleClassActivity.this.finish();
                            } else {
                                Toast.makeText(SingleClassActivity.this, "Cancellation not successful", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Log.e(TAG,"OK from fragment");
                        VolleyLog.e(TAG, "Error: " + error.getMessage());

                    }
                }
                );
                requestQueue.add(jsonObjectRequest);


            }
        });

        buttonReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("routineID",intent.getIntExtra("routineID", 0));
                editor.putString("cancelDate",preferences.getString("dateName", null));
                editor.putInt("timeDuration", intent.getIntExtra("classDuration", 0));
                editor.putString("roomStatus", intent.getStringExtra("courseStatus"));
                editor.commit();
                SingleChoiceClass my_choice_dialog = new SingleChoiceClass();
                my_choice_dialog.show(getSupportFragmentManager(), "Choice Dialog");
            }
        });


    }
}
