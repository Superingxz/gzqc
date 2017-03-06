package com.xolo.gzqc.ui.activity.consumers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.child.ConsumerMain;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 申请退款
 */
public class ApplyRefuceActivity extends BaseActivity {

    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private String orderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_refuce);
        ButterKnife.bind(this);
    init();
}

    private void init() {
        orderid = getIntent().getStringExtra(IntentConstant.ORDERID);
    }

    @OnClick(R.id.btn_commit)
    public void onClick() {
        if (TextUtils.isEmpty(getText(etComment))){
            ToastUtil.showShort(mContext,"请填写退款理由");
            return;
        }
        returngoods();
    }

    /**
     *     7-23 订单退货接口
     returngoods(userid,orderid,reason)
     当前用户ID:userid,订单子表id:orderid,退货原因:reason
     */
    private void returngoods(){
        RequestParams params = creatParams("returngoods");
        params.addBodyParameter("orderid",orderid);
        params.addBodyParameter("reason",getText(etComment));

        mLoad.show(mContext);
        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")){
                    setResult(RESULT_OK);
                    finish();
                }else {
                    mLoad.dismiss(mContext);
                }
                ToastUtil.showShort(mContext,result.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });
    }


}
