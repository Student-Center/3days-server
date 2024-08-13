plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "weave2-server"

// support
include(":support:common")
project(":support:common").projectDir = file("support/common")

// domain
include(":domain")
project(":domain").projectDir = file("domain")

// application
include(":application")
project(":application").projectDir = file("application")

// infrastructure
include(":infrastructure:jpa")
project(":infrastructure:jpa").projectDir = file("infrastructure/jpa")

// bootstrap
include(":bootstrap:api")
project(":bootstrap:api").projectDir = file("bootstrap/api")
