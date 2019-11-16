package com.example.wildlifeconnect.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wildlifeconnect.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
Button raise,search,view,update,request;
int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        type=getIntent().getIntExtra("type",-1);

        initialiseViews();

        setTheButtons();

        raise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here a new activity will be open
                Intent intent =new Intent(MainActivity.this,FlagFormActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goto search page
                Intent intent =new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //view all red flags worldwide
                Intent intent =new Intent(MainActivity.this,viewAllFlags.class);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update the current Flag
                Intent intent =new Intent(MainActivity.this,UpdateFlagActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for a president to see the requests for new id and new flag requests
                Intent intent =new Intent(MainActivity.this,requestsActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });


    }

    private void setTheButtons() {
        if(type==0){
            //president
            request.setVisibility(View.VISIBLE);
            raise.setText("Raise A Flag");
        }
        else if(type==1){
            //subLeader
            request.setVisibility(View.GONE);
            raise.setText("Request A Flag");
        }
        else{
            //error
        }
    }

    private void initialiseViews() {
        raise=findViewById(R.id.);
        search=findViewById(R.id.);
        view=findViewById(R.id.);
        update=findViewById(R.id.);
        request=findViewById(R.id.);
    }
}
