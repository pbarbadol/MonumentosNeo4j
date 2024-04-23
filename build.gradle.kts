plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.neo4j.driver:neo4j-java-driver:5.19.0")
    //Para json
    implementation ("com.fasterxml.jackson.core:jackson-annotations:2.14.2")
    implementation ("com.sparkjava:spark-core:2.9.4")
    implementation ("com.google.code.gson:gson:2.8.9")


}

tasks.test {
    useJUnitPlatform()
}