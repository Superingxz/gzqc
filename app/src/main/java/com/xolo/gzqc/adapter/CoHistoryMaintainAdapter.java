package com.xolo.gzqc.adapter;

import android.content.Context;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CoHistoryRecord;

import java.util.List;

/**
 * 历史维修记录状态
 * Created by Administrator on 2016/10/12.
 */
public class CoHistoryMaintainAdapter extends CommonAdapter<CoHistoryRecord>{
    public CoHistoryMaintainAdapter(Context context, int itemLayoutId, List<CoHistoryRecord> mDatas) {
        super(context, itemLayoutId, mDatas);
    }
    @Override
    public void convert(ViewHolder helper, CoHistoryRecord item) {
        if(helper.getPosition()==0){
            helper.setText(R.id.co_accessories_date,"维修日期");
            helper.setText(R.id.co_accessories_price,"维修费");
            helper.setText(R.id.co_accessories_totle_price,"备注说明");
        }else{
            helper.setText(R.id.co_accessories_date,item.getIn_time());
            helper.setText(R.id.co_accessories_price,item.getTotalamount());
            if(Integer.valueOf(item.getStatus())<10){
                helper.setText(R.id.co_accessories_totle_price,"维修中");
            }else{
                helper.setText(R.id.co_accessories_totle_price,"完工");
            }
        }

    }
}
