package com.xolo.gzqc.ui.activity.supplier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ListView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.SupplierOrderAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.child.SupplierGoods;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/17.
 */
public class SeleteOrderActivity extends BaseActivity {
    ListView lv;
    List<SupplierGoods> supplierGoodsList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selete_order);
        supplierGoodsList = (List<SupplierGoods>) getIntent().getSerializableExtra("goods");
        lv = (ListView) findViewById(R.id.lv);
        final SupplierOrderAdapter supplierOrderAdapter = new SupplierOrderAdapter(SeleteOrderActivity.this, R.layout.item_supplier_order, supplierGoodsList);
        supplierOrderAdapter.setSupplierOrderAdapterIface(new SupplierOrderAdapter.SupplierOrderAdapterIface() {
            @Override
            public void Pay(final SupplierGoods item) {
                LoadDialog.show(SeleteOrderActivity.this);
                RequestParams requestParams = creatParams("orderpaysuccess");
                requestParams.addBodyParameter("orderid", item.getC_order_m_id());
                HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        LoadDialog.dismiss(mContext);
                    }

                    @Override
                    public void onSuccess(BaseBean result) {
                        if (result.getRes().equals("1")) {
                            for (SupplierGoods supplierGoods : supplierGoodsList) {
                                if (supplierGoods.getC_order_m_id().equals(item.getC_order_m_id())) {
                                    supplierGoods.setStatus("20");
                                    supplierOrderAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                        ToastUtil.showShort(SeleteOrderActivity.this, result.getMsg());
                        LoadDialog.dismiss(mContext);
                    }
                });
            }

            @Override
            public void sendGoods(SupplierGoods item) {
                Intent intent = new Intent(SeleteOrderActivity.this, GoodsDetailsActivity.class);
                intent.putExtra("goods", (Serializable) item);
                startActivityForResult(intent, 0x002);
            }
        });
        lv.setAdapter(supplierOrderAdapter);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x002 && resultCode == Activity.RESULT_OK)  {
            setResult(Activity.RESULT_OK);
        }
    }
}
