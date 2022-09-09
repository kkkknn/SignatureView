package com.kkkkkn.signatureView;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineSegment;

public class GeometryMathUtil {
    // 方法描述: 线段角度
    private static double angle(Coordinate p0, Coordinate p1){
        LineSegment ls=new LineSegment(p0,p1);
        return ls.angle();
    }

    // 方法描述: 移动点
    private static Coordinate movePoint(Coordinate p,double angle,double distance){
        double sin=Math.sin(angle);
        double a= distance*sin;
        double cos=Math.cos(angle);
        double b= distance*cos;
        return new Coordinate(p.x+ (float) b,p.y+ (float) a);
    }


    //给出2控制点，求出上方模拟点
    public static Coordinate getUpperSimulation(Coordinate p0,Coordinate p1,double distance){
        //当c点在b点上方
        double angle=angle(p0,p1)+Math.PI/2;
        return movePoint(p1,angle,distance);
    }

    //给出2控制点，求出下方模拟点
    public static Coordinate getLowerSimulation(Coordinate p0,Coordinate p1,double distance){
        //当c点在b点下方
        double angle=angle(p1,p0)+Math.PI/2;
        return movePoint(p1,angle,distance);
    }


}
