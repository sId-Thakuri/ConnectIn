package com.siddhant.authenticatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText memail, mpassword, musername, mcontact;
    Button mbtnsignin;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userID;

    ProgressBar progressBar;
    TextView mbtnlogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);



        memail = findViewById(R.id.email);
        mpassword = findViewById(R.id.password);
        musername = findViewById(R.id.username);
        mcontact = findViewById(R.id.contact);
        mbtnsignin = findViewById(R.id.btnsignin);
        mbtnlogin = findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null){

            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mbtnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String email = memail.getText().toString().trim();
                final String password = mpassword.getText().toString().trim();
                final String username = musername.getText().toString();
                final String contact = mcontact.getText().toString();




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

                //
                //
                ///
                ////
                /// REGISTER USER IN FIREBASE



                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    ///addOnComplete listener to know that the user has been created
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();

                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("username",username);
                            user.put("email",email);
                            user.put("contact",contact);
                            user.put("Password",password);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d(TAG, "onSuccess: user profile is created for "+ userID);

                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: "+ e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText( Register.this, "Error Occured"+ task.getException().getMessage(), Toast.LENGTH_SHORT ).show();

                        }

                    }
                });



            }
        });


        mbtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));

            }
        });






    }


}
