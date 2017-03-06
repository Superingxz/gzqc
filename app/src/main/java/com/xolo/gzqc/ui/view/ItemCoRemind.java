package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CoRemind;
import com.xolo.gzqc.utils.ToastUtil;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ItemCoRemind extends LinearLayout implements View.OnLongClickListener{
    TextView remind_text_Num, remind_text_title, message,remind_text_time;
    Context context;
    Button remind_subscribe_bt, remind_maintain_bt;
    int position;

    public ItemCoRemind(Context context) {
        super(context);
        init(context);
    }

    public ItemCoRemind(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        this.context = context;
        View.inflate(context, R.layout.item_co_remind, this);
        initView();
        setOnLongClickListener(this);
    }

    void initView() {
        //remind_maintain_bt 知道了
        //remind_subscribe_bt 马上付款
        remind_text_time=(TextView)findViewById(R.id.remind_text_time);
        remind_text_Num = (TextView) findViewById(R.id.remind_text_num);
        remind_text_title = (TextView) findViewById(R.id.remind_text_title);
        message = (TextView) findViewById(R.id.message);
        remind_subscribe_bt = (Button) findViewById(R.id.remind_subscribe_bt);
        remind_maintain_bt = (Button) findViewById(R.id.remind_maintain_bt);
        remind_maintain_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClick.readMsg(position, ItemCoRemind.this);
            }
        });

        remind_subscribe_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               if(data.getWarn_type().equals("购件付款")){
                   itemOnClick.pay();
                }else if(data.getWarn_type().equals("报价提醒")){
                   itemOnClick.priceRemind();
                }
            }
        });
    }

    ItemOnClick itemOnClick;

    public ItemOnClick getItemOnClick() {
        return itemOnClick;
    }

    public void setItemOnClick(ItemOnClick itemOnClick) {
        this.itemOnClick = itemOnClick;
    }

    @Override
    public boolean onLongClick(View v) {
        itemOnClick.remoMsg(position,data);


        return false;
    }


    public interface ItemOnClick {

        void readMsg(int position, ItemCoRemind itemCoRemind);

        void pay();
        void priceRemind();
        void remoMsg(int position,CoRemind data);
    }

    CoRemind data;

    public CoRemind getData() {
        return data;
    }

    public void setData(CoRemind data) {
        this.data = data;
    }

    public void setData(int num, CoRemind data, int position) {
        this.data = data;
        this.position = position;
        remind_text_Num.setText(String.valueOf(num));
        remind_text_title.setText(data.getWarn_type());
        message.setText(data.getContent2());
        remind_text_time.setText(data.getWarn_time());
        if (data.getIs_know().equals("1")) {
            setBtColorGrey();
        }
//        if (data.getIs_pay().equals("1")) {
//            remind_subscribe_bt.setVisibility(View.VISIBLE);
//        }else{
//            remind_subscribe_bt.setVisibility(View.GONE);
//        }
        if(data.getWarn_type().equals("保养提醒")){
            remind_subscribe_bt.setVisibility(View.GONE);
        }else if(data.getWarn_type().equals("购件付款")){
            remind_subscribe_bt.setText("马上付款");
        }else if(data.getWarn_type().equals("报价提醒")){
            if(!TextUtils.isEmpty(data.getOperate_status())&&data.getOperate_status().equals("1")){
                remind_subscribe_bt.setText("已处理");
                remind_subscribe_bt.setBackgroundResource(R.drawable.shape_grey);
                remind_subscribe_bt.setTextColor(Color.parseColor("#cccccc"));
            }else{
                remind_subscribe_bt.setText("去确认");
            }

        }else if(data.getWarn_type().equals("预约提醒")){
            remind_subscribe_bt.setVisibility(View.GONE);
        }else if(data.getWarn_type().equals("完工提醒")){
            remind_subscribe_bt.setVisibility(View.GONE);
        }


    }

    public  void setBtColorGrey() {
        remind_maintain_bt.setClickable(false);
        remind_maintain_bt.setBackgroundResource(R.drawable.shape_grey);
        remind_maintain_bt.setTextColor(Color.parseColor("#cccccc"));
    }
}
