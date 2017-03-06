package com.xolo.gzqc.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.Maintain_Record;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class MaintainRecordAdapter extends  CommonAdapter<Maintain_Record> {
    public MaintainRecordAdapter(Context context, int itemLayoutId, List<Maintain_Record> mDatas) {
        super(context, itemLayoutId, mDatas);
    }

    @Override
    public void convert(ViewHolder helper, Maintain_Record item) {
        //((ImageView) helper.getView(R.id.owner_info_img)).setBackgroundResource(R.mipmap.bmw);
        ((TextView) helper.getView(R.id.owner_number)).setText(item.getPhone());
        ((TextView) helper.getView(R.id.owner_name)).setText(item.getName());
        ((TextView) helper.getView(R.id.subscribe_time)).setText(item.getOperatdate());
        ((TextView) helper.getView(R.id.plan_time)).setText(item.getPlan_com_time());
        ((TextView) helper.getView(R.id.carno)).setText(item.getCarno());
        helper.setText(R.id.fault_des,item.getFault_description());

    }
}
