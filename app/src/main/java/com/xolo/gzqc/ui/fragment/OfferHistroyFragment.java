package com.xolo.gzqc.ui.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.OfferExpandableAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.OfferHistroy;
import com.xolo.gzqc.bean.child.Workamt;
import com.xolo.gzqc.bean.postJson.Offer;
import com.xolo.gzqc.bean.postJson.Part;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.CustomExpandableListView;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 历史报价 -  报价
 */
public class OfferHistroyFragment extends BaseFragment {


    @BindView(R.id.lv)
    ListView lv;
    private CommenAdapter<OfferHistroy> adapter;

    public OfferHistroyFragment() {
        // Required empty public constructor
    }

    private String  carno;

    private List<OfferHistroy>  list_histroy = new ArrayList<>();

    private int position_select = -1;

    private LoadDialog loadDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_appointment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
          initLv();
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_offer_histroy, mContent, list_histroy, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, final int position) {
                final OfferHistroy offerHistroy = list_histroy.get(position);

                holder.setText(R.id.item1,"第"+(position+1)+"次报价    "+offerHistroy.getOperatdate());
                holder.setText(R.id.price_part,offerHistroy.getTotalsaleprice());
                holder.setText(R.id.price_cost,offerHistroy.getTotalworkamt());
                holder.setText(R.id.price_total,offerHistroy.getTotalprice());

                final CustomExpandableListView lv_item = holder.getView(R.id.item2);
                lv_item.setAdapter(new OfferExpandableAdapter(offerHistroy.getOfferList(),mContent));

                lv_item.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        Offer offer = offerHistroy.getOfferList().get(groupPosition);
                        List<Part> repairlist = offer.getRepairlist();
                        if (repairlist.size()==0){
                              quotereparibymaintenanceid(repairlist,offer.getBf_quoted_priced_id(),position);
                        }
                        return false;
                    }
                });

                View ll_price = holder.getView(R.id.ll_price);

                if (offerHistroy.isShow()){
                    lv_item.setVisibility(View.VISIBLE);
                    ll_price.setVisibility(View.VISIBLE);
                }else {
                    lv_item.setVisibility(View.GONE);
                    ll_price.setVisibility(View.GONE);
                }

                holder.getView(R.id.item1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (offerHistroy.getOfferList().size()==0){
                            quoteorderm(offerHistroy.getBf_receive_car_id(),position);
                        }

                        if (offerHistroy.isShow()){
                            offerHistroy.setShow(false);
                        }else
                            offerHistroy.setShow(true);

                        adapter.notifyDataSetChanged();
                    }
                });

            }
        });

        lv.setAdapter(adapter);

    }

    @Override
    public void load() {
        super.load();
        if (! TextUtils.isEmpty(carno)){
          getoldquoteordermlist();
        }
    }

    /**
     *     2-55-1 车主报价确认接口--查询历史报价列表接口(已作废的报价) getoldquoteordermlist(userid,carno,owen_id)
     当前用户ID:userid,车牌号：carno
     */
    private  void getoldquoteordermlist(){
        RequestParams params = creatParams("getoldquoteordermlist");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter("carno",carno);
        params.addBodyParameter("is_deleted","1");

        loadDialog.show(mContent);

        HttpUtil.getInstance().post( params, ORMBean.OfferHistroyBean.class, new HttpUtil.HttpCallBack<ORMBean.OfferHistroyBean>() {
            @Override
            public void onSuccess(ORMBean.OfferHistroyBean result) {
                if (result.getRes().equals("1")){
                    List<OfferHistroy> data = result.getData();
                    list_histroy.clear();
                    list_histroy.addAll(data);

                    adapter.notifyDataSetChanged();
                }else {
                    list_histroy.clear();
                    adapter.notifyDataSetChanged();
                }
                loadDialog.dismiss(mContent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContent);
            }
        });
    }


    public void setCarno(String carno) {
        this.carno = carno;
        isLoaded =false;
        list_histroy.clear();
        adapter.notifyDataSetChanged();
    }


    /**
     * 2-55-2 报价接口--查询历史报价接口(已作废的报价) getoldquoteorderm(userid,carno,is_deleted)
     当前用户ID:userid,车牌号：carno,是否作废：is_deleted
     */
    private void quoteorderm(final String receive_id, final int position) {
        RequestParams params = creatParams("getoldquoteorderm");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("bf_receive_car_id", receive_id);
        params.addBodyParameter("is_deleted", "1");
        params.addBodyParameter("times_cnt",String.valueOf(position+1));


        HttpUtil.getInstance().postLoading(mContent,params, ORMBean.OfferBean.class, new HttpUtil.HttpCallBack<ORMBean.OfferBean>() {
            @Override
            public void onSuccess(ORMBean.OfferBean result) {

                    List<Offer> data = result.getData();

                List<Offer> offerList = list_histroy.get(position).getOfferList();
                offerList.clear();
                offerList.addAll(data);

                    adapter.notifyDataSetChanged();

                getrepairsum(receive_id,position);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

        });
    }


    /**
     * 2-54 报价接口--根据维修项目id查询配件清单 quotereparibymaintenanceid(userid,bf_quoted_priced_id,is_deleted)
     * 当前用户ID:userid,维修项目ID:bf_quoted_priced_id,是否报废：is_deleted
     */
    private void quotereparibymaintenanceid(final List<Part> repairlist, String id, int position) {
        RequestParams params = creatParams("quotereparibymaintenanceid");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("bf_quoted_priced_id", id);
        params.addBodyParameter("times_cnt",String.valueOf(position+1));

        loadDialog.show(mContent);

        HttpUtil.getInstance().post(params, ORMBean.PartBean.class, new HttpUtil.HttpCallBack<ORMBean.PartBean>() {
            @Override
            public void onSuccess(ORMBean.PartBean result) {
                if (result.getRes().equals("1")){
                    List<Part> data = result.getData();

                    repairlist.clear();
                    repairlist.addAll(data);

                    adapter.notifyDataSetChanged();
                }
                loadDialog.dismiss(mContent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContent);
            }
        });
    }


    /**
     * 2-55-3 报价接口--查询工时费统计(totalworkamt)/配件费统计(totalsaleprice)/总金额(totalprice) getrepairsum(userid,carno)
     当前用户ID:userid,车牌号：carno
     */
      private  void getrepairsum(String receive_id, final int position){
          RequestParams params = creatParams("getrepairsum");
          params.addBodyParameter(Key.USER_ID,user.getUser_id());
          params.addBodyParameter("bf_receive_id",receive_id);
          params.addBodyParameter("is_deleted","1");
          params.addBodyParameter("is_history","1");
          params.addBodyParameter("times_cnt",String.valueOf(position+1));

          HttpUtil.getInstance().postLoading(mContent, params, ORMBean.WorkamtBean.class, new HttpUtil.HttpCallBack<ORMBean.WorkamtBean>() {
              @Override
              public void onSuccess(ORMBean.WorkamtBean result) {
                  Workamt workamt = result.getData().get(0);
                  OfferHistroy offerHistroy = list_histroy.get(position);
                  offerHistroy.setTotalprice(workamt.getTotalprice());
                  offerHistroy.setTotalworkamt(workamt.getTotalworkamt());
                  offerHistroy.setTotalsaleprice(workamt.getTotalsaleprice());

                  adapter.notifyDataSetChanged();
              }

              @Override
              public void onError(Throwable ex, boolean isOnCallback) {

              }
          });
      }

}
