package com.xolo.gzqc.ui.fragment.consumers;


import android.content.Intent;
import android.os.Bundle;
import android.print.PrinterId;
import android.support.v4.app.Fragment;
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
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.EvaluationActivity;
import com.xolo.gzqc.ui.activity.consumers.ApplyRefuceActivity;
import com.xolo.gzqc.ui.activity.consumers.LogisticsActivity;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

/**
 * 全部订单
 */
public class AllOrderFragment extends BaseFragment {

    private ListView listView;

    private List<Order>  list_order = new ArrayList<>();
    private CommenAdapter<Order> orderCommenAdapter;

    public AllOrderFragment() {
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
    protected void init() {
        orderCommenAdapter = new CommenAdapter<>(R.layout.item_order, mContent, list_order, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                final Order order = list_order.get(position);
                final String status = order.getStatus();
                final ArrayList<Order.GoodsBean> goods = (ArrayList<Order.GoodsBean>) order.getGoods();

                holder.setVisibility(R.id.tv_status,View.VISIBLE);
                holder.setText(R.id.order_id,"订单号："+order.getOrder_number());
                holder.setText(R.id.order_time,"下单时间："+order.getOperatdate());
                holder.setText(R.id.tv_qty,"共"+order.getQty_total()+"件商品  合计：");
                holder.setText(R.id.tv_price,"￥"+String.valueOf(order.getPrice_total()));

                switch (status){
                    case "10":
                        holder.setText(R.id.tv_status,"待付款");
                        holder.setText(R.id.btn_1,"取消订单");
                        holder.setText(R.id.btn_2,"付款");
                        break;
                    case "20":
                        holder.setText(R.id.tv_status,"已付款待发货");
                        holder.setVisibility(R.id.btn_1,View.GONE);
                        holder.setVisibility(R.id.btn_2,View.GONE);
                        break;
                    case "30":
                        holder.setText(R.id.btn_1,"查看物流");
                        holder.setText(R.id.btn_2,"退款");
                        holder.setText(R.id.tv_status,"已发货待收货");
                        break;
                    case "50":
                        holder.setText(R.id.btn_1,"查看物流");
                        holder.setText(R.id.btn_2,"评论");
                        holder.setText(R.id.tv_status,"交易成功");
                        break;
                    case "40":
                        holder.setText(R.id.btn_1,"查看物流");
                        holder.setText(R.id.btn_2,"取消退款");
                        holder.setText(R.id.tv_status,"退款中");
                        break;
                    case "60":
                        holder.setVisibility(R.id.btn_1,View.GONE);
                        holder.setVisibility(R.id.btn_2,View.GONE);
                        holder.setText(R.id.tv_status," 交易关闭");
                        break;
                }

                if (status.equals("20")||status.equals("60")){
                    holder.setVisibility(R.id.btn_1,View.GONE);
                    holder.setVisibility(R.id.btn_2,View.GONE);
                }else {
                    holder.setVisibility(R.id.btn_1,View.VISIBLE);
                    holder.setVisibility(R.id.btn_2,View.VISIBLE);
                }

                holder.setOnClicklistener(R.id.btn_1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                          if (status.equals("10")){
                              cancelorder(order.getC_order_m_id());
                          } else {
                              Intent intent = new Intent(mContent, LogisticsActivity.class);
                              intent.putExtra(IntentConstant.ORDER,order);
                              startActivity(intent);
                          }
                    }
                });


                holder.setOnClicklistener(R.id.btn_2, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (status.equals("30")){
                            Intent intent = new Intent(mContent, ApplyRefuceActivity.class);
                            intent.putExtra(IntentConstant.ORDERID,order.getC_order_m_id());
                            startActivityForResult(intent,REQUEST_CODE);
                        }else if (status.equals("50")){
                            Intent intent = new Intent(mContent, EvaluationActivity.class);
                            intent.putExtra(IntentConstant.ORDERID,order.getC_order_m_id());
                            startActivityForResult(intent,REQUEST_CODE);
                        }else if (status.equals("40")){
                            removereturngoods(order.getC_order_m_id());
                        }
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

    @Override
    public void load() {
        super.load();
        searchordersbystatus();
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
        params.addBodyParameter("status","");

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


    /**
     * 7-24 取消退货接口
     removereturngoods(userid,orderid)
     当前用户ID:userid,订单子表id:orderid
     */
    private void  removereturngoods(String id){
        RequestParams params = creatParams("removereturngoods");
        params.addBodyParameter("orderid",id);

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==-1){
            searchordersbystatus();
        }
    }

}
