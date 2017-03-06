package com.xolo.gzqc.ui.activity.pickcar;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.child.Ordermbuyparts;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.OwnerInfoActivity;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

/**
 * 预约详细信息
 */
public class BookingDetailsActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.carno)
    TextView carno;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.deal)
    CheckBox deal;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.give_time)
    TextView giveTime;

    private Ordermbuyparts order;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        dialog = creatDateDialog(giveTime);

        titleview.setRightText("保存");
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!deal.isChecked()) {
                    ToastUtil.showShort(mContext, "请勾选已处理");
                    return;
                }
                if (TextUtils.isEmpty(getText(giveTime))) {
                    ToastUtil.showShort(mContext, "请选择预交车日期");
                    return;
                }
                listrepairordermdeal();
            }
        });

        order = (Ordermbuyparts) getIntent().getSerializableExtra(Key.OBJECT);
        if (order != null) {
            carno.setText(order.getCarno());
            name.setText(order.getName());
            phone.setText(order.getPhone());
            date.setText(order.getPlan_com_time());
            description.setText(order.getFault_description());
        }

    }

    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.give_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText().toString().trim()));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ToastUtil.showShort(mContext,"无打电话的权限");
                    return;
                }
                startActivity(intent);
                break;
            case R.id.btn_2:
                Intent intent1 = new Intent(mContext, OwnerInfoActivity.class);
                intent1.putExtra(Key.IS_READ,true);
                intent1.putExtra("phone",order.getPhone());
                startActivity(intent1);
                break;
            case R.id.btn_3:
                startFriendDetailsPage();
                break;
            case R.id.give_time:
                dialog.show();
                break;
        }
    }

/**
 *        2-12 预约维修处理接口 listrepairordermdeal(userid,orderm_id,plan_give_time,deal_remark,dept_name,carOwner_id)
 *        当前用户ID:userid,预约id:orderm_id,计划交车时间:plan_give_time,处理说明:deal_remark,维修厂名称:dept_name,车主ID:carOwner_id
 */
    private void listrepairordermdeal(){
        RequestParams params = creatParams("listrepairordermdeal");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter("orderm_id",order.getBf_orderm_id());
        params.addBodyParameter("plan_give_time",giveTime.getText().toString().trim());
        params.addBodyParameter("deal_remark",remark.getText().toString().trim());
        params.addBodyParameter("dept_name",user.getDept_name());
        params.addBodyParameter("carOwner_id",order.getBc_car_owner_id());

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext,result.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void startFriendDetailsPage() {
        RongIM.getInstance().startPrivateChat(mContext,getText(phone),getText(name));
    }

}
