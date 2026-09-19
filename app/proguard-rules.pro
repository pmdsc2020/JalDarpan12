# Keep the JavaScript bridge used for printing
-keepclassmembers class com.dsc.jaldarpan.MainActivity$JsBridge {
   public *;
}
-keepattributes JavascriptInterface
