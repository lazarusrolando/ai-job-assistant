# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt

# Keep Retrofit and serialization
-keep class retrofit2.** { *; }
-keep class kotlinx.serialization.** { *; }
