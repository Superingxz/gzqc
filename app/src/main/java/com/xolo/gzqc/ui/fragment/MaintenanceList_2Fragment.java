package com.xolo.gzqc.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.OfferExpandableAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.postJson.Offer;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.PayDetailsActivity;
import com.xolo.gzqc.ui.view.CustomExpandableListView;
import com.xolo.gzqc.utils.HttpUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 维修清单-未付结账
 */
public class MaintenanceList_2Fragment extends BaseFragment {


    @BindView(R.id.lv)
    CustomExpandableListView lv;

    private List<Offer> list_offer = new ArrayList<>();

    private String receive_id;

    private OfferExpandableAdapter adapter;

    public MaintenanceList_2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        PayDetailsActivity activity = (PayDetailsActivity) context;
        receive_id = activity.getCarInfo().getBf_receive_id();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maintenance_list_2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        initLv();
    }

    @Override
    public void load() {
        super.load();
        quoteallorderm();
    }

    private void initLv() {
        adapter = new OfferExpandableAdapter(list_offer, mContext);

        lv.setAdapter(adapter);

//        lv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//
//                List<Part> repairlists = list_offer.get(groupPosition).getRepairlist();
//                if (repairlists.size() == 0) {
//                    quotereparibymaintenanceid(groupPosition, list_offer.get(groupPosition).getBf_quoted_priced_id());
//                }
//                return false;
//            }
//        });

    }

    /**
     * 2-55-4 报价接口--根据接车表id查询出所有的报价信息（维修项目清单以及配件清单） actiondo:quoteallorderm,
     * 接车表ID:bf_receive_car_id,userid：978691E5-E3AC-4A26-96F7-EFDF28910712
     */
    private void quoteallorderm() {
        RequestParams params = creatParams("quoteallorderm");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("bf_receive_car_id", receive_id);

        mLoad.show(mContent);
        HttpUtil.getInstance().post(params, ORMBean.OfferBean.class, new HttpUtil.HttpCallBack<ORMBean.OfferBean>() {
            @Override
            public void onSuccess(ORMBean.OfferBean result) {
                if (result.getRes().equals("1")) {
                    List<Offer> data = result.getData();

                    list_offer.clear();
                    list_offer.addAll(data);

                    adapter.notifyDataSetChanged();
                } else {
                    list_offer.clear();
                    adapter.notifyDataSetChanged();
                }

                mLoad.dismiss(mContent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContent);
            }
        });
    }




}
