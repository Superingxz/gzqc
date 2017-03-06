package com.xolo.gzqc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CoAccessories;
import com.xolo.gzqc.bean.child.CoSheduli;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class CoAccessoriesAdapter extends CommonAdapter<CoAccessories> {
    public CoAccessoriesAdapter(Context context, int itemLayoutId, List<CoAccessories> mDatas) {
        super(context, itemLayoutId, mDatas);
    }

    @Override
    public void convert(ViewHolder helper, CoAccessories item) {
        if (helper.getPosition() == getData().size() - 1) {
            helper.getView(R.id.co_view).setVisibility(View.GONE);
        } else if (helper.getPosition() == 0) {
            helper.getView(R.id.co_view).setVisibility(View.VISIBLE);
            ((TextView) helper.getView(R.id.co_accessories_name)).setTextColor(Color.parseColor("#3a3a3a"));
            ((TextView) helper.getView(R.id.co_accessories_num)).setTextColor(Color.parseColor("#3a3a3a"));
            ((TextView) helper.getView(R.id.co_accessories_totle_price)).setTextColor(Color.parseColor("#3a3a3a"));
            ((TextView) helper.getView(R.id.co_accessories_unit_price)).setTextColor(Color.parseColor("#3a3a3a"));
            ((TextView) helper.getView(R.id.co_accessories_name)).getPaint().setFakeBoldText(true);
            ((TextView) helper.getView(R.id.co_accessories_num)).getPaint().setFakeBoldText(true);
            ((TextView) helper.getView(R.id.co_accessories_totle_price)).getPaint().setFakeBoldText(true);
            ((TextView) helper.getView(R.id.co_accessories_unit_price)).getPaint().setFakeBoldText(true);
        }
        ((TextView) helper.getView(R.id.co_accessories_name)).setText(item.getParts_name());
        ((TextView) helper.getView(R.id.co_accessories_num)).setText(item.getQty());
        ((TextView) helper.getView(R.id.co_accessories_totle_price)).setText(item.getTotalprice());
        ((TextView) helper.getView(R.id.co_accessories_unit_price)).setText(item.getSaleprice());
    }
}
