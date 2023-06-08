package com.hdcompany.plpsa888.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hdcompany.plpsa888.fragment.MedicineAddFragment;
import com.hdcompany.plpsa888.fragment.MedicineSellFragment;
import com.hdcompany.plpsa888.model.Medicine;

/* 2 PAGE NHẬP và XUẤT*/
public class MedicineInOutAdapter  extends FragmentStateAdapter {
private final Medicine medicine;

    public MedicineInOutAdapter(@NonNull FragmentActivity fragmentActivity, Medicine medicine) {
        super(fragmentActivity);
        this.medicine = medicine;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 1) {
            /* XUẤT THUỐC */
            return new MedicineSellFragment(medicine);
        }
        /* NHẬP THUỐC */
        return new MedicineAddFragment(medicine);
//        return new Fragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
