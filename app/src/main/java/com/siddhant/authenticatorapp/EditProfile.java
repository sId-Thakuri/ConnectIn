package com.siddhant.authenticatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class EditProfile extends AppCompatActivity {

    EditText username, email, contact;
    Button btnupdate;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userId;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        contact = findViewById(R.id.contact);
        btnupdate = findViewById(R.id.btnupdate);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();


        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                username.setText(documentSnapshot.getString("username"));
                email.setText(documentSnapshot.getString("email"));
                contact.setText(documentSnapshot.getString("contact"));

            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty() || email.getText().toString().isEmpty() || contact.getText().toString().isEmpty()){
                    Toast.makeText(EditProfile.this,"One or Many fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String uemail = email.getText().toString();
                user.updateEmail(uemail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        DocumentReference documentReference = fStore.collection("users").document(userId);
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("email", uemail);
                        edited.put("username", username.getText().toString());
                        edited.put("contact", contact.getText().toString());
                        documentReference.update(edited);

                        Toast.makeText(EditProfile.this, "UPDATED", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });



    }

    public void btncancel(View view){
        startActivity(new Intent(getApplicationContext(),Profile.class));
        finish();
    }
}
