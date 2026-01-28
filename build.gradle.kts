plugins {
    id("java")
}

group = "com.github.lucbf"
version = "1.0.1"

repositories {
    mavenCentral()
    flatDir{
        dir("libs")
    }
}
val embeddable by configurations.creating

configurations.implementation.get().extendsFrom(embeddable)

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(mapOf("name" to "hytale-server"))
    embeddable("org.hsqldb:hsqldb:2.7.4")
}

tasks.named<Jar>("jar") {
    // Configura o manifesto (opcional, mas recomendado se for executável)
    manifest {
        attributes["Main-Class"] = "com.github.lucbf.MagePlayer"
    }

    from(embeddable.map { if (it.isDirectory) it else zipTree(it) })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}