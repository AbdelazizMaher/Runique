plugins {
    `kotlin-dsl`
}

group = "com.zoksh.runique.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("android-application") {
            id = "runique.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
    }
}
