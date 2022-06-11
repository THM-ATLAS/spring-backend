import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.gradle.node.npm.proxy.ProxySettings
import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.npm.task.NpxTask
import com.github.gradle.node.task.NodeTask
import com.github.gradle.node.yarn.task.YarnTask

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.github.node-gradle.node") version "3.0.0"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"

}

node {
    version.set("16.14.0")
    npmVersion.set("")
    npmInstallCommand.set("install")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-ldap")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springdoc:springdoc-openapi-ui:1.5.9")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.npmInstall {

}

tasks.register("copyFrontend", Copy::class) {
    from("vue-frontend/dist/")
    to("src/main/resources/target/static")
}

tasks.register("buildFrontend", NpmTask::class) {
    dependsOn(tasks.npmInstall)
    //npmCommand.set(listOf("install"))
    npmCommand.set(listOf("run", "build"))
    inputs.dir("../vue-frontend")
}
