package com.xolo.gzqc.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.User;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.ItemCoEORemind;
import com.xolo.gzqc.ui.view.ItemCoRemind;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/10/10.
 */
public class CoRemindFragment extends LazyFragment {
    static CoRemindFragment coRemindFragment;
    View view;
    LinearLayout remind_contont;
    TitleView titleview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_co_remind, null);
    }

    private void initView(View view) {
        titleview = (TitleView) view.findViewById(R.id.titleview);
        titleview.setLeftGone();
        remind_contont = (LinearLayout) view.findViewById(R.id.remind_contont);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public static CoRemindFragment getCoRemindFragment() {
        if (coRemindFragment == null) {
            coRemindFragment = new CoRemindFragment();
        }
        return coRemindFragment;
    }


    void initData() {
        LoadDialog.show(getActivity());
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("getmywarninfo");
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoCoEngineOilRemindBean.class, new HttpUtil.HttpCallBack<ORMBean.CoCoEngineOilRemindBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(getActivity());
            }

            @Override
            public void onSuccess(ORMBean.CoCoEngineOilRemindBean result) {
                if (result.getRes().equals("1")) {
                    for (int i = 0; i < result.getData().size(); i++) {
                        ItemCoEORemind itemCoRemind = new ItemCoEORemind(mContext);
                        itemCoRemind.setPhone(MainCarOwnerFragment.coMaintainInfo.getPhone());
                        itemCoRemind.setData(i + 1, result.getData().get(i));
                        remind_contont.addView(itemCoRemind);
                    }
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                }
                LoadDialog.dismiss(getActivity());
            }
        });
    }



    @Override
    protected void loadData() {
        if (needLoad()) {
            initData();
            mHasLoadedOnce = true;
        }
    }

    @Override
    protected void init() {

    }
}
