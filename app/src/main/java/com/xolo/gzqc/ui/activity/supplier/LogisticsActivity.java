package com.xolo.gzqc.ui.activity.supplier;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.LogisticsAdapter;
import com.xolo.gzqc.adapter.SupplierGoodsItemAdapter;
import com.xolo.gzqc.adapter.SupplierOrderChildAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.SupplierGoods;
import com.xolo.gzqc.bean.child.SupplierGoodsChild;
import com.xolo.gzqc.bean.child.Traces;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 物流信息查询
 * Created by Administrator on 2016/11/25.
 */
public class LogisticsActivity extends BaseActivity {
    ScrollListView lv_order;
    ListView logistics_info_list;
    TextView orderid, order_time, out_time, companyname, num;
    SupplierGoods supplierGoods;
    LogisticsAdapter logisticsAdapter;
    List<Traces> traces = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_logistics);
        initView();
    }

    void initView() {
        supplierGoods = (SupplierGoods) getIntent().getSerializableExtra("goods");
        num = (TextView) findViewById(R.id.num);
        companyname = (TextView) findViewById(R.id.companyname);
        orderid = (TextView) findViewById(R.id.orderid);
        order_time = (TextView) findViewById(R.id.order_time);
        out_time = (TextView) findViewById(R.id.out_time);
        lv_order = (ScrollListView) findViewById(R.id.lv_order);
        logistics_info_list = (ListView) findViewById(R.id.logistics_info_list);
        if (null != supplierGoods) {
            SupplierOrderChildAdapter supplierOrderChildAdapter = new SupplierOrderChildAdapter(LogisticsActivity.this,
                    R.layout.item_suppliner_goods_detils, supplierGoods.getGoods());
            orderid.setText("订单号:" + supplierGoods.getOrder_number());
            order_time.setText("下单时间:" + supplierGoods.getOperatdate());
            if (TextUtils.isEmpty(supplierGoods.getOut_time())) {
                out_time.setVisibility(View.GONE);
            } else {
                out_time.setText("发货时间:" + supplierGoods.getOut_time());
            }
            companyname.setText(supplierGoods.getEmscompany());
            num.setText(supplierGoods.getEmsno());
            lv_order.setAdapter(supplierOrderChildAdapter);
            logisticsAdapter = new LogisticsAdapter(LogisticsActivity.this, R.layout.item_suppliner_logistics, traces);
            logistics_info_list.setAdapter(logisticsAdapter);
            getData();
        }

    }

    void getData() {
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("getlogisticsinfo");
        requestParams.addBodyParameter("shipper_code",supplierGoods.getShipper_code());
        requestParams.addBodyParameter("emsno", supplierGoods.getEmsno());

        HttpUtil.getInstance().post(requestParams, ORMBean.LogisticsBean.class, new HttpUtil.HttpCallBack<ORMBean.LogisticsBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onSuccess(ORMBean.LogisticsBean result) {
                if (result.getRes().equals("1")) {
                    logisticsAdapter.add(result.getData().get(0).getTraces());
                } else {
                    ToastUtil.showShort(LogisticsActivity.this, result.getMsg());
                }
            }
        });
    }
}
