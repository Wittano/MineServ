import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
    kotlin("plugin.jpa") version "1.4.30"
}

group = "com.wittano"
version = System.getProperty("project.version")
java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenLocal()
    mavenCentral()
}

tasks.jar {
    this.archiveBaseName.set("mineserv")
}

dependencies {
    val springVersion = "2.4.3"

    implementation(group = "org.apache.logging.log4j", name = "log4j-api", version = "2.14.0")
    implementation("org.springframework.boot:spring-boot-starter-batch:${springVersion}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${springVersion}")
    implementation("org.springframework.boot:spring-boot-starter-rsocket:${springVersion}")
    implementation("org.springframework.boot:spring-boot-starter-security:${springVersion}")
    implementation("org.springframework.boot:spring-boot-starter-webflux:${springVersion}")
    implementation("org.springframework.security:spring-security-messaging")
    implementation("org.springframework.security:spring-security-rsocket")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    //Web scrapper
    implementation(group = "org.jsoup", name = "jsoup", version = "1.13.1")
    // JJWT-API
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")

    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")
    runtimeOnly("org.postgresql:postgresql")

    testRuntimeOnly("com.h2database:h2")

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.8.0-M1")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-params", version = "5.8.0-M1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.batch:spring-batch-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    this.systemProperties(Pair("spring.profiles.active", "test"))

    useJUnitPlatform {
        this.includeEngines("junit-jupiter", "junit-vintage")
    }
}

tasks.register<Copy>("copyJar") {
    description = "Copy jar to general build directory"
    dependsOn("build")

    from("build/libs")
    into("${project.rootDir.absolutePath}/build/${project.name}")
}
