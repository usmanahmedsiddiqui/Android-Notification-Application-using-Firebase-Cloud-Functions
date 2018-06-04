package com.example.user.firebasenotificationapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.firebasenotificationapp.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends android.support.v4.app.Fragment{
private List<Users> usersList;
RecyclerView recyclerView;
UserRecyclerViewAdapter userRecyclerViewAdapter;
DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    public UsersFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycleView);
        usersList = new ArrayList<>();

        userRecyclerViewAdapter = new UserRecyclerViewAdapter(usersList,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        recyclerView.setAdapter(userRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
       usersList.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot UserList : dataSnapshot.getChildren()){

                    String user_id = UserList.getKey();
                    Users users = UserList.getValue(Users.class).WithId(user_id);
                    usersList.add(users);
                    userRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
