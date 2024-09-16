plugins {
    id(libs.plugins.openapi.generator.get().pluginId) version libs.plugins.openapi.generator.get().version.toString()
}


dependencies {
    apply(plugin = rootProject.libs.plugins.openapi.generator.get().pluginId)

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.8")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

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

