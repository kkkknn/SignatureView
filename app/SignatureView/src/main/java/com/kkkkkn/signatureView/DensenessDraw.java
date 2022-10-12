package com.kkkkkn.signatureView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;

import java.util.LinkedList;

public class DensenessDraw implements BaseDraw {
    private Paint mPaint;
    private Canvas mCanvas;
    private int penColor = Color.BLACK;
    private int backgroundColor = Color.WHITE;
    private int maxWidth = 5, minWidth;
    private Path path = new Path();
    private static final float DIFF = 0.1f;
    private PenPoint lastPoint;
    private float lastWidth;
    private int drawPathCount;
    private float zoom=1f;
    private Bitmap cacheBitmap;
    //笔轨迹、笔压力
    private final LinkedList<Path> pathLinkedList = new LinkedList<>();
    private final LinkedList<Float> widthLinkedList = new LinkedList<>();

    @Override
    public void initDraw() {
        mCanvas = new Canvas();
        mPaint = new Paint();
        //初始化画笔
        mPaint.setColor(penColor);
        //画笔设置
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);


        mPaint.setAlpha(0xFF);
        mPaint.setStrokeMiter(1f);
        //设置圆角 美化
        mPaint.setPathEffect(new CornerPathEffect(10));
        //设置阴影 美化
        mPaint.setShadowLayer(1, 0, 0,penColor);
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
    }

    @Override
    public void addPoint(PenPoint penPoint) {
        if (penPoint == null) {
            return;
        }

        //绘制当前点
        addPath(penPoint);
        drawPath();
    }

    private void drawPath() {
        for (int i = drawPathCount; i < pathLinkedList.size(); i++) {
            mPaint.setStrokeWidth(widthLinkedList.get(i));
            mCanvas.drawPath(pathLinkedList.get(i), mPaint);
            drawPathCount++;
        }
        if (!path.isEmpty()) {
            mPaint.setStrokeWidth(lastWidth);
            mCanvas.drawPath(path, mPaint);
        }
        mPaint.setStrokeWidth(0);

    }

    private void addPath(PenPoint penPoint) {
        if (penPoint.penType == PenPoint.PEN_TYPE.PEN_DOWN) {
            path.reset();
            path.moveTo(penPoint.x, penPoint.y);
            lastPoint = penPoint;
            lastWidth = penPoint.pressure*zoom;
        } else if (penPoint.penType == PenPoint.PEN_TYPE.PEN_MOVE) {
            float zlx = (penPoint.x + lastPoint.x) / 2;
            float zly = (penPoint.y + lastPoint.y) / 2;

            float valupz = penPoint.pressure*zoom;

            path.lineTo(zlx, zly);

            if (lastWidth != valupz) {
                widthLinkedList.add((float) valupz);
                pathLinkedList.add(path);
                path = new Path();
                path.moveTo(lastPoint.x, lastPoint.y);
                lastWidth = (float) valupz;
            }


            lastPoint = penPoint;
            lastPoint.x = zlx;
            lastPoint.y = zly;
        }
    }


    @Override
    public void clear() {
        //路径重置
        path.reset();
        pathLinkedList.clear();
        widthLinkedList.clear();
        drawPathCount = 0;
        if (mCanvas != null) {
            mCanvas.drawColor(backgroundColor);
        }
    }


    @Override
    public void setBitmap(Bitmap bitmap) {
        this.cacheBitmap = bitmap;
        mCanvas.setBitmap(cacheBitmap);
        mCanvas.drawColor(backgroundColor);
    }

    @Override
    public void setWidthRange(int maxWidth, int minWidth) {
        this.maxWidth = maxWidth;
        this.minWidth = minWidth;
    }

    @Override
    public void setPenColor(int color) {
        this.penColor = color;
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
