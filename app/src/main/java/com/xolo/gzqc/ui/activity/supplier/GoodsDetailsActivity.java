package com.xolo.gzqc.ui.activity.supplier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CommonAdapter;
import com.xolo.gzqc.adapter.SearchDialogAdapter;
import com.xolo.gzqc.adapter.SupplierOrderChildAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.SelectShipper;
import com.xolo.gzqc.bean.child.SupplierGoods;
import com.xolo.gzqc.bean.child.SupplierGoodsChild;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 供应商发货
 * Created by Administrator on 2016/12/16.
 */
public class GoodsDetailsActivity extends BaseActivity {
    ScrollListView listView;
    SupplierGoods goods;
    SupplierOrderChildAdapter adpter;
    TextView orderid, order_time, address, name, phone;
    LinearLayout send_layout;
    Button send_bt;
    RelativeLayout et_img_layout;
    List<SelectShipper> seletelist = new ArrayList<>();
    TextView maintain_et;
    EditText expressage_num, expressage_free;
    TextView totle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_supplier_send_goods);
        initView();
    }

    void getSelectShipperData() {
        LoadDialog.show(mContext);
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("selectshipper");
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.SelectShipperBean.class, new HttpUtil.HttpCallBack<ORMBean.SelectShipperBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.SelectShipperBean result) {
                if (result.getRes().equals("1")) {
                    backupsList.addAll(result.getData());
                    seletelist.addAll(result.getData());
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }

    String emscompany, shippercode;
    List<SelectShipper> backupsList=new ArrayList<>();
    private void initView() {
        expressage_free = (EditText) findViewById(R.id.expressage_free);
        expressage_num = (EditText) findViewById(R.id.expressage_num);
        maintain_et = (TextView) findViewById(R.id.maintain_et);
        et_img_layout = (RelativeLayout) findViewById(R.id.et_img_layout);
        maintain_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    creatSearchListDialog(GoodsDetailsActivity.this, "选择快递公司", seletelist, new SearchDialogAdapter.SearchDialogAdapterIface<SelectShipper>() {
                        @Override
                        public void setItemData(CommonAdapter.ViewHolder helper, SelectShipper item) {
                            helper.setText(R.id.item2, item.getName());
                        }

                        @Override
                        public void onItemClick(int position, SelectShipper item) {
                            maintain_et.setText(item.getName());
                            emscompany = item.getName();
                            shippercode = item.getCode();
                        }

                        @Override
                        public void afterTextChanged(String text, SearchDialogAdapter searchDialogAdapter) {
                            if (TextUtils.isEmpty(text)) {
                                searchDialogAdapter.clearAll();
                                seletelist.addAll(backupsList);
                                searchDialogAdapter.add(seletelist);
                            } else {
                                searchDialogAdapter.clearAll();
                                ArrayList<SelectShipper> selectShippers = new ArrayList<SelectShipper>();
                                for (SelectShipper selectShipper : backupsList) {
                                    if (selectShipper.getName().contains(text)) {
                                        selectShippers.add(selectShipper);
                                    }
                                }
                                searchDialogAdapter.add(selectShippers);
                            }
                        }

                        @Override
                        public void onDialogDismiss(SearchDialogAdapter searchDialogAdapter) {
                            searchDialogAdapter.clearAll();
                            seletelist.addAll(backupsList);
                            searchDialogAdapter.add(seletelist);
                        }
                    }).show();

            }
        });
        send_bt = (Button) findViewById(R.id.send_bt);
        send_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGoods();
            }
        });
        order_time = (TextView) findViewById(R.id.order_time);
        address = (TextView) findViewById(R.id.address);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        orderid = (TextView) findViewById(R.id.orderid);
        totle = (TextView) findViewById(R.id.totle);
        send_layout = (LinearLayout) findViewById(R.id.send_layout);
        goods = (SupplierGoods) getIntent().getSerializableExtra("goods");
        order_time.setText(goods.getOperatdate());
        address.setText(goods.getAddress());
        name.setText(goods.getLink_name());
        totle.setText(goods.getTotal());
        phone.setText(goods.getLink_tel());
        orderid.setText(goods.getOrder_number());
        if (goods.getStatus().equals("20")) {
            getSelectShipperData();
            send_layout.setVisibility(View.VISIBLE);
        } else {
            send_layout.setVisibility(View.GONE);
        }
        adpter = new SupplierOrderChildAdapter(GoodsDetailsActivity.this, R.layout.item_suppliner_goods_detils, goods.getGoods());
        listView = (ScrollListView) findViewById(R.id.orderid_list);
        listView.setAdapter(adpter);
    }

    void setGoods() {
        if (TextUtils.isEmpty(emscompany)) {
            ToastUtil.showShort(GoodsDetailsActivity.this, "请选择物流公司");
            return;
        }
        if (TextUtils.isEmpty(expressage_num.getText().toString())) {
            ToastUtil.showShort(GoodsDetailsActivity.this, "请输入快递单号");
            return;
        }
        if (TextUtils.isEmpty(expressage_free.getText().toString())) {
            ToastUtil.showShort(GoodsDetailsActivity.this, "请输入运费");
            return;
        }
        LoadDialog.show(mContext);
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("deliverorder");
        requestParams.addBodyParameter("ordermid", goods.getC_order_m_id());
        requestParams.addBodyParameter("ordernum", goods.getOrder_number());
        requestParams.addBodyParameter("emscompany", emscompany);
        requestParams.addBodyParameter("emsno", expressage_num.getText().toString());
        requestParams.addBodyParameter("fee", expressage_free.getText().toString());
        requestParams.addBodyParameter("shippercode", shippercode);
        HttpUtil.getInstance().post(requestParams, ORMBean.SupplierMainGoodsBean.class, new HttpUtil.HttpCallBack<ORMBean.SupplierMainGoodsBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.SupplierMainGoodsBean result) {
                ToastUtil.showLong(GoodsDetailsActivity.this, result.getMsg());
                LoadDialog.dismiss(mContext);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
}
