plugins {
    java
    id("jacoco")
    id("org.springframework.boot") version "2.7.12"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.jsonschema2dataclass") version "4.2.0"
    id("info.solidsoft.pitest") version "1.9.11"
}

val cucumberVersion = "7.13.0"

group = "uk.gov.justice.laa.crime"
java.sourceCompatibility = JavaVersion.VERSION_11

jacoco{
    //version compatible with java 11
    toolVersion = "0.8.8"
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

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.springdoc:springdoc-openapi-data-rest:1.7.0")
    implementation("org.springdoc:springdoc-openapi-ui:1.7.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.pitest:pitest:1.4.10")
    testImplementation("io.cucumber:cucumber-java:$cucumberVersion") {
        because("we want to use cucumber jvm")
    }
    testImplementation("io.cucumber:cucumber-junit:$cucumberVersion")
    //testImplementation("io.cucumber:cucumber-spring:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-core:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion") {
        because("we want to use cucumber with junit 5")
    }
    testImplementation("io.cucumber:cucumber-picocontainer:$cucumberVersion") {
        because("we want to use dependency injection in out cucumber tests")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api") {
        because("we want to use Junit 5 assertions")
    }
    testImplementation("org.junit.platform:junit-platform-suite") {
        because("we want to use Junit 5 @Suite annotation to select/run cucumber tests")
    }
    testImplementation("org.junit.vintage:junit-vintage-engine") {
        because("we want to use Junit 5 @CucumberOptions annotation to configure")
    }

    testRuntimeOnly("org.junit.platform:junit-platform-console") {
        because("we want run cucumber tests from the console")
    }

    testImplementation("org.junit.platform:junit-platform-suite-api")
    testImplementation("org.junit.platform:junit-platform-commons")
    testImplementation("org.junit.platform:junit-platform-launcher")
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
    // Location of the JSON Schema file(s). This may refer to a single file or a directory of files.
    source.setFrom("src/main/resources/schemas")
    // Target directory for generated Java source files. The plugin will add this directory to the
    // java source set so the compiler will find and compile the newly generated source files.
//    targetDirectoryPrefix = file("${project.buildDir}/temporaryJsonToPojo/sources/js2d")

    // Package name used for generated Java classes (for types where a fully qualified name has not
    // been supplied in the schema using the 'javaType' property).
    targetPackage.set("uk.gov.justice.laa.crime.cfecrime.api")

    // Whether to include JSR-303/349 annotations (for schema rules like minimum, maximum, etc) in
    // generated Java types. Schema rules and the annotation they produce:
    //  - maximum = @DecimalMax
    //  - minimum = @DecimalMin
    //  - minItems,maxItems = @Size
    //  - minLength,maxLength = @Size
    //  - pattern = @Pattern
    //  - required = @NotNull
    // Any Java fields which are an object or array of objects will be annotated with @Valid to
    // support validation of an entire document tree.
    generateBuilders.set(true)
//	useInnerClassBuilders = true
    includeJsr303Annotations.set(true)
    useBigDecimals.set(true)
//	includeDynamicBuilders = true
    // What type to use instead of string when adding string properties of format "date-time" to Java types
//	dateTimeType = "java.time.LocalDateTime"
}

//"**/api/**" -- excluded because request is not checked
//**/generated/** - generated code (customised)
//**/enums/** - enumerations causing code coverage issues
//**/meansassessment/** - Class's not part of this project (code coverage not required)
val jacocoExclude = listOf("**/generated/**", "**/enums/**","**/api/**", "**/meansassessment/**")
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
