package com.xolo.gzqc.adapter;

import android.content.Context;
import android.view.View;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.ShopFitment;
import com.xolo.gzqc.utils.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/12/8.
 */
public class ShopFitmentAdapter extends  CommonAdapter<ShopFitment> {
    Context context;
    public ShopFitmentAdapter(Context context, int itemLayoutId, List<ShopFitment> mDatas) {
        super(context, itemLayoutId, mDatas);
        this.context=context;
    }

    @Override
    public void convert(final ViewHolder helper, final ShopFitment item) {
        helper.setText(R.id.serial_number,String.valueOf(helper.getPosition()+1));
        helper.setText(R.id.order_title,item.getGoods_name());
        helper.setText(R.id.price,"¥"+item.getSales_price());
        helper.getView(R.id.up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helper.getPosition()!=0){
                    shopFitmentAdapterIface.upOnClick(item,helper.getPosition());
                }else{
                    ToastUtil.showShort(context,"已是第一个商品");
                }

            }
        });
        helper.getView(R.id.down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helper.getPosition()!=getData().size()-1){
                    shopFitmentAdapterIface.dnwnOClick(item,helper.getPosition());
                }else{
                    ToastUtil.showShort(context,"已是最后一个商品");
                }

            }
        });
    }

    public ShopFitmentAdapterIface getShopFitmentAdapterIface() {
        return shopFitmentAdapterIface;
    }

    public void setShopFitmentAdapterIface(ShopFitmentAdapterIface shopFitmentAdapterIface) {
        this.shopFitmentAdapterIface = shopFitmentAdapterIface;
    }

   public ShopFitmentAdapterIface shopFitmentAdapterIface;
    public interface  ShopFitmentAdapterIface{
        void upOnClick(ShopFitment item,int position);
        void dnwnOClick(ShopFitment itemt,int position);
    }
}
