package com.dinesh.hungervalley;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleRestaurant extends AppCompatActivity {

    private Toolbar toolbar;
    String restauratId;
    ImageView header_image;
    DatabaseReference mRestaurantDatabase;
    DatabaseReference mMenuDatabase;
    TextView txt_title, txt_type, txt_res_add;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    ProgressBar progressBar;
    LinearLayout cartLayout;
    TextView item_count, price;
    String food_price, newPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_restaurant);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        header_image = (ImageView) findViewById(R.id.headerimage);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_type = (TextView) findViewById(R.id.txt_type);
        txt_res_add = (TextView) findViewById(R.id.restaurant_add);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        cartLayout = (LinearLayout) findViewById(R.id.cart_layout);
        item_count = (TextView) findViewById(R.id.item_count);
        price = (TextView) findViewById(R.id.price);

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


        mRestaurantDatabase = FirebaseDatabase.getInstance().getReference().child("Restaurants").child(restauratId);
        mRestaurantDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String banner_url = dataSnapshot.child("Banner").getValue().toString();
                String type = dataSnapshot.child("Restaurant_type").getValue().toString();
                String res_address = dataSnapshot.child("Address").getValue().toString();

                txt_title.setText(restauratId);
                txt_type.setText(type);
                txt_res_add.setText(res_address);

                Picasso
                        .with(getApplicationContext())
                        .load(banner_url)
                        .into(header_image);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mMenuDatabase = FirebaseDatabase.getInstance().getReference().child("Restaurants").child(restauratId).child("Menu");

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        progressBar.setVisibility(View.VISIBLE);

        FirebaseRecyclerAdapter<MenuModel, SingleRestaurant.FriendsViewHolder> friendsRecyclerView = new FirebaseRecyclerAdapter<MenuModel, FriendsViewHolder>(

                MenuModel.class,
                R.layout.list_menu_item,
                SingleRestaurant.FriendsViewHolder.class,
                mMenuDatabase

        ) {
            @Override
            protected void populateViewHolder(final SingleRestaurant.FriendsViewHolder viewHolder, final MenuModel model, final int position) {

                final String list_menu_id = getRef(position).getKey();

                final ElegantNumberButton button = viewHolder.mView.findViewById(R.id.number_button);
                final Button btn_add = (Button) viewHolder.mView.findViewById(R.id.btn_add);

                mMenuDatabase.child(list_menu_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        food_price =    dataSnapshot.child("Price").getValue().toString();

                        viewHolder.setName(list_menu_id);
                        viewHolder.setPrice(food_price);



                        btn_add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                button.setVisibility(View.VISIBLE);
                                button.setNumber("1");
                                btn_add.setVisibility(View.GONE);

                                cartLayout.setVisibility(View.VISIBLE);
                                item_count.setText("1Item");

                                //Log.d("DINESH",);
                                //price.setText(food_price);

                            }
                        });


                        button.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                            @Override
                            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                                if (newValue == 0) {

                                    btn_add.setVisibility(View.VISIBLE);
                                    button.setVisibility(View.GONE);
                                    cartLayout.setVisibility(View.GONE);
                                    price.setText(" ");

                                } else {

                                    int i = Integer.parseInt(food_price) * Integer.parseInt(button.getNumber());
                                    price.setText(String.valueOf(i));

                                }
                            }
                        });

                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        recyclerView.setAdapter(friendsRecyclerView);

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

        public void setPrice(String from) {

            TextView fromTxt = (TextView) mView.findViewById(R.id.price);
            fromTxt.setText(from);

        }


    }
}
