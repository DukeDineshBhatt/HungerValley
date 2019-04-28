package com.dinesh.hungervalley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    int flags;
    EditText editTextMobile, editTextPassword;
    Button button_login;
    String mobile, passwordEntered;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("");


        editTextPassword = (EditText) findViewById(R.id.password);
        editTextMobile = (EditText) findViewById(R.id.mobile);
        button_login = (Button) findViewById(R.id.button_login);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressbar.setVisibility(View.VISIBLE);
                mobile = editTextMobile.getText().toString().trim();
                passwordEntered = editTextPassword.getText().toString().trim();

                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference usersRef = database.getReference("Users");

                if (mobile.isEmpty() || mobile.length() < 10) {

                    editTextMobile.setError("Enter valid mobile number");
                    editTextMobile.requestFocus();
                    progressbar.setVisibility(View.GONE);
                    return;
                } else if (passwordEntered.isEmpty() || passwordEntered.length() < 6) {

                    editTextPassword.setError("Enter your 6 digit password");
                    editTextPassword.requestFocus();
                    progressbar.setVisibility(View.GONE);
                    return;
                } else {

                    usersRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            String password = snapshot.child(mobile).child("password").getValue().toString();

                            if (snapshot.hasChild(mobile) && password.equals(passwordEntered)) {

                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Log in success.", Toast.LENGTH_SHORT).show();

                                SharedPreferences mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mPrefs.edit();
                                editor.putString("user_id",mobile);
                                editor.putBoolean("is_logged_before", true); //this line will do trick
                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                //intent.putExtra("mobile", mobile);
                                startActivity(intent);
                                finish();

                            } else {
                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Incorrect details.", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            progressbar.setVisibility(View.GONE);
                        }
                    });
                }


            }

        });

        progressbar.setVisibility(View.GONE);
    }
}
