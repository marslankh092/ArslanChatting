package com.example.arslanchatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.arslanchatting.databinding.ActivityAuthentcationBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthentcationActivity extends AppCompatActivity {
    @NonNull
    ActivityAuthentcationBinding binding;
    String name, email, password;
    DatabaseReference dbrefernce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAuthentcationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbrefernce = FirebaseDatabase.getInstance().getReference("Arslan").child("users");

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.email.getText().toString();
                password = binding.password.getText().toString();

                login();
            }


        });

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.email.getText().toString();
                password = binding.password.getText().toString();
                name = binding.name.getText().toString();

                signup();
            }


        });


    }

    private void login() {
        FirebaseAuth.
                getInstance().signInWithEmailAndPassword(email.trim(), password.trim())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(AuthentcationActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }

    private void signup() {
        FirebaseAuth.
                getInstance().createUserWithEmailAndPassword(email.trim(), password.trim())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfileChangeRequest userProfileChangeRequest =
                                new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        firebaseUser.updateProfile(userProfileChangeRequest);

                        UserModel userModel = new UserModel(FirebaseAuth.getInstance().getUid(),name,email,password);

                        //user model is pass though here
                        dbrefernce.child(FirebaseAuth.getInstance().getUid()).setValue(userModel);

                        startActivity(new Intent(AuthentcationActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(AuthentcationActivity.this, MainActivity.class));
            finish();
        }

    }
}