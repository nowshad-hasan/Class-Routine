package com.example.nowshad.project300.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    Button buttonLogin;
    CheckBox checkboxRememberMe;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextIP;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    final String  PREF_NAME="prefLogin";
    private String IP_ADDRESS;
    private ProgressDialog progressDialog;

    private String email, password;
    private String URL;
    private String TAG=MainActivity.class.getSimpleName();
    private String loginStatus;
    private int userID;
    private String userRole;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        preferences=getApplicationContext().getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        editor=preferences.edit();


        if(preferences.getBoolean("isLoggedIn",false)==true)
        {
            Intent intent=new Intent(MainActivity.this,NavigationDrawerActivity.class);
            MainActivity.this.startActivity(intent);
            MainActivity.this.finish();
        }



       buttonLogin=(Button)findViewById(R.id.buttonLogin);
       checkboxRememberMe=(CheckBox)findViewById(R.id.checkboxRememberMe);
       editTextEmail=(EditText)findViewById(R.id.inputEmail) ;
       editTextPassword=(EditText)findViewById(R.id.inputPassword);
       editTextIP=(EditText)findViewById(R.id.inputIP);



        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IP_ADDRESS=editTextIP.getText().toString();

                editor.putString("ipAddress",IP_ADDRESS);
                editor.commit();


                email=editTextEmail.getText().toString();
                password=editTextPassword.getText().toString();

                URL="http://"+preferences.getString("ipAddress",null)+":8080/ClassRoutine/LoginServlet?userEmail="+email+"&&userPass="+password;


                RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                        URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //  Log.e(TAG,"OK from fragment");
                        Log.e(TAG, response.toString());




                        try {

                            loginStatus=response.getString("loginStatus");




                            if(loginStatus.equals("success"))
                            {
                                userID=response.getInt("userID");
                                userRole=response.getString("userRole");
                                editor.putInt("userID",userID);
                                editor.putString("userRole",userRole);
                                editor.putString("userEmail",email);
                                editor.putBoolean("isLoggedIn",checkboxRememberMe.isChecked());
                                editor.commit();
                                Intent intent=new Intent(MainActivity.this,NavigationDrawerActivity.class);
                                MainActivity.this.startActivity(intent);
                                MainActivity.this.finish();
                            }
                            else if(loginStatus.equals("failed"))
                            {
                                Toast.makeText(getApplicationContext(), "Invalid user email or password", Toast.LENGTH_SHORT).show();
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
                // Log.e(TAG,"akjsdhflhas");


            }
        });
    }

    private void hideDialogue(){
        if(progressDialog.isShowing())
            progressDialog.hide();
    }
    private void showDialogue(){
        if(!progressDialog.isShowing())
            progressDialog.show();
    }
}
