package com.kkkkkn.signatureView;

import org.locationtech.jts.geom.Coordinate;

import java.io.Serializable;
import java.util.Arrays;


/**
 * @author kkkkkn
 */
public class PenPoint implements Serializable {
    public float x;
    public float y;
    public float pressure;
    public long timestamp;
    public PEN_TYPE penType;
    public float sim_low_x,sim_low_y,sim_up_x,sim_up_y;

    public PenPoint(){

    }

    public PenPoint set(float x, float y){
        this.x=x;
        this.y=y;
        timestamp= System.currentTimeMillis();
        return this;
    }

    public PenPoint(String jsonStr) {
        //注意要裁去空格
        String[] arr=jsonStr.substring(1,jsonStr.length()-1).split(",");
        x=Float.parseFloat(arr[0].trim());
        y=Float.parseFloat(arr[1].trim());
        pressure=Float.parseFloat(arr[2].trim());
        int type=Integer.parseInt(arr[3].trim());
        switch (type){
            case 0:
                penType= PEN_TYPE.PEN_DOWN;
                break;
            case 1:
                penType= PEN_TYPE.PEN_MOVE;
                break;
            case 2:
                penType= PEN_TYPE.PEN_UP;
                break;
            default:
                break;
        }
        timestamp= System.currentTimeMillis();
    }

    public String toArrayString() {
        String[] arr=new String[5];
        arr[0]= String.valueOf(Float.valueOf(x));
        arr[1]=String.valueOf(Float.valueOf(y));
        arr[2]=String.valueOf(Float.valueOf(pressure));
        switch (penType){
            case PEN_UP:
                arr[3]= String.valueOf(Integer.valueOf(2));
                break;
            case PEN_DOWN:
                arr[3]=String.valueOf(Integer.valueOf(0));
                break;
            case PEN_MOVE:
                arr[3]=String.valueOf(Integer.valueOf(1));
                break;
            default:
                break;
        }
        arr[4]=String.valueOf(Long.valueOf(timestamp));
        return Arrays.toString(arr);
    }

    public String toArrayString(int offsetX,int offsetY) {
        String[] arr=new String[5];
        arr[0]= String.valueOf(Float.valueOf(x+offsetX));
        arr[1]=String.valueOf(Float.valueOf(y+offsetY));
        arr[2]=String.valueOf(Float.valueOf(pressure));
        switch (penType){
            case PEN_UP:
                arr[3]= String.valueOf(Integer.valueOf(2));
                break;
            case PEN_DOWN:
                arr[3]=String.valueOf(Integer.valueOf(0));
                break;
            case PEN_MOVE:
                arr[3]=String.valueOf(Integer.valueOf(1));
                break;
            default:
                break;
        }
        arr[4]=String.valueOf(Long.valueOf(timestamp));
        return Arrays.toString(arr);
    }

    public void setSimLow(Coordinate coordinate){
        this.sim_low_y=(float) coordinate.y;
        this.sim_low_x=(float) coordinate.x;
    }

    public void setSimUp(Coordinate coordinate){
        this.sim_up_y=(float) coordinate.y;
        this.sim_up_x=(float) coordinate.x;
    }

    /**
     * 是否含有模拟点
     */
    public boolean hasSim(){
        return sim_low_y != 0
                && sim_up_y != 0
                && sim_low_x != 0
                && sim_up_x != 0;
    }

    public enum PEN_TYPE{
        /**
         * 抬笔
         */
        PEN_UP,
        /**
         * 落笔
         */
        PEN_DOWN,
        /**
         * 移动
         */
        PEN_MOVE
    }


    public float velocityFrom(PenPoint start) {
        long diff = this.timestamp - start.timestamp;
        if(diff <= 0) {
            diff = 1;
        }
        float velocity = distanceTo(start) / diff;
        if (Float.isInfinite(velocity) || Float.isNaN(velocity)) {
            velocity = 0;
        }
        return velocity;
    }

    public float distanceTo(PenPoint point) {
        return (float) Math.sqrt(Math.pow(point.x - this.x, 2) + Math.pow(point.y - this.y, 2));
    }
}
