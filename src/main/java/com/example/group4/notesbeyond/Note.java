package com.example.group4.notesbeyond;

import android.net.Uri;

import java.io.Serializable;

public class Note implements Serializable{

    private String data;
    private Uri bitmap;
    private String Name;

    public Note(String name){
        Name = name;
        data = "";
    }

    public String getData() {
        return data;
    }

    public String getName(){
        return Name;
    }

    public Uri getBitmap(){
        return bitmap;
    }

    public void editData(String info) {
        data = info;
    }

    public void editData(Uri bmap){
        bitmap = bmap;
    }
}
