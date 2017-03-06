package com.xolo.gzqc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CoHistroyMaintainState;
import com.xolo.gzqc.bean.child.CoSheduli;
import com.xolo.gzqc.bean.child.ComponentProgress;
import com.xolo.gzqc.ui.view.TabView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class CoSheduliAdapter extends CommonAdapter<CoHistroyMaintainState> {
    public CoSheduliAdapter(Context context, int itemLayoutId, List<CoHistroyMaintainState> mDatas) {
        super(context, itemLayoutId, mDatas);
    }

    @Override
    public void convert(ViewHolder helper, CoHistroyMaintainState item) {
        if (helper.getPosition() == getData().size() - 1) {
            helper.getView(R.id.co_view).setVisibility(View.GONE);
        } else if (helper.getPosition() == 0) {
            helper.getView(R.id.co_view).setVisibility(View.VISIBLE);
            ((TextView) helper.getView(R.id.co_accessories_name)).setTextColor(Color.parseColor("#3a3a3a"));
            ((TextView) helper.getView(R.id.co_accessories_num)).setTextColor(Color.parseColor("#3a3a3a"));
            ((TextView) helper.getView(R.id.co_accessories_unit_price)).setTextColor(Color.parseColor("#3a3a3a"));
            ((TextView) helper.getView(R.id.co_accessories_totle_price)).setTextColor(Color.parseColor("#3a3a3a"));
            ((TextView) helper.getView(R.id.co_accessories_state)).setTextColor(Color.parseColor("#3a3a3a"));
        }
        helper.setText(R.id.co_accessories_name, item.getParts_name());
        helper.setText(R.id.co_accessories_num, item.getQty());
        helper.setText(R.id.co_accessories_unit_price, item.getSaleprice());
        helper.setText(R.id.co_accessories_totle_price, item.getTotalsaleprice());
    if(helper.getPosition()!=0){
        if (item.getStatus().equals("1")) {
            helper.setText(R.id.co_accessories_state, "已采购");
        } else {
            helper.setText(R.id.co_accessories_state, "采购中");
        }
    }else{
        helper.setText(R.id.co_accessories_state, "状态");
        helper.setText(R.id.co_accessories_unit_price, "单价");
        helper.setText(R.id.co_accessories_totle_price, "总价");
    }
    }

}
