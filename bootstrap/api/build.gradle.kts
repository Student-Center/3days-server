plugins {
    id(libs.plugins.openapi.generator.get().pluginId) version libs.plugins.openapi.generator.get().version.toString()
}


dependencies {
    apply(plugin = rootProject.libs.plugins.openapi.generator.get().pluginId)

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${libs.versions.springdoc.openapi.get()}")
    implementation("io.swagger.core.v3:swagger-annotations:${libs.versions.swagger.annotations.get()}")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation(project(":support:common"))

    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":infrastructure:persistence"))
    implementation(project(":infrastructure:sms"))

    implementation(libs.spring.boot.starter.web)

    developmentOnly(libs.spring.boot.devtools)
    developmentOnly(libs.spring.boot.docker.compose)
}

val openApiGeneratePath = "${layout.buildDirectory.get()}/generated"

openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/openapi/openapi.yaml")
    outputDir.set(openApiGeneratePath)
    apiPackage.set("com.threedays.oas.api")
    modelPackage.set("com.threedays.oas.model")
    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
            "useTags" to "true",
            "useSpringBoot3" to "true",  // SpringBoot 3, Jakarta 의존성 사용
        )
    )
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn("openApiGenerate")
}

sourceSets {
    main {
        kotlin.srcDir("$openApiGeneratePath/src/main/kotlin")
    }
}

