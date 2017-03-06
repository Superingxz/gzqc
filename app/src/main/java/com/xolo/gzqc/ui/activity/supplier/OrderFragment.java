package com.xolo.gzqc.ui.activity.supplier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.SupplierOrderAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.SupplierGoods;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.fragment.LazyFragment;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单中心fragment
 * Created by Administrator on 2016/12/16.
 */
public class OrderFragment extends LazyFragment {
    View view;
    ListView lv;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void loadData() {
        if (needLoad()) {
            getData(getArguments().getString("type"));
            mHasLoadedOnce = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_supplier_order, null);
            initView(view);
        }
        return view;
    }

    List<SupplierGoods> list = new ArrayList<>();
    SupplierOrderAdapter supplierOrderAdapter;

    void initView(View view) {
        supplierOrderAdapter = new SupplierOrderAdapter(getActivity(), R.layout.item_supplier_order, list);
        supplierOrderAdapter.setSupplierOrderAdapterIface(new SupplierOrderAdapter.SupplierOrderAdapterIface() {
            @Override
            public void Pay(final SupplierGoods item) {
                LoadDialog.show(getContext());
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
                            for (SupplierGoods supplierGoods : list) {
                                if (supplierGoods.getC_order_m_id().equals(item.getC_order_m_id())) {
                                    supplierGoods.setStatus("20");
                                    supplierOrderAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                        ToastUtil.showShort(getContext(), result.getMsg());
                        LoadDialog.dismiss(mContext);
                    }
                });
            }

            @Override
            public void sendGoods(SupplierGoods item) {
                Intent intent = new Intent(getActivity(), GoodsDetailsActivity.class);
                intent.putExtra("goods", (Serializable) item);
                startActivityForResult(intent, 0x002);
            }
        });
        lv = (ListView) view.findViewById(R.id.lv);
        lv.setAdapter(supplierOrderAdapter);

    }

    ChangeData changeData;

    public ChangeData getChangeData() {
        return changeData;
    }

    public void setChangeData(ChangeData changeData) {
        this.changeData = changeData;
    }

    public interface ChangeData {
        void change();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x002 && resultCode == Activity.RESULT_OK && getArguments().getString("type").equals("20")) {
            changeData.change();
        } else if (requestCode == 0x002 && resultCode == Activity.RESULT_OK && TextUtils.isEmpty(getArguments().getString("type"))) {
            changeData.change();
        }
    }

    public void getData(String type) {
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("selectorderbystatus");
        //	订单状态：status=20（待发货）；status=30（已发货）；status=40（待退款）
        requestParams.addBodyParameter("orderstatus", type);
        HttpUtil.getInstance().post(requestParams, ORMBean.SupplierOrderListBean.class, new HttpUtil.HttpCallBack<ORMBean.SupplierOrderListBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onSuccess(ORMBean.SupplierOrderListBean result) {
                if (result.getRes().equals("1")) {
                    list.clear();
                    list.addAll(result.getData());
                    supplierOrderAdapter.notifyDataSetChanged();
                } else {
                    list.clear();
                    supplierOrderAdapter.notifyDataSetChanged();
                    ToastUtil.showShort(getActivity(), result.getMsg());
                }
            }
        });
    }

    @Override
    protected void init() {

    }
}
