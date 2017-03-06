package com.xolo.gzqc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CoCarOrderAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CoCarOrder;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 接车单
 * Created by Administrator on 2016/10/14.
 */
public class CoCarOrderFragment extends  LazyFragment{
    String bf_receive_id;
    CoCarOrderAdapter coCarOrderAdapter;
    List<CoCarOrder> coCarOrders=new ArrayList<>();
    ScrollListView scrollListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getActivity(),R.layout.fagment_co_order,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniView(view);
    }
    void iniView(View view){
        user = SPManager.getUser(mContext);
        scrollListView=(ScrollListView)view.findViewById(R.id.lv_inventory);
        coCarOrderAdapter =new CoCarOrderAdapter(getActivity(),R.layout.item_co_maintain_prograss,coCarOrders);
        bf_receive_id=getArguments().getString("bf_receive_id");
        scrollListView.setAdapter(coCarOrderAdapter);
    }
    @Override
    protected void loadData() {
        if(needLoad()){
            getData();
            mHasLoadedOnce=true;
        }
    }

    @Override
    protected void init() {

    }


    void getData() {
        LoadDialog.show(getActivity());
        RequestParams requestParams = creatParams("getreceivingcar");
        requestParams.addBodyParameter("bf_receive_car_id", bf_receive_id);
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoCarOrderBean.class, new HttpUtil.HttpCallBack<ORMBean.CoCarOrderBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
            @Override
            public void onSuccess(ORMBean.CoCarOrderBean result) {
                if (result.getRes().equals("1")) {
                    coCarOrders.clear();
                    coCarOrders.add(new CoCarOrder("类别","描述","照片"));
                    coCarOrders.addAll(result.getData());
                    coCarOrderAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showLong(getActivity(), result.getMsg());
                }

                LoadDialog.dismiss(getActivity());
            }
        });

    }
}
