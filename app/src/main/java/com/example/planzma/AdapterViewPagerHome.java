package com.example.planzma;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdapterViewPagerHome extends FragmentPagerAdapter {
    private HomeFragment homeFragment = new HomeFragment();
    private ChatFragment chatFragment = new ChatFragment();
    private LocationFragment locationFragment = new LocationFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    Bundle bundle = new Bundle();
    public AdapterViewPagerHome(@NonNull FragmentManager fm) {
        super(fm);
        bundle.putBoolean("Dealer",false);
        homeFragment.setArguments(bundle);
        chatFragment.setArguments(bundle);
        locationFragment.setArguments(bundle);
        profileFragment.setArguments(bundle);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch (position){
            case 0 :
                return homeFragment;
            case 1:
                return chatFragment;
            case 2 :
                return locationFragment;
            case 3:
                return profileFragment;


        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
