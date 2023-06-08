package com.hdcompany.plpsa888.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayoutMediator;
import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.adapter.MedicineInOutAdapter;
import com.hdcompany.plpsa888.constant.Constant;
import com.hdcompany.plpsa888.databinding.ActivityViewMedicineDetailBinding;
import com.hdcompany.plpsa888.model.Medicine;

public class ViewMedicineDetailActivity extends AppCompatActivity {

    public ActivityViewMedicineDetailBinding mBinding;
    private Medicine mMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityViewMedicineDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        getDataIntent();
        MedicineInOutAdapter myPagerAdapter = new MedicineInOutAdapter(this, mMedicine);
        mBinding.viewPager2.setAdapter(myPagerAdapter);
        new TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText(getString(R.string.label_added));
            } else {
                tab.setText(getString(R.string.label_used));
            }
        }).attach();
    }

    public void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        mMedicine = (Medicine) bundle.getSerializable(Constant.KEY_INTENT_MEDICINE_OBJECT);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}