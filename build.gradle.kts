plugins {
    java
    id("jacoco")
    id("org.springframework.boot") version "3.1.2"
    id("org.jsonschema2dataclass") version "6.0.0"
    id("info.solidsoft.pitest") version "1.9.11"
}
apply(plugin = "io.spring.dependency-management")

/* for all the configurations */
configurations {
    all {
        /* only junit 5 should be used */
        exclude(group = "junit", module = "junit")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

val cucumberVersion = "7.13.0"

group = "uk.gov.justice.laa.crime"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

jacoco{
//    //version compatible with java 17
    toolVersion = "0.8.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.projectlombok:lombok:1.18.26")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // oauth2-essentials
//    implementation("org.dmfs:oauth2-essentials:0.22.0")
    // optional to use httpurlconnection-executor, any other HttpRequestExecutor
    // implementation will do
//    implementation("org.dmfs:httpurlconnection-executor:1.21.3")
//    implementation("org.springframework.boot:spring-boot-starter-security")
//    implementation("org.springframework.security:spring-security-oauth2-client")
//    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
//
//    implementation("uk.gov.justice.service.laa-crime:crime-commons-spring-boot-starter-rest-client:1.1.0")
//    implementation("uk.gov.justice.service.laa-crime:crime-commons-spring-boot-starter-rest-client")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.2.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.pitest:pitest:1.4.10")

    //cucumber dependencies
    testImplementation("io.cucumber:cucumber-java:$cucumberVersion") {
        because("we want to use cucumber jvm")
    }
    testImplementation("io.cucumber:cucumber-junit:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-core:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion") {
        because("we want to use cucumber with junit 5")
    }
    testImplementation("io.cucumber:cucumber-picocontainer:$cucumberVersion") {
        because("we want to use dependency injection in out cucumber tests")
    }
//    testImplementation("org.junit.jupiter:junit-jupiter-api") {
//        because("we want to use Junit 5 assertions")
//    }
    testImplementation("org.junit.platform:junit-platform-suite") {
        because("we want to use Junit 5 @Suite annotation to select/run cucumber tests")
    }
    testImplementation("org.junit.vintage:junit-vintage-engine") {
        because("we want to use Junit 5 @CucumberOptions annotation to configure")
    }

    testRuntimeOnly("org.junit.platform:junit-platform-console") {
        because("we want run cucumber tests from the console")
    }

    //testImplementation("org.junit.platform:junit-platform-suite-api")
    testImplementation("org.junit.platform:junit-platform-commons")
    testImplementation("org.junit.platform:junit-platform-suite-commons")
    testImplementation("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.platform:junit-platform-engine")
    //testImplementation("org.junit.jupiter:junit-jupiter")
    //testImplementation("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    withType<Test> {
        useJUnitPlatform ()
    }
    named("build") {
        //dependsOn("pitest")
        //dependsOn("consoleLauncherTest")
    }

}

task<JavaExec>("consoleLauncherTest"){
        dependsOn("assemble", "compileTestJava", "testClasses")
        val reportsDir = file("$buildDir/test-results")
        outputs.dir(reportsDir)
        mainClass.set("org.junit.platform.console.ConsoleLauncher")
        classpath = sourceSets["test"].runtimeClasspath
        args("--details", "tree")
        args("--scan-classpath")
        args("--include-engine", "cucumber")
        args("--reports-dir", reportsDir)
        systemProperty("cucumber.puglin", "message:build/reports/cucumber.ndjson, timeline:build/reports/timeline, html:build/reports/cucumber.html")
        //Pretty prints the output in console
        systemProperty("cucumber.plugin", "pretty")
        systemProperty("cucumber.junit-platform.naming-strategy", "long")
         //Hides cucumber ads
        systemProperty("cucumber.publish.quiet", true)
        // OPTIONAL: Force test execution even if they are up-to-date according to Gradle or use "gradle test --rerun"
        outputs.upToDateWhen { false }

        //configure jacoco agent for test coverage to generate the .exec file
        val jacocoAgent = zipTree(configurations.jacocoAgent.get().singleFile)
                .filter{it.name == "jacocoagent.jar"}
                .singleFile
        jvmArgs = listOf("-javaagent:$jacocoAgent=destfile=$buildDir/jacoco/cucumber.exec,append=false")

    }

tasks.test {
    testLogging {
        events("passed", "skipped", "failed")
    }

    finalizedBy("jacocoTestReport")
}

jsonSchema2Pojo {
    executions {
        create("main") {
            // define block with settings for a given category
            io {
                source.setFrom(files("${projectDir}/src/main/resources/schemas"))
                sourceType.set("jsonschema")
            }

            klass {
                targetPackage.set("uk.gov.justice.laa.crime.cfecrime.api")
            }
            methods.builders.set(true)
            methods.buildersDynamic.set(true)
            methods.annotateJsr303Jakarta.set(true)
            fields.floatUseBigDecimal.set(true)
            dateTime.dateTimeType.set("java.time.LocalDateTime")
        }
    }
}

//"**/api/**" -- excluded because request is not checked
//**/generated/** - generated code (customised)
//**/enums/** - enumerations causing code coverage issues
//**/meansassessment/** - Class's not part of this project (code coverage not required)
val jacocoExclude = listOf("**/generated/**", "**/stubs/utils/**","**/enums/**","**/api/**", "**/meansassessment/**")
val minimumCoverage = ".90".toBigDecimal()

tasks.jacocoTestReport {
    // tests are required to run before generating the report
    dependsOn(tasks.test)

    //Give jacoco the file generated with the cucumber tests for the coverage
    executionData(files("$buildDir/jacoco/test.exec","$buildDir/jacoco/cucumber.exec"))
    reports{
        xml.required.set(true)
    }

    group = "Reporting"
    description = "Generate jacoco coverage reports"
    classDirectories.setFrom(classDirectories.files.map {
        fileTree(it) {
            exclude(jacocoExclude)
        }
    })

    //print the report url for easier access
    doLast {
        println("Coverage Report link:")
        println("file://${project.rootDir}/build/reports/jacoco/test/html/index.html")
        println("Coverage cucumber Report link:")
        println("file://${project.rootDir}/build/reports/tests/test/index.html")
        println("RunTime cucumber Report link:")
        println("file://${project.rootDir}/build/reports/cucumber-report.html")
    }
    finalizedBy("jacocoTestCoverageVerification")
}

tasks.jacocoTestCoverageVerification {

    classDirectories.setFrom(classDirectories.files.map {
        fileTree(it) {
            exclude(jacocoExclude)
        }
    })

    violationRules {
        rule {
            element = "CLASS"
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                //Build will fail if this limit is not met
                minimum = minimumCoverage
            }
        }
    }
}

val testCoverage by tasks.registering {
    group = "verification"
    description = "Runs the tests with coverage"
    dependsOn(":test",
            ":jacocoTestReport",
            ":jacocoCoverageVerification")

    tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}
