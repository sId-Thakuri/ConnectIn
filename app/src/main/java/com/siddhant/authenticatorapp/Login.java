package com.siddhant.authenticatorapp;

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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText memail, mpassword;
    TextView mcreateBtn;
    Button mbtnlogin;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        memail = findViewById(R.id.email1);
        mpassword = findViewById(R.id.password1);
        mbtnlogin = findViewById(R.id.btnlogin);
        progressBar = findViewById(R.id.progressBar);
        mcreateBtn = findViewById(R.id.createBtn);

        fAuth = FirebaseAuth.getInstance();

        mbtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = memail.getText().toString().trim();
                String password = mpassword.getText().toString().trim();



                if (TextUtils.isEmpty(email)){
                    memail.setError("email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mpassword.setError("password is required");
                    return;
                }

                if (password.length() < 6){
                    mpassword.setError("password must be greater than 6 characters");
                    return;
                }

                progressBar.setVisibility(View.GONE);


                ///
                //
                //User Authentication

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this,"User logged in",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else{

                            Toast.makeText(Login.this, "Error !"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        mcreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
    }
}
