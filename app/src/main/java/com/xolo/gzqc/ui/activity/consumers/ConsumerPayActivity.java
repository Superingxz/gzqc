package com.xolo.gzqc.ui.activity.consumers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.Address;
import com.xolo.gzqc.bean.child.ShoppingCart;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConsumerPayActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.btn_pay)
    Button btnPay;
    @BindView(R.id.tv_qty)
    TextView tvQty;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.rl_pay)
    RelativeLayout rlPay;
    @BindView(R.id.lv)
    ScrollListView lv;
    @BindView(R.id.tv_consignee)
    TextView tvConsignee;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.btn_select_location)
    RelativeLayout btnSelectLocation;
    @BindView(R.id.tv_pay_way)
    TextView tvPayWay;
    @BindView(R.id.btn_zfb)
    LinearLayout btnZfb;
    @BindView(R.id.rb_wx)
    RadioButton rbWx;
    @BindView(R.id.btn_wx)
    LinearLayout btnWx;
    @BindView(R.id.tv_order_numer)
    TextView tvOrderNumer;

    private ArrayList<ShoppingCart> list_shoppingcart = new ArrayList<>();
    private CommenAdapter<ShoppingCart> shoppingCartCommenAdapter;

    private int qty_total;
    double price_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_pay);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initIntent();
        count();
        initLv();
    }

    private void initIntent() {
        Intent intent = getIntent();
        list_shoppingcart = intent.getParcelableArrayListExtra(IntentConstant.LIST_SHOPPING_CART);
        tvOrderNumer.setText("订单号："+intent.getStringExtra(IntentConstant.ORDERID));

        Address address = (Address) intent.getSerializableExtra(IntentConstant.ADDRESS);
        tvConsignee.setText(address.getLink_name());
        tvLocation.setText(address.getStreet());
    }

    private void initLv() {
        shoppingCartCommenAdapter = new CommenAdapter<>(R.layout.item_shopping_cart, mContext, list_shoppingcart, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                final ShoppingCart shoppingCart = list_shoppingcart.get(position);
                final List<ShoppingCart.SalecarGoodsBean> salecar_goods = shoppingCart.getSalecar_goods();

                holder.setText(R.id.tv_supplier, shoppingCart.getCompany_name());
                holder.setText(R.id.tv_qty_total, shoppingCart.getQty_total() + "");
                holder.setText(R.id.tv_price_total, shoppingCart.getPrice_total() + "");

                ListView lv = (ListView) holder.getView(R.id.slv);

                if (salecar_goods.size() > 0) {
                    lv.setAdapter(new CommenAdapter<ShoppingCart.SalecarGoodsBean>(R.layout.item_shopping_cart_good, mContext, salecar_goods, new CommenAdapter.AdapterCallback() {
                        @Override
                        public void setView(final ViewHolder holder, int position) {
                            final ShoppingCart.SalecarGoodsBean salecarGoodsBean = salecar_goods.get(position);

                            holder.loadUrl(R.id.iv, salecarGoodsBean.getPic_path());
                            holder.setText(R.id.tv_title, salecarGoodsBean.getGoods_name());
                            holder.setText(R.id.tv_price, "￥" + salecarGoodsBean.getSales_price());
                            holder.setText(R.id.tv_qty, salecarGoodsBean.getQty());
                            holder.setVisibility(R.id.btn_delect, View.GONE);

                            holder.setVisibility(R.id.btn_add, View.GONE);
                            holder.setVisibility(R.id.btn_reduct, View.GONE);
                        }
                    }));
                }

            }
        });

        lv.setAdapter(shoppingCartCommenAdapter);
    }

    @OnClick(R.id.btn_pay)
    public void onClick() {

    }


    private void count() {
        qty_total = 0;
        price_total = 0;

        for (ShoppingCart s : list_shoppingcart) {
            int qty = 0;
            double price = 0;
            for (ShoppingCart.SalecarGoodsBean g : s.getSalecar_goods()) {
                qty += Integer.parseInt(g.getQty());
                price += Double.parseDouble(g.getSales_price()) * qty;
            }
            qty_total += qty;
            price_total += price;

            s.setQty_total(qty);
            s.setPrice_total(price);
        }
        tvQty.setText("数量：" + qty_total);
        tvPrice.setText("￥" + price_total);
    }

}
