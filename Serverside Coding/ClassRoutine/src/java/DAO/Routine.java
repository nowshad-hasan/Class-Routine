/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author nowshad
 */
public class Routine {

    private MysqlConnection databaseConnection;
    public Connection connection;
    JsonObject jsonObject;
    JsonArray jsonArray;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String sql;

    public Routine() {
        super();

        databaseConnection = new MysqlConnection();
        connection = databaseConnection.getDbConnection();
    }

    public JsonObject getStudentRoutineWeekly(int sessionID, int dayID) {
//        System.out.println(sessionID+" "+dayID);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 0);
        date = calendar.getTime();
//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
//        dayID = calendar.get(Calendar.DAY_OF_WEEK);
        String currentDate = new String();

        for (int i = 0; i < 7; ++i) {
            if (i == 0) {
                calendar.add(Calendar.DATE, 0);
            } else {
                calendar.add(Calendar.DATE, 1);
            }
            date = calendar.getTime();
//            System.out.println(dateFormat.format(date));

            if (dayID == calendar.get(Calendar.DAY_OF_WEEK)) {
                currentDate = dateFormat.format(date);
                break;
            }
        }

//        currentDate="03-12-2017";
        System.out.println("Final Date " + currentDate);
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String dayName = outFormat.format(date);

        sql = "SELECT course_name,course_code,teacher_name,time_name,room_no,time_duration,room_status from reschedule_table,"
                + " routine_table,time_table,session_table,room_table,teacher_table,course_table WHERE"
                + " reschedule_table.routine_id=routine_table.routine_id and reschedule_table.time_id="
                + "time_table.time_id and teacher_table.teacher_id=routine_table.teacher_id and "
                + "routine_table.course_id=course_table.course_id and reschedule_table.room_id="
                + "room_table.room_id and routine_table.session_id=session_table.session_id and reschedule_date=? and routine_table.session_id=?";

        jsonObject = new JsonObject();
        jsonArray = new JsonArray();
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, currentDate);
            preparedStatement.setInt(2, sessionID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                JsonObject jObject = new JsonObject();

                jObject.addProperty("teacherName", resultSet.getString("teacher_name"));
                jObject.addProperty("courseName", resultSet.getString("course_name"));
                jObject.addProperty("courseCode", resultSet.getString("course_code"));
                jObject.addProperty("timeName", resultSet.getString("time_name"));
                jObject.addProperty("timeDuration", resultSet.getString("time_duration"));
                jObject.addProperty("roomNo", resultSet.getString("room_no"));
                jObject.addProperty("roomStatus", resultSet.getString("room_status"));
                jObject.addProperty("routineStatus", "rescheduled");

                System.out.println(jObject);

                jsonArray.add(jObject);

            }

            ArrayList<Integer> cancelClassList = new ArrayList<Integer>();

