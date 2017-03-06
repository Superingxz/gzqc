package com.xolo.gzqc.ui.activity.pickcar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.ItemVerhaul;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPart3 extends BaseActivity {

    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.et3)
    EditText et3;
    @BindView(R.id.confirm)
    Button confirm;

    private boolean is_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_part3);
        ButterKnife.bind(this);
        initIntent();
    }

    private void initIntent() {
        ItemVerhaul.PartsBean part = (ItemVerhaul.PartsBean) getIntent().getSerializableExtra(Key.OBJECT);
        if (part !=null){
            is_edit =true;
            et1.setText(part.getParts_name());
            et2.setText(part.getQty());
            et3.setText(part.getQty_unit());
        }
    }


    @OnClick(R.id.confirm)
    public void onClick() {
        String name = getText(et1);
        String qty = getText(et2);
        String unit = getText(et3);

        if (TextUtils.isEmpty(name)){
            ToastUtil.showShort(mContext,"请填写配件名称");
            return;
        }
        if (TextUtils.isEmpty(qty)){
            ToastUtil.showShort(mContext,"请填写数量");
            return;
        }
        if (TextUtils.isEmpty(unit)){
            ToastUtil.showShort(mContext,"请填写数量单位");
            return;
        }

        ItemVerhaul.PartsBean partsBean = new ItemVerhaul.PartsBean(unit, name, qty);

        Intent intent = new Intent();
        intent.putExtra(Key.OBJECT,partsBean);
        if (is_edit){
            setResult(2,intent);
        }else
        setResult(RESULT_OK,intent);
        finish();
    }
}
