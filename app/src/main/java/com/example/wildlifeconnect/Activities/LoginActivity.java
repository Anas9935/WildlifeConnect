package com.example.wildlifeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wildlifeconnect.Objects.Users;
import com.example.wildlifeconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView login,signup;
    ProgressBar pro;
    EditText email,password;


    FirebaseAuth fbauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initiateViews();
        fillEmailIfAvailable();

        fbauth= FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=email.getText().toString();
                String pass=password.getText().toString();

                login(mail,pass);
                pro.setVisibility(View.VISIBLE);

            }

        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goto new Id page
                Intent intent=new Intent(LoginActivity.this,signupActivity.class);
                startActivity(intent);

            }
        });


    }


    private void initiateViews() {
        login=findViewById(R.id.login_login_btn);
        signup=findViewById(R.id.login_signup_btn);
        email=findViewById(R.id.login_email_edit);
        password=findViewById(R.id.login_password_edit);
        pro=findViewById(R.id.loginProgBar);

    }

    private void login(final String mail, final String pass) {
        fbauth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user=fbauth.getCurrentUser();
                    updateUi(user);
                }else{
                   // Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this,RequestedActivity.class);
                    startActivity(intent);
                    updateUi(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                pro.setVisibility(View.GONE);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser=fbauth.getCurrentUser();
        pro.setVisibility(View.GONE);
        updateUi(currUser);
    }

    private void updateUi(FirebaseUser currUser) {
        if(currUser==null){
            pro.setVisibility(View.GONE);
            return;
        }
        //login
        saveEmailInPreference();
        password.setText("");


        pro.setVisibility(View.GONE);
        final Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("uid",currUser.getUid());
       // startActivity(intent);
        Log.e("Test", "updateUi: "+currUser.getUid() );
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        ref.child(currUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users obj=dataSnapshot.getValue(Users.class);
                if(obj!=null){
                    if(obj.getIsActive()==1){

                        int type = obj.getType();
                        intent.putExtra("type", type);
                        startActivity(intent);
                    }else if(obj.getIsActive()==0 ||obj.getIsActive()==2){
                        Intent in=new Intent(LoginActivity.this,RequestedActivity.class);
                        in.putExtra("status",obj.getIsActive());
                        in.putExtra("uid",obj.getUid());
                        startActivity(in);
                        pro.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Your Account Doesn't Exist", Toast.LENGTH_SHORT).show();
                    pro.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void saveEmailInPreference() {
        String e=email.getText().toString();
        SharedPreferences sp=getSharedPreferences("EMAIL", Context.MODE_PRIVATE);
        sp.edit().putString("EMAIL",e).apply();

    }

    private void fillEmailIfAvailable() {
        SharedPreferences sp=getSharedPreferences("EMAIL", Context.MODE_PRIVATE);
        String mail=sp.getString("EMAIL","");
        if(mail.length()>0){
            email.setText(mail);
        }
    }


}