            sql = "select routine_id from cancellation_table where cancel_date=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, currentDate);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cancelClassList.add(resultSet.getInt("routine_id"));
            }
            System.out.println(cancelClassList);

            sql = "SELECT routine_id,teacher_name,course_name, course_code,time_name,"
                    + "time_duration,room_no,room_status FROM routine_table,"
                    + "teacher_table, room_table, time_table,course_table where"
                    + " routine_table.teacher_id=teacher_table.teacher_id and "
                    + "routine_table.time_id=time_table.time_id and routine_table.room_id ="
                    + " room_table.room_id and routine_table.course_id=course_table.course_id and session_id=? and day_id=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, sessionID);
            preparedStatement.setInt(2, dayID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                JsonObject jObject = new JsonObject();

                jObject.addProperty("teacherName", resultSet.getString("teacher_name"));
                jObject.addProperty("courseName", resultSet.getString("course_name"));
                jObject.addProperty("courseCode", resultSet.getString("course_code"));
                jObject.addProperty("timeName", resultSet.getString("time_name"));
                jObject.addProperty("timeDuration", resultSet.getString("time_duration"));
                jObject.addProperty("roomNo", resultSet.getString("room_no"));
                jObject.addProperty("roomStatus", resultSet.getString("room_status"));
                if (cancelClassList.contains(resultSet.getInt("routine_id"))) {
                    jObject.addProperty("routineStatus", "canceled");
                } else {
                    jObject.addProperty("routineStatus", "regular");
                }

                jsonArray.add(jObject);

            }
            jsonObject.addProperty("dateName", currentDate);
            jsonObject.addProperty("dayName", dayName);

            jsonObject.add("daily_routine", jsonArray);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return jsonObject;

    }

    public JsonObject getSingleDayStudentRoutine(int sessionID, String singleDate) throws ParseException {
        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = inFormat.parse(singleDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String dayName = outFormat.format(date);

//        System.out.println(goal);
//SELECT session_name,course_name,teacher_name,time_name,room_no from reschedule_table, routine_table,time_table,session_table,room_table,teacher_table,course_table WHERE reschedule_table.routine_id=routine_table.routine_id and reschedule_table.time_id=time_table.time_id and teacher_table.teacher_id=routine_table.teacher_id and routine_table.course_id=course_table.course_id and reschedule_table.room_id=room_table.room_id and routine_table.session_id=session_table.session_id and reschedule_date='3-12-2017' and routine_table.session_id=1
//        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
        int dayID = calendar.get(Calendar.DAY_OF_WEEK);

//        singleDate="3-12-2017";
        sql = "SELECT course_name,course_code,teacher_name,time_name,room_no,time_duration,room_status from reschedule_table,"
                + " routine_table,time_table,session_table,room_table,teacher_table,course_table WHERE"
                + " reschedule_table.routine_id=routine_table.routine_id and reschedule_table.time_id="
                + "time_table.time_id and teacher_table.teacher_id=routine_table.teacher_id and "
                + "routine_table.course_id=course_table.course_id and reschedule_table.room_id="
                + "room_table.room_id and routine_table.session_id=session_table.session_id and reschedule_date=? and routine_table.session_id=?";

        jsonObject = new JsonObject();
        jsonArray = new JsonArray();
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, singleDate);
            preparedStatement.setInt(2, sessionID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                JsonObject jObject = new JsonObject();

                jObject.addProperty("teacherName", resultSet.getString("teacher_name"));
                jObject.addProperty("courseName", resultSet.getString("course_name"));
                jObject.addProperty("courseCode", resultSet.getString("course_code"));
                jObject.addProperty("timeName", resultSet.getString("time_name"));
                jObject.addProperty("timeDuration", resultSet.getString("time_duration"));
                jObject.addProperty("roomNo", resultSet.getString("room_no"));
                jObject.addProperty("roomStatus", resultSet.getString("room_status"));
                jObject.addProperty("routineStatus", "rescheduled");

                System.out.println(jObject);

                jsonArray.add(jObject);

            }

            ArrayList<Integer> cancelClassList = new ArrayList<Integer>();

            sql = "select routine_id from cancellation_table where cancel_date=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, singleDate);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cancelClassList.add(resultSet.getInt("routine_id"));
            }
            System.out.println(cancelClassList);

            sql = "SELECT routine_id,teacher_name,course_name, course_code,time_name,"
                    + "time_duration,room_no,room_status FROM routine_table,"
                    + "teacher_table, room_table, time_table,course_table where"
                    + " routine_table.teacher_id=teacher_table.teacher_id and "
                    + "routine_table.time_id=time_table.time_id and routine_table.room_id ="
                    + " room_table.room_id and routine_table.course_id=course_table.course_id and session_id=? and day_id=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, sessionID);
            preparedStatement.setInt(2, dayID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                JsonObject jObject = new JsonObject();

                jObject.addProperty("teacherName", resultSet.getString("teacher_name"));
                jObject.addProperty("courseName", resultSet.getString("course_name"));
                jObject.addProperty("courseCode", resultSet.getString("course_code"));
                jObject.addProperty("timeName", resultSet.getString("time_name"));
                jObject.addProperty("timeDuration", resultSet.getString("time_duration"));
                jObject.addProperty("roomNo", resultSet.getString("room_no"));
                jObject.addProperty("roomStatus", resultSet.getString("room_status"));
                if (cancelClassList.contains(resultSet.getInt("routine_id"))) {
                    jObject.addProperty("routineStatus", "canceled");
                } else {
                    jObject.addProperty("routineStatus", "regular");
                }

                jsonArray.add(jObject);

            }
            jsonObject.addProperty("dateName", singleDate);
            jsonObject.addProperty("dayName", dayName);
            jsonObject.add("daily_routine", jsonArray);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public JsonObject getTeachertRoutineWeekly(int teacherID, int dayID) {
//        System.out.println(sessionID+" "+dayID);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 0);
        date = calendar.getTime();
//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
//        dayID = calendar.get(Calendar.DAY_OF_WEEK);
        String currentDate = new String();

        for (int i = 0; i < 7; ++i) {
            if (i == 0) {
                calendar.add(Calendar.DATE, 0);
            } else {
                calendar.add(Calendar.DATE, 1);
            }
            date = calendar.getTime();
//            System.out.println(dateFormat.format(date));

            if (dayID == calendar.get(Calendar.DAY_OF_WEEK)) {
                currentDate = dateFormat.format(date);
                break;
            }
        }

//        currentDate="03-12-2017";
        System.out.println("Final Date " + currentDate);
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String dayName = outFormat.format(date);

        jsonObject = new JsonObject();
        jsonArray = new JsonArray();

        sql = "SELECT reschedule_table.routine_id,session_name,course_name,course_code,time_name,room_no,time_duration,room_status from reschedule_table,"
                + " routine_table,time_table,session_table,room_table,teacher_table,course_table WHERE"
                + " reschedule_table.routine_id=routine_table.routine_id and reschedule_table.time_id="
                + "time_table.time_id and teacher_table.teacher_id=routine_table.teacher_id and "
                + "routine_table.course_id=course_table.course_id and reschedule_table.room_id="
                + "room_table.room_id and routine_table.session_id=session_table.session_id and reschedule_date=? and routine_table.teacher_id=?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, currentDate);
            preparedStatement.setInt(2, teacherID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                JsonObject jObject = new JsonObject();

                jObject.addProperty("routineID", resultSet.getString("routine_id"));
                jObject.addProperty("sessionName", resultSet.getString("session_name"));
                jObject.addProperty("courseName", resultSet.getString("course_name"));
                jObject.addProperty("courseCode", resultSet.getString("course_code"));
                jObject.addProperty("timeName", resultSet.getString("time_name"));
                jObject.addProperty("timeDuration", resultSet.getString("time_duration"));
                jObject.addProperty("roomNo", resultSet.getString("room_no"));
                jObject.addProperty("roomStatus", resultSet.getString("room_status"));
                jObject.addProperty("routineStatus", "rescheduled");

                System.out.println(jObject);

                jsonArray.add(jObject);

            }

            ArrayList<Integer> cancelClassList = new ArrayList<Integer>();

            sql = "select routine_id from cancellation_table where cancel_date=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, currentDate);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cancelClassList.add(resultSet.getInt("routine_id"));
            }
            System.out.println(cancelClassList);

            sql = "SELECT routine_id,session_name,course_name, course_code,time_name,\n"
                    + "                time_duration,room_no,room_status FROM routine_table,\n"
                    + "                 room_table, time_table,course_table,session_table where\n"
                    + "                 routine_table.session_id=session_table.session_id and \n"
                    + "                routine_table.time_id=time_table.time_id and routine_table.room_id =\n"
                    + "                room_table.room_id and routine_table.course_id=course_table.course_id and teacher_id=? and day_id=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, teacherID);
            preparedStatement.setInt(2, dayID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                JsonObject jObject = new JsonObject();

                jObject.addProperty("routineID", resultSet.getString("routine_id"));
                jObject.addProperty("sessionName", resultSet.getString("session_name"));
                jObject.addProperty("courseName", resultSet.getString("course_name"));
                jObject.addProperty("courseCode", resultSet.getString("course_code"));
                jObject.addProperty("timeName", resultSet.getString("time_name"));
                jObject.addProperty("timeDuration", resultSet.getString("time_duration"));
                jObject.addProperty("roomNo", resultSet.getString("room_no"));
                jObject.addProperty("roomStatus", resultSet.getString("room_status"));

                if (cancelClassList.contains(resultSet.getInt("routine_id"))) {
                    jObject.addProperty("routineStatus", "canceled");
                } else {
                    jObject.addProperty("routineStatus", "regular");
                }

                jsonArray.add(jObject);

            }

            jsonObject.addProperty("dateName", currentDate);
            jsonObject.addProperty("dayName", dayName);
            jsonObject.add("daily_routine", jsonArray);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return jsonObject;

