package com.xolo.gzqc.adapter;

import java.util.List;

import com.xolo.gzqc.bean.child.CarOwnerMenu;
import com.xolo.gzqc.R;
import com.xolo.gzqc.rong.widget.DragPointView;
import com.xolo.gzqc.ui.view.CircleTextView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CarOwners_Adapter extends CommonAdapter<CarOwnerMenu> {

    public CarOwners_Adapter(Context context, int itemLayoutId, List<CarOwnerMenu> mDatas) {
        super(context, itemLayoutId, mDatas);
    }

    @Override
    public void convert(ViewHolder helper, CarOwnerMenu item) {
        if (item.getIsOnclick()) {
            ((ImageView) helper.getView(R.id.main_iv)).setBackgroundResource(item.getImg());
            ((TextView) helper.getView(R.id.main_tv)).setTextColor(Color.parseColor("#015cab"));
            //((View)helper.getView(R.id.carowners_bt_view)).setVisibility(View.VISIBLE);
        } else {
            ((ImageView) helper.getView(R.id.main_iv)).setBackgroundResource(item.getDefaultimg());
            ((TextView) helper.getView(R.id.main_tv)).setTextColor(Color.parseColor("#636363"));
            //((View)helper.getView(R.id.carowners_bt_view)).setVisibility(View.INVISIBLE);
        }
        if (helper.getPosition() == 1&& !TextUtils.isEmpty(item.getMessageNum())) {
            ((CircleTextView) helper.getView(R.id.seal_num)).setVisibility(View.VISIBLE);
            ((CircleTextView) helper.getView(R.id.seal_num)).setBackgroundColor(Color.parseColor("#ff2a2a"));
            ((CircleTextView) helper.getView(R.id.seal_num)).setText(item.getMessageNum());

        }else{
            ((CircleTextView) helper.getView(R.id.seal_num)).setVisibility(View.GONE);
        }

        ((TextView) helper.getView(R.id.main_tv)).setText(item.getTitle());


    }


}
