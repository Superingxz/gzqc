package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/6/29.
 * 图片自动放大缩小移动
 */
public class ZoomImageVIew extends ImageView implements ScaleGestureDetector.OnScaleGestureListener,View.OnTouchListener,ViewTreeObserver.OnGlobalLayoutListener{

    private static final float MAXSCLAE = 4.0f;
    private float initScale = 1.0f;
    private GestureDetector gestureDetector;
    private float[] matrixValues = new float[9];
    private boolean once = true;
    private Matrix mScaleMatrix = new Matrix();
    private ScaleGestureDetector scaleGestureDetector = null;
    private int mTouchSlop;

    public ZoomImageVIew(Context context) {
        super(context);
    }

    public ZoomImageVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setScaleType(ScaleType.MATRIX);
        scaleGestureDetector = new ScaleGestureDetector(getContext(), this);
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                float scale = getScale();
                float x = e.getX();
                float y = e.getY();
                if (scale>2.0f){
                    mScaleMatrix.postScale(initScale/scale,initScale/scale,x,y);
                }else {
                    mScaleMatrix.postScale(MAXSCLAE/scale,MAXSCLAE/scale,x,y);
                }
                checkBorad();
                setImageMatrix(mScaleMatrix);
                return true;
            }
        });
        this.setOnTouchListener(this);
        mTouchSlop=ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }

    public ZoomImageVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();

        if ((scale < MAXSCLAE && scaleFactor > 1.0f) || (scale > initScale && scaleFactor < 1.0f)) {

            if (scale * scaleFactor < initScale) {
                scaleFactor = initScale / scale;
            }
            if (scale * scaleFactor > MAXSCLAE) {
                scaleFactor = MAXSCLAE / scale;
            }

            mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkBorad();
            setImageMatrix(mScaleMatrix);
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    int lastPointCount;
    boolean isClag,isMoveLeftAndRight,isMoveTopAndBottom;
   float lastX,lastY,dx,dy;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        RectF rect = getRect();
        int width = getWidth();
        int height = getHeight();

        float x=0,y=0;

       int pointCount = event.getPointerCount();
        for (int i=0;i<pointCount;i++){
            x+=event.getX(i);
            y+=event.getY(i);
        }
        x=x/pointCount;
        y=y/pointCount;


        if (lastPointCount!=pointCount){
            isClag=false;
            lastX=x;
            lastY=y;
        }
      lastPointCount=pointCount;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                 if (rect.width()>width||rect.height()>height){
                   getParent().requestDisallowInterceptTouchEvent(true);
                 }
                   break;
            case MotionEvent.ACTION_MOVE:
                dx=x-lastX;
                dy=y-lastY;

                if (rect.width()>width||rect.height()>height){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (!isClag){
                    isClag=isClag(dx,dy);
                }
                if (isClag){

                    isMoveLeftAndRight=isMoveTopAndBottom=true;
                    if (rect.width()<width){
                        dx=0;
                        isMoveLeftAndRight=false;
                    }
                    if (rect.height()<height){
                        dy=0;
                        isMoveTopAndBottom=false;
                    }
                    mScaleMatrix.postTranslate(dx,dy);
                    checkMoveBroad(width,height);
                    setImageMatrix(mScaleMatrix);
                }
                lastX=x;
                lastY=y;
                break;
            case  MotionEvent.ACTION_UP:
            case  MotionEvent.ACTION_CANCEL:
                lastPointCount=0;
                break;
        }

        return true;
    }

    private void checkMoveBroad( int width, int height) {
        float tranX = 0,tranY=0;
        RectF rect = getRect();
        if (isMoveLeftAndRight){
            if (rect.left>0){
                tranX=  -rect.left;
            }
            if (rect.right<width){
                tranX= width-rect.right;
            }
        }
        if (isMoveTopAndBottom){
            if (rect.top>0){
                tranY=-rect.top;
            }
            if (rect.bottom<height){
                tranY=height-rect.bottom;
            }
        }
        mScaleMatrix.postTranslate(tranX,tranY);
    }

    private boolean isClag(float dx, float dy) {
        return Math.sqrt(dx*dx+dy*dy)>=mTouchSlop;
    }

    private float getScale() {
        mScaleMatrix.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X];
    }

    @Override
    public void onGlobalLayout() {

        if (once) {

                Drawable drawable = getDrawable();

                if (drawable == null) return;

                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();

                int width = getWidth();
                int height = getHeight();

                if (intrinsicWidth > width) {
                    initScale = width * 1.0f / intrinsicWidth;
                }

                if (intrinsicHeight > height) {
                    initScale = height * 1.0f / intrinsicHeight;
                }

                if (intrinsicWidth > width && intrinsicHeight > height) {
                    initScale = Math.min(width * 1.0f / intrinsicWidth, height * 1.0f / intrinsicHeight);
                }

                    mScaleMatrix.postTranslate((width-intrinsicWidth)/ 2, (height-intrinsicHeight) / 2);
                    mScaleMatrix.postScale(initScale, initScale, getWidth() / 2, getHeight() / 2);

                    setImageMatrix(mScaleMatrix);

            once=false;
        }
    }

    public  void checkBorad(){
        RectF rect = getRect();
        float  translateX=0;
        float translateY=0;

        int width = getWidth();
        int height = getHeight();

        if (width>rect.width()){
           int i= (int) (rect.left+rect.width()/2);
            translateX = width / 2 - i;
        }
        if (height>rect.height()){
            int y= (int) (rect.top+rect.height()/2);
            translateY=height/2-y;
        }
        if(width<rect.width()){
            if (rect.left>0){
                translateX=-rect.left;
            }
            if (rect.right<width){
                translateX=width-rect.right;
            }
        }
        if (height<rect.height()){
            if (rect.top>0){
                translateY=-rect.top;
            }
            if (rect.bottom<height){
                translateY=height-rect.bottom;
            }
        }

        mScaleMatrix.postTranslate(translateX,translateY);

    }

    public RectF  getRect(){
        RectF rectF = new RectF();
        Matrix matrix = mScaleMatrix;

        Drawable d = getDrawable();
        if (d==null)  {return null;}

        rectF.set(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
        matrix.mapRect(rectF);

        return rectF;
    }

    public  class AutoScaleRunnable implements Runnable {
        @Override
        public void run() {

        }
    }


}