//        sql = "SELECT session_name,course_name, course_code,time_name,\n"
//                + "                time_duration,room_no,room_status FROM routine_table,\n"
//                + "                 room_table, time_table,course_table,session_table where\n"
//                + "                 routine_table.session_id=session_table.session_id and \n"
//                + "                routine_table.time_id=time_table.time_id and routine_table.room_id =\n"
//                + "                room_table.room_id and routine_table.course_id=course_table.course_id and teacher_id=? and day_id=?";
//
//        jsonObject = new JsonObject();
//        jsonArray = new JsonArray();
//
//        try {
//
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setInt(1, teacherID);
//            preparedStatement.setInt(2, dayID);
//            resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//
//                JsonObject jObject = new JsonObject();
//
//                jObject.addProperty("sessionName", resultSet.getString("session_name"));
//                jObject.addProperty("courseName", resultSet.getString("course_name"));
//                jObject.addProperty("courseCode", resultSet.getString("course_code"));
//                jObject.addProperty("timeName", resultSet.getString("time_name"));
//                jObject.addProperty("timeDuration", resultSet.getString("time_duration"));
//                jObject.addProperty("roomNo", resultSet.getString("room_no"));
//                jObject.addProperty("roomStatus", resultSet.getString("room_status"));
//
//                jsonArray.add(jObject);
//
//            }
//            jsonObject.add("daily_routine", jsonArray);
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        System.out.print(jsonObject);
//        return jsonObject;
    }

    public JsonObject getSingleDayTeacherRoutine(int teacherID, String singleDate) throws ParseException {
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();

        sql = "SELECT reschedule_table.routine_id,session_name,course_name,course_code,time_name,room_no,time_duration,room_status from reschedule_table,"
                + " routine_table,time_table,session_table,room_table,teacher_table,course_table WHERE"
                + " reschedule_table.routine_id=routine_table.routine_id and reschedule_table.time_id="
                + "time_table.time_id and teacher_table.teacher_id=routine_table.teacher_id and "
                + "routine_table.course_id=course_table.course_id and reschedule_table.room_id="
                + "room_table.room_id and routine_table.session_id=session_table.session_id and reschedule_date=? and routine_table.teacher_id=?";

        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = inFormat.parse(singleDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String dayName = outFormat.format(date);

//        System.out.println(goal);
//SELECT session_name,course_name,teacher_name,time_name,room_no from reschedule_table, routine_table,time_table,session_table,room_table,teacher_table,course_table WHERE reschedule_table.routine_id=routine_table.routine_id and reschedule_table.time_id=time_table.time_id and teacher_table.teacher_id=routine_table.teacher_id and routine_table.course_id=course_table.course_id and reschedule_table.room_id=room_table.room_id and routine_table.session_id=session_table.session_id and reschedule_date='3-12-2017' and routine_table.session_id=1
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
        int dayID = calendar.get(Calendar.DAY_OF_WEEK);

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, singleDate);
            preparedStatement.setInt(2, teacherID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                JsonObject jObject = new JsonObject();

                jObject.addProperty("routineID", resultSet.getString("routine_id"));
                jObject.addProperty("sessionName", resultSet.getString("session_name"));
                jObject.addProperty("courseName", resultSet.getString("course_name"));
                jObject.addProperty("courseCode", resultSet.getString("course_code"));
                jObject.addProperty("timeName", resultSet.getString("time_name"));
                jObject.addProperty("timeDuration", resultSet.getString("time_duration"));
                jObject.addProperty("roomNo", resultSet.getString("room_no"));
                jObject.addProperty("roomStatus", resultSet.getString("room_status"));
                jObject.addProperty("routineStatus", "rescheduled");

                System.out.println(jObject);

                jsonArray.add(jObject);

            }

            ArrayList<Integer> cancelClassList = new ArrayList<Integer>();

            sql = "select routine_id from cancellation_table where cancel_date=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, singleDate);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cancelClassList.add(resultSet.getInt("routine_id"));
            }
            System.out.println(cancelClassList);

            sql = "SELECT routine_id,session_name,course_name, course_code,time_name,\n"
                    + "                time_duration,room_no,room_status FROM routine_table,\n"
                    + "                 room_table, time_table,course_table,session_table where\n"
                    + "                 routine_table.session_id=session_table.session_id and \n"
                    + "                routine_table.time_id=time_table.time_id and routine_table.room_id =\n"
                    + "                room_table.room_id and routine_table.course_id=course_table.course_id and teacher_id=? and day_id=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, teacherID);
            preparedStatement.setInt(2, dayID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                JsonObject jObject = new JsonObject();

                jObject.addProperty("routineID", resultSet.getString("routine_id"));
                jObject.addProperty("sessionName", resultSet.getString("session_name"));
                jObject.addProperty("courseName", resultSet.getString("course_name"));
                jObject.addProperty("courseCode", resultSet.getString("course_code"));
                jObject.addProperty("timeName", resultSet.getString("time_name"));
                jObject.addProperty("timeDuration", resultSet.getString("time_duration"));
                jObject.addProperty("roomNo", resultSet.getString("room_no"));
                jObject.addProperty("roomStatus", resultSet.getString("room_status"));

                if (cancelClassList.contains(resultSet.getInt("routine_id"))) {
                    jObject.addProperty("routineStatus", "canceled");
                } else {
                    jObject.addProperty("routineStatus", "regular");
                }

                jsonArray.add(jObject);

            }

            jsonObject.addProperty("dateName", singleDate);
            jsonObject.addProperty("dayName", dayName);
            jsonObject.add("daily_routine", jsonArray);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return jsonObject;

    }

    public JsonObject getEmptyClassWeekly(int timeDuration, String roomStatus) {

        JsonObject jsonWeeklyObject = new JsonObject();
        JsonArray jsonWeeklyArray = new JsonArray();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 0);
        date = calendar.getTime();
