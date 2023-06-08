package com.hdcompany.plpsa888.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hdcompany.plpsa888.fragment.MainFragment;
import com.hdcompany.plpsa888.fragment.ModifierFragment;
import com.hdcompany.plpsa888.fragment.ProfileFragment;

public class PLPSAAdapter  extends FragmentStateAdapter {

    public PLPSAAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1: {
                return new ModifierFragment();
            } case 2:{
                return new ProfileFragment();
            } default:{
                return new MainFragment();
            }
        }
    }

    @Override
    public int getItemCount() {
        /* CÓ 3 FRAGMENTS */
        return 3;
    }
}
