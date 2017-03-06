package com.xolo.gzqc.ui.activity.consumers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Address;
import com.xolo.gzqc.bean.child.ShoppingCart;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.ui.view.ScrollListView;
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
 * 提交订单
 */
public class SubmitOrderActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
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
    @BindView(R.id.rb_zfb)
    RadioButton rbZfb;
    @BindView(R.id.rb_wx)
    RadioButton rbWx;

    private ArrayList<ShoppingCart> list_shoppingcart = new ArrayList<>();
    private CommenAdapter<ShoppingCart> shoppingCartCommenAdapter;

    private boolean editAble;

//    购物车id
    private String c_salecar_m_id;

    /**
     * 收货人信息
     */
    private String link_name;
    private String link_phone;
    private String link_address;

    private  int qty_total ;
    double price_total ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initIntent();
        count();
        initLv();
    }

    private void initLv() {
        shoppingCartCommenAdapter = new CommenAdapter<>(R.layout.item_shopping_cart, mContext, list_shoppingcart, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                final ShoppingCart shoppingCart = list_shoppingcart.get(position);
                final List<ShoppingCart.SalecarGoodsBean> salecar_goods = shoppingCart.getSalecar_goods();

                holder.setText(R.id.tv_supplier, shoppingCart.getCompany_name());
                holder.setText(R.id.tv_qty_total,shoppingCart.getQty_total()+"");
                holder.setText(R.id.tv_price_total,shoppingCart.getPrice_total()+"");

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
                            holder.setVisibility(R.id.btn_delect, View.INVISIBLE);

                            if (editAble) {
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
                            }else {
                                holder.setVisibility(R.id.btn_add, View.GONE);
                                holder.setVisibility(R.id.btn_reduct, View.GONE);
                            }
                        }
                    }));
                }

            }
        });

        lv.setAdapter(shoppingCartCommenAdapter);
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

    private void initIntent() {
        Intent intent = getIntent();
        list_shoppingcart = intent.getParcelableArrayListExtra(IntentConstant.LIST_SHOPPING_CART);

        //购物车进来不可编辑，商品详情进来可编辑数量,还
        editAble = intent.getBooleanExtra(IntentConstant.EDITABLE, false);
    }


    /**
     *          7-11 去付款接口
     应用的界面：应用于在提交订单界面点击【去付款】按钮，系统自动生成订单，并返回订单编号、订单商品数量和订单总金额
     说明：购买商品列表字段:c_supplierinfor_id 供应商ID,c_goodsinfor_id 商品ID,goods_name 商品名称,specification_model 规格型号,unitname 商品单位名称,standprice 标准价格,
     factprice 实际价格,qty 商品数量
     gotopay(userid,clientname,clienttel,link_name,link_tel,address,json_goods,c_salecar_m_id)
     当前用户ID:userid,消费者姓名：clientname,消费者手机号：clienttel,收货人姓名：link_name,收货人电话：link_tel,收货人地址：address,购买商品列表：json_goods,
     购物车ID（如果是从购物车进入去付款，必须传此参数）:c_salecar_m_id
     */
    private void gotopay() {
        RequestParams params = creatParams("gotopay");
        params.addBodyParameter("clientname", user.getName());
        params.addBodyParameter("clienttel", user.getPhone());
        params.addBodyParameter("link_name", link_name);
        params.addBodyParameter("link_tel", link_phone);
        params.addBodyParameter("address", link_address);
        params.addBodyParameter("json_goods","{\"data\":"+list_shoppingcart.toString()+"}");
        params.addBodyParameter("c_salecar_m_id",list_shoppingcart.get(0).getC_salecar_m_id());

        mLoad.show(mContext);
        HttpUtil.getInstance().post(params, ORMBean.CreatOrderBean.class, new HttpUtil.HttpCallBack<ORMBean.CreatOrderBean>() {
            @Override
            public void onSuccess(ORMBean.CreatOrderBean result) {
                if (result.getRes().equals("1")) {
                    Intent intent = new Intent(mContext, ConsumerPayActivity.class);
                    intent.putParcelableArrayListExtra(IntentConstant.LIST_SHOPPING_CART,list_shoppingcart);
                    intent.putExtra(IntentConstant.ORDERID,result.getData().get(0).getOrder_number());
                    intent.putExtra(IntentConstant.ADDRESS,new Address(getText(tvConsignee),getText(tvLocation)));
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                }
                mLoad.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });
    }

    @OnClick({R.id.btn_commit, R.id.btn_select_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                if (TextUtils.isEmpty(getText(tvConsignee))){
                    ToastUtil.showShort(mContext,"请选择收货地址");
                    return;
                }
                 gotopay();
                break;
            case R.id.btn_select_location:
                Intent intent = new Intent(mContext, AddressManagerActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE&&resultCode == RESULT_OK){
            Address  address = (Address) data.getSerializableExtra(IntentConstant.ADDRESS);
            link_name = address.getLink_name();
            link_phone = address.getLink_tel();
            link_address = address.getProvince()+address.getCity()+address.getArea()+address.getStreet();

            tvLocation.setVisibility(View.VISIBLE);
            tvConsignee.setVisibility(View.VISIBLE);

            tvConsignee.setText(link_name+"   "+link_phone);
            tvLocation.setText(link_address);
        }
    }

}
