# Chess 

[![Gradle Build](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/actions/workflows/main.yml/badge.svg?branch=main&event=push)](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/actions/workflows/main.yml)

![Missed complexity](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/raw/badges/complexity.svg)

[![Mutants killed](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/raw/badges/mutation.svg)](config/pitest/equivalent-mutants.xml)

## Contributors
- Youran Ma
- Mengpang Xing
- Naman Dua
- Yurou Quan

## Dependencies
- JDK 11
- JUnit 5.10
- Gradle 8.10
- EasyMock 5.4.0
- Checkstyle 10.12.5
- SpotBugs Gradle plugin 6.0.25
- PIT Gradle plugin 1.19.0


## Exceptions
Swing view classes, verified manually.
### Code Coverage
Run `bash ./gradlew jacocoTestCoverageVerification`, then open the
[HTML report](build/reports/jacoco/test/html/index.html) locally.

Requires 100% cyclomatic complexity coverage, excluding View classes, `Main`,
and enums. This check also runs during `build`.

### Mutation Testing
Run `bash ./gradlew pitest` (or `.\gradlew.bat pitest` on Windows) with JDK 11.
Open the [HTML report](build/reports/pitest/index.html) locally. PIT uses the same
class exclusions as JaCoCo.
`bash ./gradlew mutationVerification` requires every non-equivalent mutant to be
killed; this also runs during `check` and `build`. Equivalence proofs are recorded
in `config/pitest/equivalent-mutants.xml` and must be reviewed after source changes.
The badge shows killed/eligible mutants, excluding only reviewed equivalents;
it updates on `main` alongside the complexity badge.

### Others
- SpotBugs `EI_EXPOSE_REP` is suppressed only on `Game.getBoard()`. The design
  and BVA cases G01/G06 require returning the supplied board so the GUI observes
  the same board across resets. Returning a defensive copy would break that
  contract. The method annotation records this reason; other findings still fail
  the build.

## Static analysis
Run `bash ./gradlew check` for tests, Checkstyle, SpotBugs, coverage, and mutation verification.
`build` runs the same checks; findings fail the build.

Reports: `build/reports/checkstyle/` and `build/reports/spotbugs/`.
Checkstyle rules: `config/checkstyle/checkstyle.xml`.

## Comment style
Every production class and enum starts with a short Javadoc comment describing
its responsibility. Keep implementation comments where they explain a reason or
constraint that is not clear from the code; avoid comments that repeat the code.
