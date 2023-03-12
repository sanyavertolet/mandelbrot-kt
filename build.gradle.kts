import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.cqfn.diktat.diktat-gradle-plugin") version "1.2.4.1"
}

group = "com.sanyavertolet"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}


kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.junit.jupiter:junit-jupiter:5.8.1")
                implementation("org.slf4j:slf4j-api:1.7.30")
                implementation("ch.qos.logback:logback-classic:1.2.3")
                implementation("ch.qos.logback:logback-core:1.2.3")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "mandelbrot"
            packageVersion = "1.0.0"
        }
    }
}

diktat {
    diktatConfigFile = rootProject.file("diktat-analysis.yml")
    inputs {
        include("src/**/*.kt")
        exclude("src/*Test/**/*.kt")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}