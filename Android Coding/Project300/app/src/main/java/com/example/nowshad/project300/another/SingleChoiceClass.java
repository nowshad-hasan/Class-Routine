package com.example.nowshad.project300.another;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.example.nowshad.project300.activity.DailyEmptyClassActivity;
import com.example.nowshad.project300.activity.EmptyClassCalenderSelectionActivity;
import com.example.nowshad.project300.activity.SingleClassActivity;
import com.example.nowshad.project300.activity.WeeklyEmptyClassActivity;

/**
 * Created by nowshad on 12/14/17.
 */

public class SingleChoiceClass extends DialogFragment {




//    public SingleChoiceClass(Context context)
//    {
//        this.context=context;
//    }



    final CharSequence[] items={"Select From Week","Select From Calender"};
private String selection;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

 final Context context;

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        builder.setTitle("Select One").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch(i)
                {
                    case 0:
                        selection=(String)items[i];
                        break;
                    case 1:
                        selection=(String)items[i];
                        break;
                    default:
                        selection=(String)items[i];
                        break;
                }

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"Your choice is "+selection,Toast.LENGTH_SHORT).show();
                if(selection.equals("Select From Calender"))
                {
                    Intent intent=new Intent(getContext(),EmptyClassCalenderSelectionActivity.class);
                    SingleChoiceClass.this.startActivity(intent);

                }
                else if(selection.equals("Select From Week"))
                {
                    Intent intent=new Intent(getContext(),WeeklyEmptyClassActivity.class);
                    SingleChoiceClass.this.startActivity(intent);
                }
            }
        });


        return builder.create();
    }
}
