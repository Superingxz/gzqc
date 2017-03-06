package com.xolo.gzqc.ui.fragment.consumers;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.GoodInfo;
import com.xolo.gzqc.ui.activity.consumers.GoodsInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品详情-商品参数
 */
public class ProductParametersFragment extends BaseFragment implements GoodsInfoActivity.GoodInfoCallBack {


    @BindView(R.id.tv_good_name)
    TextView tvGoodName;
    @BindView(R.id.tv_good_category)
    TextView tvGoodCategory;
    @BindView(R.id.tv_good_specifications)
    TextView tvGoodSpecifications;
    @BindView(R.id.tv_good_describe)
    TextView tvGoodDescribe;

    public ProductParametersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_parameters, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {

    }

    @Override
    public void readInfo(GoodInfo goodInfo) {
tvGoodName.setText(goodInfo.getGoods_name());
        tvGoodCategory.setText(goodInfo.getFirst_type());
        tvGoodSpecifications.setText(goodInfo.getSecond_type());
        tvGoodDescribe.setText(goodInfo.getGoods_description());
    }
}
