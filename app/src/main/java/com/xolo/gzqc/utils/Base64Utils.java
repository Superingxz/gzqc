package com.xolo.gzqc.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * base64的转换工具类
 */
public class Base64Utils {

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public static String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 90, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static String Bitmap2StrByBase64(Bitmap bit,int  proportion) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, proportion, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static String Bitmap2StrByBase64(Bitmap[] bit) {
        StringBuffer  buffer = new StringBuffer("");
        for (int i = 0;i<bit.length;i++){
            if (bit[i]!=null){
                if (i!=0){
                    buffer.append(",");
                }
                buffer.append(Bitmap2StrByBase64(bit[i]));
            }
        }
        return buffer.toString();
    }

//    public static String Bitmap2StrByBase64(List<String> bit) {
//        StringBuffer  buffer = new StringBuffer("");
//        for (int i = 0;i<bit.size();i++){
//            if (bit.get(i)!=null){
//                if (i!=0){
//                    buffer.append(",");
//                }
//                buffer.append(Bitmap2StrByBase64(bit[i]));
//            }
//        }
//        return buffer.toString();
//    }

    public static String Bitmap2StrByBase64(List<Bitmap> bit, String spli) {
        StringBuffer  buffer = new StringBuffer("");
        for (int i = 0;i<bit.size();i++){
            if (bit.get(i)!=null){
                if (i!=0){
                    buffer.append(spli);
                }
                buffer.append(Bitmap2StrByBase64(bit.get(i)));
            }
        }
        return buffer.toString();
    }

    public static String Bitmap2StrByBase64(Bitmap[] bit,int  proportion) {
        StringBuffer  buffer = new StringBuffer("");
        for (int i = 0;i<bit.length;i++){
            if (i!=0){
                buffer.append(",");
            }
            buffer.append(Bitmap2StrByBase64(bit[i], proportion));
        }
        return buffer.toString();
    }

    public static Bitmap Base64StrByBitmap(String base64){
        byte[] decode = Base64.decode(base64, Base64.DEFAULT);
        return  BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }

}
