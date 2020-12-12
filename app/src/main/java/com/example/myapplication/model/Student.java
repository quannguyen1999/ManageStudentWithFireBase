package com.example.myapplication.model;

public class Student {
    private String mssv;
    private String hoTen;

    public Student(String mssv, String hoTen) {
        this.mssv = mssv;
        this.hoTen = hoTen;
    }

    public Student() {
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    @Override
    public String toString() {
        return "Student{" +
                "mssv='" + mssv + '\'' +
                ", hoTen='" + hoTen + '\'' +
                '}';
    }
}
