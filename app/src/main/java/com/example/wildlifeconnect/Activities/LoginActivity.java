package com.example.wildlifeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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


            }

        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goto new Id page

            }
        });


    }


    private void initiateViews() {
        login=findViewById(R.id.login_login_btn);
        signup=findViewById(R.id.login_signup_btn);
        email=findViewById(R.id.login_email_edit);
        password=findViewById(R.id.login_password_edit);

    }

    private void login(String mail, String pass) {
        fbauth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user=fbauth.getCurrentUser();
                    updateUi(user);
                }else{
                    Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                    updateUi(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser=fbauth.getCurrentUser();
        updateUi(currUser);
    }

    private void updateUi(FirebaseUser currUser) {
        if(currUser==null){
            return;
        }
        //login
        saveEmailInPreference();
        password.setText("");
        final Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("uid",currUser.getUid());
       // startActivity(intent);
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        ref.child(fbauth.getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users obj=dataSnapshot.getValue(Users.class);
                int type=obj.getType();
                intent.putExtra("type",type);
                startActivity(intent);
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
