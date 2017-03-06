package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;

/**
 * Created by Administrator on 2016/9/29.
 */
public class ChooseEditView extends LinearLayout {
    public ChooseEditView(Context context) {
        super(context);
        initView(context);
    }

    public ChooseEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    void initView(Context context) {
        View.inflate(context, R.layout.fragment_add_maintain_tem,this);
        tv_title=(TextView)findViewById(R.id.maintain_text1);

        img=(ImageView)findViewById(R.id.maintain_img1);

        et_img_layout=(RelativeLayout)findViewById(R.id.et_img_layout);
        et_img_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imagOnClickIface.showDialog();
            }
        });
    }
   public  ImagOnClickIface imagOnClickIface;
    public  interface  ImagOnClickIface{
        void showDialog();
    }

    public ImagOnClickIface getImagOnClickIface() {
        return imagOnClickIface;
    }

    public void setImagOnClickIface(ImagOnClickIface imagOnClickIface) {
        this.imagOnClickIface = imagOnClickIface;
    }

    TextView tv_title;
    ImageView img;
    RelativeLayout et_img_layout;
    EditText maintain_et;
   public  void iniData(String title,boolean isShowImg){
       tv_title.setText(title);
       if(isShowImg){
           img.setVisibility(View.VISIBLE);
       }else{
           img.setVisibility(View.GONE);
       }
    }

    public  void setEtText(String text){
        maintain_et.setText(text);
    }
    public  String  getEtText(){
        return  maintain_et.getText().toString();
    }
}
