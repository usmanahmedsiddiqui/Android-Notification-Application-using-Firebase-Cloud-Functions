package com.example.user.firebasenotificationapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {
TextView NotifyData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        NotifyData = (TextView)findViewById(R.id.text);

        String message = getIntent().getStringExtra("message");
        String from  = getIntent().getStringExtra("from_user_id");

        NotifyData.setText("From: "+from+" Message: "+message);
    }
}
