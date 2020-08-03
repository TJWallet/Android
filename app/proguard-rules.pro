# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# 代码混淆压缩比，在0~7之间
-optimizationpasses 5
# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames
# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses
# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify
-verbose
# 避免混淆泛型
-keepattributes Signature


# 保留R下面的资源
-keep class **.R$* {*;}
# 保留四大组件，自定义的Application等这些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
   private static final java.io.ObjectStreamField[]   serialPersistentFields;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
}
# For using GSON @Expose annotation
-keepattributes *Annotation*

-keep class com.tianji.tjwallet.entity.**{ *; }
-keep class com.tianji.blockchainwallet.entity.**{ *; }
-keep class com.tianji.tjwallet.restful.**{ *; }
-keep class com.tianji.tjwallet.utils.HttpVolley{ *; }
-keep class com.tianji.tjwallet.sharepreferences.**{ *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
# Gson specific classes
-keep class com.google.gson.stream.** { *; }

-keep class com.google.gson.examples.models.** { *; }
-keep interface com.google.gson.examples.models.** { *; }
-keep class com.google.gson.examples.upgrade.internal.VersionInfo {*;}

-keep class com.android.volley.** {*;}
-keepclassmembers enum com.android.volley.**{ *; }
-keepclassmembers enum java.net.HttpURLConnection{ *; }
-keep class java.net.HttpURLConnection {*;}

-keepclassmembers enum * { *; }
-keepclassmembers enum com.tianji.tjwallet.entity.** { *; }
-keepclassmembers enum com.tianji.tjwallet.sharepreferences.**{ *; }
-keepclassmembers enum com.tianji.tjwallet.utils.HttpVolley{ *; }
-keepclassmembers enum com.tianji.tjwallet.restful.**{ *; }


-keep public class org.web3j.crypto.**{*;}



-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}