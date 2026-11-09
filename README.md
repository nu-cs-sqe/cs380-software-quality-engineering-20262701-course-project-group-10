# Chess 

[![Gradle Build](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/actions/workflows/main.yml/badge.svg?branch=main&event=push)](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/actions/workflows/main.yml)

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
- Checkstyle 9.3
- SpotBugs Gradle plugin 6.0.25


## Exceptions
Swing view classes, verified manually.
### Code Coverage
N/A
### Mutation Testing
N/A
### Others
- SpotBugs `EI_EXPOSE_REP` is suppressed only on `Game.getBoard()`. The design
  and BVA cases G01/G06 require returning the supplied board so the GUI observes
  the same board across resets. Returning a defensive copy would break that
  contract. The method annotation records this reason; other findings still fail
  the build.

## Static analysis
Run `bash ./gradlew check` to run tests, Checkstyle, and SpotBugs. These checks also run
with `bash ./gradlew build`, including in CI. Findings fail the build.

Run individual checks with `bash ./gradlew checkstyleMain checkstyleTest spotbugsMain spotbugsTest`.
HTML reports are generated under `build/reports/checkstyle/` and `build/reports/spotbugs/`.
Checkstyle rules are defined in `config/checkstyle/checkstyle.xml`.
