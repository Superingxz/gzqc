package com.xolo.gzqc.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;


import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.CarOwner_CarInfo;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class CarOwner_CarInfoAdapter extends  CommonAdapter<CarInfo>{


    public CarOwner_CarInfoAdapter(Context context, int itemLayoutId, List<CarInfo> mDatas) {
        super(context, itemLayoutId, mDatas);
    }

    @Override
    public void convert(ViewHolder helper, CarInfo item) {
        ((TextView) helper.getView(R.id.owner_car_number)).setText(item.getCarno());
        ((TextView) helper.getView(R.id.owner_car_code)).setText(item.getBrands()+"/"+item.getTypecode());
      //  x.image().bind(((ImageView) helper.getView(R.id.owner_info_img)),item.getImg());

    }
}
