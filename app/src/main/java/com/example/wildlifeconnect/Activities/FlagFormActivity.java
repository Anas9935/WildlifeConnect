package com.example.wildlifeconnect.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wildlifeconnect.Objects.FlagObject;
import com.example.wildlifeconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FlagFormActivity extends AppCompatActivity {
int type,flagCondn;
String uid;
ArrayList<String> tagList;
ArrayAdapter<String> adapter;
EditText tagEdit,titleEdit;
Button addTag,location1,location2,cal,saveBtn;
TextView locText,calText;
ListView tagListtView;
float lat,lon;
long timeExpStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_form);

        type=getIntent().getIntExtra("type",-1);
        initializeViews();

        uid= FirebaseAuth.getInstance().getUid();

        setupPage();

        tagList=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tagList);
        tagListtView.setAdapter(adapter);

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tags=tagEdit.getText().toString();
                    tagList.add(tags);
                    adapter.notifyDataSetChanged();
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void save() {
        String sub=titleEdit.getText().toString();
        if(type==0){
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Flags");
            String key=ref.push().getKey();
            if(key!=null){
                long time=System.currentTimeMillis();
                FlagObject obj=new FlagObject(key,1,uid,sub,tagList,1,lat,lon,time,timeExpStamp);
                ref.child(key).setValue(obj);

            }
        }
        else{
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("reqFlags");
            String key=ref.push().getKey();
            if(key!=null){
                long time=System.currentTimeMillis();
                FlagObject obj=new FlagObject(key,1,uid,sub,tagList,0,lat,lon,time,timeExpStamp);
                ref.child(key).setValue(obj);

            }
        }

        finish();
    }

    private void setupPage() {
        if(type==0){
            saveBtn.setText("Raise");
        }else if(type==1){
            saveBtn.setText("Request");
        }else{
            //error
        }
    }

    private void initializeViews() {
        tagEdit=findViewById(R.id);
        titleEdit=findViewById(R.id);
        addTag=findViewById(R.id);
        location1=findViewById(R.id);
        location2=findViewById(R.id);
        cal=findViewById(R.id);
        saveBtn=findViewById(R.id);
        locText=findViewById(R.id);
        calText=findViewById(R.id);
        tagListtView=findViewById(R.id);

    }
}
