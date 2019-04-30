package com.dinesh.hungervalley;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static com.dinesh.hungervalley.SingleRestaurant.MY_PREFS_NAME;


public class CartFragment extends Fragment {

    TextView cart;
    LinearLayout layoutEmpty;
    ArrayList<String> arrPackage;

    private ListView mListView;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cart = view.findViewById(R.id.cart);
        layoutEmpty = view.findViewById(R.id.layout_empty);
        mListView = (ListView) view.findViewById(R.id.listView);



        return view;
    }


}
