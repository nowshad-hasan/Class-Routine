package com.example.nowshad.project300.another;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nowshad.project300.R;

import java.util.ArrayList;

/**
 * Created by nowshad on 12/5/17.
 */

public class StudentRoutineListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private Activity activity;



    private ArrayList<String> arrayListTeacherName;
    private ArrayList<String> arrayListCourseName;
    private ArrayList<String> arrayListCourseCode;
    private ArrayList<String> arrayListTime;
    private ArrayList<Integer> arrayListDuration;
    private ArrayList<String> arrayListRoomNo;
    private ArrayList<String> arrayListCourseStatus;
    private ArrayList<String> arrayListRoutineStatus;

    public StudentRoutineListAdapter(Activity mainActivity,ArrayList<String> arrayListTeacherName,ArrayList<String> arrayListCourseName,ArrayList<String> arrayListCourseCode,
                                     ArrayList<String> arrayListTime,ArrayList<Integer> arrayListDuration,ArrayList<String> arrayListRoomNo,ArrayList<String> arrayListCourseStatus,
                                     ArrayList<String> arrayListRoutineStatus)
    {
        super(mainActivity,R.layout.student_row);
        context=mainActivity;

        activity=mainActivity;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayListTeacherName=arrayListTeacherName;
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
        return arrayListTeacherName.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view==null)
            view=activity.getLayoutInflater().inflate(R.layout.student_row,viewGroup,false);

        TextView teacherName=(TextView)view.findViewById(R.id.teacherName);
        TextView courseName=(TextView)view.findViewById(R.id.courseName);
        TextView courseCode=(TextView)view.findViewById(R.id.courseCode);
        TextView time=(TextView)view.findViewById(R.id.time);
//        TextView duration=(TextView)view.findViewById(R.id.duration);
        TextView roomNo=(TextView)view.findViewById(R.id.roomNo);
//        TextView courseStatus=(TextView)view.findViewById(R.id.courseStatus);
        TextView routineStatus=(TextView)view.findViewById(R.id.routineStatus);

        teacherName.setText(arrayListTeacherName.get(position)+" , ");
        courseName.setText(arrayListCourseName.get(position)+" , ");
        courseCode.setText(arrayListCourseCode.get(position));
        time.setText("Time: "+arrayListTime.get(position));
//        duration.setText(arrayListDuration.get(position).toString());
        roomNo.setText("Room No: "+arrayListRoomNo.get(position));
//        courseStatus.setText(arrayListCourseStatus.get(position));
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
            routineStatus.setTextColor(Color.BLUE);
        }



        return view;
    }

}
