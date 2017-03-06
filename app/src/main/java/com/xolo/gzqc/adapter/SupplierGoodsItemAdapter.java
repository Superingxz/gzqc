package com.xolo.gzqc.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.SupplierMainGoods;

import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */
public class SupplierGoodsItemAdapter extends  CommonAdapter<SupplierMainGoods>{
    Context context;
    public SupplierGoodsItemAdapter(Context context, int itemLayoutId, List<SupplierMainGoods> mDatas) {
        super(context, itemLayoutId, mDatas);
        this.context=context;
    }

    @Override
    public void convert(ViewHolder helper, SupplierMainGoods item) {
        Glide.with(context).load(item.getPic_1()).error(R.mipmap.img_error).into((ImageView)helper.getView(R.id.supplier_img_item));
        helper.setText(R.id.goods_name,item.getGoods_name());
        helper.setText(R.id.price,"¥"+item.getSales_price());
        helper.setText(R.id.num,item.getTotal());
    }
}
