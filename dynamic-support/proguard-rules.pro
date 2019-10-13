# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/pranav/Library/Android/sdk/tools/proguard/proguard-android.txt
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

# Keep application class.
-keep public class * extends android.app.Application

# Keep methods in Activity that could be used in the XML.
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# Keep support library classes.
#-keep class android.support.v4.widget.** { *; }
#-keep class android.support.v7.widget.** { *; }
#-keep class android.support.design.widget.** { *; }
#-keep class android.support.design.internal.** { *; }

# Keep AndroidX classes.
-keep class androidx.core.widget.** { *; }
#-keep class androidx.appcompat.widget.** { *; }
-keep class androidx.appcompat.view.menu.** { *; }
-keep class androidx.recyclerview.widget.** { *; }
-keep class androidx.viewpager.widget.** { *; }

# Keep Material Components classes.
-keep class com.google.android.material.internal.** { *; }
-keep class com.google.android.material.navigation.** { *; }
-keep class com.google.android.material.textfield.** { *; }

# Keep all the Dynamic Support models.
-keep class com.pranavpandey.android.dynamic.support.model.** { *; }