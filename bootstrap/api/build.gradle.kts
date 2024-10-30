import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(libs.plugins.openapi.generator.get().pluginId) version libs.plugins.openapi.generator.get().version.toString()
    id(libs.plugins.jib.get().pluginId) version libs.versions.jib
}


dependencies {
    apply(plugin = rootProject.libs.plugins.openapi.generator.get().pluginId)

    implementation(project(":support:common"))
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":infrastructure:persistence"))
    implementation(project(":infrastructure:sms"))
    implementation(project(":infrastructure:redis"))
    implementation(project(":infrastructure:rest"))

    implementation(libs.spring.boot.starter.web)
    implementation(libs.springdoc.openapi.webmvc.ui)
    implementation(libs.swagger.annotations)
    implementation(libs.spring.boot.starter.validation)

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
            "useSpringBoot3" to "true",
        )
    )
}

tasks.withType<KotlinCompile> {
    dependsOn("openApiGenerate")
}

sourceSets {
    main {
        kotlin.srcDir("$openApiGeneratePath/src/main/kotlin")
    }
}

val originImage = "bellsoft/liberica-openjdk-alpine:21"

jib {
    from {
        image = originImage
    }
    container {
        ports = listOf("8080")
    }
}
