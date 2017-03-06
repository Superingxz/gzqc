package com.xolo.gzqc.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CoCarOrder;
import com.xolo.gzqc.bean.child.CoMaintainPrograss;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13.
 */
public class CoCarOrderAdapter extends CommonAdapter<CoCarOrder> {
    public CoCarOrderAdapter(Context context, int itemLayoutId, List<CoCarOrder> mDatas) {
        super(context, itemLayoutId, mDatas);
    }
    @Override
    public void convert(ViewHolder helper, CoCarOrder item) {
        helper.setText(R.id.co_accessories_type,item.getType_name());
        helper.setText(R.id.co_maintain_content,item.getDescription());
        helper.setText(R.id.co_maintain_photo_text,item.getPhoto());
        ((TextView) helper.getView(R.id.co_maintain_photo_text)).setVisibility(View.VISIBLE);
        if(helper.getPosition()!=0){
            ((ImageView)helper.getView(R.id.co_maintain_photo)).setVisibility(View.VISIBLE);
            ((TextView) helper.getView(R.id.co_maintain_photo_text)).setVisibility(View.GONE);
            x.image().bind(((ImageView)helper.getView(R.id.co_maintain_photo)),item.getPhoto());
        }

    }
}
