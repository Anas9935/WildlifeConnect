package com.example.wildlifeconnect.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wildlifeconnect.Objects.FlagObject;
import com.example.wildlifeconnect.R;
import com.example.wildlifeconnect.UtilityClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FlagFormActivity extends AppCompatActivity {
int type,flagCondn;
String uid;
ArrayList<String> tagList;
ArrayAdapter<String> adapter;
EditText tagEdit,titleEdit;
Button location2,saveBtn;
TextView locText,calText;
ListView tagListtView;
float lat,lon;
long timeExpStamp;
ImageView addTag,location1,cal;

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
                    tagEdit.setText("");
            }
        });


        final Calendar myCalendar = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                timeExpStamp=myCalendar.getTimeInMillis();
                calText.setText(UtilityClass.getdate(timeExpStamp));
            }

        };

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FlagFormActivity.this,date , myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        tagEdit=findViewById(R.id.flag_form_tags);
        titleEdit=findViewById(R.id.flag_form_title);
        addTag=findViewById(R.id.flag_form_add_btn);
        location1=findViewById(R.id.flag_form_loc);
        location2=findViewById(R.id.flag_form_pickLoc);
        cal=findViewById(R.id.flag_form_cal);
        saveBtn=findViewById(R.id.flag_form_raiseBtn);
        locText=findViewById(R.id.flag_form_locText);
        calText=findViewById(R.id.flag_form_timeText);
        tagListtView=findViewById(R.id.flag_form_listView);

    }
}
