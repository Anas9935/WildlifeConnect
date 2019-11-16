package com.example.wildlifeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wildlifeconnect.Objects.Users;
import com.example.wildlifeconnect.R;
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

    EditText orgName,regNumber,userName,presUId,email,password,confPass;
    RadioGroup desigRadioGrp;
    RadioButton presBtn,leadBtn;
    Button register,currLoc,findLoc;
    float lon=0.0f,lat=0.0f;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeViews();
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
                getCurrLoc();
            }
        });

        findLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPosn();
            }
        });
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
            if(leadBtn.isEnabled()){
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
        String confPa=confPass.getText().toString();


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
        String org=orgName.getText().toString();
        String regNo=regNumber.getText().toString();
        String n=userName.getText().toString();
        String mail=email.getText().toString();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        String key=ref.push().getKey();
        if(key!=null){
            Users  newuser=new Users(key,0,n,org,regNo,mail,key,lat,lon);
            ref.child(key).setValue(newuser);
            Intent intent=new Intent(signupActivity.this,MainActivity.class);
            intent.putExtra("type",0);
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
                    saveDataInRequestDatabase(presuid);
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

        String pas=password.getText().toString();
        String org=orgName.getText().toString();
        String regNo=regNumber.getText().toString();
        String n=userName.getText().toString();
        String mail=email.getText().toString();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("requestUsers");
        String key=ref.push().getKey();
        if(key!=null){
            Users  newuser=new Users(key,1,n,org,regNo,mail,presuid,lat,lon);
            newuser.setPassword(pas);
            ref.child(key).setValue(newuser);

            Intent intent=new Intent(signupActivity.this,RequestedActivity.class);
            startActivity(intent);
            finish();


        }

    }


    private void setupRadioBtn() {
        presBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    presBtn.setEnabled(true);
                    leadBtn.setEnabled(false);
                    presUId.setVisibility(View.GONE);
                } else {
                    presBtn.setEnabled(false);
                    leadBtn.setEnabled(true);
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



    }
}
