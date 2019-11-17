package com.example.wildlifeconnect.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wildlifeconnect.Objects.FlagObject;
import com.example.wildlifeconnect.Objects.Users;
import com.example.wildlifeconnect.R;
import com.example.wildlifeconnect.UserInterface;
import com.example.wildlifeconnect.UtilityClass;

import java.util.ArrayList;

public class AllFlagAdapter extends ArrayAdapter<FlagObject> {
    ArrayList<FlagObject> list;
    Context context;
    public AllFlagAdapter(Context context,ArrayList<FlagObject> lst){
        super(context,0,lst);
        list=lst;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.all_flag_todo,parent,false);
        }
        FlagObject current=list.get(position);
        final TextView title,org,tags,mail,loc,rOn;
        title=view.findViewById(R.id.all_flag_todo_tag);
        org=view.findViewById(R.id.all_flag_todo_ngoName);
        tags=view.findViewById(R.id.all_flag_todo_tagsList);
        loc=view.findViewById(R.id.all_flag_todo_locationName);
        mail=view.findViewById(R.id.all_flag_todo_mailAddr);
        rOn=view.findViewById(R.id.all_flag_todo_raiseDate);

        title.setText(current.getSubject());
        UtilityClass.getName(current.getRaiserId(), new UserInterface() {
            @Override
            public void onUserReceived(Users user) {
                if(user!=null){
                    org.setText(user.getOgrName());
                    mail.setText("("+user.getEmail()+")");
                }
            }
        });
        StringBuilder builder=new StringBuilder();
        for(String i:current.getTags()){
            builder.append("•").append(i).append("\n");
        }
        tags.setText(builder.toString());
        loc.setText(UtilityClass.getLocn(current.getfLat(),current.getfLon()));
        rOn.setText(UtilityClass.getdate(current.getRaiseTime()));

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",mail.getText().toString(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding the Flag Of"+title.getText().toString());

                context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
