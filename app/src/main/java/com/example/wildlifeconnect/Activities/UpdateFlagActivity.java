package com.example.wildlifeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.wildlifeconnect.Adapters.UpdateFalgAdapter;
import com.example.wildlifeconnect.Objects.FlagObject;
import com.example.wildlifeconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UpdateFlagActivity extends AppCompatActivity {
    String uid;

ArrayList<FlagObject> list;
ArrayAdapter<FlagObject> adapter;
ListView lview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_flag);

        uid= FirebaseAuth.getInstance().getUid();

        lview=findViewById(R.id.updateFlagListView);

        list=new ArrayList<>();
        adapter=new UpdateFalgAdapter(this,list);
        lview.setAdapter(adapter);

        getList();

    }

    private void getList() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Flags");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FlagObject obj=dataSnapshot.getValue(FlagObject.class);
                if(obj!=null){
                    if(obj.getRaiserId().equals(uid)){
                        list.add(obj);
                        adapter.notifyDataSetChanged();
                    }
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
