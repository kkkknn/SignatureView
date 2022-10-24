package com.kkkkkn.signatureView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kkkkkn
 */
public class SignView extends SurfaceView implements SurfaceHolder.Callback{
    private static final String TAG = "SignView";
    /**
     * 默认绘制方式：速度
     */
    private int drawType;
    /**
     * 最大宽度
     */
    private int maxWidth;
    /**
     * 最小宽度
     */
    private int minWidth;
    /**
     * 是否允许坐标事件
     */
    private boolean allowTouch=true;

    private int penColor;
    private int backgroundColor;
    private Thread canvasDraw;
    private boolean update=false;
    private BaseDraw baseDraw;
    private Bitmap cacheBitmap;
    private boolean drawing=false;
    private final Paint mPaint=new Paint();
    private float zoom=1;


    public SignView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取相应自定义属性
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.SignView);
        drawType = typedArray.getInt(R.styleable.SignView_drawType, 0);
        penColor = typedArray.getColor(R.styleable.SignView_penColor, Color.BLACK);
        backgroundColor = typedArray.getColor(R.styleable.SignView_backgroundColor, Color.WHITE);
        maxWidth = typedArray.getInt(R.styleable.SignView_maxPenWidth, 5);
        minWidth = typedArray.getInt(R.styleable.SignView_minPenWidth, 0);
        zoom = typedArray.getFloat(R.styleable.SignView_zoom, 0.9f);
        allowTouch = typedArray.getBoolean(R.styleable.SignView_allowTouch, true);

        SurfaceHolder mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        this.post(() -> {
            if(cacheBitmap==null){
                cacheBitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
            }
            switch (drawType){
                case 1:
                    baseDraw = new PressDraw();
                    break;
                case 2:
                    baseDraw = new DensenessDraw();
                    break;
                default:
                    baseDraw = new SpeedDraw();
                    break;

            }
            baseDraw.initDraw();
            baseDraw.setPenColor(penColor);
            baseDraw.setBackGroundColor(backgroundColor);
            baseDraw.setZoom(zoom);
            baseDraw.setBitmap(cacheBitmap);
            baseDraw.setWidthRange(maxWidth,minWidth);
        });
    }

    public void setZoom(float zoom){
        if(baseDraw==null){
            return;
        }
        baseDraw.setZoom(zoom);
    }

    public void setWidthRange(int maxWidth,int minWidth){
        if(baseDraw==null){
            return;
        }
        baseDraw.setWidthRange(maxWidth,minWidth);
    }

    /**
     * 手动添加坐标点
     * @param point 坐标点数据
     */
    public void onTouch(PenPoint point){
        if(point==null){
            return;
        }
        baseDraw.addPoint(point);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()){
            return false;
        }
        if(event.getPointerCount() > 1){
            return false;
        }
        if(!allowTouch){
            performClick();
            return super.onTouchEvent(event);
        }
        PenPoint point =new PenPoint();
        point.x=event.getX();
        point.y=event.getY();
        point.pressure=event.getPressure();
        point.timestamp=System.currentTimeMillis();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                point.penType= PenPoint.PEN_TYPE.PEN_DOWN;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                point.penType=PenPoint.PEN_TYPE.PEN_MOVE;
                break;
            case MotionEvent.ACTION_UP:
                point.penType= PenPoint.PEN_TYPE.PEN_UP;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            default:
                break;
        }

        //发送坐标点事件
        if(baseDraw!=null){
            baseDraw.addPoint(point);
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void clear() {
        baseDraw.clear();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        drawing=true;
        canvasDraw=new Thread(){
            @Override
            public void run() {
                while (drawing){
                    if(!update){
                        long start_time = System.currentTimeMillis();

                        Canvas canvas=getHolder().lockCanvas();
                        if(canvas!=null){
                            if (cacheBitmap!=null){
                                canvas.drawBitmap(cacheBitmap,0,0,mPaint);
                            }
                            //释放canvas对象并提交画布
                            getHolder().unlockCanvasAndPost(canvas);
                        }

                        long end_time = System.currentTimeMillis();
                        long value_time=end_time - start_time;
                        if (value_time < 30) {
                            try {
                                Thread.sleep(30 - (value_time));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }
                Log.i(TAG, "run: 线程执行完成");
            }
        };
        canvasDraw.start();
        Log.i(TAG, "surfaceCreated: 线程创建s");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //关闭线程
        drawing=false;
    }

    public void setAllowTouch(boolean allowTouch){
        this.allowTouch=allowTouch;
    }

    public boolean isAllowTouch(){
        return allowTouch;
    }

    public Bitmap getSignatureBitmap() {
        return Bitmap.createBitmap(cacheBitmap, 0, 0,cacheBitmap.getWidth(), cacheBitmap.getHeight());
    }
}
