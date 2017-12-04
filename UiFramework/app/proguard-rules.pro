# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\vicmob_yf002\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#--------------------------------------实体类----------------------------
#-keep class com.base.** { *; }
#-keep class com.dao.** { *; }
#-keep class com.db.** { *; }
-keep class com.entity.** { *; }
#-keep class com.Interface.View.** { *; }
#-keep class com.MyApp.** { *; }
#-keep class com.service.** { *; }
#-keep class com.ui.activity.** { *; }
#-keep class com.ui.Adapter.** { *; }
#-keep class com.ui.fragment.** { *; }
#-keep class com.ui.view.** { *; }
#-keep class com.ui.view.swipebacklayout.** { *; }
#-keep class com.ui.view.swipedeletelayout.** { *; }
#-keep class com.utils.** { *; }
#-keep class com.widget.** { *; }
#-------------------------------------------------------------------------------------
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

-dontwarn com.nineoldandroids.*
-keep class com.nineoldandroids.** {*;}

-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** {*;}

# volley
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* {*;}
-keep class com.android.volley.Request$* {*;}
-keep class com.android.volley.RequestQueue$* {*;}
-keep class com.android.volley.toolbox.HurlStack$* {*;}
-keep class com.android.volley.toolbox.ImageLoader$* {*;}
-keep class org.apache.http.** {*;}

-keep class com.tapadoo.**{*;}
-keep class com.bigkoo.pickerview.**{*;}
-keep class android.support.graphics.drawable.**{*;}
-keep class android.support.v7**{*;}
-keep class com.joanzapata.android.**{*;}
 -dontwarn butterknife.internal.**

# -keep class butterknife.** { *; }
#   -dontwarn butterknife.internal.**
#   -keep class **$$ViewBinder { *; }
#   -keepclasseswithmembernames class * { @butterknife.* <fields>;}
#   -keepclasseswithmembernames class * { @butterknife.* <methods>;}

-keep class com.lljjcoder.**{*;}
-keep class android.support.design.**{*;}
# Gson
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }

# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }
# OrmLite
-keepattributes *DatabaseField*
-keepattributes *DatabaseTable*
-keepattributes *SerializedName*
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }

-keep class org.hamcrest.** { *; }
-keep class com.sevenheaven.iosswitch.** { *; }
-keep class org.junit.** { *; }
-keep class jxl.** { *; }
-keep class com.wang.avi.** { *; }#?????
-keep class org.apache.log4j.** { *; }
-keep class com.squareup.picasso.** { *; }
-keep class android.support.v7.** { *; }
-keep class android.support.annotation.** { *; }
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep class android.support.v3.** { *; }
-keep class android.support.graphics.drawable.** { *; }
-keep class android.support.transition.** { *; }
-keepattributes  *Annotation*
-keepclassmembers class com.package.bo


-keepclassmembers class * { @com.j256.ormlite.field.DatabaseField *;}
-keepattributes Signature
-keep public class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
-keep public class * extends com.j256.ormlite.android.apptools.OpenHelperManager
-keep class com.j256.ormlite.** { *; }
-keep class com.j256.ormlite.android.** { *; }
-keep class com.j256.ormlite.field.** { *; }
-keep class com.j256.ormlite.stmt.** { *; }
-dontwarn com.j256.ormlite.**
-dontwarn com.j256.ormlite.android.**
-dontwarn com.j256.ormlite.field.**
-dontwarn com.j256.ormlite.stmt.**

-keep  class java.** {*;}
-keep  class javax.** {*;}
-keep  class org.slf4j.** {*;}
-keep  class com.squareup.okhttp.** {*;}
-keep  class demo.** {*;}
-keep  class codehaus {*;}
-ignorewarnings
-keep class javax.ws.rs.** { *; }
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep public class * extends javax.**
-keep class javax.lang.mode.** { *; }
-keep class javax.annotation.processing.** { *; }
-keep class javax.tools.** { *; }
-keep class javax.persistence.** { *; }
-keep class java.nio.file.** { *; }
-keep class jorg.codehaus.mojo.** { *; }
-keep class javax.annotation.processing.Processor

-keep class org.apache.**{*;}
-dontwarn org.apache.**

-keep class android.backport.webp.** { *; }
-keep class org.xutils.** { *;}
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation { *; }
-keep public class * extends com.lidroid.xutils.**
-keep public interface org.xutils.** {*;}
-dontwarn org.xutils.**

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#----------------------------------------------------------------------------
#-----------------------------------------------------------------------------------------


