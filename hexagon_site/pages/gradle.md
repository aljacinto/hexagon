
# Build Variables

The build process and imported build scripts (like the ones documented here) use variables to
customize their behaviour. It is possible to add/change variables of a build from the following
places:

1. In the project's `gradle.properties` file.
2. In your user's gradle configuration: `~/.gradle/gradle.properties`.
3. Passing them from the command line with the following switch: `-Pkey=val`.
4. Defining a project's extra property inside `build.gradle`. Ie: `project.ext.key='val'`.

For examples and reference, check [.travis.yml], [build.gradle] and [gradle.properties].

[.travis.yml]: https://github.com/hexagonkt/hexagon/blob/master/.travis.yml
[build.gradle]: https://github.com/hexagonkt/hexagon/blob/master/build.gradle
[gradle.properties]: https://github.com/hexagonkt/hexagon/blob/master/gradle.properties

# Helper scripts

These scripts can be added to your build to include a whole new capability to your building logic.

To use them, you can import the online versions, or copy them to your `gradle` directory before
importing the script.

You can import these scripts by adding add `apply from: $gradleScripts/$script.gradle` to your
`build.gradle` file some of them may require additional plugins inside the `plugins` section in the
root `build.gradle`. Check toolkit's `build.gradle` files for examples.

## Bintray

This script setup the project/module for publishing in [Bintray].

It publishes all artifacts attached to the `mavenJava` publication (check [kotlin.gradle] publishing
section) at the bare minimum binaries are published. For an Open Source project, you must include
sources and javadoc also.

To use it apply `$gradleScripts/bintray.gradle` and add the
`id 'com.jfrog.bintray' version 'VERSION'` plugin to the root `build.gradle`.

To setup this script's parameters, check the [build variables section]. This helper settings are:

* bintrayKey (REQUIRED): if not defined will try to load BINTRAY_KEY environment variable.
* bintrayUser (REQUIRED): or BINTRAY_USER environment variable if not defined.
* bintrayRepo (REQUIRED): Bintray's repository to upload the artifacts.
* license (REQUIRED): the license used to publish in Bintray.
* vcsUrl (REQUIRED): code repository location.

[Bintray]: https://bintray.com
[kotlin.gradle]: https://github.com/hexagonkt/hexagon/blob/master/gradle/kotlin.gradle
[build variables section]: /gradle/#build-variables

## Dokka

This script setup [Dokka] tool and add a JAR with the project's code documentation to the published
JARs.

All modules' Markdown files are added to the documentation and test classes ending in `SamplesTest`
are available to be referenced as samples.

To use it apply `$gradleScripts/dokka.gradle` and add the
`id 'org.jetbrains.dokka' version 'VERSION'` plugin to the root `build.gradle`.

The format for the generated documentation will be `javadoc` to make it compatible with current
IDEs.

[Dokka]: https://github.com/Kotlin/dokka

## Icons

Create web icons (favicon and thumbnails for browsers/mobile) from image SVGs (logos).

For image rendering you will need [rsvg] (librsvg2-bin) and [imagemagick] installed in the
development machine.

To use it apply `$gradleScripts/icons.gradle` to your `build.gradle`.

To setup this script's parameters, check the [build variables section]. This helper settings are:

* logoSmall (REQUIRED): SVG file used to render the small logo. Used for the favicon.
* logoLarge (REQUIRED): SVG file used to render the large logo.
* logoWide (REQUIRED): SVG file used to render the wide logo. Used for MS Windows tiles.

[rsvg]: https://github.com/GNOME/librsvg
[imagemagick]: https://www.imagemagick.org

## JMH

This scripts adds support for running [JMH micro benchmarks][JMH].

To use it apply `$gradleScripts/jmh.gradle` and add the
`id 'me.champeau.gradle.jmh' version 'VERSION'` plugin to the root `build.gradle`.

To setup this script's parameters, check the [build variables section]. This helper settings are:

* jmhBenchmarkVersion: JMH version. The default is 1.21.
* iterations (REQUIRED): number of measurement iterations to do.
* benchmarkModes (REQUIRED): benchmark mode. Available modes are:
  Throughput/thrpt, AverageTime/avgt, SampleTime/sample, SingleShotTime/ss, All/all
* batchSize (REQUIRED): number of benchmark method calls per operation (some benchmark modes can
  ignore this setting).
* fork (REQUIRED): how many times to forks a single benchmark. Use 0 to disable forking altogether.
* operationsPerInvocation (REQUIRED): operations per invocation.
* timeOnIteration (REQUIRED): time to spend at each measurement iteration.
* warmup (REQUIRED): time to spend at each warmup iteration.
* warmupBatchSize (REQUIRED): number of benchmark method calls per operation.
* warmupIterations (REQUIRED): number of warmup iterations to do.

Sample benchmark code:

