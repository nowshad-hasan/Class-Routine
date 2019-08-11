package com.example.nowshad.project300.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.nowshad.project300.fragment.DailyClassFragment;
import com.example.nowshad.project300.fragment.HomeFragment;
import com.example.nowshad.project300.fragment.WeeklyClassFragment;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

public class NavigationDrawerActivity extends AppCompatActivity {


    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View navHeader;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
//    private AllDonorFragment allDonorFragment;
    private TextView textViewUserEmail;
    private TextView textViewUserName;

    public static int navItemIndex=0;

    private static final String TAG_HOME = "home";
    private static final String TAG_WEEKLY_CLASS = "weeklyClass";
    private static final String TAG_DAILY_CLASS = "dailyClass";
//    private static final String TAG_UNAVAILABLE = "unavailableDonor";
//    private static final String TAG_EX_DONOR = "exDonor";
    public static String CURRENT_TAG = TAG_HOME;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress =true;
    private Handler handler;

    private String TAG=MainActivity.class.getSimpleName();
    private String URL;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        preferences=getSharedPreferences("prefLogin",MODE_PRIVATE);
        editor=preferences.edit();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        textViewUserEmail=(TextView)navHeader.findViewById(R.id.userEmail);
        textViewUserName=(TextView)navHeader.findViewById(R.id.userName);

        textViewUserEmail.setText(preferences.getString("userEmail",null));


        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        setUpNavigationView();



        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }


        URL="http://"+preferences.getString("ipAddress",null)+":8080/ClassRoutine/getUserDetailsServlet?userID="+preferences.getInt("userID",0)+"&&userRole="+preferences.getString("userRole",null);

        RequestQueue requestQueue= Volley.newRequestQueue(NavigationDrawerActivity.this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //  Log.e(TAG,"OK from fragment");
                Log.e(TAG, response.toString());




                try {
                    if(preferences.getString("userRole",null).equals("teacher"))
                    {
                        userName=response.getString("teacherName");
                        editor.putString("userName",userName);
                        editor.putInt("teacherID",response.getInt("teacherID"));
                        editor.commit();

                    }
                    else if(preferences.getString("userRole",null).equals("student"))
                    {
                        userName=response.getString("studentName");
                        editor.putString("userName",userName);
                        editor.putString("studentReg",response.getString("studentReg"));
                        editor.putString("studentSession",response.getString("studentSession"));

                        FirebaseMessaging.getInstance().subscribeToTopic(response.getString("studentSession"));

                        editor.putInt("sessionID",response.getInt("studentSessionID"));
                        editor.commit();

                    }

                  //  Toast.makeText(NavigationDrawerActivity.this,preferences.getString("studentSession",null),Toast.LENGTH_SHORT).show();


                    textViewUserName.setText(userName);


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
    private void setUpNavigationView()
    {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        navItemIndex=0;
                        CURRENT_TAG=TAG_HOME;

                        break;
                    case R.id.nav_weekly_class:
                        navItemIndex=1;
                        CURRENT_TAG=TAG_WEEKLY_CLASS;
                        break;
                    case R.id.nav_daily_class:
                        navItemIndex=2;
                        CURRENT_TAG=TAG_DAILY_CLASS;
                        break;
                    case R.id.nav_manage_profile:
                        startActivity(new Intent(NavigationDrawerActivity.this,ManageProfileActivity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_privacy_statement:
                        startActivity(new Intent(NavigationDrawerActivity.this,PrivacyStatementActivity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    default:
                        navItemIndex=0;

                }

                if(item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                item.setChecked(true);

                loadHomeFragment();

                return true;

            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer,R.string.closeDrawer)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }


    private  void loadHomeFragment()
    {
        selectNavMenu();

        setToolbarTitle();

        if(getSupportFragmentManager().findFragmentByTag(CURRENT_TAG)!=null)
        {
            drawerLayout.closeDrawers();
//            toggleFab();
            return;
        }

        Runnable mPendingRunnable =new Runnable() {
            @Override
            public void run() {
                Fragment fragment=getHomeFragment();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame,fragment,CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

        if(mPendingRunnable!=null)
            handler.post(mPendingRunnable);

        drawerLayout.closeDrawers();
        invalidateOptionsMenu();
    }

    private void setToolbarTitle()
    {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }
    private void selectNavMenu()
    {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }



    private Fragment getHomeFragment()
    {
        switch (navItemIndex){
            case 0:
                HomeFragment homeFragment=new HomeFragment();
                return homeFragment;
            case 1:
                WeeklyClassFragment weeklyClassFragment=new WeeklyClassFragment();
                return weeklyClassFragment;
            case 2:
                DailyClassFragment dailyClassFragment =new DailyClassFragment();
                return dailyClassFragment;
            default:
                return new HomeFragment();
        }
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawers();
            return;
        }


        // shouldLoadHomeFragOnBackPress is useless.I also can use  true here.
        if(shouldLoadHomeFragOnBackPress)
        {
            if(navItemIndex!=0)
            {
                navItemIndex=0;
                CURRENT_TAG=TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.main,menu);




        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.action_settings)
        {

            startActivity(new Intent(NavigationDrawerActivity.this,SettingsActivity.class));

            finish();
        }

        if(id==R.id.action_logout){
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();

            preferences=getSharedPreferences("prefLogin",MODE_PRIVATE);
//            Toast.makeText(getApplicationContext(), String.valueOf(preferences.getBoolean("isLoggedIn",false)), Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor;
            editor=preferences.edit();

            if(preferences.getString("userRole",null).equals("student"))
            {
                FirebaseMessaging.getInstance().unsubscribeFromTopic((preferences.getString("studentSession",null)));
            }

            editor.clear();
            editor.commit();



            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            NavigationDrawerActivity.this.startActivity(intent);
            NavigationDrawerActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    }

