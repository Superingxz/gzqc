package com.xolo.gzqc.adapter;

import android.content.Context;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.ProjectChild;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class CoProjectChildAdapter extends  CommonAdapter<ProjectChild> {
    public CoProjectChildAdapter(Context context, int itemLayoutId, List<ProjectChild> mDatas) {
        super(context, itemLayoutId, mDatas);
    }

    @Override
    public void convert(ViewHolder helper, ProjectChild item) {
//        ((TextView) helper.getView(R.id.accessories_name)).setText(item.getName());
//        ((TextView) helper.getView(R.id.accessories_util_price)).setText(item.getUtil_price());
//        ((TextView) helper.getView(R.id.accessories_num)).setText(item.getNum());
//        ((TextView) helper.getView(R.id.accessories_price)).setText(item.getTotle_price());
    }
}
