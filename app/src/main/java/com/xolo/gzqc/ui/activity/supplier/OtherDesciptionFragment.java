package com.xolo.gzqc.ui.activity.supplier;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.child.SupplierSellGoods;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;


import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 其他说明
 * Created by Administrator on 2016/12/9.
 */
public class OtherDesciptionFragment extends BaseFragment {
    View view;
    EditText honor;
    EditText goods_df;
    Button submit;
    String goodsid;

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getContext(), R.layout.fragment_other_description, null);
            honor = (EditText) view.findViewById(R.id.honor);
            goods_df = (EditText) view.findViewById(R.id.goods_df);
            submit = (Button) view.findViewById(R.id.submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit();
                }
            });
            if(supplierSellGoods!=null){
                goods_df.setText(supplierSellGoods.getGoods_compared());
                honor.setText(supplierSellGoods.getHonor());
            }
        }
        return view;
    }
    SupplierSellGoods  supplierSellGoods;
    public  void setData(final SupplierSellGoods supplierSellGoods) {
        this.supplierSellGoods=supplierSellGoods;
        goodsid=supplierSellGoods.getC_goodsinfor_id();
    }

    void submit() {
        if (TextUtils.isEmpty(goodsid)) {
            ToastUtil.showShort(getContext(), "请先上传商品");
            return;
        }
        LoadDialog.show(mContext);
        RequestParams requestParams = creatParams("savegoodsothers");
        requestParams.addBodyParameter("userid", "jxstar6852949");
        requestParams.addBodyParameter("goodsid", goodsid);
        requestParams.addBodyParameter("honor", honor.getText().toString());
        requestParams.addBodyParameter("conpared", goods_df.getText().toString());
        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(getContext(), result.getMsg());
                LoadDialog.dismiss(mContext);
            }
        });
    }

    @Override
    protected void init() {

    }
}
