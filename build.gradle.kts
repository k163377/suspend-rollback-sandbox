import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    id("nu.studer.jooq") version "7.1.1"
}

group = "org.wrongwrong"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    implementation("com.h2database:h2")

    implementation("org.springframework.boot:spring-boot-starter-jooq")
    // ここのバージョン類はdependency-managementプラグインの上書きのため
    implementation("org.jooq:jooq:3.16.4")
    implementation("org.jooq:jooq-meta:3.16.4")
    implementation("org.jooq:jooq-codegen:3.16.4")
    jooqGenerator("org.jooq:jooq-meta-extensions:3.16.4")
    jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:3.0.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
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

jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                        properties = listOf(
                            Property().withKey("scripts").withValue("src/main/resources/schema.sql"),
                            Property().withKey("sort").withValue("semantic")
                        )
                    }
                }
            }
        }
    }
}
