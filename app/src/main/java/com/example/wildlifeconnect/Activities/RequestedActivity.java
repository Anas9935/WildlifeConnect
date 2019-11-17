package com.example.wildlifeconnect.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wildlifeconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestedActivity extends AppCompatActivity {
Button backbtn;
String uid;
TextView tv;
int status=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested);

        status= getIntent().getIntExtra("status", 0);

        tv=findViewById(R.id.requestedTv);

        if(status==2){
            tv.setText("Your Id Is Rejected");
           uid=getIntent().getStringExtra("uid");
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
            ref.child(uid).removeValue();
        }

        backbtn=findViewById(R.id.req_back_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }


}
