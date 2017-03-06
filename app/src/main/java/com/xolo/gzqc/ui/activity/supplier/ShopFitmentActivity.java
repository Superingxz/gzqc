package com.xolo.gzqc.ui.activity.supplier;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.ShopFitmentAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.ShopFitment;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 店铺装修
 * Created by Administrator on 2016/12/8.
 */
public class ShopFitmentActivity extends BaseActivity {
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.save)
    Button save;
    ShopFitmentAdapter shopFitmentAdapter;
    List<ShopFitment> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_fitment);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void initView() {
        shopFitmentAdapter = new ShopFitmentAdapter(this, R.layout.item_shop_fitment, list);
        shopFitmentAdapter.setShopFitmentAdapterIface(new ShopFitmentAdapter.ShopFitmentAdapterIface() {
            @Override
            public void upOnClick(ShopFitment item, int postion) {
                setdata(true, item, postion);
            }

            @Override
            public void dnwnOClick(ShopFitment item, int postion) {
                setdata(false, item, postion);
            }
        });
        lv.addHeaderView(View.inflate(this, R.layout.shop_fitment_title, null));
        lv.setAdapter(shopFitmentAdapter);
    }

    private void setdata(boolean b, ShopFitment item, int postion) {
        List<GoodsOrder> goodslist = new ArrayList<GoodsOrder>();
        if (b) {
            ShopFitment shopFitment = shopFitmentAdapter.getData().get(postion - 1);
            ShopFitment shopFitment1 = shopFitmentAdapter.getData().get(postion);
            String Sort_num=shopFitment.getSort_num();
            String Sort_num1=shopFitment1.getSort_num();
            shopFitment.setSort_num(Sort_num1);
            shopFitment1.setSort_num(Sort_num);
            shopFitmentAdapter.getData().set(postion, shopFitment);
            shopFitmentAdapter.getData().set(postion - 1, shopFitment1);

        } else {
            ShopFitment shopFitment = shopFitmentAdapter.getData().get(postion + 1);
            ShopFitment shopFitment1 = shopFitmentAdapter.getData().get(postion);

            String Sort_num=shopFitment.getSort_num();
            String Sort_num1=shopFitment1.getSort_num();
            shopFitment.setSort_num(Sort_num1);
            shopFitment1.setSort_num(Sort_num);
            shopFitmentAdapter.getData().set(postion, shopFitment);
            shopFitmentAdapter.getData().set(postion + 1, shopFitment1);
        }
        shopFitmentAdapter.notifyDataSetChanged();
        for (ShopFitment shopFitment : shopFitmentAdapter.getData()) {
            GoodsOrder order = new GoodsOrder();
            order.setSort_num(shopFitment.getSort_num());
            order.setGoods_id(shopFitment.getC_goodsinfor_id());
            goodslist.add(order);
        }
        Gson gson = new Gson();
        Data data=new Data();
        data.setData(goodslist);
        string = gson.toJson(data);
    }

    String string;

    @OnClick({R.id.save})
    void OnClick(View view) {
        switch (R.id.save) {
            case R.id.save:
                seleteChange();
                break;
        }
    }

    class Data {
        List<GoodsOrder> data;

        public List<GoodsOrder> getData() {
            return data;
        }

        public void setData(List<GoodsOrder> data) {
            this.data = data;
        }
    }

    class GoodsOrder {
        String goods_id;
        String sort_num;

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getSort_num() {
            return sort_num;
        }

        public void setSort_num(String sort_num) {
            this.sort_num = sort_num;
        }
    }

    void seleteChange() {
        if (TextUtils.isEmpty(string)) {
            ToastUtil.showShort(ShopFitmentActivity.this, "请更换顺序后重试");
            return;
        }
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("updategoodsinfosort");
        requestParams.addBodyParameter("json_project", string);
        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(ShopFitmentActivity.this, result.getMsg());
            }
        });
    }

    void getData() {
        LoadDialog.show(mContext);
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("getgoodsinfobysupport");
        HttpUtil.getInstance().post(requestParams, ORMBean.ShopFimentBean.class, new HttpUtil.HttpCallBack<ORMBean.ShopFimentBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.ShopFimentBean result) {
                if (result.getRes().equals("1")) {
                    list.clear();
                    list.addAll(result.getData());
                    shopFitmentAdapter.notifyDataSetChanged();
                }
                LoadDialog.dismiss(mContext);
            }
        });
}
}
