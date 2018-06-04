package com.example.user.firebasenotificationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email,password;
    Button submit,register;
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar)findViewById(R.id.loginProgressBar);
        email = (EditText)findViewById(R.id.email);
        password= (EditText)findViewById(R.id.password);
        submit = (Button)findViewById(R.id.submit);
        register = (Button)findViewById(R.id.register);

        submit.setOnClickListener(this);
        register.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                login();
                break;

            case R.id.register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
        }

    }

    private void login() {


        String EmailTxt = email.getText().toString();
        String PassTxt = password.getText().toString();


        if (!TextUtils.isEmpty(EmailTxt) && !TextUtils.isEmpty(PassTxt)) {
            progressBar.setVisibility(View.VISIBLE);

            auth.signInWithEmailAndPassword(EmailTxt,PassTxt).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        String token_id = FirebaseInstanceId.getInstance().getToken();
                        String user_id = auth.getCurrentUser().getUid();

                        DatabaseReference user_data = databaseReference.child(user_id);
                        user_data.child("token_id").setValue(token_id).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressBar.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this,"Failure",Toast.LENGTH_SHORT).show();
                            }
                        });

                        }else{
                        Toast.makeText(LoginActivity.this,"Login Error",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });

        }
    }

}
