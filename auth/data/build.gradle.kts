plugins {
    alias(libs.plugins.runique.android.library)
}

android {
    namespace = "com.zoksh.auth.data"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}