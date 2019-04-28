package com.dinesh.hungervalley;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.FirebaseApp;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    BottomNavigationView navigation;
    public static final String PREFS_NAME = "MyPrefsFile";
    private int mSelectedItem;
    private static final String SELECTED_ITEM = "arg_selected_item";
    Window window;
    int flags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("hasLoggedIn", true);
        editor.commit();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hunger Valley");

        window = getWindow();
        FirebaseApp.initializeApp(this);

        navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = navigation.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = navigation.getMenu().getItem(0);
        }

        selectFragment(selectedItem);
    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        item.setCheckable(true);
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.restaurant:

                RestaurantFragment fragmentone = new RestaurantFragment();
                toolbar.setTitle("Hunger Valley");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_frame, fragmentone);

                //flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
                //flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // use XOR here for remove LIGHT_STATUS_BAR from flags
                //getWindow().getDecorView().setSystemUiVisibility(flags);
                getWindow().setStatusBarColor(ContextCompat
                        .getColor(this, R.color.colorPrimaryDark));

                toolbar.setVisibility(View.VISIBLE);
                ft.commit();

                break;
            case R.id.cart:

                CartFragment fragmentone_Three = new CartFragment();
                FragmentTransaction ft_three = getSupportFragmentManager().beginTransaction();
                ft_three.replace(R.id.fragment_frame, fragmentone_Three);
                toolbar.setVisibility(View.GONE);

                flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
                getWindow().getDecorView().setSystemUiVisibility(flags);
                getWindow().setStatusBarColor(Color.WHITE);


                ft_three.commit();

                break;
            case R.id.account:

                AccountFragment tabFragmentTwo = new AccountFragment();
                FragmentTransaction ft_two = getSupportFragmentManager().beginTransaction();
                ft_two.replace(R.id.fragment_frame, tabFragmentTwo);
                toolbar.setVisibility(View.GONE);

                flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
                getWindow().getDecorView().setSystemUiVisibility(flags);
                getWindow().setStatusBarColor(Color.WHITE);

                ft_two.commit();

                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();


        if (frag != null) {
            RestaurantFragment fragmentone = new RestaurantFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_frame, fragmentone);

            ft.commit();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = navigation.getMenu().getItem(0);

        if (mSelectedItem != homeItem.getItemId()) {

            selectFragment(homeItem);

            // Select home item
            navigation.setSelectedItemId(homeItem.getItemId());
        } else {
            super.onBackPressed();
        }
    }

}

