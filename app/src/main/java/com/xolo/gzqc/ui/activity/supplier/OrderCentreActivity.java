package com.xolo.gzqc.ui.activity.supplier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.io.Serializable;

/**
 * 订单中心
 * Created by Administrator on 2016/12/13.
 */
public class OrderCentreActivity extends BaseActivity {
    EditText order_name, goods_no;
    OrderFragment orderFragment, orderFragment1, orderFragment2, orderFragment3;
    TabView tab_view;
    TextView shart_time, end_time;
    Button submit;
    ImageView delete_img1, delete_img2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_order_centre);
        initView();
    }

    void initView() {
        delete_img1 = (ImageView) findViewById(R.id.delete_img1);
        delete_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shart_time.setText("");

            }
        });
        delete_img2 = (ImageView) findViewById(R.id.delete_img2);
        delete_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_time.setText("");
            }
        });
        submit = (Button) findViewById(R.id.submit);
        tab_view = (TabView) findViewById(R.id.tab_view);
        order_name = (EditText) findViewById(R.id.order_name);
        goods_no = (EditText) findViewById(R.id.goods_no);
        shart_time = (TextView) findViewById(R.id.shart_time);
        end_time = (TextView) findViewById(R.id.end_time);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        shart_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatDateDialog(shart_time).show();
            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatDateDialog(end_time).show();
            }
        });
        orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "");
        orderFragment.setArguments(bundle);

        Bundle bundle1 = new Bundle();
        bundle1.putString("type", "20");
        orderFragment1 = new OrderFragment();
        orderFragment1.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putString("type", "30");
        orderFragment2 = new OrderFragment();
        orderFragment2.setArguments(bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putString("type", "40");
        orderFragment3 = new OrderFragment();
        orderFragment3.setArguments(bundle3);
        tab_view.setTitle(new String[]{"全部", "待发货", "已发货", "待退货"});
        tab_view.setFragments(new Fragment[]{orderFragment, orderFragment1, orderFragment2, orderFragment3});
        orderFragment.setChangeData(new OrderFragment.ChangeData() {
            @Override
            public void change() {
                orderFragment.getData("");
                orderFragment1.getData("20");
            }
        });
        orderFragment1.setChangeData(new OrderFragment.ChangeData() {
            @Override
            public void change() {
                orderFragment.getData("");
                orderFragment1.getData("20");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x002 && resultCode == Activity.RESULT_OK) {
            orderFragment.getData("");
            orderFragment1.getData("20");
        }

    }
    void getData() {
        RequestParams requestParams = creatParams("selectorderbygoodsname");
        requestParams.addBodyParameter("goodsname", order_name.getText().toString());
        requestParams.addBodyParameter("begindate", shart_time.getText().toString());
        requestParams.addBodyParameter("enddate", end_time.getText().toString());
        HttpUtil.getInstance().post(requestParams, ORMBean.SupplierOrderListBean.class, new HttpUtil.HttpCallBack<ORMBean.SupplierOrderListBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onSuccess(ORMBean.SupplierOrderListBean result) {
                if (result.getRes().equals("1")) {
                    Intent intent = new Intent(OrderCentreActivity.this, SeleteOrderActivity.class);
                    intent.putExtra("goods", (Serializable) result.getData());
                    startActivityForResult(intent, 0x002);
                } else {
                    ToastUtil.showShort(OrderCentreActivity.this, result.getMsg());
                }
            }
        });

    }
}
