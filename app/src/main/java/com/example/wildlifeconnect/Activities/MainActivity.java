package com.example.wildlifeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.wildlifeconnect.R;
import com.example.wildlifeconnect.myServiceReceiver;
import com.google.firebase.auth.FirebaseAuth;
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
        startServices();

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

    private void startServices() {
        Intent intent=new Intent(MainActivity.this, myServiceReceiver.class);
        PendingIntent pIntent=PendingIntent.getBroadcast(this,myServiceReceiver.REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),60000000L,pIntent);

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
        raise=findViewById(R.id.main_raise_flag);
        search=findViewById(R.id.main_searchFlag);
        view=findViewById(R.id.main_view_flag);
        update=findViewById(R.id.main_update_flag);
        request=findViewById(R.id.main_requests);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_sign_up,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_action_signout :{
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
