package com.xolo.gzqc.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwner_add_MaintainAdapter;
import com.xolo.gzqc.adapter.ExpandableListViewAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_Add_Maintain;
import com.xolo.gzqc.bean.child.CarOwner_CarInfo;
import com.xolo.gzqc.bean.child.CarnoInfo;
import com.xolo.gzqc.bean.child.ChildHistroyPrice;
import com.xolo.gzqc.bean.child.CoAccessories;
import com.xolo.gzqc.bean.child.CoHistroyPrice;
import com.xolo.gzqc.bean.child.CoProject;
import com.xolo.gzqc.bean.child.User;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.CoHistoryLinearLayout;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 维修历史
 * Created by Administrator on 2016/9/27.
 */
public class CoHistoryFragment extends LazyFragment {
    LinearLayout co_hi_view;
    List<CoProject> coProjects = new ArrayList<>();
    List<CoHistroyPrice> coHistroyPrices = new ArrayList<>();
    View view;
    String carno;

    public void setCarno(String carno, User user) {
        this.carno = carno;
        this.user = user;
        mHasLoadedOnce = false;
        if (needLoad()) {
            getHistroyData();
            mHasLoadedOnce = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view=inflater.inflate(R.layout.fragment_co_history_price, null);
            initView(view);
        }
        return view;
    }

    void initView(View view) {
        co_hi_view = (LinearLayout) view.findViewById(R.id.co_hi_view);
        ScrollListView lv = (ScrollListView) view.findViewById(R.id.lv);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    //获取历史报价列表 第几次报价
    void getHistroyData() {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getoldquoteordermlist");
        requestParams.addBodyParameter("carno", carno);
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoHistroyBean.class, new HttpUtil.HttpCallBack<ORMBean.CoHistroyBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoHistroyBean result) {
                if (result.getRes().equals("1")) {
                    co_hi_view.removeAllViews();
                    coHistroyPrices = result.getData();
                    for (int i = 0; i < coHistroyPrices.size(); i++) {
                        getSecondData(i + 1);
                    }
                    co_hi_view.setVisibility(View.VISIBLE);
                } else {
                    co_hi_view.setVisibility(View.GONE);
                }
                LoadDialog.dismiss(mContext);
            }
        });
    }

    //获取三级目录数据
    void getThreeData(final int position, String bf_quoted_priced_id) {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("quotereparibymaintenanceid");
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("bf_quoted_priced_id", bf_quoted_priced_id);
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
                    childHistroyPrices.get(position).setCoAccessories(result.getData());
                } else {
                    // ToastUtil.showLong(getContext(), result.getMsg());
                }
                if (position + 1 == childHistroyPrices.size()) {
                    for (int i = 0; i < coHistroyPrices.size(); i++) {
                        CoHistoryLinearLayout coHistoryLinearLayout = new CoHistoryLinearLayout(getActivity());
                        coHistoryLinearLayout.initData(coHistroyPrices.get(i));
                        coHistoryLinearLayout.setChildHistroyPrices(coHistroyPrices.get(i).getCoProjects());
                        co_hi_view.addView(coHistoryLinearLayout);
                        if (i != coHistroyPrices.size() - 1) {
                            co_hi_view.addView(View.inflate(getActivity(), R.layout.view_line, null));
                        }
                    }
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }

    List<ChildHistroyPrice> childHistroyPrices = new ArrayList<>();

    //获取二级目录数据
    void getSecondData(final int times_cnt) {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getoldquoteorderm");
        requestParams.addBodyParameter("carno", carno);
        requestParams.addBodyParameter("is_deleted", "0");
        requestParams.addBodyParameter("times_cnt", String.valueOf(times_cnt));//第几次报价
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("owen_id", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoHistroyChildBean.class, new HttpUtil.HttpCallBack<ORMBean.CoHistroyChildBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }

            @Override
            public void onSuccess(ORMBean.CoHistroyChildBean result) {
                if (result.getRes().equals("1")) {
                    childHistroyPrices = result.getData();
                    coHistroyPrices.get(times_cnt - 1).setCoProjects(childHistroyPrices);
                    for (int i = 0; i < childHistroyPrices.size(); i++) {
                        getThreeData(i, childHistroyPrices.get(i).getBf_quoted_priced_id());
                    }

                } else {
                    //   ToastUtil.showLong(getContext(), result.getMsg());

                }
                LoadDialog.dismiss(mContext);
            }
        });

    }

    void getDataChild(final int position, String bf_quoted_priced_id) {
        if (TextUtils.isEmpty(bf_quoted_priced_id)) {
            return;
        }
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("quotereparibymaintenanceid");
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("bf_quoted_priced_id", bf_quoted_priced_id);
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
                    result.getData().add(0, new CoAccessories("配件名称", "单价", "数量", "金额"));
                    coProjects.get(position).setCoAccessories(result.getData());
                } else {
                    // ToastUtil.showLong(getContext(), result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });
    }

    @Override
    protected void loadData() {
        if (needLoad()) {
            if (!TextUtils.isEmpty(carno) && user != null) {
                getHistroyData();
                mHasLoadedOnce = true;
            }
        }
    }

    @Override
    protected void init() {

    }
}
