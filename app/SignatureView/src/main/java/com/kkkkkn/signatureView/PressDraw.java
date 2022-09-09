package com.kkkkkn.signatureView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.Log;

import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.LinkedList;

class PressDraw implements BaseDraw{
    public static final String TAG="PressDraw";
    private Paint mPaint;
    private Canvas mCanvas;
    private int penColor=Color.BLACK;
    private int backgroundColor=Color.WHITE;
    private int maxWidth=5,minWidth;
    private int drawPathCount;
    private Bitmap cacheBitmap;
    private float zoom=1f;
    //笔轨迹、笔压力
    private final LinkedList<PenPath> pathLinkedList=new LinkedList<>();
    private PenPoint startPoint,lastPoint,controlPoint;

    @Override
    public void initDraw() {
        mCanvas=new Canvas();
        mPaint=new Paint();
        //初始化画笔
        mPaint.setColor(penColor);
        //画笔设置
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);


        mPaint.setAlpha(0xFF);
        mPaint.setStrokeMiter(1f);
        //设置圆角 美化
        mPaint.setPathEffect(new CornerPathEffect(10) );
        //设置阴影 美化
        mPaint.setShadowLayer(1,0,0,penColor);
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
    }

    @Override
    public void addPoint(PenPoint penPoint) {
        if(penPoint==null){
            return;
        }

        //绘制当前点
        addPath(penPoint);
        drawPath();
    }


    private void drawPath() {
        for (int i = drawPathCount; i < pathLinkedList.size(); i++) {
            PenPath path=pathLinkedList.get(i);
            if(path!=null){
                //落笔时候绘制圆
                /*if(path.startPoint.penType== PenPoint.PEN_TYPE.PEN_DOWN && !path.startPoint.hasSim()){
                    canvas.drawCircle(path.endpoint.x,path.endpoint.y,(getPenWidth(path.endpoint.pressure,config)/2),paint);
                }*/
                mCanvas.drawPath(path.path, mPaint);

            }
            drawPathCount++;
        }

    }

    private void addPath(PenPoint point){
        if(point.penType== PenPoint.PEN_TYPE.PEN_DOWN){
            //重置相关数据
            startPoint=point;
            lastPoint=point;
            controlPoint=null;
            return;
        }else if(point.penType== PenPoint.PEN_TYPE.PEN_UP){
            return;
        }

        if (lastPoint!=null){
            //保留2位小数
            double value_line= point.pressure*zoom;
            if(value_line>maxWidth){
                value_line=maxWidth;
            }else if(value_line<minWidth){
                value_line=minWidth;
            }

            //根据三角函数算出第三点模拟点
            Coordinate start=new Coordinate(lastPoint.x,lastPoint.y);
            Coordinate end=new Coordinate(point.x,point.y);

            point.setSimUp(GeometryMathUtil.getUpperSimulation(start,end,value_line));
            point.setSimLow(GeometryMathUtil.getLowerSimulation(start,end,value_line));
            //Log.i("TAG", "pointMessage1:distance "+distance+"   distance_time  "+distance_time+"  value_line  "+value_line+"   line"+line+"   }}  "+point.pressure);
        }

        if(controlPoint==null){
            controlPoint=point;
        }else {
            PenPath path=new PenPath();
            path.startPoint=startPoint;
            path.controlPoint=controlPoint;
            path.endpoint=point;
            path.drawPath();
            if(!path.isEmpty()){
                pathLinkedList.add(path);
                startPoint=controlPoint;
                controlPoint=point;
            }

        }

        lastPoint=point;
    }


    @Override
    public void clear() {
        pathLinkedList.clear();
        drawPathCount=0;
        if(mCanvas!=null){
            mCanvas.drawColor(backgroundColor);
        }
    }


    @Override
    public void setBitmap(Bitmap bitmap) {
        this.cacheBitmap=bitmap;
        mCanvas.setBitmap(cacheBitmap);
        mCanvas.drawColor(backgroundColor);
    }

    @Override
    public void setWidthRange(int maxWidth, int minWidth) {
        this.maxWidth=maxWidth;
        this.minWidth=minWidth;
    }

    @Override
    public void setPenColor(int color) {
        this.penColor=color;
        mPaint.setColor(penColor);
        mPaint.setShadowLayer(1f,0,0,penColor);
    }

    @Override
    public void setZoom(float zoom) {
        this.zoom=zoom;
    }
    @Override
    public void setBackGroundColor(int color) {
        this.backgroundColor=color;
    }
}
