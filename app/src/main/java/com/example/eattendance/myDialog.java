package com.example.eattendance;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class myDialog extends DialogFragment {
    public static final String CLASS_ADD_DIALOG="addclass";
    public static final String CLASS_UPDATE_DIALOG="updateclass";
    public static final String STUDENT_ADD_DIALOG="addstudent";
    public static final String STUDENT_UPDATE_DIALOG="updatestudent";

    private OnclickListener listener;
    private int roll;
    private String getname;

    public myDialog(int roll, String name) {

        this.roll = roll;
        this.getname = name;
    }

    public myDialog() {

    }

    public interface OnclickListener{
        void onClick(String text1,String text2);
    }

    public void setListener(OnclickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog=null;
        if(getTag().equals(CLASS_ADD_DIALOG))dialog=getAddClassDialog();
        if(getTag().equals(CLASS_UPDATE_DIALOG))dialog=getUpdateClassDialog();
        if(getTag().equals(STUDENT_ADD_DIALOG))dialog=getAddStudentDialog();
        if(getTag().equals(STUDENT_UPDATE_DIALOG))dialog=getUpdateStudentDialog();
        return dialog;
    }

    private Dialog getUpdateStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);

        TextView tittle = view.findViewById(R.id.tittleDialog);
        tittle.setText(" UPDATE STUDENT");

        EditText name_edt = view.findViewById(R.id.edt01);
       EditText roll_edt = view.findViewById(R.id.edt02);
        name_edt.setHint("Student Name");
        roll_edt.setHint("University Number");

        Button cancel = view.findViewById(R.id.cancel);
        Button add = view.findViewById(R.id.save);
        add.setText("UPADTE");
        name_edt.setText(getname);
        roll_edt.setText(roll+"");
        roll_edt.setEnabled(false);
        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String name = name_edt.getText().toString();
            String roll = roll_edt.getText().toString();
            listener.onClick(name,roll);
            dismiss();

        });
        return builder.create();
    }


    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);

        TextView tittle = view.findViewById(R.id.tittleDialog);
        tittle.setText("ADD NEW CLASS");

        EditText class_edt = view.findViewById(R.id.edt01);
        EditText subject_edt = view.findViewById(R.id.edt02);
        class_edt.setHint("Class Name");
        subject_edt.setHint("Subject Name");

        Button cancel = view.findViewById(R.id.cancel);
        Button add = view.findViewById(R.id.save);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String classname = class_edt.getText().toString();
            String subjectname = subject_edt.getText().toString();
           listener.onClick(classname,subjectname);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getUpdateClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);

        TextView tittle = view.findViewById(R.id.tittleDialog);
        tittle.setText("UPDATE CLASS");

        EditText class_edt = view.findViewById(R.id.edt01);
        EditText subject_edt = view.findViewById(R.id.edt02);
        class_edt.setHint("Class Name");
        subject_edt.setHint("Subject Name");

        Button cancel = view.findViewById(R.id.cancel);
        Button update = view.findViewById(R.id.save);
        update.setText("UPADTE");

        cancel.setOnClickListener(v -> dismiss());
        update.setOnClickListener(v -> {
            String classname = class_edt.getText().toString();
            String subjectname = subject_edt.getText().toString();
            listener.onClick(classname,subjectname);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);

        TextView tittle = view.findViewById(R.id.tittleDialog);
        tittle.setText("ADD NEW STUDENT");

        EditText name_edt = view.findViewById(R.id.edt01);
        EditText roll_edt = view.findViewById(R.id.edt02);
        name_edt.setHint("Student Name");
        roll_edt.setHint("University Number");

        Button cancel = view.findViewById(R.id.cancel);
        Button add = view.findViewById(R.id.save);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String name = name_edt.getText().toString();
            String roll = roll_edt.getText().toString();
            name_edt.setText("");
            roll_edt.setText("");

            listener.onClick(name,roll);

        });
        return builder.create();
    }

}
