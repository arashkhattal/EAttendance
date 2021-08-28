package com.example.eattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    StudentAdapter studentadapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Studentitem> studentitem = new ArrayList<>();
    private database database;
    private int cid;
    private MyCalendar calendar;
    Toolbar toolbar;
    private TextView subtittle;


    private String classname;
    private String subjectname;
    private int position;


    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        calendar = new MyCalendar();
        database = new database(this);
        fab  = findViewById(R.id.addstudent);
        fab.setOnClickListener(v -> showAddStudentdialog());


        Intent intent = getIntent();
        classname = intent.getStringExtra("ClassName");
        subjectname = intent.getStringExtra("SubjectName");
        position = intent.getIntExtra("position",-1);
        cid  =  intent.getIntExtra("cid",-1);

        setToolbar();
        loadData();

        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        studentadapter = new StudentAdapter(this,studentitem);
        recyclerView.setAdapter(studentadapter);
        studentadapter.setOnItemClickListener(position -> changestatus(position));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(simpleCallback2);
        itemTouchHelper2.attachToRecyclerView(recyclerView);

        loadStatusData();

    }

    private void loadData() {
        Cursor  cursor  = database.getStudentTable(cid);
        studentitem.clear();
        while (cursor.moveToNext()){
            long sid = cursor.getLong(cursor.getColumnIndex(database.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndex(database.STUDENT_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndex(database.STUDENT_NAME_KEY));
            studentitem.add(new Studentitem(sid,roll,name));
        }
        cursor.close();
    }
    private void loadStatusData(){
        for (Studentitem studentitem : studentitem){
            String status =  database.getStatus(studentitem.getSid(),calendar.getDate());
            if(status!= null)studentitem.setStatus(status);
            else studentitem.setStatus("");
        }
        studentadapter.notifyDataSetChanged();
    }

    private void changestatus(int position) {
        String status = studentitem.get(position).getStatus();
        if (status.equals("PRESENT"))status = "ABSENT";
        else status = "PRESENT";

        studentitem.get(position).getStatus(status);
        studentadapter.notifyItemChanged(position);
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.bottomnav);
        TextView tittle = toolbar.findViewById(R.id.title_toolbar);
        subtittle = toolbar.findViewById(R.id.subtittle_toolbar);
        ImageButton save  = toolbar.findViewById(R.id.save_attent);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStatus();
                Toast.makeText(getApplicationContext(),"Data Saved",Toast.LENGTH_SHORT).show();
            }
        });
        ImageButton cal  = toolbar.findViewById(R.id.changedate);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalender();
            }
        });

        tittle.setText(classname);
        subtittle.setText(subjectname+ " | " +calendar.getDate());
    }

    private void saveStatus() {
        for (Studentitem studentitem : studentitem){
            String status =  studentitem.getStatus();
            if (status!= "PRESENT") status="ABSENT";
            long value = database.addStatus(studentitem.getSid(),calendar.getDate(),status);

            if (value==-1)database.updateStatus(studentitem.getSid(),calendar.getDate(),status);
        }
    }

    private void showCalender(){
        calendar.show(getSupportFragmentManager(),"");
        calendar.setOnCalendarOkClickListener((this::onCalendarOkClicked));
    }

    private void onCalendarOkClicked(int year, int month, int day) {
        calendar.setDate(year,month,day);
        subtittle.setText(subjectname+" | "+calendar.getDate());
        loadStatusData();
    }

    private void showAddStudentdialog() {
        myDialog dialog = new myDialog();
        dialog.show(getSupportFragmentManager(),myDialog.STUDENT_ADD_DIALOG);
        dialog.setListener((name,roll)->addstudent(name,roll));
    }

    private void addstudent(String name, String roll_string) {
        int roll = Integer.parseInt(roll_string);
        long sid  = database.addStudent(cid,roll,name);
        Studentitem studentitems = new Studentitem(sid,roll,name);
        studentitem.add(studentitems);
        studentadapter.notifyDataSetChanged();
    }

    private void showupdateStudentdialog(int position) {
        myDialog dialog = new myDialog(studentitem.get(position).getRoll(),studentitem.get(position).getname());
        dialog.show(getSupportFragmentManager(),myDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListener((name,roll)->updateStudent(position,name,roll));
    }

    private void updateStudent(int position,String name,String roll) {
        database.updateStudent(studentitem.get(position).getSid(),name);
        studentitem.get(position).setname(name);
        studentadapter.notifyItemChanged(position);
    }


    // SWIPE PROGRAM
    ItemTouchHelper.SimpleCallback simpleCallback  = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder builder  = new AlertDialog.Builder(StudentActivity.this);
            builder.setTitle("Delete Student");
            builder.setMessage("Are You Sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int position = viewHolder.getAdapterPosition();
                    database.deleteStudent(studentitem.get(position).getSid());
                    studentitem.remove(position);
                    studentadapter.notifyItemRemoved(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    studentadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            builder.show();
        }
    };
    ItemTouchHelper.SimpleCallback simpleCallback2  = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder builder  = new AlertDialog.Builder(StudentActivity.this);
            builder.setTitle("Update Student");
//            builder.setMessage("Are You Sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int position = viewHolder.getAdapterPosition();
                    showupdateStudentdialog(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    studentadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            builder.show();
        }
    };


}