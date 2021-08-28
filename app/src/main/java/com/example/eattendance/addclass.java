package com.example.eattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class addclass extends AppCompatActivity {
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Classitem> classitem = new ArrayList<>();
    database database;  //database declaration


    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addclass);


        database = new database(this);//database line

        fab  = findViewById(R.id.addclass);
        fab.setOnClickListener(v ->showdialog());


        loadData();


        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        classAdapter = new ClassAdapter(this,classitem);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(position -> gotoItemActivity(position));

        // swipe action
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(simpleCallback2);
        itemTouchHelper2.attachToRecyclerView(recyclerView);



    }

    private void loadData() {
        Cursor cursor = database.getClassTable();
        classitem.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(database.C_ID));
            String className = cursor.getString(cursor.getColumnIndex(database.CLASS_NAME_KEY));
            String subjectName = cursor.getString(cursor.getColumnIndex(database.SUBJECT_NAME_KEY));

            classitem.add(new Classitem(id,className,subjectName));
        }
    }


    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this,StudentActivity.class);
        intent.putExtra("ClassName",classitem.get(position).getClassname());
        intent.putExtra("SubjectName",classitem.get(position).getSubjectname());
        intent.putExtra("position",position);
        intent.putExtra("cid", classitem.get(position).getCid());
        startActivity(intent);
    }

    private void showdialog() {
     myDialog dialog = new myDialog();
     dialog.show(getSupportFragmentManager(),myDialog.CLASS_ADD_DIALOG);
     dialog.setListener((classname,subjectname)->addclass(classname,subjectname));
    }


    private void addclass(String classname, String subjectname) {
        long cid = database.addclass(classname, subjectname);
        classitem.add(new Classitem(cid, classname, subjectname));
        classAdapter.notifyDataSetChanged();

    }

    private void showupdatedialog(int position) {
        myDialog dialog = new myDialog();
        dialog.show(getSupportFragmentManager(),myDialog.CLASS_UPDATE_DIALOG);
        dialog.setListener((classname,subjectname)->updateclass(position,classname,subjectname));
    }

    public void updateclass(int position, String classname, String subjectname) {
        database.updateclass(classitem.get(position).getCid(),classname,subjectname);
        classitem.get(position).setClassname(classname);
        classitem.get(position).setSubjectname(subjectname);
        classAdapter.notifyItemChanged(position);
    }



    // SWIPE PROGRAM
    ItemTouchHelper.SimpleCallback simpleCallback  = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder builder  = new AlertDialog.Builder(addclass.this);
            builder.setTitle("Delete Class");
            builder.setMessage("Are You Sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                     int position = viewHolder.getAdapterPosition();
                    database.deleteclass(classitem.get(position).getCid());
                    classitem.remove(position);
                    classAdapter.notifyItemRemoved(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    classAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
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
            AlertDialog.Builder builder  = new AlertDialog.Builder(addclass.this);
            builder.setTitle("Update Class");
            builder.setMessage("Are You Sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int position = viewHolder.getAdapterPosition();
                    showupdatedialog(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    classAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            builder.show();
        }



    };


}