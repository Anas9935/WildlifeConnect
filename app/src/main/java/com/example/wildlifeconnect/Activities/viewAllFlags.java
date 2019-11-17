package com.example.wildlifeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.wildlifeconnect.Adapters.AllFlagAdapter;
import com.example.wildlifeconnect.Objects.FlagObject;
import com.example.wildlifeconnect.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class viewAllFlags extends AppCompatActivity {
ListView lView;
ArrayList<FlagObject> list;
ArrayAdapter<FlagObject> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_flags);
        lView=findViewById(R.id.all_flags_lv);

        list=new ArrayList<>();
        adapter=new AllFlagAdapter(this,list);
        lView.setAdapter(adapter);

        populateList();

    }

    private void populateList() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Flags");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FlagObject obj=dataSnapshot.getValue(FlagObject.class);
                if(obj!=null){
                    list.add(obj);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
