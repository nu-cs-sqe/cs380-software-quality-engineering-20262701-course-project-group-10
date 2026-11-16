import java.security.MessageDigest
import java.util.Properties
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element

fun Element.text(name: String): String = getElementsByTagName(name).item(0)?.textContent
    ?: throw GradleException("Missing mutation field: $name")

fun Element.identity(): List<String> = listOf(
    text("mutatedClass"), text("mutatedMethod"), text("methodDescription"),
    text("mutator"), text("lineNumber"), text("description"),
    if (getElementsByTagName("index").length > 0) {
        val indexes = getElementsByTagName("index")
        (0 until indexes.length).joinToString(",") { indexes.item(it).textContent }
    } else text("indexes")
)

fun parseXml(file: File): Element {
    val factory = DocumentBuilderFactory.newInstance().apply {
        setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
    }
    return factory.newDocumentBuilder().parse(file).documentElement
}

val reportFile = layout.buildDirectory.file("reports/pitest/mutations.xml")
val badgeFile = layout.buildDirectory.file("badges/mutation.json")
val summaryFile = layout.buildDirectory.file("reports/pitest/verification.properties")
val equivalentsFile = file("config/pitest/equivalent-mutants.xml")

val mutationBadge = tasks.register("mutationBadge") {
    group = "verification"
    description = "Reports killed/eligible mutants, excluding only reviewed equivalent mutants"
    dependsOn("pitest")
    inputs.file(reportFile)
    inputs.file(equivalentsFile)
    inputs.dir("src/main/java")
    outputs.files(badgeFile, summaryFile)

    doLast {
        val approved = parseXml(equivalentsFile).getElementsByTagName("mutant")
        val equivalents = (0 until approved.length).map { approved.item(it) as Element }
        if (equivalents.map { it.identity() }.toSet().size != equivalents.size) {
            throw GradleException("Duplicate equivalent-mutant identities")
        }
        equivalents.forEach { mutant ->
            require(mutant.text("reason").isNotBlank()) { "An equivalence proof is required" }
            val source = file("src/main/java/${mutant.text("mutatedClass").replace('.', '/')}.java")
            val hash = MessageDigest.getInstance("SHA-256").digest(source.readBytes())
                .joinToString("") { "%02x".format(it.toInt() and 255) }
            if (hash != mutant.text("sourceSha256")) {
                throw GradleException("Re-review equivalent mutants after changes to $source")
            }
        }
        val nodes = parseXml(reportFile.get().asFile).getElementsByTagName("mutation")
        val mutants = (0 until nodes.length).map { nodes.item(it) as Element }
        if (mutants.isEmpty()) throw GradleException("PIT generated no mutants")
        val identities = mutants.map { it.identity() }
        equivalents.forEach {
            if (identities.count { id -> id == it.identity() } != 1) {
                throw GradleException("Equivalent-mutant approval is stale or ambiguous: ${it.identity()}")
            }
        }
        val approvedIds = equivalents.map { it.identity() }.toSet()
        val exempt = mutants.filter {
            it.getAttribute("status") == "SURVIVED" && it.identity() in approvedIds
        }
        val eligible = mutants - exempt.toSet()
        if (eligible.isEmpty()) throw GradleException("No eligible mutants remain")
        val killed = eligible.count { it.getAttribute("status") == "KILLED" }
        val unresolved = eligible.filter { it.getAttribute("status") != "KILLED" }
        unresolved.forEach {
            logger.error("Unresolved mutant: ${it.text("mutatedClass")}.${it.text("mutatedMethod")}:" +
                "${it.text("lineNumber")} ${it.getAttribute("status")}: ${it.text("description")}")
        }
        val color = if (unresolved.isEmpty()) "brightgreen" else "red"
        val json = """{"schemaVersion":1,"label":"mutants killed","message":"$killed/${eligible.size}","color":"$color"}"""
        badgeFile.get().asFile.apply { parentFile.mkdirs(); writeText(json + "\n") }
        summaryFile.get().asFile.writeText(
            "unresolved=${unresolved.size}\nkilled=$killed\ntotal=${eligible.size}\nequivalent=${exempt.size}\n"
        )
        logger.lifecycle("Mutants killed: $killed/${eligible.size}; ${exempt.size} reviewed equivalents excluded")
    }
}

val mutationVerification = tasks.register("mutationVerification") {
    group = "verification"
    description = "Requires every non-equivalent mutant to have KILLED status"
    dependsOn(mutationBadge)
    inputs.file(summaryFile)
    doLast {
        val summary = Properties().apply { summaryFile.get().asFile.inputStream().use { load(it) } }
        if (summary.getProperty("unresolved").toInt() != 0) {
            throw GradleException("100% mutants killed required: ${summary.getProperty("unresolved")} unresolved mutants; see PIT report")
        }
    }
}

tasks.named("check") {
    dependsOn(mutationVerification)
}
