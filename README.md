<img src="https://raw.githubusercontent.com/pranavpandey/dynamic-support/master/graphics/dynamic-support.png" width="160" height="160" align="right" hspace="20">

# Dynamic Support

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/pranavpandey/dynamic-support.svg?branch=master)](https://travis-ci.org/pranavpandey/dynamic-support)
[![Download](https://api.bintray.com/packages/pranavpandey/android/dynamic-support/images/download.svg)](https://bintray.com/pranavpandey/android/dynamic-support/_latestVersion)

A complete library to build apps for Android 4.0 (API 14) and above devices with a built-in theme 
engine. It is built on top of the latest [app compat library](https://developer.android.com/topic/libraries/support-library/features.html) 
to provide best compatibility. 

>Since v2.0.0, it uses [AndroidX](https://developer.android.com/jetpack/androidx/) so, first
[migrate](https://developer.android.com/jetpack/androidx/migrate) your project to AndroidX.
 
<img src="https://raw.githubusercontent.com/pranavpandey/dynamic-support/master/graphics/play/ads-screen-1.png" width="280" height="486"><img src="https://raw.githubusercontent.com/pranavpandey/dynamic-support/master/graphics/play/ads-screen-3.png" width="280" height="486"><img src="https://raw.githubusercontent.com/pranavpandey/dynamic-support/master/graphics/play/ads-screen-4.png" width="280" height="486">

<img src="https://raw.githubusercontent.com/pranavpandey/dynamic-support/master/graphics/play/ads-screen-5.png" width="280" height="486"><img src="https://raw.githubusercontent.com/pranavpandey/dynamic-support/master/graphics/play/ads-screen-6.png" width="280" height="486"><img src="https://raw.githubusercontent.com/pranavpandey/dynamic-support/master/graphics/play/ads-screen-7.png" width="280" height="486">

---

## Contents

- [Installation](https://github.com/pranavpandey/dynamic-support#installation)
- [Usage](https://github.com/pranavpandey/dynamic-support#usage)
    - [Theme engine](https://github.com/pranavpandey/dynamic-support#theme-engine)
    - [Background aware](https://github.com/pranavpandey/dynamic-support#background-aware)
    - [Sample](https://github.com/pranavpandey/dynamic-support#sample)
    - [Proguard](https://github.com/pranavpandey/dynamic-support#proguard)
- [License](https://github.com/pranavpandey/dynamic-support#license)

---

## Installation

It can be installed by adding the following dependency to your `build.gradle` file:

```groovy
dependencies {
    // For AndroidX enabled projects.
    implementation 'com.pranavpandey.android:dynamic-support:2.3.0'

    // For legacy projects.
    implementation 'com.pranavpandey.android:dynamic-support:1.3.0'
}
```

---

## Usage
It is a collection of activities, fragments, widgets, views and some utility functions required to 
build a standard Android app. It also provides some in-built use cases like an intro screen, 
drawer activity, about screen, collapsing app bar, bottom navigation, color picker, multiple 
locales, runtime permissions, etc. which can be used and customised according to the need.

> For complete reference, please read the [documentation](https://pranavpandey.github.io/dynamic-support).

### Theme engine
Each activity and widget can be themed by using the in-built theme engine with background aware 
functionality to avoid any visibility issues. Colors can be selected by using the provided 
[material design colors](https://material.io/guidelines/style/color.html) or by selecting a custom 
color from the in-built picker which supports HEX, HSV and ARGB values.

### Background aware
Below are the same colors applied on the light and dark backgrounds respectively. But the final 
color is adjusted according to the colored view background to provide best visibility.

<img src="https://raw.githubusercontent.com/pranavpandey/dynamic-support/master/graphics/ads-bg-aware-1.png" width="280" height="486"><img src="https://raw.githubusercontent.com/pranavpandey/dynamic-support/master/graphics/ads-bg-aware-2.png" width="280" height="486"><img src="https://raw.githubusercontent.com/pranavpandey/dynamic-support/master/graphics/ads-bg-aware-3.png" width="280" height="486">

### Sample
This library is fully commented so, please check the individual classes or files for the 
documentation. Basic documentation will be available soon.

Checkout the `sample` to know more about the basic implementation. 

### Proguard
This library uses reflection at some places to theme widgets at runtime. So, their original name
must be preserved to theme them properly. It will automatically apply the appropriate rules if 
proguard is enabled in the project.

The following rules will be applied by this library:

```yml
dependencies {
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
    
    # Gson uses generic type information stored in a class file when working with fields.
    # Proguard removes such information by default, so configure it to keep all of it.
    -keepattributes Signature
    
    # For using GSON @Expose annotation
    -keepattributes *Annotation*
    
    # Gson specific classes
    -keep class sun.misc.Unsafe { *; }
    -keep class com.google.gson.** { *; }
}
```

---

## Apps using Dynamic Support

All of my apps are using some or all part of this library. You can download them from Play Store. 
Please email me if you are using this library and want to feature your app here. Also, please 
checkout the `Rotation` and `Everyday` apps to experience the full potential of this library.

- [Rotation - Orientation Manager](https://play.google.com/store/apps/details?id=com.pranavpandey.rotation)
- [Everyday - Calendar Widget](https://play.google.com/store/apps/details?id=com.pranavpandey.calendar)

---

## Translations
- German (de) - Flubberlutsch
- Indonesian (in) - Gamal Kevin A
- Italian (it) - Nicola
- Spanish (es) - Dave
- Turkish (tr) - Fatih Fırıncı
- Chinese (Simplified) (zh-rCN) - John Canny

---

## Author

Pranav Pandey

[![GitHub](https://img.shields.io/github/followers/pranavpandey?label=GitHub&style=social)](https://github.com/pranavpandey)
[![Follow on Twitter](https://img.shields.io/twitter/follow/pranavpandeydev?label=Follow&style=social)](https://twitter.com/intent/follow?screen_name=pranavpandeydev)
[![Donate via PayPal](https://img.shields.io/static/v1?label=Donate&message=PayPal&color=blue)](https://paypal.me/pranavpandeydev)

---

## License

    Copyright 2018 Pranav Pandey

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
