package com.example.wildlifeconnect;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.wildlifeconnect.Objects.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilityClass {

    public static String getdate(long timestamp){

        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
        return format.format(new Date(timestamp));
    }
    public static  String getLocn(float lat,float lon){
        return "New Delhi";
    }
    public static void getName(String uid, final UserInterface nif){
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference("Users");
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users name=dataSnapshot.getValue(Users.class);
                if(name!=null){
                    nif.onUserReceived(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
