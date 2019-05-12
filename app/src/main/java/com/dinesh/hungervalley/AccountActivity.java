package com.dinesh.hungervalley;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends BaseActivity {

    Button btn_login;
    LinearLayout layout,layout_account;
    TextView textViewUsername,textViewPhone;
    int flags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = (LinearLayout) findViewById(R.id.layout);
        layout_account = (LinearLayout) findViewById(R.id.layout_account);
        textViewPhone = (TextView)findViewById(R.id.phone);
        textViewUsername = (TextView)findViewById(R.id.username);

        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);


        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        final String userId = (shared.getString("user_id", ""));

        // other setup code
        SharedPreferences mPrefs = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        if (mPrefs.getBoolean("is_logged_before", false)) {

            layout.setVisibility(View.INVISIBLE);
            layout_account.setVisibility(View.VISIBLE);

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference usersRef = database.getReference("Users");

            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String username = dataSnapshot.child(userId).child("username").getValue().toString();
                    String mobile = dataSnapshot.child(userId).child("mobile_number").getValue().toString();
                    textViewUsername.setText(username);
                    textViewPhone.setText(mobile);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        } else {

            layout.setVisibility(View.VISIBLE);
            layout_account.setVisibility(View.INVISIBLE);
        }


        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AccountActivity.this, StartActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_account;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.account;
    }
}
