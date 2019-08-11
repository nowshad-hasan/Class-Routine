package com.example.nowshad.project300.fragment;

import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.nowshad.project300.activity.DailyClassActivity;
import com.example.nowshad.project300.activity.MainActivity;
import com.example.nowshad.project300.activity.NavigationDrawerActivity;
import com.example.nowshad.project300.another.StudentRoutineListAdapter;
import com.example.nowshad.project300.another.TeacherRoutineListAdapter;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_SINGLE;


public class DailyClassFragment extends Fragment {


private MaterialCalendarView materialCalendarView;
private Button buttonGotoDate;
    private ProgressDialog progressDialog;
    private TextView textViewDateName;
    private TextView textViewDayName;
    private String URL;
    SharedPreferences preferences;
    private String TAG=HomeFragment.class.getSimpleName();

    SharedPreferences.Editor editor;


    public DailyClassFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_daily_class, container, false);
        materialCalendarView=(MaterialCalendarView)view.findViewById(R.id.calendarView);
buttonGotoDate=(Button)view.findViewById(R.id.buttonGo);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);





        Date date=new Date();
        materialCalendarView.setSelectionMode(SELECTION_MODE_SINGLE);
        materialCalendarView.setSelectedDate(date);
//        materialCalendarView.getSelectedDate();

                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");


        buttonGotoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentDate=simpleDateFormat.format(materialCalendarView.getSelectedDate().getDate());

                System.out.println("sdfgsdfgsdfg");
                Intent intent=new Intent(getContext(),DailyClassActivity.class);
                intent.putExtra("currentDate",currentDate);
               getContext().startActivity(intent);


//                Toast.makeText(getActivity(), simpleDateFormat.format(materialCalendarView.getSelectedDate().getDate()), Toast.LENGTH_SHORT).show();

            }
        });

//        System.out.println("Current time => "+c.getTime());

//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        String currentDate = df.format(c.getTime());
    }




}
