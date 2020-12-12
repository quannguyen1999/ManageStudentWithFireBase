package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText txtEmail, txtPassword;

    Button btnLogin;

    ProgressBar progressBar;

    //kết nối tới fireBase
    FirebaseAuth firebaseAuth;

    TextView txtCreateNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //tắt thanh bar
        getSupportActionBar().hide();

        //ánh xạ id
        metaData();

       //bắt sự kiện cho các button
        listenEvent();
    }

    private void listenEvent() {
        //kiểm tra tài khoản tồn tại hay không
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    txtEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    txtPassword.setError("Password is required");
                    return;
                }

                if (password.length() < 6) {
                    txtPassword.setError("Password must be >= 6 charaters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(Login.this, "error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        //khi muốn chuyển sang trang đăng nhập
        txtCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
    }

    private void metaData() {
        txtEmail = findViewById(R.id.txtEmailLogin);
        txtPassword = findViewById(R.id.txtPassLogin);
        progressBar = findViewById(R.id.progressBar2);
        firebaseAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
        txtCreateNew = findViewById(R.id.txtCreateNew);
    }
}