plugins {
    java
    id("org.springframework.boot") version "3.5.13"
    id("io.spring.dependency-management") version "1.1.7"
    id("jacoco")
    id("checkstyle")
}

group = "com.task"
version = "0.0.1-SNAPSHOT"
description = "parking"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val mapstructVersion = "1.5.5.Final"
    val lombokBindingVersion = "0.2.0"
    val springDocVersion = "2.8.6"

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

    compileOnly("org.projectlombok:lombok")

    runtimeOnly("com.mysql:mysql-connector-j")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:$lombokBindingVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

checkstyle {
    toolVersion = "10.12.5"
    configFile = file("${project.rootDir}/config/checkstyle/checkstyle.xml")
    configDirectory.set(file("${project.rootDir}/config/checkstyle"))
    isIgnoreFailures = false
    maxWarnings = 0
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

val jacocoExclusions = listOf(
    "**/dto/**",
    "**/entity/**",
    "**/config/**",
    "**/exception/**",
    "**/mapper/**",
    "**/*Application*"
)

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it) {
            exclude(jacocoExclusions)
        }
    }))
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)

    violationRules {
        rule {
            limit {
                minimum = "0.8".toBigDecimal()
            }
        }
    }

    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it) {
            exclude(jacocoExclusions)
        }
    }))
}
