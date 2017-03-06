package com.xolo.gzqc.ui.activity.pickcar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ReceiveannexBean;
import com.xolo.gzqc.bean.child.Receiveannex;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 随车附件
 */
public class ReceiveannexActivity extends BaseActivity {

    @BindView(R.id.cb_1)
    CheckBox cb1;
    @BindView(R.id.cb_2)
    CheckBox cb2;
    @BindView(R.id.cb_3)
    CheckBox cb3;
    @BindView(R.id.cb_4)
    CheckBox cb4;
    @BindView(R.id.cb_5)
    CheckBox cb5;
    @BindView(R.id.cb_6)
    CheckBox cb6;
    @BindView(R.id.cb_7)
    CheckBox cb7;
    @BindView(R.id.elseItem)
    EditText elseItem;
    @BindView(R.id.confirm)
    Button confirm;

    private ArrayList<Receiveannex> list = new ArrayList<>();

    private ReceiveannexBean receiveannexBean;

    private Receiveannex receiveannex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiveannex);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        int type = intent.getIntExtra(IntentConstant.TYPE, 0);
        /**
         * 只读信息
         */
        if (type == 1) {
            setClickale(new View[]{cb1, cb2, cb3, cb4, cb5, cb6, cb7});
            setEditable(new EditText[]{elseItem});
            confirm.setVisibility(View.GONE);
        }

        list = intent.getParcelableArrayListExtra(Key.OBJECT);

        if (list != null) {

            for (Receiveannex r : list) {
                if (r.getAnnex_code().equals("1001")) {
                    if (r.getIs_exist().equals("1"))
                        cb1.setChecked(true);
                }
                if (r.getAnnex_code().equals("2001")) {
                    if (r.getIs_exist().equals("1"))
                        cb2.setChecked(true);
                }
                if (r.getAnnex_code().equals("3001")) {
                    if (r.getIs_exist().equals("1"))
                        cb3.setChecked(true);
                }
                if (r.getAnnex_code().equals("4001")) {
                    if (r.getIs_exist().equals("1"))
                        cb4.setChecked(true);
                }
                if (r.getAnnex_code().equals("5001")) {
                    if (r.getIs_exist().equals("1"))
                        cb5.setChecked(true);
                }
                if (r.getAnnex_code().equals("6001")) {
                    if (r.getIs_exist().equals("1"))
                        cb6.setChecked(true);
                }
                if (r.getAnnex_code().equals("7001")) {
                    if (r.getIs_exist().equals("1"))
                        cb7.setChecked(true);
                }
                if (r.getAnnex_code().equals("8001")) {
                    elseItem.setText(r.getAnnex_name());
                }

            }
        }

    }

    //    {"data":[{"annex_code":"1001","annex_name":"行驶证","is_exist":"1"},{"annex_code":"2001","annex_name":"车辆登记证","is_exist":"1"},
//        {"annex_code":"3001","annex_name":"随车工具","is_exist":"1"},{"annex_code":"4001","annex_name":"钥匙","is_exist":"1"},
//        {"annex_code":"5001","annex_name":"备胎","is_exist":"1"},{"annex_code":"6001","annex_name":"三角牌","is_exist":"0"},
//        {"annex_code":"7001","annex_name":"灭火器","is_exist":"1"}
//        ,{"annex_code":"8001","annex_name":"其它","is_exist":"0"}]}'
    @OnClick(R.id.confirm)
    public void onClick() {
        list = new ArrayList<>();

        list.add(new Receiveannex("1001", "行驶证", cb1.isChecked() ? "1" : "0"));
        list.add(new Receiveannex("2001", "车辆登记证", cb2.isChecked() ? "1" : "0"));
        list.add(new Receiveannex("3001", "随车工具", cb3.isChecked() ? "1" : "0"));
        list.add(new Receiveannex("4001", "钥匙", cb4.isChecked() ? "1" : "0"));
        list.add(new Receiveannex("5001", "备胎", cb5.isChecked() ? "1" : "0"));
        list.add(new Receiveannex("6001", "三角牌", cb6.isChecked() ? "1" : "0"));
        list.add(new Receiveannex("7001", "灭火器", cb7.isChecked() ? "1" : "0"));
        list.add(new Receiveannex("8001", elseItem.getText().toString().trim(), TextUtils.isEmpty(elseItem.getText().toString().trim()) ? "0" : "1"));


        Intent intent = new Intent();

        intent.putParcelableArrayListExtra(Key.JSON_PATRS, list);

        setResult(2, intent);

        finish();
    }


}