```kotlin
import org.openjdk.jmh.annotations.Benchmark

open class Benchmark {
    @Benchmark fun foo() {
        println("foo bench")
        Thread.sleep(100L)
    }

    @Benchmark fun bar() {
        println("bar bench")
        Thread.sleep(100L)
    }
}
```

## JUnit

Uses JUnit 5 as the test framework.

To use it apply `$gradleScripts/junit.gradle` to your `build.gradle`.

To setup this script's parameters, check the [build variables section]. This helper settings are:

* junitVersion: JUnit version (5+), the default value is: 5.5.1.

[JMH]: https://openjdk.java.net/projects/code-tools/jmh

## Kotlin

Adds Kotlin's Gradle plugin. It sets up:

- Java version
- Repositories
- Kotlin dependencies
- Resource processing (replacing build variables)
- Cleaning (deleting runtime files as logs and dump files)
- Tests (pass properties, output and mocks)
- Setup coverage report
- IDE settings for IntelliJ and Eclipse (download dependencies' sources and API documentation)
- Published artifacts (binaries, sources and test): sourceJar and testJar tasks
- Jar with dependencies: jarAll task

To use it apply `$gradleScripts/kotlin.gradle` and add the
`id 'org.jetbrains.kotlin.jvm' version 'VERSION'` plugin to the root `build.gradle`.

To setup this script's parameters, check the [build variables section]. This helper settings are:

* kotlinVersion (REQUIRED): Kotlin version.
* kotlinCoroutinesVersion (REQUIRED): Kotlin coroutines version.
* mockkVersion: MockK mocking library version. If no value is supplied, version 1.9.3 is taken.
* jacocoVersion: Jacoco code coverage tool version.

## Kotlin JS

This script provides the following tasks for compiling Kotlin to JavaScript:

* `jsAll`: compiles the project to JavaScript including all of
  its dependencies. It copies all the resulting files to `build/js`.
* `assembleWeb`: copies all project resources and JavaScript files to `build/web`.

IMPORTANT: This script must be applied at the end of the build script.

To use it apply `$gradleScripts/kotlin_js.gradle` at the end of the build script, also apply the
`kotlin2js` plugin. And finally, add the `id 'org.jetbrains.kotlin.jvm' version 'VERSION'` plugin to
the root `build.gradle`.
 
Applying this script at the beginning won't work until it allows dependencies to be merged (a bug).

To setup this script's parameters, check the [build variables section]. This helper settings are:

* javaScriptDirectory: JavaScript directory inside the `web` directory. By default it is: "js".

## Service

Gradle's script for a service or application. It adds two extra tasks:

* buildInfo: add configuration file (`service.properties`) with build variables to the package.
* serve: Run the service in another thread. This allow the possibility to 'watch' source changes. To
  run the services and watch for changes you need to execute this task with the	`--continuous`
  (`-t`) Gradle flag. Ie: `gw -t serve`.

To use it apply `$gradleScripts/service.gradle` to your `build.gradle`.

## JBake

Adds support for site generation using [JBake].

To generate the site execute: `gw bake` and to test it run: `gw bakePreview`.

The preview site will be served at: [http://localhost:8888](http://localhost:8888). You can change
the port defining the `sitePort` variable inside `gradle.properties`.

To use it apply `$gradleScripts/jbake.gradle` and add the
`id 'org.jbake.site' version 'VERSION'` plugin to the root `build.gradle`.

JBake `content` folder can not be changed (it seems a bug).

To generate clean URLs, add the following settings:

```groovy
configuration['uri.noExtension'] = true
configuration['uri.noExtension.prefix'] = '/'
```

To setup this script's parameters, check the [build variables section]. This helper settings are:

* siteHost: site canonical URL, by default it is: "".
* configData: JBake settings map. It is an empty map by default.
* jbakeVersion: JBake version. By default: "2.6.4".
* sitePort: preview site port for development. It is "8888" if not set.

[JBake]: https://jbake.org

## SonarQube

Set up the project to be analyzed by the [SonarQube instance running in the cloud][sonarcloud].

To use it apply `$gradleScripts/sonarqube.gradle` and add the
`id 'org.sonarqube' version 'VERSION'` plugin to the root `build.gradle`.

To setup this script's parameters, check the [build variables section]. This helper settings are:

* sonarqubeProject (REQUIRED): ID used to locate the project in SonarQube host.
* sonarqubeOrganization (REQUIRED): organization owning the project.
* sonarqubeHost: SonarQube server to be used. By default it is: `https://sonarcloud.io`.
* sonarqubeToken (REQUIRED): If not set, the `SONARQUBE_TOKEN` environment variable will be used.

[sonarcloud]: https://sonarcloud.io

## TestNG

Uses TestNG as the test framework.

To use it apply `$gradleScripts/testng.gradle` to your `build.gradle`.

To setup this script's parameters, check the [build variables section]. This helper settings are:

* testngVersion: TestNG version, the default value is: 6.14.3.
