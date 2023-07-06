plugins {
    java
    id("jacoco")
    id("org.springframework.boot") version "2.7.12"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.jsonschema2dataclass") version "4.2.0"
    id("info.solidsoft.pitest") version "1.9.11"
}

group = "uk.gov.justice.laa.crime"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.springdoc:springdoc-openapi-data-rest:1.7.0")
    implementation("org.springdoc:springdoc-openapi-ui:1.7.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.pitest:pitest:1.4.10")
}

tasks {
    withType<Test> {
        useJUnitPlatform ()
    }

    //named("build") {
    //    dependsOn("pitest")
    //}

}

tasks.test {
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


val jacocoExclude = listOf("**/generated/**", "**/enums/**", "**/api**", "**/request/**")
val minimumCoverage = ".90".toBigDecimal()

tasks.jacocoTestReport {
    // tests are required to run before generating the report
    dependsOn(tasks.test)
    //print the report url for easier access
    doLast {
        println("Coverage Report link:")
        println("file://${project.rootDir}/build/reports/jacoco/test/html/index.html")
    }
    group = "Reporting"
    description = "Generate jacoco coverage reports"
    classDirectories.setFrom(classDirectories.files.map {
        fileTree(it) {
            exclude(jacocoExclude)
        }
    })

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