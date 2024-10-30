dependencies {
    implementation(project(":support:common"))
    implementation(project(":domain"))
    implementation(project(":application"))

    implementation(libs.spring.boot.core)
    implementation(libs.spring.cloud.starter.openfeign)

}
