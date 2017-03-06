package com.xolo.gzqc.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CoMaintainDetile;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public class CoPrograssDetilesAdapter extends CommonAdapter<CoMaintainDetile> {
    public CoPrograssDetilesAdapter(Context context, int itemLayoutId, List<CoMaintainDetile> mDatas) {
        super(context, itemLayoutId, mDatas);
    }

    @Override
    public void convert(ViewHolder helper, CoMaintainDetile item) {
        helper.setText(R.id.co_accessories_project, item.getItemt_name());
        helper.setText(R.id.co_maintain_team, item.getTeam_name());
        helper.setText(R.id.co_finish_text, item.getSet_finished());
        if (helper.getPosition() >= 1) {
            ((TextView) helper.getView(R.id.co_finish_text)).setVisibility(View.GONE);
            ((CheckBox) helper.getView(R.id.detile_cb)).setVisibility(View.VISIBLE);
            if (item.getSet_finished().equals("1")) {
                ((CheckBox) helper.getView(R.id.detile_cb)).setChecked(true);
            } else {
                ((CheckBox) helper.getView(R.id.detile_cb)).setChecked(false);
            }
        }else{
            ((TextView) helper.getView(R.id.co_finish_text)).setVisibility(View.VISIBLE);
            ((CheckBox) helper.getView(R.id.detile_cb)).setVisibility(View.GONE);
        }

    }
}
