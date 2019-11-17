package com.example.wildlifeconnect;


import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.wildlifeconnect.Activities.viewAllFlags;
import com.example.wildlifeconnect.Objects.FlagObject;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewFlagService extends IntentService {
Context context;
    private String CHANNEL_NAME="Notification";
    private String CHANNEL_ID="NEWS";
    private String CHANNEL_DESCRIPTION="This is a Test Notification";

    public NewFlagService() {
        super("Flag Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        createChannel();
        Log.e("SERVICe", "onCreate: "+"SErvice created" );
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        createNotification();

    }

    private void createNotification() {
        Log.e("Service", "createNotification: "+"notificationCreated" );
        checkNewFlags();

    }

    private void checkNewFlags() {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Flags");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FlagObject obj=dataSnapshot.getValue(FlagObject.class);
                if(obj!=null){
                    sendNotif(obj);

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

    private void createChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel nc=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            nc.setDescription(CHANNEL_DESCRIPTION);
            nc.setShowBadge(true);
            NotificationManager nm=getSystemService(NotificationManager.class);
            assert nm != null;
            nm.createNotificationChannel(nc);
        }
    }

    private void sendNotif(FlagObject obj) {
        Intent intent=new Intent(context, viewAllFlags.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent=PendingIntent.getActivity(this,0,intent,0);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.flag))
                .setSmallIcon(R.drawable.flag)//ic_stat_onesignal_default )
                .setContentText("flag is raised somewhere")
                .setContentTitle("Flag Notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pIntent)
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(true);

        NotificationManagerCompat nmc= NotificationManagerCompat.from(this);
        nmc.notify(101,builder.build());
    }
}
