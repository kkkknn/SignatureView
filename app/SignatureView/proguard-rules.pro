#---------------------------------基本指令以及一些固定不混淆的代码--开始--------------------------------

#<基本指令>
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#记录生成的日志数据,gradle build时在本项目根目录输出apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#</基本指令>


#<R文件>
-keep class **.R$* {
 *;
}
#</R文件>

#<enum>
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#</enum>

#<natvie>
-keepclasseswithmembernames class * {
    native <methods>;
}
#</natvie>

#---------------------------------基本指令以及一些固定不混淆的代码--结束-----------

#---------------------------------第三方包--开始-------------------------------

#<org.locationtech.jts:jts-core:1.19.0>
-dontwarn org.locationtech.jts.geom.**
-keep class org.locationtech.jts.geom.Coordinate { *;}
-keep class org.locationtech.jts.geom.LineSegment { *;}
#</org.locationtech.jts:jts-core:1.19.0>


#----------------------------------第三方包--结束--------------------------

#---------------------------------一些不要混淆的代码--开始-------------------


#<自定义View的类>
-keep class com.kkkkkn.signatureView.SignView {
   public *;
}

#</自定义View的类>

#---------------------------------一些不要混淆的代码--结束------------------