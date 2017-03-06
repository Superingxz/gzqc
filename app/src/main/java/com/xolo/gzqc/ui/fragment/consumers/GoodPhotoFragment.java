package com.xolo.gzqc.ui.fragment.consumers;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.GoodInfo;
import com.xolo.gzqc.ui.activity.consumers.GoodsInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品详情--图文详情
 */
public class GoodPhotoFragment extends BaseFragment implements GoodsInfoActivity.GoodInfoCallBack {


    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.iv3)
    ImageView iv3;

    public GoodPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_good_photo, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {

    }

    @Override
    public void readInfo(GoodInfo goodInfo) {
        if (!TextUtils.isEmpty(goodInfo.getPic_1())){
            loadPhoto(goodInfo.getPic_1(),iv1);
        }
        if (!TextUtils.isEmpty(goodInfo.getPic_1())){
            loadPhoto(goodInfo.getPic_2(),iv2);
        }
        if (!TextUtils.isEmpty(goodInfo.getPic_1())){
            loadPhoto(goodInfo.getPic_3(),iv3);
        }
    }
}
