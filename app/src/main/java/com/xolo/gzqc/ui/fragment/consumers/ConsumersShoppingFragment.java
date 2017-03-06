package com.xolo.gzqc.ui.fragment.consumers;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.ShoppingCart;
import com.xolo.gzqc.configuration.Constant;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.consumers.SubmitOrderActivity;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消费者购物车
 */
public class ConsumersShoppingFragment extends BaseFragment {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_qty)
    TextView tvQty;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.rl_pay)
    RelativeLayout rlPay;
    private ArrayList<ShoppingCart> list_shoppingcart = new ArrayList<>();
    private CommenAdapter<ShoppingCart> shoppingCartCommenAdapter;

    private  int qty_total ;
    double price_total ;

    public ConsumersShoppingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consumers_shopping, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        titleview.setLeftGone();
        initLv();
//        opensalecar();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            opensalecar();
        }
    }

    private void initLv() {
        shoppingCartCommenAdapter = new CommenAdapter<>(R.layout.item_shopping_cart, mContent, list_shoppingcart, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                final ShoppingCart shoppingCart = list_shoppingcart.get(position);
                final List<ShoppingCart.SalecarGoodsBean> salecar_goods = shoppingCart.getSalecar_goods();

                holder.setText(R.id.tv_supplier, shoppingCart.getCompany_name());
                holder.setText(R.id.tv_qty_total,shoppingCart.getQty_total()+"");
                holder.setText(R.id.tv_price_total,shoppingCart.getPrice_total()+"");

                ListView lv = (ListView) holder.getView(R.id.slv);

                if (salecar_goods.size() > 0) {
                    lv.setAdapter(new CommenAdapter<ShoppingCart.SalecarGoodsBean>(R.layout.item_shopping_cart_good, mContent, salecar_goods, new CommenAdapter.AdapterCallback() {
                        @Override
                        public void setView(final ViewHolder holder, int position) {
                            final ShoppingCart.SalecarGoodsBean salecarGoodsBean = salecar_goods.get(position);

                            holder.loadUrl(R.id.iv, salecarGoodsBean.getPic_path());
                            holder.setText(R.id.tv_title, salecarGoodsBean.getGoods_name());
                            holder.setText(R.id.tv_price, "￥" + salecarGoodsBean.getSales_price());
                            holder.setText(R.id.tv_qty, salecarGoodsBean.getQty());

                            holder.setOnClicklistener(R.id.btn_add, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int i = Integer.parseInt(salecarGoodsBean.getQty()) + 1;
                                    salecarGoodsBean.setQty(String.valueOf(i));
                                    holder.setText(R.id.tv_qty, String.valueOf(String.valueOf(i)));
                                    count();
                                    shoppingCartCommenAdapter.notifyDataSetChanged();
                                }
                            });
                            holder.setOnClicklistener(R.id.btn_reduct, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int i = Integer.parseInt(salecarGoodsBean.getQty()) - 1;
                                    if (i >= 0) {
                                        salecarGoodsBean.setQty(String.valueOf(i));
                                        holder.setText(R.id.tv_qty, String.valueOf(i));
                                        count();
                                        shoppingCartCommenAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                            holder.setOnClicklistener(R.id.btn_delect, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new AlertDialog.Builder(mContent).setMessage("确定删除").setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            delesalecargoods(shoppingCart.getC_salecar_m_id(), salecarGoodsBean.getC_goodsinfor_id());
                                        }
                                    }).show();
                                }
                            });

                        }
                    }));
                }

            }
        });

        lv.setAdapter(shoppingCartCommenAdapter);
    }


    /**
     * 7-4 打开购物车接口
     * 应用的界面：应用于用户点击【购物车】按钮显示的界面。
     * opensalecar(userid)
     * 当前用户ID:userid
     */
    private void opensalecar() {
        RequestParams params = creatParams("opensalecar");
        params.addBodyParameter(Key.ACTION, "opensalecar");

        mLoad.show(mContent);
        HttpUtil.getInstance().post( params, ORMBean.ShoppingCartBean.class, new HttpUtil.HttpCallBack<ORMBean.ShoppingCartBean>() {
            @Override
            public void onSuccess(ORMBean.ShoppingCartBean result) {
                if (result.getRes().equals("1")){
                    list_shoppingcart.clear();
                    list_shoppingcart.addAll(result.getData());
                    count();
                }else {
                    list_shoppingcart.clear();
                    tvQty.setText("数量："+0);
                    tvPrice.setText("￥"+0);
                }
                shoppingCartCommenAdapter.notifyDataSetChanged();
                mLoad.dismiss(mContent);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContent);
            }
        });
    }

    /**
     * 7-5 删除购物车商品接口
     * 应用的界面：应用于用户点击【购物车】上的【删除】按钮时删除购物车的商品。
     * delesalecargoods(userid,c_salecar_m_id,c_goodsinfor_id)
     * 当前用户ID:userid,购物车ID:c_salecar_m_id,商品ID:c_goodsinfor_id
     */
    private void delesalecargoods(String c_salecar_m_id, String c_goodsinfor_id) {
        RequestParams params = creatParams("delesalecargoods");
        params.addBodyParameter("c_salecar_m_id", c_salecar_m_id);
        params.addBodyParameter("c_goodsinfor_id", c_goodsinfor_id);

        mLoad.show(mContent);
        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {
                    opensalecar();
                } else {
                    ToastUtil.showShort(mContent, result.getMsg());
                    mLoad.dismiss(mContent);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.show(mContent);
            }
        });
    }

    @OnClick(R.id.btn_buy)
    public void onClick() {
        if (list_shoppingcart.size()>0){
            Intent intent = new Intent(mContext, SubmitOrderActivity.class);
            intent.putParcelableArrayListExtra(IntentConstant.LIST_SHOPPING_CART,list_shoppingcart);
            startActivity(intent);
        }
    }


    private void count(){
        qty_total = 0;
        price_total = 0;

        for (ShoppingCart  s:list_shoppingcart){
            int qty = 0;
            double price = 0;
            for (ShoppingCart.SalecarGoodsBean g: s.getSalecar_goods()){
                qty+=Integer.parseInt(g.getQty());
                price += Double.parseDouble(g.getSales_price())*qty;
            }
            qty_total+=qty;
            price_total+=price;

            s.setQty_total(qty);
            s.setPrice_total(price);
        }
        tvQty.setText("数量："+qty_total);
        tvPrice.setText("￥"+price_total);
    }

}
