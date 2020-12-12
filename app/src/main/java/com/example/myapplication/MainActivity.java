package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.adapter.ListStudentAdapter;
import com.example.myapplication.model.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ClickItemListener {

//    String url ="https://servicelogin-78bb3-default-rtdb.firebaseio.com/";

    //kết nối tới firebase
    FirebaseDatabase database;

    DatabaseReference myRef;

    ImageButton btnLogout;

    EditText edtMa, edtHoTen;

    Button btnThem, btnXoa, btnCapNhap;

    //recycle view
    private RecyclerView rclView;

    private ArrayList<Student> listStudents;

    private ListStudentAdapter listStudentAdapter;

    ArrayAdapter<String> adapter;

    private int itemSelcted = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ánh xạ dữ liệu
        metaData();

        //khởi tạo dữ liệu
        initData();

        //Lắng nghe sự kiện các button
        listenEvent();
    }

    private void listenEvent() {
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemSelcted == -1) {
                    Toast.makeText(MainActivity.this, "please choose", Toast.LENGTH_LONG);
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Delete?")
                            .setMessage("Are you sure ? ")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    String ma = edtMa.getText().toString();

                                    Query queryRef = myRef.child("Student").orderByChild("mssv").equalTo(ma);

                                    queryRef.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            snapshot.getRef().setValue(null);


                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    itemSelcted = -1;
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }

            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mssv = edtMa.getText().toString();
                String hoTen = edtHoTen.getText().toString();
                boolean gioiTinh = false;
                if (hoTen.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Mã sinh viên chưa nhập", Toast.LENGTH_LONG).show();
                    edtMa.requestFocus();
                    return;
                }
                if (hoTen.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Họ tên chưa nhập", Toast.LENGTH_LONG).show();
                    edtHoTen.requestFocus();
                    return;
                }

                Student student = new Student(mssv, hoTen);

                myRef.child("Student").push().setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        edtMa.setText("");
                        edtHoTen.setText("");
                        Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                    }
                });

//                refreshStudents();

                closeKeyBoard();
            }
        });

        btnCapNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemSelcted == -1) {
                    Toast.makeText(MainActivity.this, "please choose", Toast.LENGTH_LONG);
                } else {
                    String mssv = edtMa.getText().toString();
                    String hoTen = edtHoTen.getText().toString();
                    boolean gioiTinh = false;
                    if (hoTen.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Mã sinh viên chưa nhập", Toast.LENGTH_LONG).show();
                        edtMa.requestFocus();
                        return;
                    }
                    if (hoTen.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Họ tên chưa nhập", Toast.LENGTH_LONG).show();
                        edtHoTen.requestFocus();
                        return;
                    }

                    Student student = new Student(mssv, hoTen);

                    Query queryRef = myRef.child("Student").orderByChild("mssv").equalTo(mssv);

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            snapshot.getRef().setValue(student);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    closeKeyBoard();

                    itemSelcted = -1;
                }
            }
        });
    }

    public void metaData() {
        btnLogout = findViewById(R.id.imageButton);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtMa = findViewById(R.id.edtMaSV);
        btnCapNhap = findViewById(R.id.btnCapNhap);
        btnThem = findViewById(R.id.btnThem);
        btnXoa = findViewById(R.id.btnXoa);
        rclView = findViewById(R.id.rclView);
    }

    public void refreshStudents() {
        myRef.child("Student").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("Call here:");

                Student student = snapshot.getValue(Student.class);

                listStudents.add(student);

                ClickItemListener clickItemListener = new ClickItemListener() {
                    @Override
                    public void onItemCLick(Student student, int position) {
                        edtMa.setText(student.getMssv());
                        edtHoTen.setText(student.getHoTen());

                        itemSelcted = position;
                    }
                };

                listStudentAdapter = new ListStudentAdapter(MainActivity.this
                        , listStudents, clickItemListener);

                rclView.setAdapter(listStudentAdapter);

                listStudentAdapter.notifyDataSetChanged();

                rclView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("change");

                Student student = snapshot.getValue(Student.class);
                try {
                    for(int i=0;i<listStudents.size();i++){
                        if(listStudents.get(i).getMssv().equalsIgnoreCase(student.getMssv())){
                            listStudents.get(i).setHoTen(student.getHoTen());
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                listStudentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                System.out.println("remove");

                edtHoTen.setText("");
                edtMa.setText("");

                Student student = snapshot.getValue(Student.class);
                try {
                    for (Student listStudent : listStudents) {
                        System.out.println(listStudent);
                        if(listStudent.getMssv().equalsIgnoreCase(student.getMssv())){
                            System.out.println("found");
                            listStudents.remove(listStudent);
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }


                listStudentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("move");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("canceled");
            }
        });


    }


    //đóng keyboard khi nhập enter
    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void initData() {
        listStudents = new ArrayList<>();

        listStudentAdapter = new ListStudentAdapter(this, listStudents, this);

        rclView.setAdapter(listStudentAdapter);

        rclView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();

        myRef = database.getInstance().getReference();

        refreshStudents();
    }

    //nhận dữ liệu khi xảy ra
    @Override
    public void onItemCLick(Student student, int position) {
        edtMa.setText(student.getMssv());
        edtHoTen.setText(student.getHoTen());
        itemSelcted = position;
    }


}