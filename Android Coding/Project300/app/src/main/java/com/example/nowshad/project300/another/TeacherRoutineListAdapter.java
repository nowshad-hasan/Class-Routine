package com.example.nowshad.project300.another;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nowshad.project300.R;
import com.example.nowshad.project300.activity.SingleClassActivity;

import java.util.ArrayList;

/**
 * Created by nowshad on 12/5/17.
 */

public class TeacherRoutineListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Activity activity;


    private ArrayList<Integer> arrayListRoutineID;
    private ArrayList<String> arrayListSession;
    private ArrayList<String> arrayListCourseName;
    private ArrayList<String> arrayListCourseCode;
    private ArrayList<String> arrayListTime;
    private ArrayList<Integer> arrayListDuration;
    private ArrayList<String> arrayListRoomNo;
    private ArrayList<String> arrayListCourseStatus;
    private ArrayList<String> arrayListRoutineStatus;

    public TeacherRoutineListAdapter(Activity mainActivity,ArrayList<Integer> arrayListRoutineID,ArrayList<String> arrayListSession,ArrayList<String> arrayListCourseName,ArrayList<String> arrayListCourseCode,
                                     ArrayList<String> arrayListTime,ArrayList<Integer> arrayListDuration,ArrayList<String> arrayListRoomNo,ArrayList<String> arrayListCourseStatus,
                                     ArrayList<String> arrayListRoutineStatus)
    {
        context=mainActivity;

        activity=mainActivity;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayListRoutineID=arrayListRoutineID;
        this.arrayListSession=arrayListSession;
        this.arrayListCourseName=arrayListCourseName;
        this.arrayListCourseCode=arrayListCourseCode;
        this.arrayListTime=arrayListTime;
        this.arrayListDuration=arrayListDuration;
        this.arrayListRoomNo=arrayListRoomNo;
        this.arrayListCourseStatus=arrayListCourseStatus;
        this.arrayListRoutineStatus=arrayListRoutineStatus;
    }



    @Override
    public int getCount() {
        return arrayListSession.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if(view==null)
            view=activity.getLayoutInflater().inflate(R.layout.teacher_row,viewGroup,false);

        TextView sessionName=(TextView)view.findViewById(R.id.sessionName);
        TextView courseName=(TextView)view.findViewById(R.id.courseName);
        TextView courseCode=(TextView)view.findViewById(R.id.courseCode);
        TextView time=(TextView)view.findViewById(R.id.time);
        TextView duration=(TextView)view.findViewById(R.id.duration);
        TextView roomNo=(TextView)view.findViewById(R.id.roomNo);
         TextView courseStatus=(TextView)view.findViewById(R.id.courseStatus);
        TextView routineStatus=(TextView)view.findViewById(R.id.routineStatus);

        sessionName.setText(arrayListSession.get(position));
        courseName.setText(arrayListCourseName.get(position));
        courseCode.setText(arrayListCourseCode.get(position));
        time.setText(arrayListTime.get(position));
        duration.setText(arrayListDuration.get(position).toString());
        roomNo.setText(arrayListRoomNo.get(position));
        courseStatus.setText(arrayListCourseStatus.get(position));
        routineStatus.setText(arrayListRoutineStatus.get(position));

        if(arrayListRoutineStatus.get(position).equals("regular"))
        {
            routineStatus.setTextColor(Color.GREEN);
        }
        else if(arrayListRoutineStatus.get(position).equals("canceled"))
        {
            routineStatus.setTextColor(Color.RED);
        }

        else if(arrayListRoutineStatus.get(position).equals("rescheduled"))
        {
            routineStatus.setTextColor(Color.YELLOW);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,SingleClassActivity.class);
                intent.putExtra("routineID",arrayListRoutineID.get(position));
                intent.putExtra("sessionName",arrayListSession.get(position));
                intent.putExtra("courseName",arrayListCourseName.get(position));
                intent.putExtra("courseCode",arrayListCourseCode.get(position));
                intent.putExtra("classTime",arrayListTime.get(position));
                intent.putExtra("classDuration",arrayListDuration.get(position));
                intent.putExtra("classRoom",arrayListRoomNo.get(position));
                intent.putExtra("courseStatus",arrayListCourseStatus.get(position));
                intent.putExtra("routineStatus",arrayListRoutineStatus.get(position));
                context.startActivity(intent);
            }
        });

        return view;
    }
}
