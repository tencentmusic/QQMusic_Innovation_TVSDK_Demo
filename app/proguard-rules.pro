#指定代码的压缩级别
-optimizationpasses 5

#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

 #优化  不优化输入的类文件
-dontoptimize

 #预校验
-dontpreverify

 #混淆时是否记录日志
-verbose

 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#保护注解
-keepattributes *Annotation*
#保留源文件名和行号，便于定位外网错误
-keepattributes SourceFile,LineNumberTable

# 保持哪些类不被混淆，这段是官方写法，其中有些未必有用，放上无妨
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.view.View
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
#gson
-keepattributes Signature
-keep class com.google.gson.Gson {*;}
#native jni接口
-keepclasseswithmembernames class * {
    native <methods>;
}



#播放引擎相关
-keep class com.tencent.qqmusic.mediaplayer.util.Util4NativeCommon
-keep class com.tencent.qqmusic.mediaplayer.formatdetector.FormatDetector
-keep class com.tencent.qqmusic.mediaplayer.formatdetector.FormatDetector{*;}
-keep class com.tencent.qqmusic.mediaplayer.codec.BaseDecoder
-keep class com.tencent.qqmusic.mediaplayer.codec.NativeDecoder{*;}
-keep class com.tencent.qqmusic.mediaplayer.codec.BaseDecoder { *; }
-keep class * extends com.tencent.qqmusic.mediaplayer.codec.BaseDecoder
-keep class * extends com.tencent.qqmusic.mediaplayer.codec.BaseDecoder{*;}
-keep class com.tencent.qqmusic.mediaplayer.AudioInformation
-keep class com.tencent.qqmusic.mediaplayer.AudioInformation { *; }
-keep class com.tencent.qqmusic.mediaplayer.upstream.IDataSource
-keep class com.tencent.qqmusic.mediaplayer.upstream.IDataSource {
    public <methods>;
}
-keep class com.tencent.qqmusic.mediaplayer.AudioPlayerConfigure
-keep class com.tencent.qqmusic.mediaplayer.AudioPlayerConfigure{ *; }
-keep class com.tencent.qqmusic.mediaplayer.NLog
-keep class com.tencent.qqmusic.mediaplayer.NLog { *; }
-keep class com.tencent.qqmusic.mediaplayer.NativeLog
-keep class com.tencent.qqmusic.mediaplayer.NativeLog { *; }

#忽略警告
#-ignorewarning

#####################记录生成的日志数据,gradle build时在本项目根目录输出################

#apk 包内所有 class 的内部结构
#-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt

#如果引用了v4或者v7包
-dontwarn android.support.**

############<span></span>混淆保护自己项目的部分代码以及引用的第三方jar包library-end##################

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#协程
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}



-keep class com.tencent.qqmusictvsdk.account.** { *; }
-keep class com.tencent.qqmusictvsdk.auth.** { *; }
-keep class com.tencent.qqmusictvsdk.lyric.** { *; }
-keep class com.tencent.qqmusictvsdk.network.** { *; }
-keep class com.tencent.qqmusictvsdk.player.** { *; }
-keep class com.tencent.config.** { *; }
-keep class com.tencent.qqmusiccommon.** { *; }
-keep class com.tencent.qqmusictvsdk.InvalidTokenException { *; }
-keep class com.tencent.qqmusictvsdk.ITokenProvider { *; }
-keep class com.tencent.qqmusictvsdk.QQMusicSDK { *; }
-keep class com.tencent.qqmusictvsdk.SDKException { *; }
-keep class com.tencent.qqmusictvsdk.Token { *; }