package com.kkkkkn.signatureView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

class SpeedDraw implements BaseDraw {
    private Canvas mCanvas;
    private Bitmap cacheBitmap;
    private Paint mPaint;
    private int maxWidth=5,minWidth;
    private int penColor=Color.BLACK;
    private int backgroundColor=Color.WHITE;
    private float zoom=0.9f;

    //View state
    private final List<PenPoint> mPoints=new ArrayList<>();
    private float mLastVelocity;
    private float mLastWidth;
    // Cache
    private List<PenPoint> mPointsCache = new ArrayList<>();
    private ControlTimedPoints mControlTimedPointsCached = new ControlTimedPoints();
    private Bezier mBezierCached = new Bezier();

    @Override
    public void initDraw() {
        mCanvas=new Canvas();
        mPaint=new Paint();
        mPaint.setColor(penColor);

        //Fixed parameters
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        //设置防抖动
        mPaint.setDither(true);
        //设置圆角 美化
        //mPaint.setPathEffect(new CornerPathEffect(5) );
        //设置阴影 美化
        mPaint.setShadowLayer(1f,0,0,penColor);

    }

    @Override
    public void addPoint(PenPoint penPoint) {
        if(penPoint==null){
            return;
        }
        switch (penPoint.penType) {
            case PEN_DOWN:
                mPoints.clear();
                addTouchPoint(getNewPoint(penPoint.x, penPoint.y));
                break;

            case PEN_MOVE:

            case PEN_UP:
                addTouchPoint(getNewPoint(penPoint.x, penPoint.y));
                break;

            default:
                break;
        }

    }

    private void addTouchPoint(PenPoint newPoint) {
        mPoints.add(newPoint);

        int pointsCount = mPoints.size();
        if (pointsCount > 3) {

            ControlTimedPoints tmp = calculateCurveControlPoints(mPoints.get(0), mPoints.get(1), mPoints.get(2));
            PenPoint c2 = tmp.c2;
            recyclePoint(tmp.c1);

            tmp = calculateCurveControlPoints(mPoints.get(1), mPoints.get(2), mPoints.get(3));
            PenPoint c3 = tmp.c1;
            recyclePoint(tmp.c2);

            Bezier curve = mBezierCached.set(mPoints.get(1), c2, c3, mPoints.get(2));

            PenPoint startPoint = curve.startPoint;
            PenPoint endPoint = curve.endPoint;

            float velocity = endPoint.velocityFrom(startPoint);
            velocity = Float.isNaN(velocity) ? 0.0f : velocity;

            velocity = zoom * velocity
                    + (1 - zoom) * mLastVelocity;

            float newWidth = strokeWidth(velocity)-0.5f;

            addBezier(curve, mLastWidth, newWidth);

            mLastVelocity = velocity;
            mLastWidth = newWidth;

            recyclePoint(mPoints.remove(0));

            recyclePoint(c2);
            recyclePoint(c3);

        } else if (pointsCount == 1) {
            PenPoint firstPoint = mPoints.get(0);
            mPoints.add(getNewPoint(firstPoint.x, firstPoint.y));
        }
    }

    private void recyclePoint(PenPoint point) {
        mPointsCache.add(point);
    }

    private PenPoint getNewPoint(float x, float y) {
        int mCacheSize = mPointsCache.size();
        PenPoint penPoint;
        if (mCacheSize == 0) {
            // Cache is empty, create a new point
            penPoint = new PenPoint();
        } else {
            // Get point from cache
            penPoint = mPointsCache.remove(mCacheSize - 1);
        }

        return penPoint.set(x, y);
    }

    private void addBezier(Bezier curve, float startWidth, float endWidth) {
        float originalWidth = mPaint.getStrokeWidth();
        float widthDelta = endWidth - startWidth;
        float drawSteps = (float) Math.ceil(curve.length());

        for (int i = 0; i < drawSteps; i++) {
            // Calculate the Bezier (x, y) coordinate for this step.
            float t = ((float) i) / drawSteps;
            float tt = t * t;
            float ttt = tt * t;
            float u = 1 - t;
            float uu = u * u;
            float uuu = uu * u;

            float x = uuu * curve.startPoint.x;
            x += 3 * uu * t * curve.control1.x;
            x += 3 * u * tt * curve.control2.x;
            x += ttt * curve.endPoint.x;

            float y = uuu * curve.startPoint.y;
            y += 3 * uu * t * curve.control1.y;
            y += 3 * u * tt * curve.control2.y;
            y += ttt * curve.endPoint.y;

            // Set the incremental stroke width and draw.
            mPaint.setStrokeWidth(startWidth + ttt * widthDelta);
            mCanvas.drawPoint(x, y, mPaint);
        }

        mPaint.setStrokeWidth(originalWidth);
    }

    private ControlTimedPoints calculateCurveControlPoints(PenPoint s1, PenPoint s2, PenPoint s3) {
        float dx1 = s1.x - s2.x;
        float dy1 = s1.y - s2.y;
        float dx2 = s2.x - s3.x;
        float dy2 = s2.y - s3.y;

        float m1X = (s1.x + s2.x) / 2.0f;
        float m1Y = (s1.y + s2.y) / 2.0f;
        float m2X = (s2.x + s3.x) / 2.0f;
        float m2Y = (s2.y + s3.y) / 2.0f;

        float l1 = (float) Math.sqrt(dx1 * dx1 + dy1 * dy1);
        float l2 = (float) Math.sqrt(dx2 * dx2 + dy2 * dy2);

        float dxm = (m1X - m2X);
        float dym = (m1Y - m2Y);
        float k = l2 / (l1 + l2);
        if (Float.isNaN(k)){
            k = 0.0f;
        }
        float cmX = m2X + dxm * k;
        float cmY = m2Y + dym * k;

        float tx = s2.x - cmX;
        float ty = s2.y - cmY;

        return mControlTimedPointsCached.set(getNewPoint(m1X + tx, m1Y + ty), getNewPoint(m2X + tx, m2Y + ty));
    }

    private float strokeWidth(float velocity) {
        return Math.max(maxWidth / (velocity + 1), minWidth);
    }

    @Override
    public void clear() {
        mPoints.clear();
        mLastVelocity = 0;
        mLastWidth = (minWidth + maxWidth) / 2f;

        if (mCanvas != null) {
            mCanvas.drawColor(backgroundColor);
        }
    }


    @Override
    public void setBitmap(Bitmap bitmap) {
        cacheBitmap=bitmap;
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
