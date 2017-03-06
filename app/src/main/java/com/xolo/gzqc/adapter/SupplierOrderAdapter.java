package com.xolo.gzqc.adapter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.child.SupplierGoods;
import com.xolo.gzqc.ui.activity.supplier.GoodsDetailsActivity;
import com.xolo.gzqc.ui.activity.supplier.LogisticsActivity;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */
public class SupplierOrderAdapter extends CommonAdapter<SupplierGoods> {
    Context context;

    public SupplierOrderAdapter(Context context, int itemLayoutId, List<SupplierGoods> mDatas) {
        super(context, itemLayoutId, mDatas);
        this.context = context;
    }

    public interface SupplierOrderAdapterIface {
        void Pay(SupplierGoods item);

        void sendGoods(SupplierGoods item);
    }

    SupplierOrderAdapterIface supplierOrderAdapterIface;

    public SupplierOrderAdapterIface getSupplierOrderAdapterIface() {
        return supplierOrderAdapterIface;
    }

    public void setSupplierOrderAdapterIface(SupplierOrderAdapterIface supplierOrderAdapterIface) {
        this.supplierOrderAdapterIface = supplierOrderAdapterIface;
    }

    @Override
    public void convert(ViewHolder helper, final SupplierGoods item) {
        helper.setText(R.id.orderid, "订单号:" + item.getOrder_number());
        helper.setText(R.id.order_time, "下单时间:" + item.getOperatdate());
        helper.setText(R.id.totle_price, "合计:" + item.getTotal());
        if (TextUtils.isEmpty(item.getOut_time())) {
            helper.getView(R.id.out_time).setVisibility(View.GONE);
        } else {
            helper.setText(R.id.out_time, "发货时间:" + item.getOut_time());
        }

        ScrollListView listView = (ScrollListView) helper.getView(R.id.lv_chlid);
        SupplierOrderChildAdapter supplierOrderChildAdapter = new SupplierOrderChildAdapter(context, R.layout.item_suppliner_goods_detils, item.getGoods());
        listView.setAdapter(supplierOrderChildAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                statTIvity(item);
            }
        });
        //订单状态：10为待付款，20为已付款待发货，30为已发货待收货，40为退款中，50为交易成功，60为交易关闭
        Button button = (Button) helper.getView(R.id.send_goods);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getStatus().equals("10")) {
                    if (supplierOrderAdapterIface != null) {
                        supplierOrderAdapterIface.Pay(item);
                    }
                } else {
                    statTIvity(item);
                }
            }
        });
        button.setVisibility(View.VISIBLE);
        if (item.getStatus().equals("10")) {
            helper.setText(R.id.order_staus, "待付款");
            button.setText("付款");
        } else if (item.getStatus().equals("20")) {
            helper.setText(R.id.order_staus, "待发货");
            button.setText("发货");
        } else if (item.getStatus().equals("30")) {
            helper.setText(R.id.order_staus, "待收货");
            button.setText("查看物流");
        } else if (item.getStatus().equals("40")) {
            helper.setText(R.id.order_staus, "退款中");
            button.setText("同意退款");
        } else if (item.getStatus().equals("50")) {
            helper.setText(R.id.order_staus, "交易成功");
            button.setVisibility(View.GONE);
        } else if (item.getStatus().equals("60")) {
            helper.setText(R.id.order_staus, "交易关闭");
            button.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.GONE);
        }
        helper.getView(R.id.root_view_goods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statTIvity(item);
            }


        });

    }

    private void statTIvity(SupplierGoods item) {
        if (item.getStatus().equals("20")) {
            supplierOrderAdapterIface.sendGoods(item);
        } else if (item.getStatus().equals("30")) {
            Intent intent = new Intent(context, LogisticsActivity.class);
            intent.putExtra("goods", (Serializable) item);
            context.startActivity(intent);
        } else if (item.getStatus().equals("40")) {
            ToastUtil.showShort(context, "退款功能正在开发中...");
        }
    }
}
