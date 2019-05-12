package com.dinesh.hungervalley;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends BaseActivity {

    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mCartListDatabase;
    private RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayout layout_empty;
    String uId, restaurantId;
    int flags;
    String bookskey, s;

    MyAdapter adapter;
    TextView restaurant;

    ArrayList<CartDataSetGet> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = (RecyclerView) findViewById(R.id.upload_list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        layout_empty = (LinearLayout) findViewById(R.id.layout_empty);
        restaurant = (TextView) findViewById(R.id.restaurant);

        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        uId = (shared.getString("user_id", ""));


        mCartListDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View");


        mCartListDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    progressBar.setVisibility(View.GONE);
                    layout_empty.setVisibility(View.INVISIBLE);

                    SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
                    restaurantId = (shared.getString("restaurant", ""));

                    restaurant.setText(restaurantId);

                    linearLayoutManager = new LinearLayoutManager(CartActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setNestedScrollingEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                    mCartListDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId).child(restaurantId);
                    mCartListDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list = new ArrayList<CartDataSetGet>();


                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                for (DataSnapshot ds : dataSnapshot1.getChildren()) {

                                    if (ds.getKey().equals("Total price")) {

                                        restaurant.setText(restaurantId);

                                    }else {

                                        CartDataSetGet p = ds.getValue(CartDataSetGet.class);
                                        list.add(p);
                                    }

                                }

                            }
                            adapter = new MyAdapter(CartActivity.this, list);
                            recyclerView.setAdapter(adapter);

                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(CartActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

                    progressBar.setVisibility(View.GONE);
                    layout_empty.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*if (isNetworkConnectionAvailable() == true) {

            FirebaseRecyclerAdapter<CartDataSetGet, FriendsViewHolder> friendsRecyclerView = new FirebaseRecyclerAdapter<CartDataSetGet, FriendsViewHolder>(

                    CartDataSetGet.class,
                    R.layout.list_cart_item,
                    FriendsViewHolder.class,
                    mCartListDatabase

            ) {
                @Override
                protected void populateViewHolder(final FriendsViewHolder viewHolder, CartDataSetGet model, int position) {

                    String list_user_id = getRef(position).getKey();

                    mCartListDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()){

                                viewHolder.name.setText(ds.getKey());
                            }


                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            };

            recyclerView.setAdapter(friendsRecyclerView);
*/


    }




 /*   public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView price, name;


        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            price = (TextView) itemView.findViewById(R.id.price);
            name = (TextView) itemView.findViewById(R.id.name);


        }

    }*/


    @Override
    int getContentViewId() {
        return R.layout.activity_cart;

    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.cart;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) CartActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
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
}
