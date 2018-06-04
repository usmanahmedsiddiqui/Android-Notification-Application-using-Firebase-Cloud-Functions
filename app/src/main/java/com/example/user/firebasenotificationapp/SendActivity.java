package com.example.user.firebasenotificationapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendActivity extends AppCompatActivity implements View.OnClickListener {
TextView user_name;
EditText message;
Button send;
private String UserName;
private String UserID;

ProgressBar progressBar;
FirebaseAuth auth;
DatabaseReference databaseReference;
String currentUserID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        user_name  = (TextView)findViewById(R.id.user_name);
        message = (EditText)findViewById(R.id.message);
        send = (Button)findViewById(R.id.sendButton);
        progressBar = (ProgressBar)findViewById(R.id.sendProgressBar);


        UserName = getIntent().getStringExtra("user_name");
        UserID = getIntent().getStringExtra("user_id");

        user_name.setText("Send to "+UserName);

        auth = FirebaseAuth.getInstance();
        currentUserID  = auth.getCurrentUser().getUid();


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        send.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendButton:

                SendNotification();
                break;

        }
    }

    private void SendNotification() {
        progressBar.setVisibility(View.VISIBLE);
        String messageText = message.getText().toString();
        if(!TextUtils.isEmpty(messageText)){

            DatabaseReference Notification = databaseReference.child(UserID).child("Notifications").push();

            Notification.child("message").setValue(messageText);
            Notification.child("from").setValue(currentUserID).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(SendActivity.this,"Notification Sent",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SendActivity.this,"Error in Sending Notification",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });


        }

    }
}
