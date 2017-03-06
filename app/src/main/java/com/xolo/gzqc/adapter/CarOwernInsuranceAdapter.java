package com.xolo.gzqc.adapter;

import android.content.Context;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.view.TabView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class CarOwernInsuranceAdapter extends  CommonAdapter<String> {


    public CarOwernInsuranceAdapter(Context context, int itemLayoutId, String[] mDatas) {
        super(context, itemLayoutId, mDatas);
    }

    @Override
    public void convert(ViewHolder helper, String item) {
        ((TextView)helper.getView(R.id.insurance_lv_text)).setText(item);
    }
}
