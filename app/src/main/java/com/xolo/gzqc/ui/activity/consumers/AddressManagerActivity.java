package com.xolo.gzqc.ui.activity.consumers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Address;
import com.xolo.gzqc.configuration.IntentConstant;
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
 * 管理收货地址
 */
public class AddressManagerActivity extends BaseActivity {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.add_address)
    Button addAddress;

    private List<Address> list_adress = new ArrayList<>();
    private CommenAdapter<Address> addressCommenAdapter;

    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initTitle();
        initLv();
        getusershopaddress();
    }

    private void initTitle() {
        titleview.setRightText("管理");
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    isEdit = false;
                    titleview.setRightText("管理");
                    addAddress.setVisibility(View.GONE);
                } else {
                    isEdit = true;
                    titleview.setRightText("完成");
                    addAddress.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initLv() {
        addressCommenAdapter = new CommenAdapter<>(R.layout.item_address, mContext, list_adress, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                final Address address = list_adress.get(position);
                holder.setText(R.id.tv_consignee, "收货人：" + address.getLink_name() + "   " + address.getLink_tel());
                holder.setText(R.id.tv_location, address.getProvince() + address.getCity() + address.getArea() + address.getStreet());

                if (isEdit) {
                    holder.setVisibility(R.id.rl_hidden, View.VISIBLE);
                    if (address.getDefault_addr().equals("1")) {
                        holder.setCheck(R.id.cb_default, true);
                        holder.setClickable(R.id.cb_default, false);
                    } else {
                        holder.setCheck(R.id.cb_default, false);
                        holder.setOnClicklistener(R.id.cb_default, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setdefaultaddress(address.getC_clientaddr_id());
                            }
                        });
                    }

                    holder.setOnClicklistener(R.id.btn_delect, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deladdress(address.getC_clientaddr_id());
                        }
                    });
                    holder.setOnClicklistener(R.id.btn_edit, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, EditAddressActivity.class);
                            intent.putExtra(IntentConstant.ADDRESS, address);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    });
                } else
                    holder.setVisibility(R.id.rl_hidden, View.GONE);

                if (address.getDefault_addr().equals("1")) {
                    holder.setVisibility(R.id.tv_default, View.VISIBLE);
                } else
                    holder.setVisibility(R.id.tv_default, View.GONE);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(IntentConstant.ADDRESS, list_adress.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        lv.setAdapter(addressCommenAdapter);
    }


    /**
     * 7-25 获取当前用户的收货地址接口
     * 应用的界面：应用于用户维护自己的收货地址
     * getusershopaddress(userid)
     * 当前用户ID:userid
     */
    private void getusershopaddress() {
        RequestParams params = creatParams("getusershopaddress");

        mLoad.show(mContext);
        HttpUtil.getInstance().post(params, ORMBean.AddressBean.class, new HttpUtil.HttpCallBack<ORMBean.AddressBean>() {
            @Override
            public void onSuccess(ORMBean.AddressBean result) {
                if (result.getRes().equals("1")) {
                    list_adress.clear();
                    list_adress.addAll(result.getData());
                    addressCommenAdapter.notifyDataSetChanged();
                }
                mLoad.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            getusershopaddress();
        }
    }

    /**
     * 7-13 设置默认地址
     * setdefaultaddress(userid,c_clientaddr_id)
     * 当前用户ID:userid,收货地址表ID:c_clientaddr_id
     */
    private void setdefaultaddress(String id) {
        RequestParams params = creatParams("setdefaultaddress");
        params.addBodyParameter("c_clientaddr_id", id);

        mLoad.show(mContext);
        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {
                    getusershopaddress();
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                    mLoad.dismiss(mContext);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });
    }

    @OnClick(R.id.add_address)
    public void onClick() {
        Intent intent = new Intent(mContext, EditAddressActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * 7-27 删除当前用户的收货地址接口
     应用的界面：应用于用户维护自己的收货地址
     deladdress(userid,c_clientaddr_id)
     当前用户ID:userid,收货地址ID:c_clientaddr_id
     */
    private void  deladdress(String id){
        RequestParams params = creatParams("deladdress");
        params.addBodyParameter("c_clientaddr_id",id);

        mLoad.show(mContext);
        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                  if (result.getRes().equals("1")){
                         getusershopaddress();
                  }else {
                      mLoad.dismiss(mContext);
                  }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });
    }

}
