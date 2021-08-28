package com.example.eattendance;

public class Studentitem {
    private  long sid;
    private int roll;
    private String status;
    private String name;

    public Studentitem(long sid, int roll,  String name) {
        this.sid = sid;
        this.roll = roll;
        status="";
        this.name = name;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int  roll) {
        this.roll = roll;
    }


    public String getStatus() { return status;}

    public void getStatus(String status) { this.status = status;}

    public void setStatus(String status) {
        this.status = status;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

}
