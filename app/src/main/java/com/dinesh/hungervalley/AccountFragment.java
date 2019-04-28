package com.dinesh.hungervalley;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class AccountFragment extends Fragment {

    Button btn_login;
    LinearLayout layout,layout_account;
    TextView textViewUsername,textViewPhone;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        layout = (LinearLayout) view.findViewById(R.id.layout);
        layout_account = (LinearLayout) view.findViewById(R.id.layout_account);
        textViewPhone = (TextView)view.findViewById(R.id.phone);
        textViewUsername = (TextView)view.findViewById(R.id.username);


        SharedPreferences shared = getContext().getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        final String userId = (shared.getString("user_id", ""));

        // other setup code
        SharedPreferences mPrefs = getContext().getSharedPreferences("myAppPrefs", MODE_PRIVATE);
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


        btn_login = (Button) view.findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);

            }
        });


        return view;

    }

}
