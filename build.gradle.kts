import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.cadixdev.licenser") version "0.6.1"
    `maven-publish`
    signing
}

group = "net.hitmc"
version = "1.0.0"
description = "Utility for minigame lobbies"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    api("net.axay:kspigot:1.19.0")
    testImplementation(kotlin("test"))
}

license {
    header(rootProject.file("HEADER"))
    include("**/*.java", "**/*.kt")
    newLine(true)
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

signing {
    sign(publishing.publications)
}

configure<PublishingExtension> {
    repositories {
        maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
            name = "ossrh"
            credentials {
                username = project.findProperty("sonatypeUsername") as String? ?: System.getenv("SONATYPE_USERNAME")
                password = project.findProperty("sonatypePassword") as String? ?: System.getenv("SONATYPE_PASSWORD")
            }
        }
    }
    publications {
        register<MavenPublication>(project.name) {
            from(components["java"])
            groupId = "de.jvstvshd.hitmc"
            artifactId = project.name.toLowerCase()
            version = project.version.toString()
            pom {
                name.set(project.name)
                description.set(project.description)
                developers {
                    developer {
                        name.set("JvstvsHD")
                    }
                }
                licenses {
                    license {
                        name.set("GNU General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
                    }
                }
                url.set("https://github.com/HitMC-Network/lobby-countdown")
                scm {
                    connection.set("scm:git:git://github.com/HitMC-Network/lobby-countdown.git")
                    url.set("https://github.com/HitMC-Network/lobby-countdown/tree/main")
                }
            }
        }
    }
}