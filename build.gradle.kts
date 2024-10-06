import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("jacoco")
    id("jacoco-report-aggregation")
    id("java-test-fixtures")
    id(libs.plugins.sonarqube.get().pluginId) version libs.plugins.sonarqube.get().version.toString()
    id(libs.plugins.spring.boot.get().pluginId) version libs.plugins.spring.boot.get().version.toString()
    id(libs.plugins.spring.dependency.management.get().pluginId) version libs.plugins.spring.dependency.management.get().version.toString()
    id(libs.plugins.kotlin.jvm.get().pluginId) version libs.plugins.kotlin.jvm.get().version.toString()
    id(libs.plugins.kotlin.jpa.get().pluginId) version libs.plugins.kotlin.jpa.get().version.toString()
    id(libs.plugins.kotlin.spring.get().pluginId) version libs.plugins.kotlin.spring.get().version.toString()
}

allprojects {
    group = "com.threedays"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "jacoco")
    apply(plugin = "java-test-fixtures")
    apply(plugin = "org.sonarqube")
    apply(plugin = "idea")
    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.jpa.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.spring.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.kapt.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.noarg.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.allopen.get().pluginId)
    apply(plugin = rootProject.libs.plugins.spring.boot.get().pluginId)
    apply(plugin = rootProject.libs.plugins.spring.dependency.management.get().pluginId)

    dependencies {
        implementation(rootProject.libs.kotlin.logging)
        implementation(rootProject.libs.kotlin.stdlib)
        implementation(rootProject.libs.kotlin.reflect)
        implementation(rootProject.libs.kotlin.coroutines.core)

        implementation(rootProject.libs.jackson.module.kotlin)
        implementation(rootProject.libs.jackson.databind)
        implementation(rootProject.libs.jackson.jsr310)
        implementation(rootProject.libs.jackson.dataformat.yaml)

        annotationProcessor(rootProject.libs.spring.boot.configuration.processor)

        testImplementation(rootProject.libs.mockk)
        testImplementation(rootProject.libs.spring.mockk)
        testImplementation(rootProject.libs.kotest.runner.junit5)
        testImplementation(rootProject.libs.kotest.assertions.core)
        testImplementation(rootProject.libs.kotest.extensions.spring)
        testImplementation(rootProject.libs.spring.boot.starter.test)
        testImplementation(rootProject.libs.fixture.monkey)

        testImplementation(kotlin("test"))

        testFixturesImplementation(rootProject.libs.fixture.monkey)
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(rootProject.libs.versions.java.get()))
        }
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.add("-Xjsr305=strict")
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

sonar {
    properties {
        property("sonar.projectKey", "Student-Center_3days-server")
        property("sonar.organization", "student-center")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.coverage.jacoco.xmlReportPaths", layout.projectDirectory.file("support/jacoco/build/reports/jacoco/testCodeCoverageReport/testCodeCoverageReport.xml"))
    }
}


val allProjects = project.allprojects
    .asSequence()
    .filter { it.name != "3days-server" }
    .filter { it.name != "support" }
    .filter { it.name != "domain" }
    .filter { it.name != "application" }
    .filter { it.name != "infrastructure" }
    .filter { it.name != "bootstrap" }
    .toList()

project(":support:jacoco") {
    apply(plugin = "jacoco-report-aggregation")

    dependencies {
        allProjects.forEach {
            add("jacocoAggregation", project(it.path))
        }
    }
}
