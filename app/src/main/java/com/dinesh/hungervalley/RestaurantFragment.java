package com.dinesh.hungervalley;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ss.com.bannerslider.Slider;

public class RestaurantFragment extends Fragment {

    private Slider slider;


    public RestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        Slider.init(new PicassoImageLoadingService(getContext()));

        slider = view.findViewById(R.id.banner_slider1);
        setupViews();



        return view;

    }

    private void setupViews() {


        //delay for testing empty view functionality
        slider.postDelayed(new Runnable() {
            @Override
            public void run() {
                slider.setAdapter(new MainSliderAdapter());
                slider.setSelectedSlide(0);
            }
        }, 1500);

    }


}
