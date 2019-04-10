package com.dinesh.hungervalley;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.dinesh.hungervalley.SingleRestaurant.MY_PREFS_NAME;


public class CartFragment extends Fragment {

    TextView cart;
    String name;
    int idName;
    LinearLayout layout;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cart = view.findViewById(R.id.cart);
        layout = view.findViewById(R.id.layout);

        Gson gson = new Gson();

        SharedPreferences prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String storedHashMapString = prefs.getString("cart", null);


        if (storedHashMapString != null) {

            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            HashMap<String, String> testHashMap2 = gson.fromJson(storedHashMapString, type);

            String toastString = testHashMap2.get("food_name") + " | " + testHashMap2.get("total_price");
            Toast.makeText(getActivity(), toastString, Toast.LENGTH_LONG).show();

            name = prefs.getString("food_name", "No name defined");//"No name defined" is the default value.
            idName = prefs.getInt("total_price", 0); //0 is the default value.
            cart.setText(String.valueOf(idName));

        } else {

            layout.setVisibility(View.VISIBLE);
        }

        return view;
    }

}
