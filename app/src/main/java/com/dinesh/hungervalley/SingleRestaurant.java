package com.dinesh.hungervalley;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SingleRestaurant extends AppCompatActivity {

    private Toolbar toolbar;
    String restauratId;
    ImageView header_image;
    DatabaseReference mRestaurantDatabase;
    DatabaseReference mMenuDatabase;
    DatabaseReference mCartDatabase;
    TextView txt_title, txt_type, txt_res_add;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    ProgressBar progressBar;
    LinearLayout cartLayout;
    TextView item_count, price;

    ArrayList<Integer> m = new ArrayList<Integer>();
    ArrayList<String> food = new ArrayList<>();

    String uId;

    public static final String MY_PREFS_NAME = "HungerValleyCart";

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

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        uId = (shared.getString("user_id", ""));

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

        mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);

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
            protected void populateViewHolder(final SingleRestaurant.FriendsViewHolder viewHolder, final MenuModel model, int position) {

                final String list_menu_id = getRef(position).getKey();

                mMenuDatabase.child(list_menu_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String food_price = dataSnapshot.child("Price").getValue().toString();
                        final String food_type = dataSnapshot.child("Type").getValue().toString();
                        final String food_name = dataSnapshot.child("FoodName").getValue().toString();
                        final String food_id = dataSnapshot.child("FoodId").getValue().toString();

                        viewHolder.setName(list_menu_id);
                        viewHolder.price.setText(food_price);
                        viewHolder.setImage(food_type);

                        viewHolder.add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                viewHolder.layout_button.setVisibility(View.VISIBLE);
                                viewHolder.add.setVisibility(View.GONE);

                                int count = Integer.parseInt(String.valueOf(viewHolder.textCount.getText()));

                                m.add(Integer.valueOf(food_price));

                                int i;
                                double sum = 0;
                                for (i = 0; i < m.size(); i++) {

                                    sum += m.get(i);
                                    price.setText(String.valueOf(sum));
                                    item_count.setText(String.valueOf(m.size() + "item"));

                                }

                                mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List");

                                HashMap<String, Object> cartMap = new HashMap<>();
                                cartMap.put("pName", food_name);
                                cartMap.put("price", food_price);
                                cartMap.put("quantity", count);

                                mCartDatabase.child("User View").child(uId).child(restauratId).child(food_id)
                                        .updateChildren(cartMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SingleRestaurant.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });


                            }
                        });


                        viewHolder.buttonInc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int count = Integer.parseInt(String.valueOf(viewHolder.textCount.getText()));
                                count++;
                                viewHolder.textCount.setText(String.valueOf(count));


                                m.add(Integer.valueOf(food_price));

                                int i;
                                double sum = 0;
                                for (i = 0; i < m.size(); i++) {

                                    sum += m.get(i);
                                    price.setText(String.valueOf(sum));
                                    item_count.setText(String.valueOf(m.size() + "item"));

                                }

                                mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List");

                                HashMap<String, Object> cartMap = new HashMap<>();
                                cartMap.put("pName", food_name);
                                cartMap.put("price", food_price);
                                cartMap.put("quantity", count);

                                mCartDatabase.child("User View").child(uId).child(restauratId).child(food_id)
                                        .updateChildren(cartMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SingleRestaurant.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });

                            }
                        });

                        viewHolder.buttonDec.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int count = Integer.parseInt(String.valueOf(viewHolder.textCount.getText()));

                                if (count == 1) {

                                    viewHolder.layout_button.setVisibility(View.GONE);
                                    viewHolder.add.setVisibility(View.VISIBLE);

                                    mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId).child(restauratId);

                                    mCartDatabase.child(food_id).removeValue();
                                } else if (count > 0) {

                                    count--;

                                    viewHolder.textCount.setText(String.valueOf(count));

                                    m.remove(Integer.valueOf(food_price));

                                    int i;
                                    double sum = 0;
                                    for (i = 0; i < m.size(); i++)
                                        sum += m.get(i);
                                    price.setText(String.valueOf(sum));
                                    item_count.setText(String.valueOf(m.size() + "item"));


                                    mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List");

                                    HashMap<String, Object> cartMap = new HashMap<>();
                                    cartMap.put("pName", food_name);
                                    cartMap.put("price", food_price);
                                    cartMap.put("quantity", count);

                                    mCartDatabase.child("User View").child(uId).child(restauratId).child(food_id)
                                            .updateChildren(cartMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        //Toast.makeText(SingleRestaurant.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });

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

        Button buttonInc, buttonDec, add;
        TextView textCount, price;
        ImageView type_image;
        LinearLayout layout_button;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            buttonInc = (Button) itemView.findViewById(R.id.btn_add);
            buttonDec = (Button) itemView.findViewById(R.id.btn_minus);
            add = (Button) itemView.findViewById(R.id.add);
            textCount = (TextView) itemView.findViewById(R.id.text);
            price = (TextView) itemView.findViewById(R.id.price);
            type_image = (ImageView) itemView.findViewById(R.id.type_image);
            layout_button = (LinearLayout) itemView.findViewById(R.id.layout_button);

        }

        public void setName(String name) {
            TextView userName = (TextView) itemView.findViewById(R.id.name);
            userName.setText(name);
        }


        public void setImage(String image) {
            ImageView imageView = (ImageView) mView.findViewById(R.id.type_image);

            if (!image.equals("Non-Veg")) {

                Picasso
                        .with(mView.getContext())
                        .load(R.drawable.veg)
                        .into(imageView);

            } else {

                Picasso
                        .with(mView.getContext())
                        .load(R.drawable.non_veg)
                        .into(imageView);

            }

        }

    }
}
