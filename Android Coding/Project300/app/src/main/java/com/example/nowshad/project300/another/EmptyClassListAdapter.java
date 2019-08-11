package com.example.nowshad.project300.another;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nowshad.project300.R;
import com.example.nowshad.project300.activity.NotificationActivity;
import com.example.nowshad.project300.activity.RescheduleClassActivity;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by nowshad on 12/14/17.
 */

public class EmptyClassListAdapter extends BaseAdapter {

    private String TAG=EmptyClassListAdapter.class.getSimpleName();


    private Context context;
    private LayoutInflater layoutInflater;
    private Activity activity;


    private ArrayList<String> arrayListTimeName=new ArrayList<>();
    private ArrayList<Integer> arrayListTimeID=new ArrayList<>();
    private ArrayList<ArrayList> arrayListEmptyRoomInfo=new ArrayList<>();

    private ArrayList<RoomInfo> roomInfoArrayList=new ArrayList<>();
    private SharedPreferences preferences;



//    public EmptyClassListAdapter(Activity mainActivity,ArrayList<String> arrayListTimeName,ArrayList<Integer> arrayListTimeID,ArrayList<ArrayList> arrayListEmptyRoomInfo) {
//
//        context=mainActivity;
//
//        activity=mainActivity;
//        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.arrayListTimeName=arrayListTimeName;
//        this.arrayListTimeID=arrayListTimeID;
//        this.arrayListEmptyRoomInfo=arrayListEmptyRoomInfo;
//
//
//
//    }

//    @Override
//    public int getCount() {
//        return arrayListTimeName.size();
//    }


    public EmptyClassListAdapter(Activity mainActivity, ArrayList<RoomInfo> roomInfoArrayList)
    {


        context=mainActivity;
        activity=mainActivity;
        preferences=context.getSharedPreferences("prefLogin",MODE_PRIVATE);
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.roomInfoArrayList=roomInfoArrayList;
    }

    @Override
    public int getCount() {
        return roomInfoArrayList.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(roomInfoArrayList.get(position).getInfoType()==0)
            return 0;
        else {

            return 1;
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

//    @Override
//    public View getView(int position, View view, ViewGroup viewGroup) {
//        if(view==null)
//        {
//            view=activity.getLayoutInflater().inflate(R.layout.empty_class_parent,viewGroup,false);
//
//        }
//
//
//
//
//
//        TextView textViewTimeName=(TextView)view.findViewById(R.id.textViewParentTimeName);
//        TextView textViewTimeID=(TextView)view.findViewById(R.id.textViewParentTimeID);
//
//        LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.linearLayout);
//
//        textViewTimeName.setText(arrayListTimeName.get(position));
//        textViewTimeID.setText(Integer.toString(arrayListTimeID.get(position)));
//
//        ArrayList<RoomInfo> roomInfos=arrayListEmptyRoomInfo.get(position);
//        Log.e(TAG, "dfgsdfg");
//        for(int i=0;i<roomInfos.size();++i)
//        {
//
//
//            View child = activity.getLayoutInflater().inflate(R.layout.empty_class_child,null);
//            TextView roomNo=(TextView)child.findViewById(R.id.textViewChildRoomNo) ;
//            TextView roomID=(TextView)child.findViewById(R.id.textViewChildRoomID);
//
////            RoomInfo roomInfo=roomInfos.get(i);
//            roomNo.setText(roomInfos.get(i).getRoomNo());
//            roomID.setText(Integer.toString(roomInfos.get(i).getRoomID()));
//
//
//
//            Log.e(TAG,"Hey "+arrayListTimeName.get(position)+" "+Integer.toString(arrayListTimeID.get(position))+" "+roomInfos.get(i).getRoomNo()+" "+roomInfos.get(i).getRoomID());
//
////            Log.e(TAG,arrayListEmptyRoomInfo.get(position).get(i).toString());
//
//
//            linearLayout.addView(child);
//        }
//
//
//
//        return view;
//    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {






                if(getItemViewType(position)==0)
                {
                    if(view==null) {
                        view = activity.getLayoutInflater().inflate(R.layout.empty_class_parent, viewGroup, false);
                    }

                    TextView textViewTimeName=(TextView)view.findViewById(R.id.textViewParentTimeName);
//                    TextView textViewTimeID=(TextView)view.findViewById(R.id.textViewParentTimeID);

                    textViewTimeName.setText("Time - "+roomInfoArrayList.get(position).getTimeName());
//                    textViewTimeID.setText(Integer.toString(roomInfoArrayList.get(position).getTimeID()));
                }
                else if(getItemViewType(position)==1)
                {
                    if(view==null) {
                        view = activity.getLayoutInflater().inflate(R.layout.empty_class_child, viewGroup, false);
                    }


            TextView roomNo=(TextView)view.findViewById(R.id.textViewChildRoomNo) ;
//            TextView roomID=(TextView)view.findViewById(R.id.textViewChildRoomID);

//            RoomInfo roomInfo=roomInfos.get(i);
            roomNo.setText("Room No - "+roomInfoArrayList.get(position).getRoomNo());
//            roomID.setText(Integer.toString(roomInfoArrayList.get(position).getRoomID()));

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent=new Intent(context, RescheduleClassActivity.class);
                            intent.putExtra("routineID",preferences.getInt("routineID",0));
                            intent.putExtra("cancelDate",preferences.getString("cancelDate",null));
                            intent.putExtra("rescheduleDate",preferences.getString("rescheduleDate",null));
                            intent.putExtra("timeName",roomInfoArrayList.get(position).getTimeName());
                            intent.putExtra("roomNo",roomInfoArrayList.get(position).getRoomNo());
                            intent.putExtra("timeID",roomInfoArrayList.get(position).getTimeID());
                            intent.putExtra("roomID",roomInfoArrayList.get(position).getRoomID());
                            context.startActivity(intent);

                            Toast.makeText(context, preferences.getInt("routineID",0)+" "+preferences.getString("cancelDate",null)
                                    +" "+preferences.getString("rescheduleDate",null)+" "+roomInfoArrayList.get(position).getTimeID()+" "+roomInfoArrayList.get(position).getRoomID(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }




        return view;
    }
}
