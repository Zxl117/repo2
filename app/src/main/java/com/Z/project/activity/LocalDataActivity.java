package com.Z.project.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.Z.project.R;
import com.Z.project.adapter.LocalDataAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocalDataActivity extends BaseActivity {

    @BindView(R.id.spnner)
    Spinner spnner;
    @BindView(R.id.lv_list)
    ListView lvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_data);
        ButterKnife.bind(this);

        init();
    }

    private void init() {

        spnner.setVisibility(View.VISIBLE);

        String[] arr = {"2020-01-01", "2020-01-02", "2020-01-03"};
        spnner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,arr));

        lvList.setAdapter(new LocalDataAdapter(this));

    }
}