//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
//        dayID = calendar.get(Calendar.DAY_OF_WEEK);
        String currentDate = new String();
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        for (int counter = 0; counter < 7; ++counter) {
            if (counter == 0) {
                calendar.add(Calendar.DATE, 0);
            } else {
                calendar.add(Calendar.DATE, 1);
            }
            date = calendar.getTime();
//            System.out.println(dateFormat.format(date));

            currentDate = dateFormat.format(date);
            String dayName = outFormat.format(date);

            System.out.println("current date " + currentDate + " dayName " + dayName);

            jsonObject = new JsonObject();
            jsonArray = new JsonArray();

            int[] emptyTimeID = new int[20];
            int[] emptyRoomID = new int[20];
            String[] emptyTime = new String[20];
            String[] emptyRoom = new String[20];
            int lengthTimeID = 0;
            int lengthRoomID = 0;

            sql = "SELECT * FROM `time_table` WHERE time_duration=?";

            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, timeDuration);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    emptyTimeID[lengthTimeID] = resultSet.getInt("time_id");
                    emptyTime[lengthTimeID] = resultSet.getString("time_name");
                    lengthTimeID++;

                }

                System.out.println(lengthTimeID);

                sql = "SELECT * FROM `room_table` WHERE room_status=?";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, roomStatus);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    emptyRoomID[lengthRoomID] = resultSet.getInt("room_id");
                    emptyRoom[lengthRoomID] = resultSet.getString("room_no");
                    lengthRoomID++;
                }

                System.out.println(lengthRoomID);

                sql = "SELECT time_name,room_no from reschedule_table,room_table,"
                        + "time_table where room_table.room_id=reschedule_table.room_id and"
                        + " reschedule_table.time_id=time_table.time_id and reschedule_date=? and room_status=?";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, currentDate);
                preparedStatement.setString(2, roomStatus);
                resultSet = preparedStatement.executeQuery();

                Map<String, ArrayList<String>> myHashMap = new HashMap<String, ArrayList<String>>();

                while (resultSet.next()) {

                    System.out.println(resultSet.getString("time_name"));

                    String[] parts = resultSet.getString("time_name").split("-");
                    int part1 = Integer.parseInt(parts[0]);
                    int part2 = Integer.parseInt(parts[1]);
//                System.out.println(part1+""+part2);

                    for (int i = part1; i < part2; ++i) {
                        ArrayList<String> list;

                        if (myHashMap.containsKey(Integer.toString(i))) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it

                            list = myHashMap.get(Integer.toString(i));
                            list.add(resultSet.getString("room_no"));
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            list = new ArrayList<String>();
                            list.add(resultSet.getString("room_no"));
                            myHashMap.put(Integer.toString(i), list);
                        }
                    }
                }

                ArrayList<Integer> cancelClassList = new ArrayList<Integer>();

                sql = "select routine_id from cancellation_table where cancel_date=?";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, currentDate);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    cancelClassList.add(resultSet.getInt("routine_id"));
                }
                System.out.println(cancelClassList);

//                SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
//                Date date = inFormat.parse(singleDate);
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//
////SELECT session_name,course_name,teacher_name,time_name,room_no from reschedule_table, routine_table,time_table,session_table,room_table,teacher_table,course_table WHERE reschedule_table.routine_id=routine_table.routine_id and reschedule_table.time_id=time_table.time_id and teacher_table.teacher_id=routine_table.teacher_id and routine_table.course_id=course_table.course_id and reschedule_table.room_id=room_table.room_id and routine_table.session_id=session_table.session_id and reschedule_date='3-12-2017' and routine_table.session_id=1
//                System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
                int dayID = calendar.get(Calendar.DAY_OF_WEEK);

                sql = "SELECT routine_id,routine_table.time_id,time_table.time_name,routine_table.room_id,"
                        + " room_table.room_no from routine_table,time_table,room_table where"
                        + " routine_table.time_id=time_table.time_id and "
                        + "routine_table.room_id=room_table.room_id and day_id=? and room_status=?";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, dayID);
                preparedStatement.setString(2, roomStatus);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {

                    if (!cancelClassList.contains(resultSet.getInt("routine_id"))) {

                        System.out.println(resultSet.getString("time_name"));

                        String[] parts = resultSet.getString("time_name").split("-");
                        int part1 = Integer.parseInt(parts[0]);
                        int part2 = Integer.parseInt(parts[1]);
//                System.out.println(part1+""+part2);

                        for (int i = part1; i < part2; ++i) {
                            ArrayList<String> list;

                            if (myHashMap.containsKey(Integer.toString(i))) {
                                // if the key has already been used,
                                // we'll just grab the array list and add the value to it

                                list = myHashMap.get(Integer.toString(i));
                                list.add(resultSet.getString("room_no"));
                            } else {
                                // if the key hasn't been used yet,
                                // we'll create a new ArrayList<String> object, add the value
                                // and put it in the array list with the new key
                                list = new ArrayList<String>();
                                list.add(resultSet.getString("room_no"));
                                myHashMap.put(Integer.toString(i), list);
                            }
                        }

                    }

                }

                System.out.println(Arrays.asList(myHashMap));

                for (int i = 0; i < lengthTimeID; ++i) {

                    JsonObject jObject = new JsonObject();
                    JsonArray jArray = new JsonArray();
                    jObject.addProperty("timeName", emptyTime[i]);
                    jObject.addProperty("timeId", emptyTimeID[i]);

                    System.out.println(emptyTime[i]);

                    String[] parts = emptyTime[i].split("-");
                    int part1 = Integer.parseInt(parts[0]);
                    int part2 = Integer.parseInt(parts[1]);

                    String[] existRoomArray = new String[20];
                    ArrayList<String> existRoomList = new ArrayList<String>();

                    for (int j = part1; j < part2; ++j) {
//                    System.out.println(j);

                        if (myHashMap.containsKey(Integer.toString(j))) {

                            for (int k = 0; k < myHashMap.get(Integer.toString(j)).size(); ++k) {
//                            System.out.println(myHashMap.get(Integer.toString(j)).get(k));
                                if (!existRoomList.contains(myHashMap.get(Integer.toString(j)).get(k))) {
                                    existRoomList.add(myHashMap.get(Integer.toString(j)).get(k));
                                }
                            }
                        }
                    }

//                for(int j=0;j<existRoomList.size();++j)
//                {
//                    System.out.println(existRoomList.get(j));
//                }
                    for (int j = 0; j < lengthRoomID; ++j) {

                        JsonObject jsonRoomObject = new JsonObject();

                        if (!existRoomList.contains(emptyRoom[j])) {
                            System.out.println(emptyRoom[j]);
                            jsonRoomObject.addProperty("roomNo", emptyRoom[j]);
                            jsonRoomObject.addProperty("roomID", emptyRoomID[j]);
                            jArray.add(jsonRoomObject);
                        }

                    }
                    jObject.add("rooms", jArray);
                    jsonArray.add(jObject);

                }

                jsonObject.addProperty("dayDate", currentDate);
                jsonObject.addProperty("dayName", dayName);
                jsonObject.addProperty("dayID", dayID);
                jsonObject.add("daily_routine", jsonArray);

                jsonWeeklyArray.add(jsonObject);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        jsonWeeklyObject.add("empty_class", jsonWeeklyArray);

        return jsonWeeklyObject;

