package com.xolo.gzqc.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.Ordermbuyparts;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.ArrivalGoodsActivity;
import com.xolo.gzqc.ui.activity.pickcar.BookingDetailsActivity;
import com.xolo.gzqc.ui.activity.pickcar.CompleteActivity;
import com.xolo.gzqc.ui.activity.pickcar.DispatchingActivity;
import com.xolo.gzqc.ui.activity.pickcar.FuntionActivity;
import com.xolo.gzqc.ui.activity.pickcar.InquiryActivity;
import com.xolo.gzqc.ui.activity.pickcar.OfferActivity;
import com.xolo.gzqc.ui.activity.pickcar.OrderPurchaseActivity;
import com.xolo.gzqc.ui.activity.pickcar.PayDetailsActivity;
import com.xolo.gzqc.ui.activity.pickcar.ProgressActivity;
import com.xolo.gzqc.ui.activity.pickcar.ReadPickUpActivity;
import com.xolo.gzqc.ui.activity.pickcar.VerhaulActivity;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.TabChangeListener;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 待办事项 - 工作台
 */
public class WaitDealThingFragment extends BaseFragment{

    private ListView lv;

    private List<CarInfo> list_info = new ArrayList<>();
    private CommenAdapter<CarInfo> adapter;

    private Dialog dialog;
    private Dialog dialog_2;
    private Dialog dialog_3;
    private Dialog dialog_4;

    private FuntionActivity funtionActivity;

    /**
     * 但前点击的位置
     */
    private int position_click;

    private TabChangeListener tabchagelistener;

    @Override
    public void onAttach(Context context) {
        funtionActivity = (FuntionActivity) context;
        tabchagelistener = (TabChangeListener) funtionActivity;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lv = new ListView(mContext);
        lv.setDivider(null);
        lv.setBackgroundColor(getResources().getColor(R.color.activity_bg));
        return lv;
    }

    @Override
    protected void init() {
          initLv();
          initDialog();
          getreceivemytodo();
    }


    @Override
    public void load() {
        super.load();
        getreceivemytodo();
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_info_1, mContext, list_info, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                CarInfo carInfo = list_info.get(position);

                holder.setText(R.id.item1, "车牌号：" + carInfo.getCarno());
                holder.setText(R.id.item2, "车型：" + carInfo.getBrands() + "  " + carInfo.getTypecode());
                holder.setText(R.id.item3, "来车时间： " + carInfo.getIn_time());
                holder.setText(R.id.item4, "状态： " + carInfo.getStatus());
                holder.setText(R.id.item5, "接车员： " + carInfo.getOperatoname());
                holder.setVisibility(R.id.item5,View.VISIBLE);
                holder.loadUrl(R.id.iv,carInfo.getBrands_path());


                ImageView view = holder.getView(R.id.item7);
                view.setVisibility(View.VISIBLE);

                if (carInfo.getIs_urgent().equals("0")){
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_green));
                }else {
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_red));
                }

                CheckBox cb = holder.getView(R.id.item6);
                if (carInfo.getIs_unsettled().equals("1")){
                    cb.setChecked(true);
                }
            }
        });

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String status = list_info.get(position).getStatus();

                CarInfo carInfo = list_info.get(position);
                Intent intent = null;

                position_click = position;

                if (status.equals("已接车")){
                    intent = new Intent(mContext, ReadPickUpActivity.class);
                    intent.putExtra("type",1);
                }else if (status.equals("报价中（客户不同意）")){
                    intent = new Intent(mContext, OfferActivity.class);
                    intent.putExtra(Key.CARNO,carInfo.getCarno());
                }else if (status.equals("已报价（客户同意）")){
                    dialog_2.show();
                    return;
                }else if (status.equals("预约中")){
                    intent = new Intent(mContext, BookingDetailsActivity.class);
                    Ordermbuyparts ordermbuyparts = new Ordermbuyparts(carInfo.getCarno(), carInfo.getName(), carInfo.getPhone(), carInfo.getIn_time(), carInfo.getFault_description(),carInfo.getBc_car_owner_id(),carInfo.getBf_receive_id());
                    intent.putExtra(Key.OBJECT,ordermbuyparts);
                    startActivity(intent);
                    return;
                }else if (status.equals("拆检中")){
                    intent = new Intent(mContext, VerhaulActivity.class);
                    intent.putExtra(Key.CARNO,carInfo.getCarno());
                }else if (status.equals("待交车")){
                    intent = new Intent(mContext, PayDetailsActivity.class);
                    intent.putExtra("type",2);
                }else if (status.equals("购件到货")){
                    dialog_4.show();
                    return;
                }
                else if (status.equals("报价中（等待确认）")){
                    ToastUtil.showShort(mContent,"正在等待客户确认，请耐心等候");
                    return;
                }
                else if (status.equals("已完工")){
                    intent = new Intent(mContext, ProgressActivity.class);
                    intent.putExtra(Key.CARNO,carInfo.getCarno());
                }
                else if (status.equals("维修中")){
                    intent = new Intent(mContext, CompleteActivity.class);
                }
                else if (status.equals("拆检完成")){
                    dialog.show();
                    return;
                }else  if (status.equals("已派工")){
                    ToastUtil.showShort(mContent,"班组正在维修，请等待");
                    return;
                }
                else if (status.equals("询价（采购员已回复）")){
                    dialog_3.show();
                    return;
                }
                else if (status.equals("购件到货（部分）")){
                    intent = new Intent(mContext, ArrivalGoodsActivity.class);
                    intent.putExtra("type",1);
                    intent.putExtra(Key.CARNO,carInfo.getCarno());
                }
                else {
                    return;
                }
                intent.putExtra(Key.OBJECT,carInfo);
                startActivity(intent);
            }
        });

    }


