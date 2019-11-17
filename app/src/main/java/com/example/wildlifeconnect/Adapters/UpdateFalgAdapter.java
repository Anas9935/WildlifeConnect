package com.example.wildlifeconnect.Adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wildlifeconnect.Activities.FlagFormActivity;
import com.example.wildlifeconnect.Objects.FlagObject;
import com.example.wildlifeconnect.R;
import com.example.wildlifeconnect.UtilityClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class UpdateFalgAdapter extends ArrayAdapter<FlagObject> {

    ArrayList<FlagObject> list;
    Context context;
    public UpdateFalgAdapter(Context context,ArrayList<FlagObject> lst){
        super(context,0,lst);
        list=lst;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.flag_todo,parent,false);
        }

        final FlagObject current=list.get(position);
        final TextView title,tags,dt,locn,delBtn;
        ImageView curFlagIcon,editDateIcon;

        title=view.findViewById(R.id.flag_todo_tag);
        tags=view.findViewById(R.id.flag_todo_tagsList);
        dt=view.findViewById(R.id.flag_todo_date);
        locn=view.findViewById(R.id.flag_todo_locationName);
        delBtn=view.findViewById(R.id.flag_todo_delete_btn);
        curFlagIcon=view.findViewById(R.id.flag_todo_icon);
        editDateIcon=view.findViewById(R.id.flag_todo_editDateBtn);

        //TODO 1 set the red flag
        curFlagIcon.setImageResource(R.drawable.ic_flag_black_24dp);

        title.setText(current.getSubject());
        final StringBuilder builder=new StringBuilder();
        for(String i:current.getTags()){
            builder.append(" • ").append(i).append("\n");
        }
        tags.setText(builder.toString());
        dt.setText(UtilityClass.getdate(current.getFinalDay()));
        locn.setText(UtilityClass.getLocn(current.getfLat(),current.getfLon()));
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1=new AlertDialog.Builder(context);
                builder1.setTitle("What About The Mission");
                builder1.setPositiveButton("Success", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        current.setStatus(2);
                        saveFlagInArchive(current);
                        list.remove(position);
                        notifyDataSetChanged();

                        deleteFromFlags(current.getFid());
                    }
                }).setNegativeButton("Failure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        current.setStatus(0);
                        saveFlagInArchive(current);
                        list.remove(position);
                        notifyDataSetChanged();
                        deleteFromFlags(current.getFid());
                    }
                }).setNeutralButton("Cancel", null);

                builder1.create().show();

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
                long timeExpStamp=myCalendar.getTimeInMillis();
                current.setFinalDay(timeExpStamp);
                notifyDataSetChanged();
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Flags");
                ref.child(current.getFid()).child("finalDay").setValue(timeExpStamp);
                }

        };
        editDateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // editing the end time
                new DatePickerDialog(context,date , myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        return view;
    }

    private void deleteFromFlags(String fid) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Flags");
        ref.child(fid).removeValue();
    }

    public void saveFlagInArchive(FlagObject curr){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Archives");
        ref.child(curr.getFid()).setValue(curr);
    }

    @Nullable
    @Override
    public FlagObject getItem(int position) {
        return list.get(position);
    }
}
