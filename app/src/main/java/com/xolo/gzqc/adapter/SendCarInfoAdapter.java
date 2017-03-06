package com.xolo.gzqc.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CarOwner_SendCar_info;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class SendCarInfoAdapter extends  CommonAdapter<CarOwner_SendCar_info>{
    public SendCarInfoAdapter(Context context, int itemLayoutId, List<CarOwner_SendCar_info> mDatas) {
        super(context, itemLayoutId, mDatas);
    }

    @Override
    public void convert(ViewHolder helper, CarOwner_SendCar_info item) {
        if(helper.getPosition()==getData().size()-1){
            ((LinearLayout)helper.getView(R.id.add_info_img_layout)).setVisibility(View.VISIBLE);
            ((LinearLayout)helper.getView(R.id.send_info_layout)).setVisibility(View.GONE);
        }else{
            ((LinearLayout)helper.getView(R.id.add_info_img_layout)).setVisibility(View.GONE);
            ((TextView)helper.getView(R.id.sendcar_name)).setText(item.getName());
            ((TextView)helper.getView(R.id.sender_num_1)).setText(item.getPhone());
            ((TextView)helper.getView(R.id.sender_num_2)).setText(item.getPhone2());
        }


    }
}
