package com.xolo.gzqc.ui.activity.supplier;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.SupplierGoodsItemAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.SupplierGoods;
import com.xolo.gzqc.bean.child.SupplierMainGoods;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.PhotoUtils;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 供应商首页
 * Created by Administrator on 2016/12/9.
 */
public class SupplierActivity extends BaseActivity {
    @BindView(R.id.order_centent)
    LinearLayout order_centent;
    @BindView(R.id.goods_sell)
    LinearLayout goods_sell;
    @BindView(R.id.shop_fitment)
    LinearLayout shop_fitment;
    @BindView(R.id.notice)
    LinearLayout notice;
    @BindView(R.id.supplier_gv)
    GridView supplier_gv;
    SupplierGoodsItemAdapter supplierGoodsItemAdapter;
    @BindView(R.id.titleview)
    TitleView titleview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_main);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    List<SupplierMainGoods> supplierMainGoodses = new ArrayList<>();

    void getData() {
        LoadDialog.show(mContext);
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("getgoodslist");
        HttpUtil.getInstance().post(requestParams, ORMBean.SupplierMainGoodsBean.class, new HttpUtil.HttpCallBack<ORMBean.SupplierMainGoodsBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.SupplierMainGoodsBean result) {
                if (result.getRes().equals("1")) {
                    supplierMainGoodses.clear();
                    supplierMainGoodses.addAll(result.getData());
                    supplierGoodsItemAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showShort(SupplierActivity.this, result.getMsg());
                }
                LoadDialog.dismiss(mContext);

            }
        });
    }

    private void initView() {
        titleview.setLeftGone();
        supplierGoodsItemAdapter = new SupplierGoodsItemAdapter(SupplierActivity.this, R.layout.item_supplier_main, supplierMainGoodses);
        supplier_gv.setAdapter(supplierGoodsItemAdapter);
        supplier_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SupplierActivity.this, GoodsSellActivity.class);
                intent.putExtra("goodsid", supplierGoodsItemAdapter.getItem(position).getC_goodsinfor_id());
                startActivityForResult(intent, 0x003);
            }
        });
        supplier_gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                LoadDialog.show(SupplierActivity.this);
                RequestParams requestParams = creatParams("deletegoods");
                requestParams.addBodyParameter("goodsid", supplierMainGoodses.get(position).getC_goodsinfor_id());
                HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        LoadDialog.dismiss(mContext);
                    }

                    @Override
                    public void onSuccess(BaseBean result) {
                        if (result.getRes().equals("1")) {
                            supplierMainGoodses.remove(position);
                            supplierGoodsItemAdapter.notifyDataSetChanged();
                        }
                        ToastUtil.showShort(SupplierActivity.this, result.getMsg());
                        LoadDialog.dismiss(mContext);
                    }
                });

                return false;
            }
        });
    }

    @OnClick({R.id.order_centent, R.id.goods_sell, R.id.shop_fitment, R.id.notice})
    void OnClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.order_centent:
                intent = new Intent(SupplierActivity.this, OrderCentreActivity.class);
                startActivity(intent);
                break;
            case R.id.goods_sell:
                intent = new Intent(SupplierActivity.this, GoodsSellActivity.class);
                startActivityForResult(intent, 0x003);
                break;
            case R.id.shop_fitment:
                intent = new Intent(SupplierActivity.this, ShopFitmentActivity.class);
                startActivity(intent);
                break;
            case R.id.notice:
                intent = new Intent(SupplierActivity.this, NoticeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 0x003) {
            getData();
        }
    }

    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
