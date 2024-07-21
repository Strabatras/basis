import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id ("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()
}

val springBootStarter: String by project
val logback: String by project
val lombok: String by project

dependencies {

    implementation("org.springframework.boot:spring-boot-starter:$springBootStarter")
    implementation("ch.qos.logback:logback-classic:$logback:")
    implementation("ch.qos.logback:logback-core:$logback")

    compileOnly("org.projectlombok:lombok:$lombok")
    annotationProcessor("org.projectlombok:lombok:$lombok")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("listener")
        archiveVersion.set("0.1")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "ru.dedus.Main"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
