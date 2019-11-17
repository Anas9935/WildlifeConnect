package com.example.wildlifeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SearchActivity extends AppCompatActivity {
EditText searchEV;
ListView lView;
ImageView icon;
ArrayList<FlagObject> list;
ArrayAdapter<FlagObject> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initializeViews();

        list=new ArrayList<>();
        adapter=new AllFlagAdapter(this,list);
        lView.setAdapter(adapter);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=searchEV.getText().toString();
                if(text!=null){
                    list.clear();
                    search(text);
                    searchFromArchives(text);
                }
            }
        });

    }

    private void searchFromArchives(final String text) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Archives");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FlagObject obj=dataSnapshot.getValue(FlagObject.class);
                if(obj!=null){
                    if(obj.getSubject().equals(text)){
                        list.add(obj);
                        adapter.notifyDataSetChanged();
                    }else{
                        ArrayList<String> lst=obj.getTags();
                        for(String i:lst){
                            if(i.equals(text)){
                                list.add(obj);
                                adapter.notifyDataSetChanged();
                            }
                        }
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

    private void search(final String text) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Flags");
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    FlagObject obj=dataSnapshot.getValue(FlagObject.class);
                    if(obj!=null){
                        if(obj.getSubject().equals(text)){
                            list.add(obj);
                            adapter.notifyDataSetChanged();
                        }else{
                            ArrayList<String> lst=obj.getTags();
                            for(String i:lst){
                                if(i.equals(text)){
                                    list.add(obj);
                                    adapter.notifyDataSetChanged();
                                }
                            }
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

    private void initializeViews() {
        searchEV=findViewById(R.id.search_editView);
        lView=findViewById(R.id.search_lView);
        icon=findViewById(R.id.search_icon);
    }
}
