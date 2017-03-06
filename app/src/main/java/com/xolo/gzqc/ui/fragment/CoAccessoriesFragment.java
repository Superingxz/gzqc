package com.xolo.gzqc.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwner_CarInfoAdapter;
import com.xolo.gzqc.adapter.CarOwner_add_MaintainAdapter;
import com.xolo.gzqc.adapter.CoAccessoriesAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_Add_Maintain;
import com.xolo.gzqc.bean.child.CarOwner_CarInfo;
import com.xolo.gzqc.bean.child.CarnoInfo;
import com.xolo.gzqc.bean.child.CoAccessories;
import com.xolo.gzqc.bean.child.CoSheduli;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 配件清单
 * Created by Administrator on 2016/9/27.
 */
public class CoAccessoriesFragment extends LazyFragment {
    ScrollListView lv, lv_inventory;
    List<CoAccessories> listco;
    CoAccessoriesAdapter coAdapter;
    View view;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    List<CarOwner_CarInfo> carlist;
    List<String> liststring = new ArrayList<String>();
    Dialog dialog;
    String bf_receive_car_id;
    public void setBf_receive_car_id(String bf_receive_car_id) {
        this.bf_receive_car_id = bf_receive_car_id;
            getData();

    }
    private void initView(View view) {
        lv_inventory = (ScrollListView) view.findViewById(R.id.lv_inventory);
        lv = (ScrollListView) view.findViewById(R.id.lv);
        listco = new ArrayList<>();
        coAdapter = new CoAccessoriesAdapter(getActivity(), R.layout.item_co_access, listco);
        lv_inventory.setAdapter(coAdapter);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            user = SPManager.getUser(mContext);
            view = inflater.inflate(R.layout.fragment_co_accessories_list, null);
            initView(view);
        }
        return view;
    }

    @Override
    protected void loadData() {
        if (needLoad()) {
            mHasLoadedOnce = true;
        }
    }

    @Override
    protected void init() {

    }
    //获取配件清单数据
    void getData() {
        if (bf_receive_car_id == null) {
            return;
        }
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("quoterepariorderm");
        requestParams.addBodyParameter("bf_receive_car_id", bf_receive_car_id);
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("is_deleted", "0");
        requestParams.addBodyParameter("owen_id", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoAccessoriesBean.class, new HttpUtil.HttpCallBack<ORMBean.CoAccessoriesBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoAccessoriesBean result) {
                if (result.getRes().equals("1")) {
                    listco.clear();
                    listco.add(new CoAccessories("配件名称", "单价", "数量", "总价"));
                    listco.addAll(result.getData());
                    coAdapter.notifyDataSetChanged();
                    lv_inventory.setVisibility(View.VISIBLE);
                } else {
                    lv_inventory.setVisibility(View.GONE);
                }

                LoadDialog.dismiss(mContext);
            }
        });

    }
}
