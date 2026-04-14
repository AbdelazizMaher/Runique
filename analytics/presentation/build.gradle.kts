plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.zoksh.analytics.presentation"
}

dependencies {
    implementation(projects.analytics.domain)
}