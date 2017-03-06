package com.xolo.gzqc;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xolo.gzqc.utils.LogUtil;

/**
 * 车牌键盘
 */
public class LicenseKeyboardUtil {

    private Context ctx;
    private KeyboardView keyboardView;
    private Keyboard k1;// 省份简称键盘
    private Keyboard k2;// 数字字母键盘
    private String[] provinceShort;
    private String[] letterAndDigit;
    private EditText edits;

    private int type = 1;

    public LicenseKeyboardUtil(final Context ctx, final EditText edits, KeyboardView keyboard) {
        this.ctx = ctx;
        this.edits = edits;
        k1 = new Keyboard(ctx, R.xml.province_short_keyboard);
        k2 = new Keyboard(ctx, R.xml.lettersanddigit_keyboard);
        keyboardView = keyboard;
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        //设置为true时,当按下一个按键时会有一个popup来显示<key>元素设置的android:popupCharacters=""
        keyboardView.setPreviewEnabled(true);
        //设置键盘按键监听器
        keyboardView.setOnKeyboardActionListener(listener);
        provinceShort = new String[]{"京", "津", "冀", "鲁", "晋", "蒙", "辽", "吉", "黑"
                , "沪", "苏", "浙", "皖", "闽", "赣", "豫", "鄂", "湘"
                , "粤", "桂", "渝", "川", "贵", "云", "藏", "陕", "甘"
                , "青", "琼", "新", "港", "澳", "台", "宁"};

        letterAndDigit = new String[]{"0","1", "2", "3", "4", "5", "6", "7", "8", "9"
                , "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"
                , "A", "S", "D", "F", "G", "H", "J", "K", "L"
                , "Z", "X", "C", "V", "B", "N", "M"};

        edits.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!edits.hasFocus()){
                    edits.setText("");

                    closeKeybord(edits,ctx);

                    int inputback = edits.getInputType();
                    edits.setInputType(InputType.TYPE_NULL);
                    showKeyboard();
                    edits.setInputType(inputback);

                    edits.setSelection(edits.getText().length());
                }else
                   return true;

                return false;
            }
        });

        edits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 7){
                    hideKeyboard();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edits.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        edits.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard();
                }
            }
        });
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener(){

        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }


        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }


        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable text = edits.getText();
            int selectionStart = edits.getSelectionStart();

            if (primaryCode == 44){
                   type = 2;
                 keyboardView.setKeyboard(k2);
             }else  if (primaryCode == 66){
                 type = 1;
                 keyboardView.setKeyboard(k1);
             }else  if (primaryCode == 88){
                if (selectionStart >0){
                    text.delete(selectionStart-1,selectionStart);
                }
             }else if(primaryCode == 55){
                hideKeyboard();
             }else {
                 if (type == 1){
                     text.insert(selectionStart,provinceShort[primaryCode]);
//                   edits.setText(  text.toString()+provinceShort[primaryCode]  );
                 }else if (type == 2){
                     text.insert(selectionStart,letterAndDigit[primaryCode]);
//                   edits.setText(  text.toString()+letterAndDigit[primaryCode]  );
                 }
             }
        }

    };

    /**
     * 显示键盘
     */
    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
            edits.clearFocus();
        }
    }

    public boolean isShow(){
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            return  true;
        }else {
            return false;
        }
    }


    /**
     * 关闭软键盘
     *
     * @param mEditText
     *            输入框
     * @param mContext
     *            上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

}
