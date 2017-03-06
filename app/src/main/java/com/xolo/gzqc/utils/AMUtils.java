package com.xolo.gzqc.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AMing on 15/12/21.
 * Company RongCloud
 */
public class AMUtils {

    /**
     * 手机号正则表达式
     **/
    public final static String MOBLIE_PHONE_PATTERN = "^((13[0-9])|(15[0-9])|(18[0-9])|(14[7])|(17[0|6|7|8]))\\d{8}$";

    /**
     * 通过正则验证是否是合法手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isMobile(String phoneNumber) {
        Pattern p = Pattern.compile(MOBLIE_PHONE_PATTERN);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }



    /**
     * 验证车牌的正表达式
     * @param carNum
     * @return
     */
    public static boolean validateCarNum(String carNum) {

        boolean result = false;

        String[] provence = new String[] { "京", "津", "冀", "晋", "辽", "吉", "黑", "沪", "苏", "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘", "粤", "桂", "琼", "渝",

                "川", "黔", "滇", "藏", "陕", "甘", "青", "宁", "新", "港", "澳", "蒙" };

        String reg = "[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";

        boolean firstChar = false;

        if (carNum.length() > 0) {

            firstChar = Arrays.asList(provence).contains(carNum.substring(0, 1));

        }

        try {

            Pattern p = Pattern.compile(reg);

            Matcher m = p.matcher(carNum);

            if (m.matches() && firstChar) {

                result = true;

            } else {

                result = false;

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;

    }
}
