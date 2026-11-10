plugins {
    id("java")
    checkstyle
    jacoco
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


jacoco {
    toolVersion = "0.8.12"
}

// Only views, the entry point, and the project's enums are exempt.
val coverageExclusions = listOf(
    "ui/BoardPanel.class",
    "ui/BoardPanel\$*.class",
    "ui/ChessFrame.class",
    "ui/ChessFrame\$*.class",
    "ui/Main.class",
    "domain/Color.class",
    "domain/PieceType.class"
)
val coverageClasses = sourceSets.main.get().output.asFileTree.matching {
    exclude(coverageExclusions)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    classDirectories.setFrom(coverageClasses)
    reports {
        xml.required = true
        html.required = true
        csv.required = false
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)
    classDirectories.setFrom(coverageClasses)
    violationRules {
        rule {
            limit {
                counter = "COMPLEXITY"
                value = "MISSEDCOUNT"
                maximum = "0".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

tasks.register("complexityBadge") {
    group = "verification"
    description = "Generates a missed-complexity badge for all eligible production classes"
    dependsOn(tasks.jacocoTestReport)

    val reportFile = layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml")
    val outFile = layout.buildDirectory.file("badges/complexity.json")
    inputs.file(reportFile)
    outputs.file(outFile)

    doLast {
        val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance().apply {
            isValidating = false
            setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        }
        val report = factory.newDocumentBuilder().parse(reportFile.get().asFile)
        val counters = report.documentElement.childNodes
        var missed = -1
        var covered = -1
        for (i in 0 until counters.length) {
            val counter = counters.item(i) as? org.w3c.dom.Element ?: continue
            if (counter.tagName == "counter" && counter.getAttribute("type") == "COMPLEXITY") {
                missed = counter.getAttribute("missed").toInt()
                covered = counter.getAttribute("covered").toInt()
            }
        }
        val total = missed + covered
        if (missed < 0 || covered < 0 || total == 0) {
            throw GradleException("No eligible production complexity found in JaCoCo report")
        }
        val color = if (missed == 0) "brightgreen" else "red"
        val json = """{"schemaVersion":1,"label":"missed complexity","message":"$missed/$total","color":"$color"}"""
        outFile.get().asFile.apply {
            parentFile.mkdirs()
            writeText(json + "\n")
        }
        println("Badge: $json")
    }
}
