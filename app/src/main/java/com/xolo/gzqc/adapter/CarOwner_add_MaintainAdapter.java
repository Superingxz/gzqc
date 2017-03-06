package com.xolo.gzqc.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CarOwner_Add_Maintain;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.utils.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class CarOwner_add_MaintainAdapter extends CommonAdapter<CarOwner_Add_Maintain> {
    boolean isFocusable;
    public CarOwner_add_MaintainAdapter(Context context, int itemLayoutId, List<CarOwner_Add_Maintain> mDatas) {
        super(context, itemLayoutId, mDatas);
        this.mDatas=mDatas;
    }
//    android:clickable="false"
//    android:focusable="false"
    public  void setEditeFocusable(boolean b){
        this.isFocusable=b;
    }
    @Override
    public void convert(ViewHolder helper, final CarOwner_Add_Maintain item) {
        final int position=helper.getPosition();
        final EditText editText=(EditText) helper.getConvertView().findViewById(R.id.maintain_et);
        editText.setTag(helper.getPosition());
        if(isFocusable){
            editText.setClickable(true);
            editText.setFocusable(true);
        }else{
            editText.setClickable(false);
            editText.setFocusable(false);
        }
        if(!item.isImag()){
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if(!TextUtils.isEmpty(s.toString())&&imaOnlickiface!=null&&((int)editText.getTag())==position){
                        imaOnlickiface.changeEdit(position,s.toString());
                    }
                }
            });
        }


        ((TextView) helper.getView(R.id.maintain_text1)).setText(item.getName());
        if (item.isImag()) {
            ((RelativeLayout) helper.getView(R.id.et_img_layout)).setVisibility(View.VISIBLE);
        } else {
            ((RelativeLayout) helper.getView(R.id.et_img_layout)).setVisibility(View.GONE);
        }

        ((EditText) helper.getView(R.id.maintain_et)).setText(item.getEttext());
        ((RelativeLayout) helper.getView(R.id.et_img_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imaOnlickiface==null){
                    return;
                }
                imaOnlickiface.rightImgOnlic(position);
            }
        });
    }

    public ImaOnlickIface imaOnlickiface;

    public interface ImaOnlickIface {
        void rightImgOnlic(int posotion);
        void changeEdit(int position,String st);
    }

    public ImaOnlickIface getImaOnlickiface() {
        return imaOnlickiface;
    }

    public void setImaOnlickiface(ImaOnlickIface imaOnlickiface) {
        this.imaOnlickiface = imaOnlickiface;
    }
}
