package com.xolo.gzqc.ui.fragment.consumers;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Order;
import com.xolo.gzqc.configuration.Constant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 待付款订单
 */
public class WaitPayOrderFragment extends BaseFragment {

    private ListView listView;

    private List<Order>  list_order = new ArrayList<>();
    private CommenAdapter<Order> orderCommenAdapter;

    public WaitPayOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listView = new ListView(mContent);
        listView.setBackgroundColor(ContextCompat.getColor(mContent, R.color.white));
        listView.setDivider(null);
        return listView;
    }

    @Override
    public void load() {
        super.load();
        searchordersbystatus();
    }

    @Override
    protected void init() {
        orderCommenAdapter = new CommenAdapter<>(R.layout.item_order, mContent, list_order, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                final Order order = list_order.get(position);
                final List<Order.GoodsBean> goods = order.getGoods();

                holder.setText(R.id.order_id,"订单号："+order.getOrder_number());
                holder.setText(R.id.order_time,"下单时间："+order.getOperatdate());
                holder.setText(R.id.tv_qty,"共"+order.getQty_total()+"件商品  合计：");
                holder.setText(R.id.tv_price,"￥"+String.valueOf(order.getPrice_total()));
                holder.setText(R.id.btn_1,"取消订单");
                holder.setText(R.id.btn_2,"付款");

                holder.setOnClicklistener(R.id.btn_1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelorder(order.getC_order_m_id());
                    }
                });
                holder.setOnClicklistener(R.id.btn_2, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                ListView lv = holder.getView(R.id.slv);
                lv.setAdapter(new CommenAdapter<>(R.layout.item_order_good, mContent, goods, new CommenAdapter.AdapterCallback() {
                    @Override
                    public void setView(ViewHolder holder, int position) {
                        Order.GoodsBean goodsBean = goods.get(position);

                        holder.loadUrl(R.id.iv,goodsBean.getPic_path());
                        holder.setText(R.id.tv_title,goodsBean.getGoodsname());
                        holder.setText(R.id.tv_price,goodsBean.getFactprice());
                        holder.setText(R.id.tv_qty,"x"+goodsBean.getQty());
                    }
                }));
            }
        });
        listView.setAdapter(orderCommenAdapter);
    }

    /**
     * 7-20 分状态查询订单列表信息
     * searchordersbystatus(userid,status)
     * 当前用户ID:userid,订单状态:status 注意:status待付款:10;待收货:30;已收货：50，退款中：40
     */
    private void searchordersbystatus() {
        RequestParams params = creatParams("searchordersbystatus");
//        RequestParams params = new RequestParams(Constant.BASE_URL);
//        params.addBodyParameter(Key.USER_ID,"E501FF7C-805D-43D9-A5E8-B0BEC77CD780");
//        params.addBodyParameter(Key.ACTION,"searchordersbystatus");
        params.addBodyParameter("status","10");

        mLoad.show(mContent);
        HttpUtil.getInstance().post( params, ORMBean.OrderBean.class, new HttpUtil.HttpCallBack<ORMBean.OrderBean>() {
            @Override
            public void onSuccess(ORMBean.OrderBean result) {
                if (result.getRes().equals("1")){
                    list_order.clear();
                    list_order.addAll(result.getData());
                    count();
                    orderCommenAdapter.notifyDataSetChanged();
                }else {
                    list_order.clear();
                    orderCommenAdapter.notifyDataSetChanged();
                }
                mLoad.dismiss(mContent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContent);
            }
        });

    }

    /**
     * 7-18 取消订单
     cancelorder(userid,c_order_m_id)
     当前用户ID:userid,订单ID:c_order_m_id
     */
    private void  cancelorder(String id){
        RequestParams params = creatParams("cancelorder");
        params.addBodyParameter("c_order_m_id",id);

        mLoad.show(mContent);
        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")){
                        searchordersbystatus();
                }else {
                    mLoad.dismiss(mContent);
                }
                ToastUtil.showShort(mContent,result.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContent);
            }
        });
    }


    private  void count(){
        for (Order o:list_order){
            int qty = 0;
            double price = 0;

            for (Order.GoodsBean g:o.getGoods()){
                qty+=Integer.parseInt(g.getQty());
                price+=Double.parseDouble(g.getFactprice())*qty;
            }

            o.setQty_total(qty);
            o.setPrice_total(price);
        }
    }

}
