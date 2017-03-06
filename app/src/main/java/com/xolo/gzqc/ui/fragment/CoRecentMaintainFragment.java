package com.xolo.gzqc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.MaintainRecordAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Maintain_Record;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 最近维修记录
 * Created by Administrator on 2016/9/28.
 */
public class CoRecentMaintainFragment extends LazyFragment {
    ListView maintain_re_lv;
    List<Maintain_Record> maintain_records;
    MaintainRecordAdapter maintainRecordAdapter;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        maintain_re_lv=(ListView)view.findViewById(R.id.maintain_re_lv);
        maintain_records=new ArrayList<>();
        super.onViewCreated(view, savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fagment_recent_mainatain,null);
    }
    @Override
    protected void loadData() {
        if(needLoad()){
            initData();
            mHasLoadedOnce=true;
        }
    }

    @Override
    protected void init() {

    }
    void initData() {
        LoadDialog.show(getActivity());
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("getorderlist");
        requestParams.addBodyParameter("is_deal", "1");
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoMaintainRecord.class, new HttpUtil.HttpCallBack<ORMBean.CoMaintainRecord>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(getActivity());
            }

            @Override
            public void onSuccess(ORMBean.CoMaintainRecord result) {
                if (result.getRes().equals("1")) {
                    maintainRecordAdapter = new MaintainRecordAdapter(getActivity(), R.layout.item_maintain_record, result.getData());
                    maintain_re_lv.setAdapter(maintainRecordAdapter);
                    mHasLoadedOnce = true;
                } else {
                    mHasLoadedOnce=false;
                    ToastUtil.showShort(mContext, result.getMsg());
                }
                LoadDialog.dismiss(getActivity());
            }
        });

    }
}
