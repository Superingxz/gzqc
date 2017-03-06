package com.xolo.gzqc.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.SupplierGoodsChild;

import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */
public class SupplierOrderChildAdapter extends CommonAdapter<SupplierGoodsChild> {
    Context context;
    public SupplierOrderChildAdapter(Context context, int itemLayoutId, List<SupplierGoodsChild> mDatas) {
        super(context, itemLayoutId, mDatas);
        this.context=context;
    }

    @Override
    public void convert(ViewHolder helper, SupplierGoodsChild item) {
        Glide.with(context).load(item.getPic_path()).error(R.mipmap.img_error).into((ImageView)helper.getView(R.id.img));
        helper.setText(R.id.order_title, item.getGoodsname());
        helper.setText(R.id.standard_num,"x"+item.getQty());
        helper.setText(R.id.order_title_standard,"¥"+item.getFactprice());

    }
}
