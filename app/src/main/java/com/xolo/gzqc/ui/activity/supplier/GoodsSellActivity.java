package com.xolo.gzqc.ui.activity.supplier;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.SupplierSellGoods;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品上架
 * Created by Administrator on 2016/12/9.
 */
public class GoodsSellActivity extends BaseActivity {
    GoodsSellParameterFragemnt goodsSellParameterFragemnt;
    OtherDesciptionFragment otherDesciptionFragment;
    RelevancePhotoFragment relevancePhotoFragment;
    private String[] title = {"产品基本参数", "相关图片", "其他说明"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_sell);
        // ButterKnife.bind(this);
        initView();
        getGoods();
    }


    private void initView() {
        TabView tab = (TabView) findViewById(R.id.tab);
        goodsSellParameterFragemnt = new GoodsSellParameterFragemnt();
        otherDesciptionFragment = new OtherDesciptionFragment();
        relevancePhotoFragment = new RelevancePhotoFragment();
        goodsSellParameterFragemnt.setSaveGoodsResultIfce(new GoodsSellParameterFragemnt.SaveGoodsResultIfce() {
            @Override
            public void retrunGoodsId(String id) {
                otherDesciptionFragment.setGoodsid(id);
                relevancePhotoFragment.setGoodsid(id);
                setResult(Activity.RESULT_OK);
            }
        });
        tab.setTitle(title);
        tab.setFragments(new Fragment[]{goodsSellParameterFragemnt, relevancePhotoFragment, otherDesciptionFragment});

    }

    void getGoods() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("goodsid"))) {
            LoadDialog.show(mContext);
            RequestParams requestParams = creatParams("getgoodsinfo");
            requestParams.addBodyParameter("goodsid", getIntent().getStringExtra("goodsid"));
            HttpUtil.getInstance().post(requestParams, ORMBean.SupplierSellGoodsBean.class, new HttpUtil.HttpCallBack<ORMBean.SupplierSellGoodsBean>() {
                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                   LoadDialog.dismiss(mContext);
                }

                @Override
                public void onSuccess(ORMBean.SupplierSellGoodsBean result) {
                    if (result.getRes().equals("1")) {
                        if (null != result.getData() && null != result.getData().get(0)) {
                            goodsSellParameterFragemnt.setData(result.getData().get(0));
                            relevancePhotoFragment.setData(result.getData().get(0));
                            otherDesciptionFragment.setData(result.getData().get(0));
                        }
                    }
                    ToastUtil.showShort(GoodsSellActivity.this, result.getMsg());
                    LoadDialog.dismiss(mContext);
                }
            });
        }

    }
}
