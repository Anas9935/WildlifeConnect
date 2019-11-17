package com.example.wildlifeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.wildlifeconnect.Adapters.RequestAdapter;
import com.example.wildlifeconnect.Objects.FlagObject;
import com.example.wildlifeconnect.Objects.RequestObject;
import com.example.wildlifeconnect.Objects.Users;
import com.example.wildlifeconnect.R;
import com.example.wildlifeconnect.UserInterface;
import com.example.wildlifeconnect.UtilityClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class requestsActivity extends AppCompatActivity {
ArrayAdapter<RequestObject> adapter;
ArrayList<RequestObject> list;
ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        listView=findViewById(R.id.request_listView);
        list=new ArrayList<>();
        adapter=new RequestAdapter(this,list);
        listView.setAdapter(adapter);

        populateList();
        populateUsers();

    }

    private void populateUsers() {
        final String uid=FirebaseAuth.getInstance().getUid();
       /// Log.e("Tag", "populateUsers: "+uid );
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Users user=dataSnapshot.getValue(Users.class);
                if(user!=null){
                 ///   Log.e("t", "onChildAdded: "+user.getPresUid() );
                    if(user.getPresUid().equals(uid)){
                        if(user.getIsActive()==0){
                            list.add(new RequestObject(null,user,1));
                            adapter.notifyDataSetChanged();
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

    private void populateList() {
        final String uid= FirebaseAuth.getInstance().getUid();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("reqFlags");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final FlagObject fl=dataSnapshot.getValue(FlagObject.class);
                if(fl!=null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UtilityClass.getName(fl.getRaiserId(), new UserInterface() {
                                @Override
                                public void onUserReceived(Users user) {
                                    if(user.getPresUid().equals(uid)){
                                        list.add(new RequestObject(fl,null,0));
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }).start();

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
