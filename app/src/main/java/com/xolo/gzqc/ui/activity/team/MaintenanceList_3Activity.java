package com.xolo.gzqc.ui.activity.team;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.PartHistroyExpandableAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.postJson.Offer;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.CustomExpandableListView;
import com.xolo.gzqc.utils.HttpUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaintenanceList_3Activity extends BaseActivity {

    @BindView(R.id.lv)
    CustomExpandableListView lv;

    private String  receive_id;

    private List<Offer> list_offer = new ArrayList<>();
    private PartHistroyExpandableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_list_3);
        ButterKnife.bind(this);

        receive_id = getIntent().getStringExtra("id");

        initLv();
        quoteallorderm();
    }

    private void initLv() {
        adapter = new PartHistroyExpandableAdapter(list_offer, mContext);
        lv.setAdapter(adapter);

    }

    /**
     * 2-55-4 报价接口--根据接车表id查询出所有的报价信息（维修项目清单以及配件清单） actiondo:quoteallorderm,
     * 接车表ID:bf_receive_car_id,userid：978691E5-E3AC-4A26-96F7-EFDF28910712
     */
    private void quoteallorderm() {
        RequestParams params = creatParams("quoteallorderm");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("bf_receive_car_id", receive_id);


        HttpUtil.getInstance().postLoading(mContext,params, ORMBean.OfferBean.class, new HttpUtil.HttpCallBack<ORMBean.OfferBean>() {
            @Override
            public void onSuccess(ORMBean.OfferBean result) {
                    List<Offer> data = result.getData();

                    list_offer.clear();
                    list_offer.addAll(data);

                    adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


}
