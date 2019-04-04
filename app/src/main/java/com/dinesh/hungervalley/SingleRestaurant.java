package com.dinesh.hungervalley;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;

public class SingleRestaurant extends AppCompatActivity {

    private Toolbar toolbar;
    String restauratId;
    ImageView header_image;
    DatabaseReference mMenuDatabase;
    TextView txt_title, txt_type, txt_res_add;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_restaurant);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        header_image = (ImageView) findViewById(R.id.headerimage);

        setSupportActionBar(toolbar);
        restauratId = ((Application) this.getApplicationContext()).getSomeVariable();
        getSupportActionBar().setTitle(restauratId);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(restauratId);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });


    }
}
