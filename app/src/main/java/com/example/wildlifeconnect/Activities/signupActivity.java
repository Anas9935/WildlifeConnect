package com.example.wildlifeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wildlifeconnect.Objects.Users;
import com.example.wildlifeconnect.R;
import com.example.wildlifeconnect.UtilityClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signupActivity extends AppCompatActivity {
    String uid;
    EditText orgName,regNumber,userName,presUId,email,password,confPass;
    RadioGroup desigRadioGrp;
    RadioButton presBtn,leadBtn;
    Button register;
    ImageView currLoc;
    float lon=0.0f,lat=0.0f;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeViews();
        mAuth=FirebaseAuth.getInstance();
        setupRadioBtn();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        currLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(signupActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET},
                        1);


            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    String loc= UtilityClass.getCurrentLoc(signupActivity.this);
                    if(loc!=null){
                        String[] locs=loc.split(" ");
                        lon=Float.parseFloat(locs[0]);
                        lat=Float.parseFloat(locs[1]);

                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(signupActivity.this, "Permission denied to get Location", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void findPosn() {
    }

    private void getCurrLoc() {
    }

    private void save() {
       
        String presuid=presUId.getText().toString();
       
        String pass=password.getText().toString();
        String confPa=confPass.getText().toString();

        if(pass.equals(confPa)){
            if(leadBtn.isChecked()){
                checkUid(presuid);
            }
            else{
                saveDataInDatabase();
            }
        }else{
            Toast.makeText(signupActivity.this,"PasswordMismatch",Toast.LENGTH_SHORT).show();
        }

    }

    private void saveDataInDatabase() {

        String mail=email.getText().toString();
        String pass=password.getText().toString();


        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signup Activity", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signupActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(signupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });



        }
    private void updateUI(FirebaseUser user) {
        if(user==null){
            Log.e("tag", "updateUI: "+"some error" );
            return;
        }
        String org=orgName.getText().toString();
        String regNo=regNumber.getText().toString();
        String n=userName.getText().toString();
        String mail=email.getText().toString();
        String presuid=presUId.getText().toString();
        uid = user.getUid();
        Users newuser = new Users(uid, 0, n, org, regNo, mail, uid, lat, lon);

        if(leadBtn.isChecked()){
            newuser.setIsActive(0);
            newuser.setType(1);
            newuser.setPresUid(presuid);
        }else {
            newuser.setIsActive(1);
        }
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            //String key=ref.push().getKey();


            ref.child(uid).setValue(newuser);

        if(leadBtn.isChecked()){

            Intent intent = new Intent(signupActivity.this, RequestedActivity.class);
            startActivity(intent);
            FirebaseAuth.getInstance().signOut();
            finish();
        }else {

            Intent intent = new Intent(signupActivity.this, MainActivity.class);
            intent.putExtra("type", 0);
            startActivity(intent);
            finish();
        }




    }
    private void checkUid(final String presuid) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child(presuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user=dataSnapshot.getValue(Users.class);
                if(user!=null){
                    saveDataInDatabase();
                }else{
                    Toast.makeText(signupActivity.this, "No president With Such id, Make Sure the caps are correct"
                            , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveDataInRequestDatabase(String presuid) {

        String mail=email.getText().toString();
        String pass=password.getText().toString();


        String org=orgName.getText().toString();
        String regNo=regNumber.getText().toString();
        String n=userName.getText().toString();




    }


    private void setupRadioBtn() {
        presBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    presBtn.setChecked(true);
                    leadBtn.setChecked(false);
                    presUId.setVisibility(View.GONE);
                } else {
                    presBtn.setChecked(false);
                    leadBtn.setChecked(true);
                    presUId.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void initializeViews(){
        orgName=findViewById(R.id.ngo_name);
        regNumber=findViewById(R.id.ngo_id);
        userName=findViewById(R.id.user_name);
        presUId=findViewById(R.id.president_id);
        email=findViewById(R.id.user_email);
        password=findViewById(R.id.first_password);
        confPass=findViewById(R.id.confirm_password);
        desigRadioGrp=findViewById(R.id.desigRadioGrp);
        presBtn=findViewById(R.id.presRadioBtn);
        leadBtn=findViewById(R.id.LeaderRadioBtn);

        register=findViewById(R.id.signup_log_in);
        currLoc=findViewById(R.id.current_location);



    }


}
