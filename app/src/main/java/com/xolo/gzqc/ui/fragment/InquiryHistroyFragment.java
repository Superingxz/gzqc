package com.xolo.gzqc.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.LicenseKeyboardUtil;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.InquiryInfo;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.InquiryHistroyActivity;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.OnKeyDownListener;
import com.xolo.gzqc.utils.Interface.TabChangeListener;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 历史询价
 */
public class InquiryHistroyFragment extends BaseFragment implements OnKeyDownListener {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.car_nunber)
    EditText carNunber;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.tb_carno)
    TableLayout tbCarno;
    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;

    private CommenAdapter<InquiryInfo> adapter;
    private List<InquiryInfo> list_carno = new ArrayList<>();

    private LicenseKeyboardUtil keyboardUtil;

    private TabChangeListener tabchagelistener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tabchagelistener = ((TabChangeListener) context);
    }



    public InquiryHistroyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inquiry_histroy, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        keyboardUtil = new LicenseKeyboardUtil(mContext, carNunber, keyboardView);
        initLv();
    }

    //    增加返回字段 query_name 询价人,replay_name 报价人,replay_time 报价时间,valid_time 报价有效时间
    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_inquiry_histroy, mContext, list_carno, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                InquiryInfo carInfo = list_carno.get(position);

                holder.setText(R.id.item1, "车牌号：" + carInfo.getCarno());
                holder.setText(R.id.item2, "车型：" + carInfo.getBrands() + "  " + carInfo.getTypecode());
                holder.setText(R.id.item3, "询价时间： " + carInfo.getOperatdate());
                holder.setText(R.id.item4, "接车员：" + carInfo.getOperatoname());

                holder.loadUrl(R.id.iv,carInfo.getBrands_path());
            }
        });

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContent, InquiryHistroyActivity.class);
                intent.putExtra("type", 2);
//                intent.putExtra(Key.CARNO, list_carno.get(position).getCarno());
                intent.putExtra(IntentConstant.INQUIRY_ID, list_carno.get(position).getBf_query_pricem_id());
                startActivity(intent);
            }
        });
    }


    /**
     * 2-78 本维修厂历史询价接口 getlistquerypricem(userid,dept_id)
     * 当前用户ID:userid,维修厂ID：dept_id
     */
    private void getlistquerypricem() {
        RequestParams params = creatParams("getlistquerypricem");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter(Key.CARNO, getText(carNunber));

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.InquiryInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.InquiryInfoBean>() {
            @Override
            public void onSuccess(ORMBean.InquiryInfoBean result) {
                List<InquiryInfo> data = result.getData();
                list_carno.clear();
                tabchagelistener.change(1,data.size());
                list_carno.addAll(data);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                list_carno.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void load() {
        super.load();
        getlistquerypricem();
    }


    @OnClick(R.id.select)
    public void onClick() {
        getlistquerypricem();
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardUtil.hideKeyboard();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil != null && keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard();
                return true;
            }else
                return false;
        }
        return false;
    }
}
