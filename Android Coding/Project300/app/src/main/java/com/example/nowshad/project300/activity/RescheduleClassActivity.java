package com.example.nowshad.project300.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RescheduleClassActivity extends AppCompatActivity {

    TextView textViewRescheduleClass;
    Button buttonProceed;
    String URL;
    String TAG=RescheduleClassActivity.class.getSimpleName();
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_class);

        preferences=RescheduleClassActivity.this.getSharedPreferences("prefLogin", MODE_PRIVATE);

        textViewRescheduleClass=(TextView)findViewById(R.id.rescheduleClassDetails);
        buttonProceed=(Button)findViewById(R.id.buttonProceed);

        final Intent intent=getIntent();

        final int routineID=intent.getIntExtra("routineID",0);
        final String cancelDate=intent.getStringExtra("cancelDate");
        final String rescheduleDate=intent.getStringExtra("rescheduleDate");
        final int timeID=intent.getIntExtra("timeID",0);
        final int roomID=intent.getIntExtra("roomID",0);
        final String timeName=intent.getStringExtra("timeName");
        String roomNo=intent.getStringExtra("roomNo");


        String message="New date is "+rescheduleDate+" and new time is "+
                timeName+" and new room is "+roomNo+". Are you sure to proceed?";
        textViewRescheduleClass.setText(message);

        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  http://localhost:8080/ClassRoutine/RescheduleClassServlet?routineID=1&&cancelDate=03-12-2017&&rescheduleDate=05-12-2017&&timeID=1&&roomID=1

                URL = "http://" + preferences.getString("ipAddress", null) + ":8080/ClassRoutine/RescheduleClassServlet?routineID="+routineID+"&&cancelDate="+cancelDate+"&&rescheduleDate="+rescheduleDate+"&&timeID="+timeID+"&&roomID="+roomID;


                RequestQueue requestQueue = Volley.newRequestQueue(RescheduleClassActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //  Log.e(TAG,"OK from fragment");
                        Log.e(TAG, response.toString());


                        JSONArray jsonArray = null;


                        try {

                            if (response.getString("rescheduleStatus").equals("successful")) {

                                Toast.makeText(RescheduleClassActivity.this, "Reschedule successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RescheduleClassActivity.this, NavigationDrawerActivity.class);
                                RescheduleClassActivity.this.startActivity(intent);
                                RescheduleClassActivity.this.finish();
                            } else {
                                Toast.makeText(RescheduleClassActivity.this, "Reschedule not successful", Toast.LENGTH_SHORT).show();
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

    }
}
