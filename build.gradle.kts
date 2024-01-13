buildscript {
    repositories {
        maven(uri("https://oss.sonatype.org/content/repositories/snapshots/"))

        maven(uri("https://maven.aliyun.com/repository/central"))
        maven(uri("https://maven.aliyun.com/repository/google"))
        maven(uri("https://maven.aliyun.com/repository/gradle-plugin"))
        maven(uri("https://maven.aliyun.com/repository/releases"))
    }
    dependencies {
        classpath(libs.kotlin.serialization)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.dagger.hilt.android) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
}