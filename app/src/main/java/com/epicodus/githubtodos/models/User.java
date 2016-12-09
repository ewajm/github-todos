package com.epicodus.githubtodos.models;

/**
 * Created by Ewa on 12/2/2016.
 */
public class User {
    private static String username;

    public static String getUsername(){
        return username;
    }

    public static void setUsername(String name){
        User.username = name;
    }
}
