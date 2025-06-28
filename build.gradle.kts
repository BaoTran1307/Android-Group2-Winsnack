// Top-level build file where you can add configuration options common to all sub-projects/modules.


plugins {
    id("com.android.application") version "8.9.1" apply false // Thay bằng phiên bản bạn dùng
    id("com.google.gms.google-services") version "4.4.2" apply false // Thay bằng phiên bản bạn dùng
}

buildscript {
    repositories {
        // Không cần khai báo google() ở đây nữa
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2") // Phiên bản mới nhất
    }
}

allprojects {
    // Xóa block repositories nếu có
}