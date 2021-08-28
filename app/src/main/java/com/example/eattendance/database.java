package com.example.eattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class database extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    // class table
    private static final String CLASS_TABLE_NAME = "CLASS_TABLE";
    public static final String C_ID = "_CID";
    public static final String CLASS_NAME_KEY = "CLASS_NAME";
    public static final String SUBJECT_NAME_KEY = "SUBJECT_NAME";

    private static final String CREATE_CLASS_TABLE =
                                "CREATE TABLE " + CLASS_TABLE_NAME + "( "+
                                C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                                CLASS_NAME_KEY+" TEXT NOT NULL, "+
                                SUBJECT_NAME_KEY+" TEXT NOT NULL, "+
                                "UNIQUE (" + CLASS_NAME_KEY + "," + SUBJECT_NAME_KEY + ")" +
                                        ");" ;

    private static final String DROP_CLASS_TABLE = "DROP TABLE IF EXISTS "+CLASS_TABLE_NAME;
    private static final String SELECT_CLASS_TABLE = "SELECT * FROM "+CLASS_TABLE_NAME;

    // student table
    private static final String STUDENT_TABLE_NAME= "STUDENT_TABLE";
    public static final String S_ID= "_SID";
    public  static final String STUDENT_NAME_KEY= "STUDENT_NAME";
    public  static final String STUDENT_ROLL_KEY= "ROLL";

    private static final String CREATE_STUDENT_TABLE = "CREATE TABLE "
                    + STUDENT_TABLE_NAME  +  " ( "
                    + S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + C_ID + " INTEGER NOT NULL, "
                    + STUDENT_NAME_KEY+" TEXT NOT NULL, "
                    + STUDENT_ROLL_KEY+" INTEGER, "
                    +  "  FOREIGN KEY (" + C_ID + " ) REFERENCES "  + CLASS_TABLE_NAME + "(" + C_ID + ")"+");";

    private static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS " +STUDENT_TABLE_NAME;
    private static final String SELECT_STUDENT_TABLE = "SELECT * FROM "+STUDENT_TABLE_NAME;

    //status table
    private static final String STATUS_TABLE_NAME= "STATUS_TABLE";
    public  static final String STATUS_ID= "_STATUSID";
    public  static final String DATE_KEY= " STATUS_DATE";
    public  static final String STATUS_KEY= "STATUS";

    private static final String CREATE_STATUS_TABLE =
            "CREATE TABLE " + STATUS_TABLE_NAME + "("+
                    STATUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                    S_ID + " INTEGER NOT NULL,"+
                    DATE_KEY+" DATE NOT NULL,"+
                    STATUS_KEY+" TEXT NOT NULL,"+
                    " UNIQUE (" +S_ID+","+DATE_KEY+"),"+
                    " FOREIGN KEY (" + S_ID + ") REFERENCES " + STUDENT_TABLE_NAME + "( " + S_ID + ")" + ");";

    private static final String DROP_STATUS_TABLE = "DROP TABLE IF EXISTS " +STATUS_TABLE_NAME;
    private static final String SELECT_STATUS_TABLE = "SELECT * FROM "+STATUS_TABLE_NAME;



    public database(@Nullable Context context) {
        super(context, "Attendance.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CLASS_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_STATUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_CLASS_TABLE);
            db.execSQL(DROP_STUDENT_TABLE);
            db.execSQL(DROP_STATUS_TABLE);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    // add class method
    long addclass(String className,String subjectName){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME_KEY,className);
        values.put(SUBJECT_NAME_KEY,subjectName);

        return database.insert(CLASS_TABLE_NAME,null,values);
    }

    Cursor getClassTable(){
        SQLiteDatabase databasee = this.getReadableDatabase();
        return databasee.rawQuery(SELECT_CLASS_TABLE,null);
    }

    // update class method
    long updateclass(long cid,String className,String subjectName){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME_KEY,className);
        values.put(SUBJECT_NAME_KEY,subjectName);

        return database.update(CLASS_TABLE_NAME,values, C_ID+"=?",new String[]{String.valueOf(cid)});
    }


    //delete  class method
     int deleteclass(long cid){
         SQLiteDatabase databasee = this.getWritableDatabase();
         return databasee.delete(CLASS_TABLE_NAME, C_ID+"=?",new String[]{String.valueOf(cid)});
     }



     // adding student  to database
    long  addStudent(long cid ,int  roll,String name){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_ID,cid);
        values.put(STUDENT_ROLL_KEY,roll);
        values.put(STUDENT_NAME_KEY,name);
        return database.insert(STUDENT_TABLE_NAME,null,values);
    }
    Cursor getStudentTable(long cid){
        SQLiteDatabase database= this.getReadableDatabase();
        return database.query(STUDENT_TABLE_NAME,null,C_ID+"=?",new String[]{String.valueOf(cid)},null,null,STUDENT_ROLL_KEY);
          }
    int deleteStudent(long sid){
        SQLiteDatabase databasee = this.getWritableDatabase();
        return databasee.delete(STUDENT_TABLE_NAME, S_ID+"=?",new String[]{String.valueOf(sid)});
    }
    long updateStudent(long sid,String name){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_NAME_KEY,name);
        return database.update(STUDENT_TABLE_NAME,values, S_ID+"=?",new String[]{String.valueOf(sid)});
    }

    long addStatus(long sid,String date,String status){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(S_ID,sid);
        values.put(DATE_KEY,date);
        values.put(STATUS_KEY,status);
        return database.insert(STATUS_TABLE_NAME,null,values);
    }
    long updateStatus(long sid,String date,String status){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_KEY,status);
        String whereClause = DATE_KEY +"='"+date+"' AND "+S_ID+"="+sid;
        return database.update(STATUS_TABLE_NAME,values,whereClause,null);
    }
    String getStatus(long sid,String date){
        String status = null;
        SQLiteDatabase database  = this.getReadableDatabase();
        String whereClause = DATE_KEY +"='"+date+"' AND "+S_ID+"="+sid;
        Cursor cursor = database.query(STATUS_TABLE_NAME,null,whereClause,null,null,null,null);
        if(cursor.moveToFirst())
            status  = cursor.getString(cursor.getColumnIndex(STATUS_KEY));
        return status;
    }

}

