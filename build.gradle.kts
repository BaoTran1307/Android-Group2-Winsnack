// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google() // Cần thiết cho Google Services
        mavenCentral() // Repository mặc định
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2") // Phiên bản Google Services
    }
}

plugins {
    id("com.android.application") version "8.9.1" apply false // Thay bằng phiên bản bạn dùng
    id("com.google.gms.google-services") version "4.4.2" apply false // Thay bằng phiên bản bạn dùng
}