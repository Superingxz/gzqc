package com.xolo.gzqc.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CarOwnerPersonalCenter;

import java.util.List;

/**
 * Created by Administrator on 2016/9/24.
 */
public class CarOwnersinfoAdapter extends CommonAdapter<CarOwnerPersonalCenter> {
    Context context;

    public CarOwnersinfoAdapter(Context context, int itemLayoutId, List<CarOwnerPersonalCenter> mDatas) {
        super(context, itemLayoutId, mDatas);
        this.context = context;
    }

    @Override
    public void convert( ViewHolder helper, final CarOwnerPersonalCenter item) {
        final int position=helper.getPosition();
       // ((ImageView) helper.getView(R.id.oinfo_img)).setBackgroundResource(item.getImaId());
        final ImageView maning=  ((ImageView) helper.getView(R.id.man_img));
        final ImageView female_img=  ((ImageView) helper.getView(R.id.female_img));
        ((TextView) helper.getView(R.id.oinfo_text)).setText(item.getText());
        ((EditText) helper.getView(R.id.oinfo_content)).setText(item.getContent());
        ((EditText) helper.getView(R.id.oinfo_content)).setVisibility(View.VISIBLE);
        if (helper.getPosition() == 1) {
            ((EditText) helper.getView(R.id.oinfo_content)).setVisibility(View.GONE);
            ((LinearLayout) helper.getView(R.id.layout_female)).setVisibility(View.VISIBLE);
            ((LinearLayout) helper.getView(R.id.layout_man)).setVisibility(View.VISIBLE);
            if (item.getContent().equals("0")) {
                maning.setBackgroundResource(R.mipmap.nil);
                female_img.setBackgroundResource(R.mipmap.sure);
            } else {
                maning.setBackgroundResource(R.mipmap.sure);
                female_img.setBackgroundResource(R.mipmap.nil);
            }
            ((LinearLayout) helper.getView(R.id.layout_female)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    maning.setBackgroundResource(R.mipmap.nil);
                    female_img.setBackgroundResource(R.mipmap.sure);
                    spinnerIface.changgeSex("0");
                }
            });

            ((LinearLayout) helper.getView(R.id.layout_man)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    maning.setBackgroundResource(R.mipmap.sure);
                    female_img.setBackgroundResource(R.mipmap.nil);
                    spinnerIface.changgeSex("1");
                }
            });
        } else {
            ((LinearLayout) helper.getView(R.id.layout_female)).setVisibility(View.GONE);
            ((LinearLayout) helper.getView(R.id.layout_man)).setVisibility(View.GONE);
        }
        if (item.getmItems() != null) {
            if(!TextUtils.isEmpty(item.getContent())){
                int j = 0;
                for (int i = 0; i < item.getmItems().length; i++) {
                    if (item.getContent().equals(item.getmItems()[i])) {
                        j = i;
                        break;
                    }
                }
                String text= item.getmItems()[0];
                item.getmItems()[0]=item.getContent();
                item.getmItems()[j]=text;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, item.getmItems());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ((Spinner) helper.getView(R.id.spinner)).setAdapter(adapter);
            ((Spinner) helper.getView(R.id.spinner)).setVisibility(View.VISIBLE);
            ((EditText) helper.getView(R.id.oinfo_content)).setVisibility(View.GONE);
            ((Spinner) helper.getView(R.id.spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(spinnerIface!=null&&position!=0){
                        spinnerIface.sipinerSelectText(position,item.getmItems()[position]);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            ((Spinner) helper.getView(R.id.spinner)).setVisibility(View.GONE);
        }
        ((EditText) helper.getView(R.id.oinfo_content)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                spinnerIface.editeTextChangge(position,s.toString());
            }
        });

    }
    SpinnerIface spinnerIface;

    public SpinnerIface getSpinnerIface() {
        return spinnerIface;
    }

    public void setSpinnerIface(SpinnerIface spinnerIface) {
        this.spinnerIface = spinnerIface;
    }

    public  interface  SpinnerIface{
        void sipinerSelectText(int position ,String text);
        void editeTextChangge(int position,String text);
        void changgeSex(String sex);
    }

    int sex = 1;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
