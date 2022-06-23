import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.12"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    id("com.github.johnrengelman.processes") version "0.5.0"
    id("org.springdoc.openapi-gradle-plugin") version "1.3.3"
}

group = "com.unikoom.nology"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.6.5")
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch:2.6.5")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.6.5")
    implementation("org.springframework.boot:spring-boot-starter-webflux:2.6.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.6")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.31")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.0-native-mt")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.6.5")

    @Suppress("GradlePackageUpdate")
    implementation("org.eclipse.jetty:jetty-reactive-httpclient")

    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.6")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.6")
    implementation("org.springdoc:springdoc-openapi-javadoc:1.6.6")

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    implementation("com.google.maps:google-maps-services:2.0.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools:2.6.5")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.6.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.5") {
        exclude(module = "mockito-core")
    }
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0-native-mt")
    testImplementation("org.assertj:assertj-core:3.22.0")
}

extra["springCloudVersion"] = "2020.0.4"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    exclude("**/ignore/**")
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    exclude("**/ignore/**")
    testLogging {
        events("passed", "skipped", "failed")
    }
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}
