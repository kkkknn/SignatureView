package com.kkkkkn.signatureView;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kkkkkn
 */
public interface BaseDraw {
    void initDraw();
    void addPoint(PenPoint penPoint);
    void clear();
    void setBitmap(Bitmap bitmap);
    void setWidthRange(int maxWidth,int minWidth);
    void setPenColor(int color);
    void setZoom(float zoom);
    void setBackGroundColor(int penColor);
}
