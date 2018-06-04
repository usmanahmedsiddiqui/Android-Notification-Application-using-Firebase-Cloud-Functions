package com.example.user.firebasenotificationapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.firebasenotificationapp.model.Notification;
import com.example.user.firebasenotificationapp.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends android.support.v4.app.Fragment{

    private List<Notification> notificationList;
    RecyclerView recyclerView;
    NotificationRecyclerViewAdapter notificationRecyclerViewAdapter;

    FirebaseAuth auth;
    String User_id;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    DatabaseReference user_notification;
    public NotificationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        auth = FirebaseAuth.getInstance();
        User_id = auth.getCurrentUser().getUid();

        user_notification = databaseReference.child(User_id).child("Notifications");

        recyclerView = (RecyclerView)view.findViewById(R.id.recycleView);
        notificationList = new ArrayList<>();

        notificationRecyclerViewAdapter = new NotificationRecyclerViewAdapter(notificationList,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        recyclerView.setAdapter(notificationRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        notificationList.clear();

        user_notification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot NotificationList : dataSnapshot.getChildren()){


                    Notification notification = NotificationList.getValue(Notification.class);
                    notificationList.add(notification);
                    notificationRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
