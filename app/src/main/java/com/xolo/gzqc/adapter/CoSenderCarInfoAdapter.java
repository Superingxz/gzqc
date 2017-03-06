package com.xolo.gzqc.adapter;

import android.content.Context;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CarOwner_SendCar_info;
import com.xolo.gzqc.bean.child.CoSenderCarInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public class CoSenderCarInfoAdapter extends CommonAdapter<CoSenderCarInfo> {
    public CoSenderCarInfoAdapter(Context context, int itemLayoutId, List<CoSenderCarInfo> mDatas) {
        super(context, itemLayoutId, mDatas);
    }

    @Override
    public void convert(ViewHolder helper, CoSenderCarInfo item) {
        helper.setText(R.id.co_sender_carno, item.getCarno());
        helper.setText(R.id.co_sender_brands, item.getBrands());
        helper.setText(R.id.co_sender_typecode, item.getTypecode());

    }
}