//        currentDate="03-12-2017";
//        System.out.println("Final Date " + currentDate);
//        int[] emptyTimeID = new int[20];
//        int[] emptyRoomID = new int[20];
//        String[] emptyTime = new String[20];
//        String[] emptyRoom = new String[20];
//        int lengthTimeID = 0;
//        int lengthRoomID = 0;
////         System.out.print(emptyClass);
//        sql = "SELECT * FROM `time_table` WHERE time_duration=?";
//
//        try {
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setInt(1, timeDuration);
//
//            resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                emptyTimeID[lengthTimeID] = resultSet.getInt("time_id");
//                emptyTime[lengthTimeID] = resultSet.getString("time_name");
//                lengthTimeID++;
//
//            }
//
//            System.out.println(lengthTimeID);
//
//            sql = "SELECT * FROM `room_table` WHERE room_status=?";
//
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1, roomStatus);
//
//            resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                emptyRoomID[lengthRoomID] = resultSet.getInt("room_id");
//                emptyRoom[lengthRoomID] = resultSet.getString("room_no");
//                lengthRoomID++;
//            }
//
//            System.out.println(lengthRoomID);
//
//            sql = "SELECT routine_table.time_id,time_table.time_name,routine_table.room_id,"
//                    + " room_table.room_no from routine_table,time_table,room_table where"
//                    + " routine_table.time_id=time_table.time_id and "
//                    + "routine_table.room_id=room_table.room_id and day_id=1 and room_status=\"lab\"";
//
//            Map<String, ArrayList<String>> myHashMap = new HashMap<String, ArrayList<String>>();
//
//            preparedStatement = connection.prepareStatement(sql);
//            resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                System.out.println(resultSet.getString("time_name"));
//
//                String[] parts = resultSet.getString("time_name").split("-");
//                int part1 = Integer.parseInt(parts[0]);
//                int part2 = Integer.parseInt(parts[1]);
////                System.out.println(part1+""+part2);
//
//                for (int i = part1; i < part2; ++i) {
//                    ArrayList<String> list;
//
//                    if (myHashMap.containsKey(Integer.toString(i))) {
//                        // if the key has already been used,
//                        // we'll just grab the array list and add the value to it
//
//                        list = myHashMap.get(Integer.toString(i));
//                        list.add(resultSet.getString("room_no"));
//                    } else {
//                        // if the key hasn't been used yet,
//                        // we'll create a new ArrayList<String> object, add the value
//                        // and put it in the array list with the new key
//                        list = new ArrayList<String>();
//                        list.add(resultSet.getString("room_no"));
//                        myHashMap.put(Integer.toString(i), list);
//                    }
//                }
//
//            }
//
//            System.out.println(Arrays.asList(myHashMap));
//
//            for (int i = 0; i < lengthTimeID; ++i) {
//
//                System.out.println(emptyTime[i]);
//
//                String[] parts = emptyTime[i].split("-");
//                int part1 = Integer.parseInt(parts[0]);
//                int part2 = Integer.parseInt(parts[1]);
//
//                String[] existRoomArray = new String[20];
//                ArrayList<String> existRoomList = new ArrayList<String>();
//
//                int counter = 0;
//
//                for (int j = part1; j < part2; ++j) {
////                    System.out.println(j);
//
//                    if (myHashMap.containsKey(Integer.toString(j))) {
//
//                        for (int k = 0; k < myHashMap.get(Integer.toString(j)).size(); ++k) {
////                            System.out.println(myHashMap.get(Integer.toString(j)).get(k));
//                            if (!existRoomList.contains(myHashMap.get(Integer.toString(j)).get(k))) {
//                                existRoomList.add(myHashMap.get(Integer.toString(j)).get(k));
//                            }
//                        }
//                    }
//                }
//
////                for(int j=0;j<existRoomList.size();++j)
////                {
////                    System.out.println(existRoomList.get(j));
////                }
//                for (int j = 0; j < lengthRoomID; ++j) {
//
//                    if (!existRoomList.contains(emptyRoom[j])) {
//                        System.out.println(emptyRoom[j]);
//                    }
//
//                }
//
//            }
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
    }

    public JsonObject getEmptyClassWeekly(int dayID, int timeDuration, String roomStatus) {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 0);
        date = calendar.getTime();
//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
//        dayID = calendar.get(Calendar.DAY_OF_WEEK);
        String currentDate = new String();

        for (int i = 0; i < 7; ++i) {
            if (i == 0) {
                calendar.add(Calendar.DATE, 0);
            } else {
                calendar.add(Calendar.DATE, 1);
            }
            date = calendar.getTime();
//            System.out.println(dateFormat.format(date));

            if (dayID == calendar.get(Calendar.DAY_OF_WEEK)) {
                currentDate = dateFormat.format(date);
                break;
            }
        }

//        currentDate="03-12-2017";
        System.out.println("Final Date " + currentDate);
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String dayName = outFormat.format(date);

        jsonObject = new JsonObject();
        jsonArray = new JsonArray();

        int[] emptyTimeID = new int[20];
        int[] emptyRoomID = new int[20];
        String[] emptyTime = new String[20];
        String[] emptyRoom = new String[20];
        int lengthTimeID = 0;
        int lengthRoomID = 0;

        sql = "SELECT * FROM `time_table` WHERE time_duration=?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, timeDuration);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                emptyTimeID[lengthTimeID] = resultSet.getInt("time_id");
                emptyTime[lengthTimeID] = resultSet.getString("time_name");
                lengthTimeID++;

            }

            System.out.println(lengthTimeID);

            sql = "SELECT * FROM `room_table` WHERE room_status=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, roomStatus);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                emptyRoomID[lengthRoomID] = resultSet.getInt("room_id");
                emptyRoom[lengthRoomID] = resultSet.getString("room_no");
                lengthRoomID++;
            }

            System.out.println(lengthRoomID);

            sql = "SELECT time_name,room_no from reschedule_table,room_table,"
                    + "time_table where room_table.room_id=reschedule_table.room_id and"
                    + " reschedule_table.time_id=time_table.time_id and reschedule_date=? and room_status=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, currentDate);
            preparedStatement.setString(2, roomStatus);
            resultSet = preparedStatement.executeQuery();

            Map<String, ArrayList<String>> myHashMap = new HashMap<String, ArrayList<String>>();

            while (resultSet.next()) {

                System.out.println(resultSet.getString("time_name"));

                String[] parts = resultSet.getString("time_name").split("-");
                int part1 = Integer.parseInt(parts[0]);
                int part2 = Integer.parseInt(parts[1]);
//                System.out.println(part1+""+part2);

                for (int i = part1; i < part2; ++i) {
                    ArrayList<String> list;

                    if (myHashMap.containsKey(Integer.toString(i))) {
                        // if the key has already been used,
                        // we'll just grab the array list and add the value to it

                        list = myHashMap.get(Integer.toString(i));
                        list.add(resultSet.getString("room_no"));
                    } else {
                        // if the key hasn't been used yet,
                        // we'll create a new ArrayList<String> object, add the value
                        // and put it in the array list with the new key
                        list = new ArrayList<String>();
                        list.add(resultSet.getString("room_no"));
                        myHashMap.put(Integer.toString(i), list);
                    }
                }
            }

            ArrayList<Integer> cancelClassList = new ArrayList<Integer>();

            sql = "select routine_id from cancellation_table where cancel_date=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, currentDate);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cancelClassList.add(resultSet.getInt("routine_id"));
            }
            System.out.println(cancelClassList);

