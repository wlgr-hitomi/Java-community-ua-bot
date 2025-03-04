 plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
 dependencies {
     implementation("org.telegram:telegrambots:6.8.0")
 }
tasks.test {
    useJUnitPlatform()
}