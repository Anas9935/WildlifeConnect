package com.example.wildlifeconnect.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wildlifeconnect.Activities.signupActivity;
import com.example.wildlifeconnect.Objects.FlagObject;
import com.example.wildlifeconnect.Objects.RequestObject;
import com.example.wildlifeconnect.Objects.Users;
import com.example.wildlifeconnect.R;
import com.example.wildlifeconnect.UserInterface;
import com.example.wildlifeconnect.UtilityClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RequestAdapter extends ArrayAdapter<RequestObject> {
    ArrayList<RequestObject> list;
    Context context;

    public RequestAdapter(@NonNull Context context,  ArrayList<RequestObject> list) {
        super(context, 0,list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        RequestObject current=list.get(position);
        if(current.getChoice()==0){
            //flag
            view=LayoutInflater.from(context).inflate(R.layout.flag_req_todo,parent,false);
            final TextView title,leader,org,tags,time,loc;
            Button accept,reject;
            accept=view.findViewById(R.id.flag_req_accept_btn);
            reject=view.findViewById(R.id.flag_req_reject_btn);
            title=view.findViewById(R.id.flag_req_title);
            leader=view.findViewById(R.id.flag_req_raiserName);
            org=view.findViewById(R.id.flag_req_raiser_loc);
            tags=view.findViewById(R.id.flag_req_tags_Connected);
            time=view.findViewById(R.id.flag_req_expected_finish_date);
            loc=view.findViewById(R.id.flag_req_raisedLoc);

            final FlagObject curr=current.getFlag();

            title.setText(curr.getSubject());
             UtilityClass.getName(curr.getRaiserId(), new UserInterface() {
                 @Override
                 public void onUserReceived(Users user) {
                     leader.setText(user.getUserName());
                     org.setText(user.getOgrName());

                 }
             });
             StringBuilder builder=new StringBuilder();
             for(String i:curr.getTags()){
                 builder.append("•").append(i).append("\n");
             }
             tags.setText(builder.toString());
             time.setText(UtilityClass.getdate(curr.getFinalDay()));
             loc.setText(UtilityClass.getLocn(curr.getfLat(),curr.getfLon()));

             accept.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     curr.setApproval(1);
                     DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Flags");
                     ref.child(curr.getFid()).setValue(curr);
                     removeFromReq(curr.getFid());
                     list.remove(position);
                     notifyDataSetChanged();
                 }
             });
             reject.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     curr.setApproval(2);
                     DatabaseReference ref= FirebaseDatabase.getInstance().getReference("RejectedFlags");
                     ref.child(curr.getFid()).setValue(curr);
                     removeFromReq(curr.getFid());
                     list.remove(position);
                     notifyDataSetChanged();

                 }
             });




        }else{
            //user
            view=LayoutInflater.from(context).inflate(R.layout.leader_req_todo,parent,false);
            final TextView uName,orgName,orgLoc,mail;
            Button accept,reject;
            accept=view.findViewById(R.id.leader_req_acceptBtn);
            reject=view.findViewById(R.id.leader_req_rejectBtn);
            uName=view.findViewById(R.id.leader_req_uName);
            orgLoc=view.findViewById(R.id.leader_req_location);
            orgName=view.findViewById(R.id.leader_req_OrgName);
            mail=view.findViewById(R.id.leader_req_mail);

            final Users curr=current.getUser();

            uName.setText(curr.getUserName());
            orgName.setText(curr.getOgrName());
            orgLoc.setText(UtilityClass.getLocn(curr.getLat(),curr.getLon()));
            mail.setText(curr.getEmail());
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(curr.getUid()).child("isActive").setValue(1);
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(curr.getUid()).child("isActive").setValue(2);
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        return view;

    }

    private void removeFromReq(String fid) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("reqFlags");
        ref.child(fid).removeValue();
    }
}
