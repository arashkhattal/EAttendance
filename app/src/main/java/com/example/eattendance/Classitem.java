package com.example.eattendance;

public class Classitem {
    public Classitem(long cid, String classname, String subjectname) {
        this.cid = cid;
        this.classname = classname;
        this.subjectname = subjectname;
    }

    private long cid;
    private String classname;
    private String subjectname;


    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public Classitem(String classname, String subjectname) {
        this.classname = classname;
        this.subjectname = subjectname;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
