package com.example.user.firebasenotificationapp;

import android.bluetooth.BluetoothAssignedNumbers;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.security.spec.PSSParameterSpec;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email, password, name;
    Button submit, login;
    CircleImageView image;
    public static final int pick_image = 1;
    private Uri uri;
    ProgressBar progressBar;


    FirebaseAuth auth;
    StorageReference StorageReference;
    DatabaseReference database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-notification-6a476.firebaseio.com/").child("Users");
        StorageReference = FirebaseStorage.getInstance().getReference().child("images");


        uri = null;
        progressBar = (ProgressBar) findViewById(R.id.registerProgressBar);
        email = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);
        login = (Button) findViewById(R.id.login);
        image = (CircleImageView) findViewById(R.id.image);
        submit.setOnClickListener(this);
        login.setOnClickListener(this);
        image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                Register();
                break;

            case R.id.login:
                finish();

                break;

            case R.id.image:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selected Picture"), pick_image);
                break;
        }
    }

    private void Register() {


        final String user_name = name.getText().toString();
        String EmailTxt = email.getText().toString();
        String PassTxt = password.getText().toString();

        if (uri != null) {

            progressBar.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(EmailTxt) && !TextUtils.isEmpty(PassTxt)) {
                auth.createUserWithEmailAndPassword(EmailTxt, PassTxt).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            final String user_id = auth.getCurrentUser().getUid();
                            StorageReference user_profile = StorageReference.child(user_id + ".jpg");

                            user_profile.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadtask) {

                                    uploadtask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                            String image_uri = downloadUrl.toString();
                                            String token_id =  FirebaseInstanceId.getInstance().getToken();
                                            DatabaseReference user_data = database.child(user_id);
                                            user_data.child("name").setValue(user_name);
                                            user_data.child("user_image").setValue(image_uri);
                                            user_data.child("token_id").setValue(token_id);;
                                            progressBar.setVisibility(View.INVISIBLE);
                                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                            finishAffinity();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Image not Uploaded", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });


                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Error", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                });
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pick_image) {
            uri = data.getData();
            image.setImageURI(uri);
        }


    }
}
