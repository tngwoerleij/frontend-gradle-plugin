# Contributing notes

## Reporting an issue

After ensuring the problem was not reported already, any issue may be created using [this page][issues].

## Development guidelines

### Starting point

- The `master` branch is a snapshot of the latest stable release. It is not intended to receive any commit/pull request
directly.
- A dedicated branch must always be created for any modification, starting from the relevant `X.Y` branch.
- A dedicated branch shall be named `feature/<my-feature>` or `fix/<my-issue>`.

### Modifying source code

#### Design

The plugin is designed so as maintenance is as simple as possible, and predictability is the highest possible. Effort to
develop new features should not depend on the plugin architecture. To achieve these goals, the plugin relies on the
following principles:
- Clean architecture: business code is entirely isolated and independent from any framework/library except the JDK.
The 2 major concepts in this architecture are use cases and providers: use cases are single-responsibility services,
while providers are interfaces with the outer world.
- Inversion of control: using dependency injection increases modularity, and eases unit testing.
- Unit tests: achieve a 100% code coverage for each use case and provider, apart from the plugin's main class and Gradle
task classes.
- Functional tests: verify integration with Gradle (I/O validation, task dependencies, exit statuses, ...)

### Recommendations

- Apart from your preferred IDE, no other tools is required.
- Use the integrated Gradle Wrapper executable `gradlew` to execute development tasks apart from an IDE.
- It is a requirement to keep the plugin independent, small. That's why resorting to 3rd-party libraries is avoided
unless mandatory (e.g. [Apache Commons Compress][apache-commons-compress] for ZIP/GZIP/TAR extraction).
- All packages, classes, methods shall have a relevant documentation. A relevant documentation provides information to
identify the responsibility and behavior of the class/method, such as developers don't have to inspect the code to
understand how to use it.
- Prefer adding relevant documentation directly in the code instead of creating an implementation document, to guarantee
accessibility for developers.
- Designing automated unit tests with a good coverage for classes tightly coupled with Gradle API is not a
trivial task, due to the design of this API. That's why all business processes in the plugin shall remain the most
independent possible, away from this API, with an appropriate level of abstraction.
- This separation between classes tightly coupled with Gradle API (inheritance) or other libraries, and classes
containing business code is implemented with 2 packages: `org.siouan.frontendgradleplugin.domain` and
`org.siouan.frontendgradleplugin.infrastructure`. 
- Unit tests shall be written for each class, with the highest coverage possible, to avoid unknown behaviors at
execution, and improve software maintainability, predictability, and reliability.
- Functional tests shall be written for each Gradle task in the `org.siouan.frontendgradleplugin.infrastructure.gradle`
package.

### Executing functional tests from an IDE

When working with an IDE, it may be useful to run tests regularly. The IDE and Gradle may have different compilation
output paths. When executing functional tests (located in the `org.siouan.frontendgradleplugin.infrastructure.gradle`
package), concrete Gradle builds are executed, and are based on Gradle's compilation output path. This path contains
classes compiled during a Gradle build, not an IDE build. Therefore, to avoid executing tests on obsolete code, it is
mandatory to execute the Gradle task `pluginUnderTestMetata` before running functional tests.

```sh
gradlew pluginUnderTestMetadata

```

### Committing

- Ideally, a feature/fix shall be provided in a single commit. Use of commit amend may be useful.
- If a commit fixes an issue, either a feature, a bug or a documentation task, provide the message
'Fixed #<issue_number>'.
- The automated test suite must be run and no test case must fail before any commit.

## Continuous integration

The project relies on [Travis CI Open Source][travis] to integrate continuously every change, pull request, in the
repository. The configuration actually allows to build and test the plugin on the environments below:

- Linux Ubuntu Bionic 18.04.3 LTS / AdoptOpenJDK 1.8+ 64 bits
- Mac OS X 10.13.3 / AdoptOpenJDK 1.8+ 64 bits
- Windows Server 2016 version 1803 / AdoptOpenJDK 1.8+ 64 bits

Ubuntu Bionic is the reference environment, used to analyze the source code with SonarCloud. By now, the plugin has been
developed on Windows 10 Home with OracleJDK 1.8.0_202 64 bits and [JetBrains IntelliJ IDEA][intellij-idea].

*Note: continuous integration of Java projects on Windows is at an early stage for the moment.*

[apache-commons-compress]: <https://commons.apache.org/proper/commons-compress/> (Apache Commons Compress)
[intellij-idea]: <https://www.jetbrains.com/idea/> (IntelliJ IDEA)
[issues]: <https://github.com/siouan/frontend-gradle-plugin/issues> (Issues)
[travis]: <https://travis-ci.com/> (Travis CI)
