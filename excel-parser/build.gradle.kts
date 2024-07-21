import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()
}

val springBootStarter: String by project
val apachePoi: String by project
val lombok: String by project


dependencies {

    implementation("org.springframework.boot:spring-boot-starter:$springBootStarter")

    implementation("org.apache.poi:poi:$apachePoi")
    implementation("org.apache.poi:poi-ooxml:$apachePoi")

    compileOnly("org.projectlombok:lombok:$lombok")
    annotationProcessor("org.projectlombok:lombok:$lombok")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("excel-parser")
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
