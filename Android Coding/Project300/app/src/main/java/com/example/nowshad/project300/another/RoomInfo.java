package com.example.nowshad.project300.another;

import android.util.Log;

/**
 * Created by nowshad on 12/14/17.
 */

public class RoomInfo {

private String TAG=RoomInfo.class.getSimpleName();


//    private String roomNo;
//    private int roomID;
//
//    public RoomInfo()
//    {
//        Log.e(TAG,"Room Info is called!");
//    }
//
//
//    public String getRoomNo() {
//        return roomNo;
//    }
//
//    public void setRoomNo(String roomNo) {
//        this.roomNo = roomNo;
//    }
//
//    public int getRoomID() {
//        return roomID;
//    }
//
//    public void setRoomID(int roomID) {
//        this.roomID = roomID;
//    }

    private String roomNo,timeName;
    private int roomID,timeID,infoType;

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getTimeID() {
        return timeID;
    }

    public void setTimeID(int timeID) {
        this.timeID = timeID;
    }

    public int getInfoType() {
        return infoType;
    }

    public void setInfoType(int infoType) {
        this.infoType = infoType;
    }
}
