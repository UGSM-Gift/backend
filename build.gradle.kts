import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.20"
	kotlin("plugin.spring") version "1.9.20"
	kotlin("plugin.jpa") version "1.9.20"
	kotlin("kapt") version "1.9.20"
}

group = "com.ugsm"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

//configurations {
//	compileOnly {
//		extendsFrom(configurations.annotationProcessor.get())
//	}

if (hasProperty("buildScan")) {
	extensions.findByName("buildScan")?.withGroovyBuilder {
		setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
		setProperty("termsOfServiceAgree", "yes")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	runtimeOnly("com.fasterxml.jackson.module:jackson-modules-java8:2.16.0")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.0")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("org.springframework.boot:spring-boot-starter-json:3.2.0")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("com.sun.xml.bind:jaxb-impl:4.0.1")
    implementation("com.sun.xml.bind:jaxb-core:4.0.1")
	implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")

	kapt("org.springframework.boot:spring-boot-configuration-processor")
	implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")

	implementation("aws.sdk.kotlin:aws-core-jvm:1.0.23")
	implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.10")
	implementation("aws.sdk.kotlin:s3:1.0.13"){
		exclude("com.squareup.okhttp3:okhttp")
	}
	implementation ("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	implementation("com.querydsl:querydsl-core:5.0.0")
	kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
	kapt("com.querydsl:querydsl-kotlin-codegen:5.0.0")


	implementation ("com.github.ulisesbocchio:jasypt-spring-boot:3.0.4")

	implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5-jakarta:2.16.0")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	systemProperty("spring.profiles.active", "prod")
	project.properties["jasypt.encryptor.password"]?.let { systemProperty("jasypt.encryptor.password", it) }
}

fun getProjectProfile():String {
	return try {
		if (project.hasProperty("profile")) {
			project.property("profile").toString()
		} else {
			"local"
		}
	} catch (e: Exception) {
		"local"
	}
}

val profile = getProjectProfile()


sourceSets {
	main {
		resources {
			srcDirs(listOf("src/main/resources", "src/main/resources-${profile}"))
		}
	}
}

tasks{
	processResources{
		duplicatesStrategy = DuplicatesStrategy.INCLUDE
	}
}