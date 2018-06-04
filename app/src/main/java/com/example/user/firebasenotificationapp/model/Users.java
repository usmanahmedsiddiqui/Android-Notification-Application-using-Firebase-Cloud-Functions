package com.example.user.firebasenotificationapp.model;

public class Users {

    String name="",user_image="";

    public String User_id;


    public Users(){

    }
    public Users(String name, String image) {
        this.name = name;
        this.user_image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }



    public Users WithId(String id){
        this.User_id = id;
        return this;
    }
}
