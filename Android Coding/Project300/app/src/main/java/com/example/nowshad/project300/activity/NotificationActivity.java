package com.example.nowshad.project300.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nowshad.project300.R;

public class NotificationActivity extends AppCompatActivity {

    private String notificationDetails;

    TextView showNotification;
    Button goHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        final Intent intent=getIntent();
         notificationDetails=intent.getStringExtra("notificationDetails");


         showNotification=(TextView)findViewById(R.id.notificationDetails);
         showNotification.setText(notificationDetails);

         goHome=(Button)findViewById(R.id.goHome);
         goHome.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(NotificationActivity.this,NavigationDrawerActivity.class);
                 NotificationActivity.this.startActivity(intent);
             }
         });

    }
}
