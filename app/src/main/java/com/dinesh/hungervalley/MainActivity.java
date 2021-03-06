package com.dinesh.hungervalley;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import ss.com.bannerslider.Slider;


public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    BottomNavigationView navigation;
    public static final String PREFS_NAME = "MyPrefsFile";
    private int mSelectedItem;
    private static final String SELECTED_ITEM = "arg_selected_item";
    Window window;
    int flags;


    private Slider slider;
    private RecyclerView recyclerView;
    ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mRestaurantDatabase;
    String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("hasLoggedIn", true);
        editor.commit();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hunger Valley");

        window = getWindow();
        FirebaseApp.initializeApp(this);



        Slider.init(new PicassoImageLoadingService(MainActivity.this));

        slider = findViewById(R.id.banner_slider1);


        recyclerView = (RecyclerView) findViewById(R.id.upload_list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        FirebaseApp.initializeApp(this);

        mRestaurantDatabase = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        mRestaurantDatabase.keepSynced(true);


        Slider.init(new PicassoImageLoadingService(MainActivity.this));

        slider = findViewById(R.id.banner_slider1);
        slider.setAdapter(new MainSliderAdapter());

        //delay for testing empty view functionality
        slider.postDelayed(new Runnable() {
            @Override
            public void run() {
                slider.setAdapter(new MainSliderAdapter());
                slider.setSelectedSlide(0);
            }
        }, 1500);

        Log.d("DINESH", "DINESH");


        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        if (isNetworkConnectionAvailable() == true) {

            FirebaseRecyclerAdapter<MyDataSetGet, FriendsViewHolder> friendsRecyclerView = new FirebaseRecyclerAdapter<MyDataSetGet, FriendsViewHolder>(

                    MyDataSetGet.class,
                    R.layout.list_item_single,
                   FriendsViewHolder.class,
                    mRestaurantDatabase

            ) {
                @Override
                protected void populateViewHolder(final FriendsViewHolder viewHolder, MyDataSetGet model, int position) {

                    final String list_user_id = getRef(position).getKey();

                    mRestaurantDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            final String name = dataSnapshot.child("Restaurant_name").getValue().toString();
                            final String type = dataSnapshot.child("Restaurant_type").getValue().toString();
                            final String image = dataSnapshot.child("Banner").getValue().toString();


                            viewHolder.setName(name);
                            viewHolder.setFrom(type);
                            viewHolder.setImage(image);

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent chatIntent = new Intent(MainActivity.this, SingleRestaurant.class);
                                    final String s = ((Application)getApplicationContext()).setSomeVariable(list_user_id);
                                    chatIntent.putExtra("restauranr_id", list_user_id);
                                    startActivity(chatIntent);


                                }
                            });

                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }
            };

            recyclerView.setAdapter(friendsRecyclerView);

        }


    }


    @Override
    int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.restaurant;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }


    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            checkNetworkConnection();
            Log.d("Network", "Not Connected");
            return false;
        }
    }

    public void checkNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;


        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }


        public void setName(String name) {
            TextView userName = (TextView) mView.findViewById(R.id.name);
            userName.setText(name);
        }


        public void setFrom(String from) {

            TextView fromTxt = (TextView) mView.findViewById(R.id.type);
            fromTxt.setText(from);

        }


        public void setImage(String image) {


            if (!image.equals("default")) {
                ImageView imageView = (ImageView) mView.findViewById(R.id.image);
                Picasso
                        .with(mView.getContext())
                        .load(image)
                        .into(imageView);

            }

        }

    }


}

