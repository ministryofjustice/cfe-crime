plugins {
	java
	id("jacoco")
	id("org.springframework.boot") version "2.7.12"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "uk.gov.justice.laa.crime"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
