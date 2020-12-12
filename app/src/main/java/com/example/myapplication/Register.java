package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;


public class Register extends AppCompatActivity {
    EditText txtFullName, txtEmail, txtPassword, txtPasswordAgain;
    Button btnRegister;
    ProgressBar progressBar;
    TextView btnLoginHere;

    //kết nối tới fire bar
    FirebaseAuth fAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //ẩn thanh bar
        getSupportActionBar().hide();

        //ánh xạ id
        metaData();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        //lắng nghe sự kiện từ các button
        listenEvent();
    }

    private void listenEvent() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
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

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(Register.this, "error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }

    private void metaData() {

        txtFullName = findViewById(R.id.txtFullName);
        txtEmail = findViewById(R.id.txtEMail);
        txtPassword = findViewById(R.id.txtPassword);
        txtPasswordAgain = findViewById(R.id.txtPasswordAgain);
        btnLoginHere = findViewById(R.id.btnLoginHere);

        btnRegister = findViewById(R.id.btnRegister);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
    }
}