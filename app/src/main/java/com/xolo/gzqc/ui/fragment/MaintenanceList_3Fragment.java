package com.xolo.gzqc.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.postJson.PartVerhaul;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaintenanceList_3Fragment extends BaseFragment {


    private ListView lv;

    private List<PartVerhaul> list_part = new ArrayList<>();
    private CommenAdapter<PartVerhaul> adapter;
    private String receive_id;

    public MaintenanceList_3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lv = new ListView(mContext);
        initIntent();
        return lv;
    }

    @Override
    protected void init() {

        initLv();

        getrepairlistbyreceiveid();
    }

    @Override
    public void load() {
        super.load();
        getrepairlistbyreceiveid();
    }

    private void initIntent() {
        receive_id = getArguments().getString(Key.RECRIVE_ID);
    }


    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_maintenance_list, mContext, list_part, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                PartVerhaul carInfo = list_part.get(position);

                holder.setText(R.id.item1, "维修项目：" + carInfo.getItemt_name());
                holder.setText(R.id.item2, "单价：" + carInfo.getWorkamt());
                holder.setText(R.id.item4, "班组：" + carInfo.getTeam_name());

              holder.getView(R.id.item3).setVisibility(View.GONE);
              holder.getView(R.id.item5).setVisibility(View.GONE);
            }
        });


        lv.setAdapter(adapter);
    }


/**
 * 2-3 通过接车单ID得到该车的维修项目清单接口 getrepairlistbyreceiveid(userid,receive_id)
 当前用户ID:userid,接车单ID:receive_id
 */
    private  void  getrepairlistbyreceiveid(){
        RequestParams params = creatParams("getrepairlistbyreceiveid");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter("receive_id",receive_id);

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.PartVerhaulBean.class, new HttpUtil.HttpCallBack<ORMBean.PartVerhaulBean>() {
            @Override
            public void onSuccess(ORMBean.PartVerhaulBean result) {
                List<PartVerhaul> data = result.getData();

                list_part.clear();
                list_part.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                list_part.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

}
