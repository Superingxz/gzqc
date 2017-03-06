package com.xolo.gzqc.ui.fragment.consumers;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.activity.consumers.AddressManagerActivity;
import com.xolo.gzqc.ui.view.ExEditText;
import com.xolo.gzqc.ui.view.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消费者个人中心
 */
public class ConsumersPersonFragment extends BaseFragment {


    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.eet_name)
    ExEditText eetName;
    @BindView(R.id.eet_sex)
    ExEditText eetSex;
    @BindView(R.id.eet_phone)
    ExEditText eetPhone;
    @BindView(R.id.eet_address)
    ExEditText eetAddress;

    public ConsumersPersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consumers_person, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        titleview.setLeftGone();
        eetName.setText(user.getName());
        eetSex.setText(user.getSex().equals("1")?"女":"男");
        eetPhone.setText(user.getPhone());
    }

    @OnClick(R.id.eet_address)
    public void onClick() {
        startActivity(new Intent(mContent, AddressManagerActivity.class));
    }
}
