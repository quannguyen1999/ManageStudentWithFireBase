package com.example.myapplication;

import com.example.myapplication.model.Student;

//dùng để gửi sự kiện lại qua MainActivity
public interface ClickItemListener {
    void onItemCLick(Student student, int position);

}
