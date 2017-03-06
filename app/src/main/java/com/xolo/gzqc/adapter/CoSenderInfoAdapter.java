package com.xolo.gzqc.adapter;

import android.content.Context;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CarOwner_SendCar_info;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public class CoSenderInfoAdapter extends CommonAdapter<CarOwner_SendCar_info> {
    public CoSenderInfoAdapter(Context context, int itemLayoutId, List<CarOwner_SendCar_info> mDatas) {
        super(context, itemLayoutId, mDatas);
    }

    @Override
    public void convert(ViewHolder helper, CarOwner_SendCar_info item) {
        helper.setText(R.id.co_sender_name, item.getName());
        helper.setText(R.id.co_sender_phone1, item.getPhone());
        helper.setText(R.id.co_sender_phone2, item.getPhone2());

    }
}