//            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
//            Date date = inFormat.parse(currentDate);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
//            String dayName = outFormat.format(date);
//SELECT session_name,course_name,teacher_name,time_name,room_no from reschedule_table, routine_table,time_table,session_table,room_table,teacher_table,course_table WHERE reschedule_table.routine_id=routine_table.routine_id and reschedule_table.time_id=time_table.time_id and teacher_table.teacher_id=routine_table.teacher_id and routine_table.course_id=course_table.course_id and reschedule_table.room_id=room_table.room_id and routine_table.session_id=session_table.session_id and reschedule_date='3-12-2017' and routine_table.session_id=1
//            System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
//            int dayID = calendar.get(Calendar.DAY_OF_WEEK);
            sql = "SELECT routine_id,routine_table.time_id,time_table.time_name,routine_table.room_id,"
                    + " room_table.room_no from routine_table,time_table,room_table where"
                    + " routine_table.time_id=time_table.time_id and "
                    + "routine_table.room_id=room_table.room_id and day_id=? and room_status=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, dayID);
            preparedStatement.setString(2, roomStatus);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                if (!cancelClassList.contains(resultSet.getInt("routine_id"))) {

                    System.out.println(resultSet.getString("time_name"));

                    String[] parts = resultSet.getString("time_name").split("-");
                    int part1 = Integer.parseInt(parts[0]);
                    int part2 = Integer.parseInt(parts[1]);
//                System.out.println(part1+""+part2);

                    for (int i = part1; i < part2; ++i) {
                        ArrayList<String> list;

                        if (myHashMap.containsKey(Integer.toString(i))) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it

                            list = myHashMap.get(Integer.toString(i));
                            list.add(resultSet.getString("room_no"));
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            list = new ArrayList<String>();
                            list.add(resultSet.getString("room_no"));
                            myHashMap.put(Integer.toString(i), list);
                        }
                    }

                }

            }

            System.out.println(Arrays.asList(myHashMap));

            for (int i = 0; i < lengthTimeID; ++i) {

                JsonObject jObject = new JsonObject();
                JsonArray jArray = new JsonArray();
                jObject.addProperty("timeName", emptyTime[i]);
                jObject.addProperty("timeID", emptyTimeID[i]);

                System.out.println(emptyTime[i]);

                String[] parts = emptyTime[i].split("-");
                int part1 = Integer.parseInt(parts[0]);
                int part2 = Integer.parseInt(parts[1]);

                String[] existRoomArray = new String[20];
                ArrayList<String> existRoomList = new ArrayList<String>();

                int counter = 0;

                for (int j = part1; j < part2; ++j) {
//                    System.out.println(j);

                    if (myHashMap.containsKey(Integer.toString(j))) {

                        for (int k = 0; k < myHashMap.get(Integer.toString(j)).size(); ++k) {
//                            System.out.println(myHashMap.get(Integer.toString(j)).get(k));
                            if (!existRoomList.contains(myHashMap.get(Integer.toString(j)).get(k))) {
                                existRoomList.add(myHashMap.get(Integer.toString(j)).get(k));
                            }
                        }
                    }
                }

//                for(int j=0;j<existRoomList.size();++j)
//                {
//                    System.out.println(existRoomList.get(j));
//                }
                for (int j = 0; j < lengthRoomID; ++j) {

                    JsonObject jsonRoomObject = new JsonObject();

                    if (!existRoomList.contains(emptyRoom[j])) {
                        System.out.println(emptyRoom[j]);
                        jsonRoomObject.addProperty("roomNo", emptyRoom[j]);
                        jsonRoomObject.addProperty("roomID", emptyRoomID[j]);
                        jArray.add(jsonRoomObject);
                    }

                }
                jObject.add("rooms", jArray);
                jsonArray.add(jObject);

            }
            jsonObject.addProperty("dateName", currentDate);
            jsonObject.addProperty("dayName", dayName);

            jsonObject.add("daily_routine", jsonArray);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return jsonObject;

    }

    public JsonObject getSingleDayEmptyClass(String singleDate, int timeDuration, String roomStatus) throws ParseException {

        jsonObject = new JsonObject();
        jsonArray = new JsonArray();

        int[] emptyTimeID = new int[20];
        int[] emptyRoomID = new int[20];
        String[] emptyTime = new String[20];
        String[] emptyRoom = new String[20];
        int lengthTimeID = 0;
        int lengthRoomID = 0;

        sql = "SELECT * FROM `time_table` WHERE time_duration=?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, timeDuration);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                emptyTimeID[lengthTimeID] = resultSet.getInt("time_id");
                emptyTime[lengthTimeID] = resultSet.getString("time_name");
                lengthTimeID++;

            }

            System.out.println(lengthTimeID);

            sql = "SELECT * FROM `room_table` WHERE room_status=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, roomStatus);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                emptyRoomID[lengthRoomID] = resultSet.getInt("room_id");
                emptyRoom[lengthRoomID] = resultSet.getString("room_no");
                lengthRoomID++;
            }

            System.out.println(lengthRoomID);

            sql = "SELECT time_name,room_no from reschedule_table,room_table,"
                    + "time_table where room_table.room_id=reschedule_table.room_id and"
                    + " reschedule_table.time_id=time_table.time_id and reschedule_date=? and room_status=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, singleDate);
            preparedStatement.setString(2, roomStatus);
            resultSet = preparedStatement.executeQuery();

            Map<String, ArrayList<String>> myHashMap = new HashMap<String, ArrayList<String>>();

            while (resultSet.next()) {

                System.out.println(resultSet.getString("time_name"));

                String[] parts = resultSet.getString("time_name").split("-");
                int part1 = Integer.parseInt(parts[0]);
                int part2 = Integer.parseInt(parts[1]);
//                System.out.println(part1+""+part2);

                for (int i = part1; i < part2; ++i) {
                    ArrayList<String> list;

                    if (myHashMap.containsKey(Integer.toString(i))) {
                        // if the key has already been used,
                        // we'll just grab the array list and add the value to it

                        list = myHashMap.get(Integer.toString(i));
                        list.add(resultSet.getString("room_no"));
                    } else {
                        // if the key hasn't been used yet,
                        // we'll create a new ArrayList<String> object, add the value
                        // and put it in the array list with the new key
                        list = new ArrayList<String>();
                        list.add(resultSet.getString("room_no"));
                        myHashMap.put(Integer.toString(i), list);
                    }
                }
            }

            ArrayList<Integer> cancelClassList = new ArrayList<Integer>();

            sql = "select routine_id from cancellation_table where cancel_date=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, singleDate);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cancelClassList.add(resultSet.getInt("routine_id"));
            }
            System.out.println(cancelClassList);

            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = inFormat.parse(singleDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String dayName = outFormat.format(date);

//SELECT session_name,course_name,teacher_name,time_name,room_no from reschedule_table, routine_table,time_table,session_table,room_table,teacher_table,course_table WHERE reschedule_table.routine_id=routine_table.routine_id and reschedule_table.time_id=time_table.time_id and teacher_table.teacher_id=routine_table.teacher_id and routine_table.course_id=course_table.course_id and reschedule_table.room_id=room_table.room_id and routine_table.session_id=session_table.session_id and reschedule_date='3-12-2017' and routine_table.session_id=1
            System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
            int dayID = calendar.get(Calendar.DAY_OF_WEEK);

            sql = "SELECT routine_id,routine_table.time_id,time_table.time_name,routine_table.room_id,"
                    + " room_table.room_no from routine_table,time_table,room_table where"
                    + " routine_table.time_id=time_table.time_id and "
                    + "routine_table.room_id=room_table.room_id and day_id=? and room_status=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, dayID);
            preparedStatement.setString(2, roomStatus);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                if (!cancelClassList.contains(resultSet.getInt("routine_id"))) {

                    System.out.println(resultSet.getString("time_name"));

                    String[] parts = resultSet.getString("time_name").split("-");
                    int part1 = Integer.parseInt(parts[0]);
                    int part2 = Integer.parseInt(parts[1]);
//                System.out.println(part1+""+part2);

                    for (int i = part1; i < part2; ++i) {
                        ArrayList<String> list;

                        if (myHashMap.containsKey(Integer.toString(i))) {
                            // if the key has already been used,
                            // we'll just grab the array list and add the value to it

                            list = myHashMap.get(Integer.toString(i));
                            list.add(resultSet.getString("room_no"));
                        } else {
                            // if the key hasn't been used yet,
                            // we'll create a new ArrayList<String> object, add the value
                            // and put it in the array list with the new key
                            list = new ArrayList<String>();
                            list.add(resultSet.getString("room_no"));
                            myHashMap.put(Integer.toString(i), list);
                        }
                    }

                }

            }

            System.out.println(Arrays.asList(myHashMap));

            for (int i = 0; i < lengthTimeID; ++i) {

                JsonObject jObject = new JsonObject();
                JsonArray jArray = new JsonArray();
                jObject.addProperty("timeName", emptyTime[i]);
                jObject.addProperty("timeID", emptyTimeID[i]);

                System.out.println(emptyTime[i]);

                String[] parts = emptyTime[i].split("-");
                int part1 = Integer.parseInt(parts[0]);
                int part2 = Integer.parseInt(parts[1]);

                String[] existRoomArray = new String[20];
                ArrayList<String> existRoomList = new ArrayList<String>();

                int counter = 0;

                for (int j = part1; j < part2; ++j) {
//                    System.out.println(j);

                    if (myHashMap.containsKey(Integer.toString(j))) {

                        for (int k = 0; k < myHashMap.get(Integer.toString(j)).size(); ++k) {
//                            System.out.println(myHashMap.get(Integer.toString(j)).get(k));
                            if (!existRoomList.contains(myHashMap.get(Integer.toString(j)).get(k))) {
                                existRoomList.add(myHashMap.get(Integer.toString(j)).get(k));
                            }
                        }
                    }
                }

//                for(int j=0;j<existRoomList.size();++j)
//                {
//                    System.out.println(existRoomList.get(j));
//                }
                for (int j = 0; j < lengthRoomID; ++j) {

                    JsonObject jsonRoomObject = new JsonObject();

                    if (!existRoomList.contains(emptyRoom[j])) {
                        System.out.println(emptyRoom[j]);
                        jsonRoomObject.addProperty("roomNo", emptyRoom[j]);
                        jsonRoomObject.addProperty("roomID", emptyRoomID[j]);
                        jArray.add(jsonRoomObject);
                    }

                }
                jObject.add("rooms", jArray);
                jsonArray.add(jObject);

            }
            jsonObject.addProperty("dateName", singleDate);
            jsonObject.addProperty("dayName", dayName);

            jsonObject.add("daily_routine", jsonArray);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return jsonObject;

    }

    public JsonObject getLoginStatus(String userEmail, String userPass) {
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();

        sql = "select * from user_table where user_email=? and user_pass=?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userEmail);
            preparedStatement.setString(2, userPass);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                jsonObject.addProperty("loginStatus", "success");
                jsonObject.addProperty("userID", resultSet.getInt("user_id"));
                jsonObject.addProperty("userRole", resultSet.getString("user_role"));
            } else {
                jsonObject.addProperty("loginStatus", "failed");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return jsonObject;

    }

    public JsonObject getUserDetails(int userID, String userRole) {
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();

        try {

            if (userRole.equals("teacher")) {

                sql = "select * from teacher_table where user_id=?";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userID);

                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    jsonObject.addProperty("teacherName", resultSet.getString("teacher_name"));
                    jsonObject.addProperty("teacherID", resultSet.getString("teacher_id"));
                }

            } else if (userRole.equals("student")) {
                sql = "SELECT student_table.session_id,student_name,student_reg,session_name FROM student_table,"
                        + "session_table WHERE student_table.session_id=session_table.session_id and user_id=?";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userID);

                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    jsonObject.addProperty("studentName", resultSet.getString("student_name"));
                    jsonObject.addProperty("studentSession", resultSet.getString("session_name"));
                    jsonObject.addProperty("studentReg", resultSet.getString("student_reg"));
                    jsonObject.addProperty("studentSessionID", resultSet.getString("session_id"));
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return jsonObject;

    }

    public JsonObject cancelClass(int routineID, String cancelDate) {
        jsonObject = new JsonObject();
        jsonArray = new JsonArray();

        //    SELECT course_name,course_code, time_name, teacher_name, room_no,session_name, day_name from course_table,teacher_table,room_table, time_table, session_table, day_table,routine_table where routine_table.course_id=course_table.course_id and routine_table.time_id=time_table.time_id and routine_table.teacher_id=teacher_table.teacher_id and routine_table.room_id=room_table.room_id and routine_table.session_id=session_table.session_id and routine_table.day_id=day_table.day_id and routine_table.routine_id=2
        sql = "INSERT INTO `cancellation_table` (`cancellation_id`, `routine_id`, `cancel_date`) VALUES (NULL, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, routineID);
            preparedStatement.setString(2, cancelDate);

            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate > 0) {
                jsonObject.addProperty("cancelStatus", "successful");

                sql = "SELECT course_name,course_code, time_name, teacher_name, room_no,session_name,"
                        + " day_name from course_table,teacher_table,room_table, time_table, session_table,"
                        + " day_table,routine_table where routine_table.course_id=course_table.course_id"
                        + " and routine_table.time_id=time_table.time_id and routine_table.teacher_id="
                        + "teacher_table.teacher_id and routine_table.room_id=room_table.room_id and"
                        + " routine_table.session_id=session_table.session_id and routine_table.day_id=day_table.day_id and routine_table.routine_id=?";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, routineID);

                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String courseName = resultSet.getString("course_name");
                    String courseNo = resultSet.getString("course_code");
                    String teacherName = resultSet.getString("teacher_name");
                    String timeName = resultSet.getString("time_name");
                    String roomNo = resultSet.getString("room_no");
                    String dayName = resultSet.getString("day_name");
                    String sessionName = resultSet.getString("session_name");

                    System.out.println(courseName + " " + courseNo + " " + teacherName + " " + timeName + " " + roomNo + " " + cancelDate + " " + dayName + " " + sessionName);
                    String message = courseName + "(" + courseNo + ")" + " .by" + teacherName + " from time " + timeName + "on Room no." + roomNo + " of " + cancelDate + "(" + dayName
                            + ") is canceled!";

                    Notification notification = new Notification();
                    notification.sendNotification(message, sessionName);

                }

            } else {
                jsonObject.addProperty("cancelStatus", "unsuccessful");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return jsonObject;
    }

    public JsonObject rescheduleClass(int routineID, String rescheduleDate, int timeID, int roomID, String cancelDate) {

        jsonObject = new JsonObject();
        String courseName=null, courseNo=null, teacherName=null, timeName=null, roomNo=null, dayName=null, sessionName=null, newTime=null, newRoom=null, dayOfWeek=null;

        sql = "INSERT INTO `reschedule_table` (`reschedule_id`, `routine_id`, `reschedule_date`, `time_id`, `room_id`) VALUES (NULL, ?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, routineID);
            preparedStatement.setString(2, rescheduleDate);
            preparedStatement.setInt(3, timeID);
            preparedStatement.setInt(4, roomID);

            int executeUpdateReschedule = preparedStatement.executeUpdate();

            sql = "INSERT INTO `cancellation_table` (`cancellation_id`, `routine_id`, `cancel_date`) VALUES (NULL, ?, ?)";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, routineID);
            preparedStatement.setString(2, cancelDate);

            int executeUpdateCancel = preparedStatement.executeUpdate();

            if (executeUpdateReschedule > 0 && executeUpdateCancel > 0) {
                jsonObject.addProperty("rescheduleStatus", "successful");

                sql = "SELECT course_name,course_code, time_name, teacher_name, room_no,session_name,"
                        + " day_name from course_table,teacher_table,room_table, time_table, session_table,"
                        + " day_table,routine_table where routine_table.course_id=course_table.course_id"
                        + " and routine_table.time_id=time_table.time_id and routine_table.teacher_id="
                        + "teacher_table.teacher_id and routine_table.room_id=room_table.room_id and"
                        + " routine_table.session_id=session_table.session_id and routine_table.day_id=day_table.day_id and routine_table.routine_id=?";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, routineID);

                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    courseName = resultSet.getString("course_name");
                    courseNo = resultSet.getString("course_code");
                    teacherName = resultSet.getString("teacher_name");
                    timeName = resultSet.getString("time_name");
                    roomNo = resultSet.getString("room_no");
                    dayName = resultSet.getString("day_name");
                    sessionName = resultSet.getString("session_name");

                    System.out.println(courseName + " " + courseNo + " " + teacherName + " " + timeName + " " + roomNo + " " + cancelDate + " " + dayName + " " + sessionName);

                }

                sql = "SELECT time_name,room_no FROM `time_table`,room_table WHERE time_id=? and room_id=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, timeID);
                preparedStatement.setInt(2, roomID);

                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {

                    newTime = resultSet.getString("time_name");
                    newRoom = resultSet.getString("room_no");
                    Date date = new SimpleDateFormat("yyyy-M-d").parse(rescheduleDate);
                    dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);

                    System.out.print(newTime + " " + newRoom + " " + rescheduleDate + " " + dayOfWeek);
                    
                    String message=courseName + "(" + courseNo + ")" + " .by" + teacherName + " from time " + timeName + "on Room no." + roomNo + " of " + cancelDate + "(" + dayName
                            + ") is canceled! and rescheduled to time "+timeName+" room no "+roomNo+" on "+rescheduleDate+" ("+dayOfWeek+")";
                    
                                        Notification notification = new Notification();
                    notification.sendNotification(message, sessionName);
                    
                }

            } else {
                jsonObject.addProperty("rescheduleStatus", "unsuccessful");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;

    }

}
