package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ClickItemListener;
import com.example.myapplication.R;
import com.example.myapplication.model.Student;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListStudentAdapter extends RecyclerView.Adapter<ListStudentAdapter.StudentViewHolder> {
    private LayoutInflater layoutInflater;

    private ArrayList<Student> listStudent;

    //dùng để thay đổi màu khi chọn columns khác
    private int selectedItem = -1;

    private ClickItemListener clickItemListener;

    public ListStudentAdapter(Context context, ArrayList<Student> listStudent, ClickItemListener clickItemListener) {
        layoutInflater = LayoutInflater.from(context);
        this.listStudent = listStudent;
        this.clickItemListener = clickItemListener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.list_student, parent, false);
        return new StudentViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, final int position) {
        Student student = listStudent.get(position);

        holder.tvMa.setText(student.getMssv());

        holder.tvHoTen.setText(student.getHoTen());

        //nếu không phải bản chọn ,set mặc định
        holder.constraintLayout.setBackgroundColor(Color.TRANSPARENT);

        if(selectedItem == position){

            //còn không thì tô đậm bảng đó
            holder.constraintLayout.setBackgroundColor(Color.GRAY);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int previousItem = selectedItem;

                selectedItem = position;

                ListStudentAdapter.this.notifyItemChanged(previousItem);

                ListStudentAdapter.this.notifyItemChanged(position);

                clickItemListener.onItemCLick(listStudent.get(position), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listStudent.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvMa, tvHoTen;

        ListStudentAdapter listStudentAdapter;

        AdapterView.OnItemClickListener itemClickListener;

        ConstraintLayout constraintLayout;

        public StudentViewHolder(@NonNull View itemView, ListStudentAdapter listStudentAdapter) {
            super(itemView);

            tvMa = itemView.findViewById(R.id.txtMaSo);
            tvHoTen = itemView.findViewById(R.id.txtHoTen);
            constraintLayout = itemView.findViewById(R.id.ct);

            this.listStudentAdapter = listStudentAdapter;
        }
    }
}
