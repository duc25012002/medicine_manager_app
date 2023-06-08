package com.hdcompany.plpsa888.activity;

import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.adapter.PLPSAAdapter;
import com.hdcompany.plpsa888.databinding.ActivityPlpsaBinding;

public class PLPSA extends BaseActivity {

    public ActivityPlpsaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* KHỞI TẠO VIEW VỚI DATA BINDING */
        binding = ActivityPlpsaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* KHÔNG CHO NGƯỜI DÙNG VUỐT NGANG PAGES */
        binding.viewpager2.setUserInputEnabled(false);

        /* GÁN VIEW PAGER */
        PLPSAAdapter plpsaAdapter = new PLPSAAdapter(PLPSA.this);
        binding.viewpager2.setAdapter(plpsaAdapter);

        /* SET MAIN FRAGMENT AS DEFAULT */
        binding.viewpager2.setCurrentItem(0);
        binding.bottomNavigationView.setSelectedItemId(R.id.nav_home);

        /* CÀI ĐẶT ĐỒNG BỘ CHO PAGER VÀ BOTTOM MENU */
        binding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        binding.bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;

                    case 1:
                        binding.bottomNavigationView.getMenu().findItem(R.id.nav_manage).setChecked(true);
                        break;
                    case 2:
                        binding.bottomNavigationView.getMenu().findItem(R.id.nav_customer_support).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        /* CÀI ĐẶT ĐỒNG BỘ CHO PAGER VÀ BOTTOM MENU */
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_manage) {
                binding.viewpager2.setCurrentItem(1);
            } else if (id == R.id.nav_home) {
                binding.viewpager2.setCurrentItem(0);
            } else if (id == R.id.nav_customer_support) {
                binding.viewpager2.setCurrentItem(2);
            }
            return true;
        });

    }
}