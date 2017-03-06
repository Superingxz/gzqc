package com.xolo.gzqc.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;



/**
 * [从本地选择图片以及拍照工具类，完美适配2.0-5.0版本]
 *
 * @author huxinwu
 * @version 1.0
 * @date 2015-1-7
 **/
public class PhotoUtils {

    private final String tag = PhotoUtils.class.getSimpleName();

    /**
     * 裁剪图片成功后返回
     **/
    public static final int INTENT_CROP = 2;
    /**
     * 拍照成功后返回
     **/
    public static final int INTENT_TAKE = 3;
    /**
     * 拍照成功后返回-
     **/
    public static final int INTENT_SELECT = 4;

    public static final String CROP_FILE_NAME = "crop_file.jpg";

    /**
     * PhotoUtils对象
     **/
    private OnPhotoResultListener onPhotoResultListener;


    public PhotoUtils(OnPhotoResultListener onPhotoResultListener) {
        this.onPhotoResultListener = onPhotoResultListener;
    }

    /**
     * 拍照
     *
     * @param
     * @return
     */
    public void takePicture(Activity activity) {
        try {
            //每次选择图片吧之前的图片删除
            clearCropFile(buildUri(activity));

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, buildUri(activity));
            if (!isIntentAvailable(activity, intent)) {
                return;
            }
            activity.startActivityForResult(intent, INTENT_TAKE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 选择一张图片
     * 图片类型，这里是image/*，当然也可以设置限制
     * 如：image/jpeg等
     *
     * @param activity Activity
     */
    @SuppressLint("InlinedApi")
    public void selectPicture(Activity activity) {
        try {
            //每次选择图片吧之前的图片删除
            clearCropFile(buildUri(activity));

            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            if (!isIntentAvailable(activity, intent)) {
                return;
            }
            activity.startActivityForResult(intent, INTENT_SELECT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建uri
     *
     * @param activity
     * @return
     */
    private Uri buildUri(Activity activity) {
        if (CommonUtils.checkSDCard()) {
            return Uri.fromFile(Environment.getExternalStorageDirectory()).buildUpon().appendPath(CROP_FILE_NAME).build();
        } else {
            return Uri.fromFile(activity.getCacheDir()).buildUpon().appendPath(CROP_FILE_NAME).build();
        }
    }

    /**
     * @param intent
     * @return
     */
    protected boolean isIntentAvailable(Activity activity, Intent intent) {
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private boolean corp(Activity activity, Uri uri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
//        cropIntent.putExtra("aspectX", 1);
//        cropIntent.putExtra("aspectY", 1);
//        cropIntent.putExtra("outputX", 200);
//        cropIntent.putExtra("outputY", 200);
        cropIntent.putExtra("return-data", false);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        Uri cropuri = buildUri(activity);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropuri);
        if (!isIntentAvailable(activity, cropIntent)) {
            return false;
        } else {
            try {
                activity.startActivityForResult(cropIntent, INTENT_CROP);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 返回结果处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (onPhotoResultListener == null) {
            Log.e(tag, "onPhotoResultListener is not null");
            return;
        }

        switch (requestCode) {
            //拍照
            case INTENT_TAKE:
                if (new File(buildUri(activity).getPath()).exists()) {
                    if (corp(activity, buildUri(activity))) {
                        return;
                    }
                    onPhotoResultListener.onPhotoCancel();
                }
                break;

            //选择图片
            case INTENT_SELECT:
                if (data != null && data.getData() != null) {
                    Uri imageUri = data.getData();
                    if (corp(activity, imageUri)) {
                        return;
                    }
                }
                onPhotoResultListener.onPhotoCancel();
                break;

            //截图
            case INTENT_CROP:
                if (resultCode == Activity.RESULT_OK && new File(buildUri(activity).getPath()).exists()) {
                    onPhotoResultListener.onPhotoResult(buildUri(activity));
                }
                break;
        }
    }

    /**
     * 删除文件
     *
     * @param uri
     * @return
     */
    public boolean clearCropFile(Uri uri) {
        if (uri == null) {
            return false;
        }

        File file = new File(uri.getPath());
        if (file.exists()) {
            boolean result = file.delete();
            if (result) {
                Log.i(tag, "Cached crop file cleared.");
            } else {
                Log.e(tag, "Failed to clear cached crop file.");
            }
            return result;
        } else {
            Log.w(tag, "Trying to clear cached crop file but it does not exist.");
        }

        return false;
    }

    /**
     * [回调监听类]
     *
     * @author huxinwu
     * @version 1.0
     * @date 2015-1-7
     **/
    public interface OnPhotoResultListener {
        public void onPhotoResult(Uri uri);

        public void onPhotoCancel();
    }

    public OnPhotoResultListener getOnPhotoResultListener() {
        return onPhotoResultListener;
    }

    public void setOnPhotoResultListener(OnPhotoResultListener onPhotoResultListener) {
        this.onPhotoResultListener = onPhotoResultListener;
    }


    /**
     * 计算图片的二次采样的缩放比例
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }



    /**
     * 图片的二次采样
     * @param path
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Context context,String path, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions

        reqWidth = DensityUtils.dp2px(context,reqWidth);
        reqHeight = DensityUtils.dp2px(context,reqHeight);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public Bitmap decodeBitmap(String path,float width,float height){
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, op); //获取尺寸信息
        //获取比例大小
        op.inPreferredConfig = Bitmap.Config.RGB_565;
        int wRatio = (int)Math.ceil(op.outWidth/width);
        int hRatio = (int)Math.ceil(op.outHeight/height);
        Log.i("Log","wRatio:"+wRatio+";width:"+width+";hR:"+hRatio+";he:"+height);
        //如果超出指定大小，则缩小相应的比例
        if(wRatio > 1 && hRatio > 1){
            if(wRatio > hRatio){
                op.inSampleSize = wRatio;
            }else{
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(path, op);
        return bmp;
    }


    /**
     * 图片的二次采样
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inDither = false;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }




    /**
     * 返回路径下压缩size倍的图片
     * @param path
     * @param size
     * @return
     */
    public static Bitmap compressFile(String path ,int size){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        return  BitmapFactory.decodeFile(path,options);
    }


    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
//        InputStream input = ac.getContentResolver().openInputStream(uri);
//        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
//        onlyBoundsOptions.inJustDecodeBounds = true;
//        onlyBoundsOptions.inDither = true;//optional
//        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
//        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
//        input.close();
//        int originalWidth = onlyBoundsOptions.outWidth;
//        int originalHeight = onlyBoundsOptions.outHeight;
//        if ((originalWidth == -1) || (originalHeight == -1))
//            return null;
//        //图片分辨率以480x800为标准
//        float hh = 800f;//这里设置高度为800f
//        float ww = 480f;//这里设置宽度为480f
//        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//        int be = 1;//be=1表示不缩放
//        LogUtil.i("with:"+originalWidth+"heigh:"+originalHeight);
//        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
//            be = (int) (originalWidth / ww);
//        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
//            be = (int) (originalHeight / hh);
//        }
//        if (be <= 0)
//            be = 1;
//        //比例压缩
//        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//        bitmapOptions.inSampleSize = be;//设置缩放比例
//        LogUtil.i("be:"+be);
//        bitmapOptions.inDither = true;//optional
//        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
//        input = ac.getContentResolver().openInputStream(uri);
//        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
//        input.close();
//
//        return compressImage(bitmap);//再进行质量压缩
        return  getZoomImage(uri,100);
    }





    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        float v =  40000.0f / baos.toByteArray().length;

        if (v<1){
            baos.reset();
            int i = (int) (v * 100);
            image.compress(Bitmap.CompressFormat.JPEG, i, baos);
        }
//        int i = (int)((100 * 1024) % (float)baos.toByteArray().length*100);
//        while (baos.toByteArray().length/1024  > 50) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            baos.reset();//重置baos即清空baos
//            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;//每次都减少10
//            LogUtil.i("compress");
//        }

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }



    /**
     * 图片的缩放方法
     *
     * @param bitmap  ：源图片资源
     * @param maxSize ：图片允许最大空间  单位:KB
     * @return
     */
    public static Bitmap getZoomImage(Uri url, double maxSize) {
        Bitmap bitmap = BitmapFactory.decodeFile(url.getPath());

        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        // 单位：从 Byte 换算成 KB
        double currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间,如果大于则压缩,小于则不压缩
        while (currentSize > maxSize) {
            // 计算bitmap的大小是maxSize的多少倍
            double multiple = currentSize / maxSize;
            // 开始压缩：将宽带和高度压缩掉对应的平方根倍
            // 1.保持新的宽度和高度，与bitmap原来的宽高比率一致
            // 2.压缩后达到了最大大小对应的新bitmap，显示效果最好
            bitmap = getZoomImage(bitmap, bitmap.getWidth() / Math.sqrt(multiple), bitmap.getHeight() / Math.sqrt(multiple));
            currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        }
        return bitmap;
    }
    /**
     * 图片的缩放方法
     *
     * @param bitmap  ：源图片资源
     * @param maxSize ：图片允许最大空间  单位:KB
     * @return
     */
    public static Bitmap getZoomImage(Bitmap bitmap, double maxSize) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        // 单位：从 Byte 换算成 KB
        double currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间,如果大于则压缩,小于则不压缩
        while (currentSize > maxSize) {
            // 计算bitmap的大小是maxSize的多少倍
            double multiple = currentSize / maxSize;
            // 开始压缩：将宽带和高度压缩掉对应的平方根倍
            // 1.保持新的宽度和高度，与bitmap原来的宽高比率一致
            // 2.压缩后达到了最大大小对应的新bitmap，显示效果最好
            bitmap = getZoomImage(bitmap, bitmap.getWidth() / Math.sqrt(multiple), bitmap.getHeight() / Math.sqrt(multiple));
            currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        }
        return bitmap;
    }

    /**
     * 图片的缩放方法
     *
     * @param orgBitmap ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap getZoomImage(Bitmap orgBitmap, double newWidth, double newHeight) {
        if (null == orgBitmap) {
            return null;
        }
        if (orgBitmap.isRecycled()) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            return null;
        }

        // 获取图片的宽和高
        float width = orgBitmap.getWidth();
        float height = orgBitmap.getHeight();
        // 创建操作图片的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(orgBitmap, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * bitmap转换成byte数组
     *
     * @param bitmap
     * @param needRecycle
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap, boolean needRecycle) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
        }
        return result;
    }


}
