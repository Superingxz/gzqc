package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.utils.DensityUtils;

/**
 * Created by Administrator on 2016/11/28.
 */
public class ExEditText extends LinearLayout {

    private  String title_left;
    private  String content;
    private  String hint_contetn;
    private  boolean showArrow;
    private  boolean hidden_top;
    private  boolean show_blow;
    private  String text_btn;
    private  int  width_left = 0;
    private  Drawable drawable;

    private View divider_top;
    private View divider_blow;
    private TextView tv_title_left;
    private EditText et_contetn;
    private ImageView iv_select;
    private Button button;


    private boolean isEdit = true;
    private ImageView iv_left;

    public ExEditText(Context context) {
        super(context);
    }

    public ExEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExEditText);
        if (a.hasValue(R.styleable.ExEditText_leftTitle)) {
            title_left = a.getString(R.styleable.ExEditText_leftTitle);
        }
        if (a.hasValue(R.styleable.ExEditText_content)) {
            content = a.getString(R.styleable.ExEditText_content);
        }
        if (a.hasValue(R.styleable.ExEditText_hintContent)) {
            hint_contetn = a.getString(R.styleable.ExEditText_hintContent);
        }
        if (a.hasValue(R.styleable.ExEditText_showArrow)) {
            showArrow = a.getBoolean(R.styleable.ExEditText_showArrow,false);
        }
        if (a.hasValue(R.styleable.ExEditText_hiddenTop)) {
            hidden_top = a.getBoolean(R.styleable.ExEditText_hiddenTop,false);
        }
        if (a.hasValue(R.styleable.ExEditText_showBlow)) {
            show_blow = a.getBoolean(R.styleable.ExEditText_showBlow,false);
        }
        if (a.hasValue(R.styleable.ExEditText_isEdit)) {
            isEdit = a.getBoolean(R.styleable.ExEditText_isEdit,true);
        }
        if (a.hasValue(R.styleable.ExEditText_textBtn)) {
            text_btn = a.getString(R.styleable.ExEditText_textBtn);
        }
        if (a.hasValue(R.styleable.ExEditText_leftwith)) {
            width_left = a.getInteger(R.styleable.ExEditText_leftwith,0);
        }
        if (a.hasValue(R.styleable.ExEditText_leftSrc)) {
            drawable = a.getDrawable(R.styleable.ExEditText_leftSrc);
        }
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_ex_edittext, this);
        divider_top = inflate.findViewById(R.id.divider_top);
        divider_blow = inflate.findViewById(R.id.divider_blow);
        tv_title_left = ((TextView) inflate.findViewById(R.id.title));
        et_contetn = ((EditText) inflate.findViewById(R.id.content));
        iv_select = ((ImageView) inflate.findViewById(R.id.iv_select));
        button = ((Button) inflate.findViewById(R.id.btn));
        iv_left = ((ImageView) inflate.findViewById(R.id.iv));

        if (hidden_top){
            divider_top.setVisibility(GONE);
        }
        if (show_blow){
            divider_blow.setVisibility(VISIBLE);
        }
        if (showArrow){
            iv_select.setVisibility(VISIBLE);
            et_contetn.setEnabled(false);
        }
        if (!isEdit){
            et_contetn.setEnabled(false);
        }
        if (!TextUtils.isEmpty(text_btn)){
            button.setVisibility(VISIBLE);
            button.setText(text_btn);
        }
        if (width_left!=0){
            tv_title_left.setWidth(DensityUtils.dp2px(context,width_left));
        }
        if (drawable!=null){
            iv_left.setImageDrawable(drawable);
            iv_left.setVisibility(VISIBLE);
        }

        tv_title_left.setText(title_left);
        et_contetn.setText(content);
        et_contetn.setHint(hint_contetn);
    }


    public void setText(String content){
        et_contetn.setText(content);
    }


    public String getText(){
      return   et_contetn.getText().toString().trim();
    }

    public void  setBtnOnclick(OnClickListener  listener){
        button.setOnClickListener(listener);
    }

    public EditText getEt_contetn() {
        return et_contetn;
    }

    public Button getButton() {
        return button;
    }

    public void setInputType(int type){
        et_contetn.setInputType(type);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return showArrow;
    }

    public void  setContentInput(int type,int maxLenght){
        et_contetn.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_contetn.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLenght)});
    }
}
