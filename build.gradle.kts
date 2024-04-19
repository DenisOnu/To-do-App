// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    val agp_version by extra("8.2.2")
    dependencies {
        //Navigation component
        val nav_version = "2.7.7"
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")

    }
}
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}