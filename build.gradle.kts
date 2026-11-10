plugins {
    id("java")
    checkstyle
    id("com.github.spotbugs") version "6.0.25"
}

group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.6")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.easymock:easymock:5.4.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks.compileJava {
    options.release = 11
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

checkstyle {
    toolVersion = "10.12.5"
    isIgnoreFailures = false
}

tasks.withType<Checkstyle>().configureEach {
    configProperties = mapOf(
        "methodNameFormat" to "^[a-z][a-zA-Z0-9]*$",
        "magicNumberSeverity" to "error"
    )
    reports {
        xml.required = true
        html.required = true
        html.stylesheet = resources.text.fromFile("config/xsl/checkstyle-noframes-severity-sorted.xsl")
    }
}

spotbugs {
    ignoreFailures = false
    showStackTraces = true
    showProgress = true
    maxHeapSize = "1g"
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask>().configureEach {
    reports.create("html") {
        required = true
        setStylesheet("fancy-hist.xsl")
    }
}

// BVA tests retain case IDs and numeric boundary inputs.
tasks.named<Checkstyle>("checkstyleTest") {
    configProperties = mapOf(
        "methodNameFormat" to ("^(?:[a-z][a-zA-Z0-9]*(?:_[a-zA-Z][a-zA-Z0-9]*)*|"
            + "[A-Z]{1,3}[0-9]{2,}_?[a-zA-Z][a-zA-Z0-9]*)$"),
        "magicNumberSeverity" to "ignore"
    )
}
