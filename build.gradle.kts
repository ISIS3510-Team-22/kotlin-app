plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.android.library") version "8.0.2" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
}