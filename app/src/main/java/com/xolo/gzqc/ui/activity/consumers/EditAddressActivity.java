package com.xolo.gzqc.ui.activity.consumers;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Address;
import com.xolo.gzqc.bean.child.Area;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.ExEditText;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//增加和修改收货地址
public class EditAddressActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.eet_name)
    ExEditText eetName;
    @BindView(R.id.eet_phone)
    ExEditText eetPhone;
    @BindView(R.id.eet_postal_code)
    ExEditText eetPostalCode;
    @BindView(R.id.eet_province)
    ExEditText eetProvince;
    @BindView(R.id.eet_city)
    ExEditText eetCity;
    @BindView(R.id.eet_country)
    ExEditText eetCountry;
    @BindView(R.id.eet_address)
    ExEditText eetAddress;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private List<Area> list_province = new ArrayList<>();
    private List<Area> list_city = new ArrayList<>();
    private List<Area> list_county = new ArrayList<>();

    private Dialog dialog_province;
    private Dialog dialog_city;
    private Dialog dialog_county;

    private String provinceid;
    private String cityid;
    private String areaid;

    private Address address_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initIntent();
        initDialog();
    }

    private void initDialog() {
        dialog_province = createAreaDialog("省", list_province, eetProvince);
        dialog_city = createAreaDialog("市", list_city, eetCity);
        dialog_county = createAreaDialog("县区", list_county, eetCountry);
    }

    private void initIntent() {
        Intent intent = getIntent();

        address_edit = (Address) intent.getSerializableExtra(IntentConstant.ADDRESS);
        if (address_edit!=null) {
            titleview.setTitle("修改地址");
            btnCommit.setText("修改");

            eetName.setText(address_edit.getLink_name());
            eetPhone.setText(address_edit.getLink_tel());
            eetPostalCode.setText(address_edit.getZip_code());
            eetProvince.setText(address_edit.getProvince());
            eetCity.setText(address_edit.getCity());
            eetCountry.setText(address_edit.getArea());
            eetAddress.setText(address_edit.getStreet());

            provinceid = address_edit.getProvince_id();
            cityid = address_edit.getCity_id();
            areaid = address_edit.getArea_id();
        }
    }


    /**
     * 7-9 新增收货地址
     * newaddress(userid,province_id,province,city_id,city,area_id,area,street,zip_code,link_name,link_tel)
     * 当前用户ID:userid,省份ID:province_id,省份:province,市id:city_id,市:city,区县ID:area_id,区县:area
     * ,街道:street,邮编:zip_code,收件人:link_name,联系电话:link_tel
     */
    private void newaddress() {
        RequestParams params = creatParams("newaddress");
        addParams(params);

        mLoad.show(mContext);
        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")){
                    setResult(RESULT_OK);
                    finish();
                }else {

                }
                ToastUtil.showShort(mContext,result.getMsg());
                mLoad.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
mLoad.dismiss(mContext);
            }
        });
    }

    @OnClick({R.id.eet_province, R.id.eet_city, R.id.eet_country,R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.eet_province:
                if (list_province.size() > 0) {
                    dialog_province.show();
                    return;
                }
                getArea(list_province, dialog_province, "");
                break;
            case R.id.eet_city:
                if (TextUtils.isEmpty(eetProvince.getText())) {
                    ToastUtil.showLong(mContext, "请先选择省");
                    return;
                }
                getArea(list_city, dialog_city, provinceid);
                break;
            case R.id.eet_country:
                if (TextUtils.isEmpty(eetCity.getText())) {
                    ToastUtil.showLong(mContext, "请先选择市");
                    return;
                }
                getArea(list_county, dialog_county, cityid);
                break;
            case R.id.btn_commit:
                if (check()){
                    return;
                }
                if (address_edit!=null){
                    updateaddress();
                }else
                newaddress();
                break;
        }
    }

    private Dialog createAreaDialog(String title, List<Area> list, final ExEditText targer) {
        return creatListDialog(title, list, targer.getEt_contetn(), new ListDialogCallBack<Area>() {
            @Override
            public String setText(Area area) {
                return area.getDistname();
            }

            @Override
            public void onClick(Area area) {

                if (targer == eetProvince) {
                    eetCity.setText("");
                    eetCountry.setText("");
                    provinceid = area.getDistid();
                    cityid = null;
                } else if (targer == eetCity) {
                    eetCountry.setText("");
                    cityid = area.getDistid();
                } else {
                    areaid = area.getDistid();
                }
            }
        });
    }

    /**
     * 接口获取省，市，县的信息
     *
     * @param list
     * @param dialog
     */
    private void getArea(final List list, final Dialog dialog, String id) {
        RequestParams params = creatParams("getregionbydistid");
        params.addBodyParameter("distid", id);

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.AreaBean.class, new HttpUtil.HttpCallBack<ORMBean.AreaBean>() {
            @Override
            public void onSuccess(ORMBean.AreaBean result) {
                List<Area> data = result.getData();

                list.clear();
                list.addAll(data);
                dialog.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private boolean check(){
        if (TextUtils.isEmpty(eetName.getText())){
            ToastUtil.showShort(mContext,"请输入收货人");
            return true;
        }
        if (TextUtils.isEmpty(eetPhone.getText())){
            ToastUtil.showShort(mContext,"请输入收货人电话");
            return true;
        }
//        if (TextUtils.isEmpty(eetPostalCode.getText())){
//            ToastUtil.showShort(mContext,"请输入邮政编码");
//            return true;
//        }
        if (TextUtils.isEmpty(eetProvince.getText())){
            ToastUtil.showShort(mContext,"请选择省");
            return true;
        }
        if (TextUtils.isEmpty(eetCity.getText())){
            ToastUtil.showShort(mContext,"请选择市");
            return true;
        }
        if (TextUtils.isEmpty(eetCountry.getText())){
            ToastUtil.showShort(mContext,"请选择县");
            return true;
        }
        if (TextUtils.isEmpty(eetAddress.getText())){
            ToastUtil.showShort(mContext,"请输入详细地址");
            return true;
        }
        return false;
    }


    /**
     * 7-12 修改收货地址
     updateaddress(userid,c_clientaddr_id,province_id,province,city_id,city,area_id,area,street,zip_code,link_name,link_tel)
     当前用户ID:userid,收货地址表ID:c_clientaddr_id,省份ID:province_id,省份:province,市id:city_id,市:city,区县ID:area_id,区县:area
     ,街道:street,邮编:zip_code,收件人:link_name,联系电话:link_tel
     */
    private void updateaddress(){
        RequestParams params = creatParams("updateaddress");
        params.addBodyParameter("c_clientaddr_id",address_edit.getC_clientaddr_id());
        addParams(params);

        mLoad.show(mContext);
        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")){
                        setResult(RESULT_OK);
                    finish();
                }else {
                    ToastUtil.showShort(mContext,result.getMsg());
                }
                mLoad.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });
    }



    private void addParams(RequestParams params){
        params.addBodyParameter("province_id",provinceid);
        params.addBodyParameter("province",eetProvince.getText());
        params.addBodyParameter("city_id",cityid);
        params.addBodyParameter("city",eetCity.getText());
        params.addBodyParameter("area_id",areaid);
        params.addBodyParameter("area",eetCountry.getText());
        params.addBodyParameter("street",eetAddress.getText());
        params.addBodyParameter("zip_code",eetPostalCode.getText());
        params.addBodyParameter("link_name",eetName.getText());
        params.addBodyParameter("link_tel",eetPhone.getText());
    }

}
