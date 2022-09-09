package com.kkkkkn.signatureView;


import android.graphics.Path;


public class PenPath {
    public Path path;
    public PenPoint startPoint, controlPoint, endpoint;

    public PenPath() {
        path = new Path();
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }


    public void drawPath() {
        /*if (isLine()) {
            drawLinePath();
        } else {
            drawCurvePath();
        }*/
        drawLinePath();
    }

    //生成路径
    private void drawLinePath() {
        if (startPoint == null || controlPoint == null || endpoint == null) {
            return;
        }

        path.moveTo(startPoint.x, startPoint.y);
        if (startPoint.hasSim()) {
            path.lineTo(startPoint.sim_low_x, startPoint.sim_low_y);
        }

        if (controlPoint.hasSim()) {
            path.lineTo(controlPoint.sim_low_x, controlPoint.sim_low_y);
        }

        if (endpoint.hasSim()) {
            path.lineTo(endpoint.sim_low_x, endpoint.sim_low_y);
            path.lineTo(endpoint.x, endpoint.y);
            path.lineTo(endpoint.sim_up_x, endpoint.sim_up_y);
        } else {
            path.lineTo(endpoint.x, endpoint.y);
        }

        if (controlPoint.hasSim()) {
            path.lineTo(controlPoint.sim_up_x, controlPoint.sim_up_y);
        }
        if (startPoint.hasSim()) {
            path.lineTo(startPoint.sim_up_x, startPoint.sim_up_y);
        }
        path.lineTo(startPoint.x, startPoint.y);

        path.close();

    }


    private void drawCurvePath() {
        if (startPoint == null || controlPoint == null || endpoint == null) {
            return;
        }

        path.moveTo(startPoint.x, startPoint.y);
        if (startPoint.hasSim()) {
            path.lineTo(startPoint.sim_low_x, startPoint.sim_low_y);
        }

        if (controlPoint.hasSim()) {
            if (endpoint.hasSim()) {
                path.quadTo(controlPoint.sim_low_x, controlPoint.sim_low_y, endpoint.sim_low_x, endpoint.sim_low_y);
                path.lineTo(endpoint.x, endpoint.y);
                path.lineTo(endpoint.sim_up_x, endpoint.sim_up_y);

            } else {
                path.quadTo(controlPoint.sim_low_x, controlPoint.sim_low_y, endpoint.x, endpoint.y);

            }

            if (startPoint.hasSim()) {
                path.quadTo(controlPoint.sim_up_x, controlPoint.sim_up_y, startPoint.sim_up_x, startPoint.sim_up_y);
            } else {
                path.quadTo(controlPoint.sim_up_x, controlPoint.sim_up_y, startPoint.x, startPoint.y);
            }
        } else {
            if (endpoint.hasSim()) {
                path.quadTo(controlPoint.x, controlPoint.y, endpoint.sim_low_x, endpoint.sim_low_y);
                path.lineTo(endpoint.x, endpoint.y);
                path.lineTo(endpoint.sim_up_x, endpoint.sim_up_y);
            } else {
                path.quadTo(controlPoint.x, controlPoint.y, endpoint.x, endpoint.y);

            }

            if (startPoint.hasSim()) {
                path.quadTo(controlPoint.x, controlPoint.y, startPoint.sim_up_x, startPoint.sim_up_y);
                path.lineTo(startPoint.x, startPoint.y);
            } else {
                path.quadTo(controlPoint.x, controlPoint.y, startPoint.x, startPoint.y);
            }
        }

    }

    //判断3点是否在一条直线
    private boolean isLine() {
        if (startPoint == null || controlPoint == null || endpoint == null) {
            return false;
        }
        int angle = getIn_angle((int) startPoint.x,(int) startPoint.y,(int) controlPoint.x,(int) controlPoint.y,(int) endpoint.x,(int) endpoint.y);
        //return  (endpoint.x - startPoint.x) * (startPoint.y - controlPoint.y) == (startPoint.x - controlPoint.x) * (endpoint.y - startPoint.y);
        //return ((int) endpoint.y - (int) startPoint.y) * ((int) controlPoint.x - (int) startPoint.x) - ((int) controlPoint.y - (int) startPoint.y) * ((int) endpoint.x - (int) startPoint.x) == 0;
        return angle<45;
    }

    private static int getIn_angle(int x1, int x2, int y1, int y2, int z1, int z2) {
        //向量的点乘
        int t = (y1 - x1) * (z1 - x1) + (y2 - x2) * (z2 - x2);

        //为了精确直接使用而不使用中间变量
        //包含了步骤：A=向量的点乘/向量的模相乘
        //          B=arccos(A)，用反余弦求出弧度
        //          result=180*B/π 弧度转角度制
        int result = (int) (180 * Math.acos(
                t / Math.sqrt
                        ((Math.abs((y1 - x1) * (y1 - x1)) + Math.abs((y2 - x2) * (y2 - x2)))
                                * (Math.abs((z1 - x1) * (z1 - x1)) + Math.abs((z2 - x2) * (z2 - x2)))
                        ))
                / Math.PI);
        //      pi   = 180
        //      x    =  ？
        //====> ?=180*x/pi

        return result;
    }
}