/**
 *    2-1 接车员工作台-我的待办事项 getreceivemytodo(userid,dept_id) 当前用户ID:userid,维修厂ID：dept_id
 */
public   void getreceivemytodo(){
    RequestParams params = creatParams("getreceivemytodo");
    params.addBodyParameter(Key.USER_ID,user.getUser_id());
    params.addBodyParameter(Key.DEPT_ID,user.getDept_id());

    HttpUtil.getInstance().postLoading(mContent, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
        @Override
        public void onSuccess(ORMBean.CarInfoBean result) {
            List<CarInfo> data = result.getData();

            list_info.clear();
            tabchagelistener.change(0,data.size());
            list_info.addAll(data);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            list_info.clear();
            adapter.notifyDataSetChanged();
        }
    });

}


    /**
     * 工作台--拆检完成状态，点击直接报价、询价、购件。
     */
    private void initDialog() {
        String[] array = new String[]{"报价", "询价", "购件"};
        List<String> strings1 = Arrays.asList(array);
        dialog = creatListDialog("", strings1, null, new ListDialogCallBack<String>() {
            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                CarInfo carInfo = list_info.get(position_click);

                Intent intent = null;
                if (s.equals("报价")) {
                    intent = new Intent(mContext, OfferActivity.class);
                } else if (s.equals("询价")) {
                    intent = new Intent(mContext, InquiryActivity.class);
                }else if (s.equals("购件")) {
                    intent = new Intent(mContext, OrderPurchaseActivity.class);
                    intent.putExtra(Key.CARNO,carInfo.getCarno());
                    startActivity(intent);
                    return;
                }
                intent.putExtra(Key.CARNO,carInfo.getCarno());
                intent.putExtra(Key.OBJECT,carInfo);
                startActivity(intent);
            }
        });


        String[] array2 = new String[]{"派工", "返回首页"};
        List<String> strings2 = Arrays.asList(array2);
        dialog_2 = creatListDialog("", strings2, null, new ListDialogCallBack<String>() {
            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                CarInfo carInfo = list_info.get(position_click);

                Intent intent = null;
                if (s.equals("派工")) {
                    intent = new Intent(mContext, DispatchingActivity.class);
                } else if (s.equals("返回首页")) {
                    funtionActivity.tabChange(0);
                    return;
                }
                intent.putExtra(Key.CARNO,carInfo.getCarno());
                intent.putExtra(Key.OBJECT,carInfo);
                startActivity(intent);
            }
        });


        String[] array3 = new String[]{"报价", "购件","返回首页"};
        List<String> strings3 = Arrays.asList(array3);
        dialog_3 = creatListDialog("", strings3, null, new ListDialogCallBack<String>() {
            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                CarInfo carInfo = list_info.get(position_click);

                Intent intent = null;
                if (s.equals("报价")) {
                    intent = new Intent(mContext, OfferActivity.class);
                }else if (s.equals("购件")){
                    intent = new Intent(mContext, OrderPurchaseActivity.class);
                    intent.putExtra(Key.CARNO,carInfo.getCarno());
                    startActivity(intent);
                    return;
                } else if (s.equals("返回首页")) {
                    funtionActivity.tabChange(0);
                    return;
                }
                intent.putExtra(Key.CARNO,carInfo.getCarno());
                intent.putExtra(Key.OBJECT,carInfo);
                startActivity(intent);
            }
        });



        String[] array4 = new String[]{"购件到货", "派工"};
        List<String> strings4 = Arrays.asList(array4);
        dialog_4 = creatListDialog("", strings4, null, new ListDialogCallBack<String>() {
            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                CarInfo carInfo = list_info.get(position_click);

                Intent intent = null;
                if (s.equals("购件到货")) {
                    intent = new Intent(mContext, ArrivalGoodsActivity.class);
                    intent.putExtra("type",1);
                }else if (s.equals("派工")){
                    intent = new Intent(mContext, DispatchingActivity.class);
                }
                intent.putExtra(Key.CARNO,carInfo.getCarno());
                intent.putExtra(Key.OBJECT,carInfo);
                startActivity(intent);
            }
        });

    }


}
