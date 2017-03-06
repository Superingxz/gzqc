package com.xolo.gzqc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwernInsuranceAdapter;

/**
 * Created by Administrator on 2016/9/26.
 */
public class NewCarInsuranceFragment extends  LazyFragment{
    ListView insurance_lv;
    String[] Strings={"214124","214124","214124","214124","214124"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newcarinsurance,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        insurance_lv=(ListView) view.findViewById(R.id.insurance_lv);
        insurance_lv.setAdapter(new CarOwernInsuranceAdapter(getActivity(),R.layout.item_insurance,Strings));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void init() {

    }
}
