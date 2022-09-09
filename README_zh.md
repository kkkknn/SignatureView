# kkkkknSignatureView

[![](https://jitpack.io/v/kkkknn/kkkkknSignatureView.svg)](https://jitpack.io/#kkkknn/kkkkknSignatureView)

<img src="https://visitor-badge.glitch.me/badge?page_id=kkkknn.kkkkknSignatureView/&right_color=blue" />

一个开源的android 签字控件

## 示例

这里使用的是三星note10+设备演示的

**速度绘制**

![速度绘制](https://github.com/kkkknn/kkkkknSignatureView/blob/master/images/speed.gif)

**压感绘制（需要设备支持压感）**

![压感绘制](https://github.com/kkkknn/kkkkknSignatureView/blob/master/images/press.gif)

## 安装

**gradle** 

添加源

~~~
repositories {
    maven { url 'https://jitpack.io' }
}
~~~

添加依赖

~~~
dependencies {
	implementation 'com.github.kkkknn:kkkkknSignatureView:${LATEST_VERSION}'
}
~~~



## 如何使用

### xml

~~~
<com.kkkkkn.signatureView.SignView
        android:layout_margin="10dp"
        android:id="@+id/sign_view"
        app:maxPenWidth="20"
        app:minPenWidth="1"
        app:zoom="0.9"
        app:backgroundColor="@color/backGroundColor"
        app:penColor="@color/penColor"
        app:drawType="speed"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
~~~

**属性说明**

| 参数名          | 参数说明                                                     |
| --------------- | ------------------------------------------------------------ |
| maxPenWidth     | 最大线宽                                                     |
| minPenWidth     | 最小线宽                                                     |
| zoom            | drawType：speed 基于上一点宽度的缩小比率0.1~1<br />drawType:  其他类型 缩放比例 |
| backgroundColor | 背景色                                                       |
| penColor        | 笔颜色                                                       |
| drawType        | 绘制类型：<br />speed  速度绘制<br />press  压力绘制<br />denseness  密集点绘制（用于触摸点坐标密集情况，例如直接从event事件读取坐标事件） |

### activity

[详见demo内activity](https://github.com/kkkknn/kkkkknSignatureView/blob/master/app/src/main/java/com/kkkkkn/kdraw/MainActivity.java)

**函数说明**

| 函数名                                 | 函数介绍                     | 参数                                                         | 返回值     |
| -------------------------------------- | ---------------------------- | ------------------------------------------------------------ | ---------- |
| void onTouch(PenPoint point)           | 添加触摸点，用于绘制         | point 坐标点实体类，[具体实现](https://github.com/kkkknn/kkkkknSignatureView/blob/master/app/SignatureView/src/main/java/com/kkkkkn/signatureView/PenPoint.java) | 无         |
| void clear()                           | 清空画布                     | 无                                                           | 无         |
| void setAllowTouch(boolean allowTouch) | 设置是否相应ontouchevent事件 | boolean                                                      | 无         |
| boolean isAllowTouch()                 | 是否允许相应ontouchevent事件 | 无                                                           | boolean    |
| Bitmap getSignatureBitmap()            | 获取当前的bitmap图像         | 无                                                           | bitmap图像 |

## 鸣谢

速度绘制是从这个项目搞的[android-signaturepad](https://github.com/gcacace/android-signaturepad)

## 许可

~~~
MIT License

Copyright (c) 2022 kkkkkn

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
~~~