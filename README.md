# kkkkknSignatureView

[**中文说明**](https://github.com/kkkknn/kkkkknSignatureView/blob/master/README_zh.md)

this is a android signature custom view

## Demo Show

this demo run on samsung note10+, the device is support pressure

**speed**

![speed](F:\Android_Study\KDraw\images\speed.gif)

**press（device must be support pressure）**

![press](F:\Android_Study\KDraw\images\press.gif)

## Install

**gradle**

~~~
repositories {
    maven { url 'https://jitpack.io' }
}
~~~

~~~
dependencies {
	implementation 'com.github.kkkknn:kkkkknSignatureView:${LATEST_VERSION}'
}
~~~

## How to use


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

**parameter description**

| parameter          | description                                                     |
| --------------- | ------------------------------------------------------------ |
| maxPenWidth     | the paint max width                                                     |
| minPenWidth     | the paint max width                                                     |
| zoom            | drawType：speed reduction factor based on the previous point range 0.1~1<br />drawType:  others zoom |
| backgroundColor | canvas color                                                       |
| penColor        | paint color                                                     |
| drawType        | type：<br />speed <br />press <br />denseness （Used in the case of dense touch point coordinates, such as directly reading coordinate events from event points） |

### activity

[See activity in the demo](https://github.com/kkkknn/kkkkknSignatureView/blob/master/app/src/main/java/com/kkkkkn/kdraw/MainActivity.java)

**function description**

| function                                 | function info                     | parameter                                                         | return     |
| -------------------------------------- | ---------------------------- | ------------------------------------------------------------ | ---------- |
| void onTouch(PenPoint point)           | if you don't want to use the onTouchEvent to listen for touch events,you can use this function to draw points           | custom point class [Entity](https://github.com/kkkknn/kkkkknSignatureView/blob/master/app/SignatureView/src/main/java/com/kkkkkn/signatureView/PenPoint.java) | void         |
| void clear()                           | clear canvas                     | void                                                           | 无         |
| void setAllowTouch(boolean allowTouch) | set use onTouchEvent  | boolean                                                      | 无         |
| boolean isAllowTouch()                 | get is use onTouchEvent | void                                                           | boolean    |
| Bitmap getSignatureBitmap()            | get the current bitmap        | void                                                           | bitmap |

## Thanks

speed draw is use the project [android-signaturepad](https://github.com/gcacace/android-signaturepad)


## License

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