dependencies {
    implementation(project(":support:common"))

    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":infrastructure:jpa"))

    implementation(libs.spring.boot.starter.web)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    developmentOnly(libs.spring.boot.devtools)
    developmentOnly(libs.spring.boot.docker.compose)
}
