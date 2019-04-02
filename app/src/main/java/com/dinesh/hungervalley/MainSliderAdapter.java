package com.dinesh.hungervalley;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide("https://firebasestorage.googleapis.com/v0/b/food-valley-6b326.appspot.com/o/Main%20Banner%2Frawat1.jpg?alt=media&token=2bf885ba-0c0e-475a-b35e-09a30e1fde7e");
                break;
            case 1:
                viewHolder.bindImageSlide("https://firebasestorage.googleapis.com/v0/b/food-valley-6b326.appspot.com/o/Main%20Banner%2FMeghna1.jpg?alt=media&token=f093b8df-8b7d-42a7-a0ce-6e1cbe478b62");
                break;
            case 2:
                viewHolder.bindImageSlide("https://firebasestorage.googleapis.com/v0/b/food-valley-6b326.appspot.com/o/Main%20Banner%2FCake1.jpg?alt=media&token=8fbae640-5de1-4e82-a909-8c501bc40d03");
                break;
        }
    }
}