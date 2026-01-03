# Add project specific ProGuard rules here.

# Keep line numbers for debugging
-keepattributes SourceFile,LineNumberTable

# Google Sign-In / Credentials API
-keep class com.google.android.libraries.identity.googleid.** { *; }
-keep class androidx.credentials.** { *; }

# Retrofit
-keepattributes Signature
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Gson - Keep DTOs
-keep class com.nikunja.aquariusly.data.remote.dto.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

# Suppress warnings
-dontwarn com.google.android.libraries.identity.googleid.**
-dontwarn androidx.credentials.**
