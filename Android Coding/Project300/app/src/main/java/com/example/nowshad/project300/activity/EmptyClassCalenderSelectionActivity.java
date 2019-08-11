package com.example.nowshad.project300.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nowshad.project300.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_SINGLE;

public class EmptyClassCalenderSelectionActivity extends AppCompatActivity {

    private MaterialCalendarView materialCalendarViewEmptyClass;
    private Button buttonGoEmptyClass;
    private String currentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_class_calender_selection);


        materialCalendarViewEmptyClass=(MaterialCalendarView)findViewById(R.id.calendarViewEmptyClass);
        buttonGoEmptyClass=(Button)findViewById(R.id.buttonGoEmptyClass);


        Date date=new Date();
        materialCalendarViewEmptyClass.setSelectionMode(SELECTION_MODE_SINGLE);
        materialCalendarViewEmptyClass.setSelectedDate(date);

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        buttonGoEmptyClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 currentDate=simpleDateFormat.format(materialCalendarViewEmptyClass.getSelectedDate().getDate());


                Intent intent=new Intent(getApplicationContext(),DailyEmptyClassActivity.class);
                intent.putExtra("currentDate",currentDate);
                EmptyClassCalenderSelectionActivity.this.startActivity(intent);
            }
        });

    }
}
