import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

repositories {
    mavenCentral()
}

// see https://github.com/gradle/gradle/issues/9279 to move to versions.gradle.kts
object Versions {
    const val kotlin = "1.3.40"
    const val mockk = "1.9.3"
    const val assertj = "3.11.1"
    const val junit5 = "5.5.2"
    const val awaitility = "4.0.1"
}

val versions by extra { Versions }

plugins {
    kotlin("jvm") version "1.3.40"
    id("com.diffplug.gradle.spotless") version "3.23.1"
    id("org.jmailen.kotlinter") version "1.26.0"
    application
    idea
}

application {
    mainClassName = "com.codurance.training.tasks.TaskListKt"
}

idea {
    module {
        setDownloadJavadoc(true)
        setDownloadSources(true)
    }
}

spotless {
    kotlin {
        ktlint()
    }
    kotlinGradle {
        target(fileTree(projectDir).apply {
            include("*.gradle.kts")
        } + fileTree("src").apply {
            include("**/*.gradle.kts")
        })
        ktlint()
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("io.mockk:mockk:${Versions.mockk}")
    testImplementation("org.assertj:assertj-core:${Versions.assertj}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.junit5}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${Versions.junit5}")
    testImplementation("org.awaitility:awaitility-kotlin:${Versions.awaitility}")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.junit5}")
}

tasks.withType(JavaExec::class) {
    standardInput = System.`in`
    standardOutput = System.out
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "5.6.2"
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
        javaParameters = true
    }
}

tasks.test {

    @Suppress("UnstableApiUsage")
    useJUnitPlatform {}

    testLogging {
        lifecycle {
            events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = true
        }
        info.events = lifecycle.events
        info.exceptionFormat = lifecycle.exceptionFormat
    }

    val failedTests = mutableListOf<TestDescriptor>()
    val skippedTests = mutableListOf<TestDescriptor>()

    // See https://github.com/gradle/kotlin-dsl/issues/836
    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
            if (result.resultType == TestResult.ResultType.FAILURE) failedTests.add(testDescriptor)
            if (result.resultType == TestResult.ResultType.SKIPPED) skippedTests.add(testDescriptor)
        }

        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            if (suite.parent == null) { // root suite
                logger.lifecycle("----")
                logger.lifecycle("Test result: ${result.resultType}")
                logger.lifecycle("Test summary: ${result.testCount} tests, " +
                    "${result.successfulTestCount} succeeded, " +
                    "${result.failedTestCount} failed, " +
                    "${result.skippedTestCount} skipped")
                if (failedTests.isNotEmpty()) {
                    logger.lifecycle("\tFailed Tests:")
                    failedTests.forEach {
                        parent?.let { parent ->
                            logger.lifecycle("\t\t" + parent.name + " - " + it.name)
                        } ?: logger.lifecycle("\t\t" + it.name)
                    }
                }

                if (skippedTests.isNotEmpty()) {
                    logger.lifecycle("\tSkipped Tests:")
                    skippedTests.forEach {
                        parent?.let { parent ->
                            logger.lifecycle("\t\t" + parent.name + " - " + it.name)
                        } ?: logger.lifecycle("\t\t" + it.name)
                    }
                }
            }
        }
    })
}

tasks.getByName("cleanTest") { group = "verification" }
