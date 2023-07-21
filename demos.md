## Telepített szoftverek

* Git
* JDK 17
* Visual Studio Code
* Docker Desktop
* DBeaver (opcionális)
* Postman (opcionális)
* Windows Subsystem for Linux 2 + Ubuntu

## Szolgáltatások

* GitHub regisztráció (ingyenes)
* Docker Hub regisztráció (ingyenes)
* AWS regisztráció (ingyenes, bankkártya szükséges)

## Maven build eszköz - gyakorlat

```shell
git clone https://github.com/Training360/javax-cip-public
xcopy /e /i javax-cip-public\employees employees
cd employees
code .
```

* Könyvtárszerkezet bemutatása
* `pom.xml` bemutatása

## Maven futtatás - gyakorlat

```shell
mvnw package
```

* `target`, `classes` könyvtár

```shell
mvnw clean
mvnw clean package
```

* Fázisok bemutatása

```shell
mvnw spring-boot:run
```

* Webes felület: `http://localhost:8080`

## Maven függőségek - gyakorlat

* Függőségek

```shell
mvnw help:effective-pom > effective-pom.log
```

* Central repo: https://repo1.maven.org/maven2
* Local repository

```shell
mvnw install
```

* Függőségek letöltése

```shell
mvnw dependency:tree > tree.log
```

* Függőség és plugin verziószámok

```shell
mvnw versions:display-dependency-updates > updates.log
```

## Maven unit tesztek futtatása - gyakorlat

```shell
mvnw test
```

* `target/surefire-reports`

## Gradle build eszköz - gyakorlat

```shell
git clone https://github.com/Training360/javax-cip-public
xcopy /e /i javax-cip-public\employees employees-gradle
cd employees
code .
```

* Könyvtárszerkezet bemutatása
* `build.gradle`, `settings.gradle` bemutatása

## Gradle futtatás - gyakorlat

```shell
gradlew tasks 
```

* Csoportokra bontva    

```shell
gradlew build
```

* `build` könyvtár

```shell
gradlew build
```

* Nem futtatja újra

```shell
gradlew --console=plain clean build
```

* Fázisok bemutatása

```shell
gradlew bootRun
```

* Webes felület: `http://localhost:8080`

## Gradle függőségek

Függőségi fa listázása

```shell
gradlew -q dependencies
```

* Central repo: https://repo1.maven.org/maven2
* Függőségek könyvtára: `~/.gradle/caches/modules-2/files-2.1`

```groovy
plugins {
  id 'com.github.ben-manes.versions' version '0.47.0'
}
```

```shell
gradlew dependencyUpdates
```

## Gradle unit tesztek futtatása

```shell
gradlew test
```

`test-results`, `build/reports` könyvtár

Plugin:

```groovy
plugins {
    id 'com.adarshr.test-logger' version '3.2.0'
}
```

Tesztek átugrása:

```shell
gradlew build -x test
```

## Maven tesztlefedettség - gyakorlat

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.7</version>
    <executions>
        <execution>
            <id>jacoco-initialize</id>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>jacoco-site</id>
            <phase>package</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

```shell
mvnw package
```

## Gradle teszt lefedettség

```groovy
plugins {
  id 'jacoco'
}

test {
  useJUnitPlatform()
  finalizedBy jacocoTestReport
}

jacoco {
  toolVersion = "0.8.8"
}

jacocoTestReport {
  dependsOn test
}

```

## Maven csomagolás - gyakorlat

```shell
mvnw package
jar -tf .\target\employees-1.0.0.jar
```

```shell
cd target
java -jar employees-1.0.0.jar
```

## Gradle csomagolás

```shell
gradlew build
jar -tf .\build\libs\employees-1.0.0.jar
```

```shell
cd build\libs
java -jar employees-1.0.0.jar
```

## Bevezetés a Docker használatába - gyakorlat

```shell
docker version
docker run hello-world
docker run -p 80:80 -d --name my-nginx nginx
docker ps
docker stop 517e15770697
docker ps -a
docker start my-nginx
docker logs -f my-nginx
docker stop my-nginx
docker rm my-nginx
```

Használható az azonosító első egyértelmű megkülönböztetést lehetővé tevő karakterei is

```shell
docker images
docker rmi nginx
```

## Nexus repo manager - gyakorlat

```shell
docker run --name nexus -d -p 8091:8081 -p 8092:8082 sonatype/nexus3
```

```shell
docker exec -it nexus cat /nexus-data/admin.password
```

## Nexus repo manager Maven proxyként - gyakorlat

```shell
mvn dependency:purge-local-repository
```

`~/.m2/settings.xml` fájlban

```xml
<settings>
    <mirrors>
        <mirror>
            <id>nexus</id>
            <mirrorOf>*</mirrorOf>
            <url>http://localhost:8091/repository/maven-public/</url>
        </mirror>
    </mirrors>
</settings>
```

## Deploy Mavennel Nexus repoba - gyakorlat

```xml
<distributionManagement>
    <repository>
        <id>nexus-releases</id>
        <url>http://localhost:8091/repository/maven-releases/</url>
    </repository>
</distributionManagement>
```

C:\Users\iviczian\.m2

`~/.m2/settings.xml` fájlban a `settings` tagen belül

```xml
<servers>
   <server>
      <id>nexus-releases</id>
      <username>admin</username>
      <password>admin</password>
   </server>
</servers>
```

```shell
mvnw deploy
```

## Nexus repo manager Gradle proxyként - gyakorlat

### Globális

A `~/.gradle/init.d/mirror.gradle` fájl tartalma:

```groovy
allprojects {
  ext.RepoConfigurator = {
    maven {
      url = uri('http://localhost:8091/repository/maven-public/') 
      allowInsecureProtocol true
    }
  }
  buildscript.repositories RepoConfigurator
  repositories RepoConfigurator
}
```

* Gradle 7-től kezdődően https-en kéne, `allowInsecureProtocol` konfig kikapcsolja ennek ellenőrzését

### Lokális

* Maven proxy repository létrehozása: `https://plugins.gradle.org/m2/`

```shell
./gradlew --status
./gradlew --stop
rm $env:USERPROFILE\.gradle\caches -r
```

```groovy
repositories {
   maven {
    url "http://localhost:8091/repository/maven-public/"
    allowInsecureProtocol true
  }
}
```

`settings.gradle` állományba, elejére:

```groovy
pluginManagement {
    repositories {
        maven {
            url "http://localhost:8091/repository/maven-public/"
            allowInsecureProtocol true
        }
    }
}
```

```shell
.\gradlew build --i
```

## Deploy Gradle Nexus repoba - gyakorlat

`settings.gradle` fájlban `employees` -> `employees-gradle`

```groovy
plugins {
    id 'maven-publish'
}

publishing {
    publications {
        bootJava(MavenPublication) {
            artifact bootJar
        }
    }
    repositories {
        maven {
            url = findProperty('nexusUrl') ?: 'http://localhost:8091/repository/maven-releases/'
            credentials {
                username findProperty('nexusUsername') ?: 'admin'
                password findProperty('nexusPassword') ?: 'admin'
            }
            allowInsecureProtocol true
        }
    }
}
```

```shell
gradlew build publish
```

## Artifactory repo manager - gyakorlat

```shell
docker run --name artifactory -d -p 8093:8081 -p 8094:8082 docker.bintray.io/jfrog/artifactory-oss:latest
```

http://localhost:8094/

`admin/password` -> `admin/admin11AA`

* User management/Settings/Allow Anonymous Access

## Artifactory repo manager Maven proxyként - gyakorlat

`~/.m2/settings.xml` fájlban

```xml
<settings>
    <mirrors>
        <mirror>
            <id>artifactory</id>
            <mirrorOf>*</mirrorOf>
            <url>http://localhost:8094/artifactory/libs-release</url>
        </mirror>
    </mirrors>
</settings>
```

```shell
mvnw dependency:purge-local-repository
```

## Deploy Mavennel Artifactory repoba - gyakorlat

```xml
<distributionManagement>
    <repository>
        <id>artifactory-releases</id>
        <url>http://localhost:8094/artifactory/libs-release-local/</url>
    </repository>
</distributionManagement>
```

C:\Users\iviczian\.m2

`~/.m2/settings.xml` fájlban a `settings` tagen belül

```xml
<servers>
    <server>
        <id>artifactory-releases</id>
        <username>admin</username>
        <password>admin11AA</password>
    </server>
</servers>
```

```shell
mvnw deploy
```

## Artifactory repo manager Gradle proxyként - gyakorlat

### Lokális

* Maven remote repository létrehozása: `https://plugins.gradle.org/m2/`

```shell
./gradlew --status
./gradlew --stop
rm $env:USERPROFILE\.gradle\caches -r
```

```groovy
repositories {
   maven {
    url "http://localhost:8094/artifactory/libs-release"
    allowInsecureProtocol true
  }
}
```

`settings.gradle` állományba, elejére:

```groovy
pluginManagement {
    repositories {
        maven {
            url "http://localhost:8094/artifactory/libs-release"
            allowInsecureProtocol true
        }
    }
}
```

```shell
.\gradlew build --i
```

## Deploy Gradle Artifactory repoba - gyakorlat

`settings.gradle` fájlban `employees` -> `employees-gradle`

```groovy
plugins {
    id 'maven-publish'
    id "com.jfrog.artifactory" version "4.32.0"
}

publishing {
    publications {
        bootJava(MavenPublication) {
            artifact bootJar
        }
    }
}

artifactory {
  contextUrl = findProperty('artifactoryUrl') ?: 'http://localhost:8094/artifactory'

  publish {
        repository {
            repoKey = 'libs-release-local'
	        username = findProperty('artifactoryUser') ?: 'admin'
            password = findProperty('artifactoryPassword') ?: 'admin11AA'
	    }
	    defaults {
            publications('bootJava')
            publishPom = false
        }
  }  
}
```

```shell
gradlew artifactoryPublish
```

## Integrációs tesztek Mavennel in-memory adatbázissal - gyakorlat

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>2.22.2</version>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

```shell
mvnw verify
```

## Integrációs tesztek Mavennel valós adatbázissal - gyakorlat

```shell
docker run -d -e MARIADB_DATABASE=employees -e MARIADB_USER=employees -e MARIADB_PASSWORD=employees -e MARIADB_ALLOW_EMPTY_ROOT_PASSWORD=yes -p 3306:3306 --name employees-it-mariadb mariadb
```

```xml
<properties>
    <test.datasource.url>jdbc:h2:mem:db;DB_CLOSE_DELAY=-1</test.datasource.url>
    <test.datasource.username>sa</test.datasource.username>
    <test.datasource.password>sa</test.datasource.password>
</properties>

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>2.22.2</version>
    <configuration>
        <systemPropertyVariables>
            <spring.datasource.url>${test.datasource.url}</spring.datasource.url>
            <spring.datasource.username>${test.datasource.username}</spring.datasource.username>
            <spring.datasource.password>${test.datasource.password}</spring.datasource.password>
        </systemPropertyVariables>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

```shell
.\mvnw clean "-Dtest.datasource.url=jdbc:mariadb://localhost/employees" "-Dtest.datasource.username=employees" "-Dtest.datasource.password=employees" verify
```

## Integrációs tesztek Gradle-lel in-memory adatbázissal - gyakorlat

```groovy
sourceSets {
    integrationTest {
        java.srcDir "$projectDir/src/integration/java"
        resources.srcDir "$projectDir/src/integration/resources"
        compileClasspath += main.output
        runtimeClasspath += main.output
    }
}

configurations {
    integrationTestImplementation.extendsFrom implementation
    integrationTestRuntimeOnly.extendsFrom runtimeOnly
}
```

`testImplementation` -> `integrationTestImplementation`

```groovy
tasks.register('integrationTest', Test) {
    description = 'Runs integration tests.'
    group = 'verification'

    useJUnitPlatform()

    testClassesDirs = sourceSets.integrationTest.output.classesDirs
    classpath = sourceSets.integrationTest.runtimeClasspath
    shouldRunAfter test
}

check.dependsOn integrationTest
```

```shell
gradlew integrationTest --i
```


## Integrációs tesztek Gradle-lel valós adatbázissal - gyakorlat

```shell
docker run -d -e MARIADB_DATABASE=employees -e MARIADB_USER=employees -e MARIADB_PASSWORD=employees -e MARIADB_ALLOW_EMPTY_ROOT_PASSWORD=yes -p 3306:3306 --name employees-it-mariadb mariadb
```

```groovy
systemProperty 'spring.datasource.url', findProperty('test.datasource.url') ?: "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1"
systemProperty 'spring.datasource.username', findProperty('test.datasource.username') ?: "su"
systemProperty 'spring.datasource.password', findProperty('test.datasource.password') ?: "su"
```

```shell
gradlew "-Ptest.datasource.url=jdbc:mariadb://localhost/employees" "-Ptest.datasource.username=employees" "-Ptest.datasource.password=employees" clean integrationTest --i
```

## Csomagolás Docker Image-be Dockerfile használatával Maven projekt esetén - gyakorlat

`Dockerfile` fájl tartalma:

```dockerfile
FROM eclipse-temurin:17
WORKDIR /app
COPY target/employees-1.0.0.jar employees.jar
CMD ["java", "-jar", "employees.jar"]
```

```shell
docker build -t employees .
```

```shell
docker run -d --name employees -p 8080:8080 employees
docker logs -f employees
```

## Csomagolás Docker Image-be Dockerfile használatával Gradle projekt esetén - gyakorlat

`Dockerfile` fájl tartalma:

```dockerfile
FROM eclipse-temurin:17
WORKDIR /app
COPY build/libs/employees-1.0.0.jar employees.jar
CMD ["java", "-jar", "employees.jar"]
```

```shell
docker build -t employees .
```

```shell
docker run -d --name employees-gradle -p 8081:8080 employees-gradle
docker logs -f employees-gradle
```

## Docker layers Maven esetén

```shell
mkdir tmp
cd tmp
java -Djarmode=layertools -jar ..\target\employees-1.0.0.jar extract
```

`Dockerfile.layered` tartalma:
  
```dockerfile
FROM eclipse-temurin:17 as builder
WORKDIR /app
COPY target/employees-1.0.0.jar employees.jar
RUN java -Djarmode=layertools -jar employees.jar extract

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
```

```shell
docker build --file Dockerfile.layered  -t employees .  
```

* `EmployeesWebController` módosítása

```java
log.debug("List employees");
```

```shell
mvnw clean package
```

## Docker layers Gradle esetén

```shell
mkdir tmp
cd tmp
java -Djarmode=layertools -jar ..\build\libs\employees-gradle-1.0.0.jar extract
```

`Dockerfile.layered` tartalma:
  
```dockerfile
FROM eclipse-temurin:17 as builder
WORKDIR /app
COPY build/libs/employees-gradle-1.0.0.jar employees.jar
RUN java -Djarmode=layertools -jar employees.jar extract

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
```

```shell
docker build --file Dockerfile.layered  -t employees-gradle .  
```

* `EmployeesWebController` módosítása

```java
log.debug("List employees");
```

```shell
gradlew clean bootJar
```

## Csomagolás Docker Image-be Mavennel Cloud Native Buildpacks használatával - gyakorlat

```shell
mvnw spring-boot:build-image
```

```shell
docker run -p 8082:8080 -d --name employees-cnb employees:1.0.0
```

## Csomagolás Docker Image-be Mavennel Cloud Native Buildpacks használatával - gyakorlat

```shell
gradlew bootBuildImage
```

```shell
docker run -p 8083:8080 -d --name employees-gradle-cnb employees-gradle:1.0.0
```


# Docker repository létrehozása Nexus-ban

* Create Docker hosted repository (port `8082`)
* Adminisztrációs felületen: _Security / Realms_ tabon: _Docker Bearer Token Realm_ hozzáadása

## Docker image deploy Nexus-ba Mavennel - gyakorlat

```shell
docker tag employees:1.0.0 localhost:8092/employees
docker login localhost:8092
docker push localhost:8092/employees
```

## Docker image deploy Nexus-ba Gradle-lel - gyakorlat

```shell
docker tag employees-gradle localhost:8092/employees-gradle
docker login localhost:8092
docker push localhost:8092/employees
```

## Docker compose használata - gyakorlat

```shell
curl https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh -o wait-for-it.sh
```

```yaml
version: '3'

services:
  mariadb:
    image: mariadb
    expose:
      - 3306
    environment:
      MARIADB_DATABASE: employees
      MARIADB_ALLOW_EMPTY_ROOT_PASSWORD: 'yes' # aposztrófok nélkül boolean true-ként értelmezi
      MARIADB_USER: employees
      MARIADB_PASSWORD: employees
    ports:
      - 3306:3306

  employees-app:
    image: employees:1.0.0
    depends_on:
      - mariadb
    expose:
      - 8080
    environment:
      SPRING_DATASOURCE_URL: 'jdbc:mariadb://mariadb:3306/employees'
      SPRING_DATASOURCE_USERNAME: employees
      SPRING_DATASOURCE_PASSWORD: employees
    volumes:
    - ./wait:/opt/wait
    entrypoint: ["/opt/wait/wait-for-it.sh", "-t", "120", "mariadb:3306", "--", "/cnb/process/web"]
    ports:
      - 8080:8080
```


```shell
docker compose up -d
docker compose logs -f
docker exec -it employees-mariadb-1 mariadb employees
```

```sql
select * from employees;
```

* DBeawer

```shell
docker compose down
```

## E2E tesztelés Selenium WebDriverrel - gyakorlat

```shell
xcopy /e /i .\javax-cip-public\employees-selenium employees-selenium
```

```dockerfile
FROM eclipse-temurin:17
WORKDIR /tests
COPY . .
RUN chmod +x mvnw
ENTRYPOINT ["./mvnw", "test"]
```

Optimalizáció:

```dockerfile
FROM eclipse-temurin:17
WORKDIR /tests
COPY pom.xml pom.xml
COPY mvnw mvnw
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw test
COPY src src
RUN ./mvnw test-compile
ENTRYPOINT ["./mvnw", "test"]
```

`docker-compose.yaml` fájl

```yaml
version: "3"
services:
  selenium-hub:
    image: selenium/hub
    ports:
      - "4442:4442"
      - "4443:4443"
      - "4444:4444"

  chrome:
    image: selenium/node-chrome
    volumes:
      - /dev/shm:/dev/shm
    depends_on:
      - selenium-hub
    environment:
      SE_EVENT_BUS_HOST: selenium-hub
      SE_EVENT_BUS_PUBLISH_PORT: 4442
      SE_EVENT_BUS_SUBSCRIBE_PORT: 4443
    expose:
      - "5900"
    privileged: true

  chrome-video:
    image: selenium/video:ffmpeg-4.3.1-20210527
    volumes:
      - ./videos:/videos
    depends_on:
      - chrome
    environment:
      DISPLAY_CONTAINER_NAME: chrome
      FILE_NAME: chrome-video.mp4

  mariadb:
    image: mariadb
    expose:
      - 3306
    environment:
      MARIADB_DATABASE: employees
      MARIADB_ALLOW_EMPTY_ROOT_PASSWORD: 'yes'
      MARIADB_USER: employees
      MARIADB_PASSWORD: employees

  employees-app:
    image: employees:1.0.0
    expose:
      - 8080
    volumes:
      - ./wait:/opt/wait
    depends_on:
      - mariadb
    environment:
      SPRING_DATASOURCE_URL: 'jdbc:mariadb://mariadb:3306/employees'
      SPRING_DATASOURCE_USERNAME: employees
      SPRING_DATASOURCE_PASSWORD: employees
    entrypoint: ["/opt/wait/wait-for-it.sh", "-t", "120", "mariadb:3306", "--", "/cnb/process/web"]

  employees-selenium:
    build: .
    image: employees-selenium
    volumes:
      - ./surefire-reports:/tests/target/surefire-reports
      - ./wait:/opt/wait
    depends_on:
      - employees-app
    environment:
      SELENIUM_DRIVER: RemoteWebDriver
      SELENIUM_HUB_URL: http://selenium-hub:4444
      SELENIUM_SUT_URL: http://employees-app:8080
    entrypoint: ["/opt/wait/wait-for-it.sh", "-t", "120", "employees-app:8080", "--", "./mvnw", "test"]
```

```shell
mkdir wait
cd wait
curl https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh -o wait-for-it.sh
cd ..
docker compose up --abort-on-container-exit
```

* a `--abort-on-container-exit` kapcsoló hatására leállítja az összeset, ha egy is leáll
* Videó
* Report

## E2E tesztelés Postman/Newman használatával - gyakorlat

```shell
xcopy /e /i .\javax-cip-public\employees-postman employees-postman
```

## E2E tesztelés Newman használatával - gyakorlat


```dockerfile
FROM postman/newman:5-ubuntu
RUN npm install -g newman-reporter-htmlextra
WORKDIR /tests
COPY collections .
ENTRYPOINT ["newman", "run", "employees.postman_collection.json", "-e", "test.postman_environment.json", "-r", "cli,htmlextra", "--reporter-htmlextra-export", "reports"]
```

`docker-compose.yaml` fájl

```yaml
version: '3'

services:
  mariadb:
    image: mariadb
    expose:
      - 3306
    environment:
      MARIADB_DATABASE: employees
      MARIADB_ALLOW_EMPTY_ROOT_PASSWORD: 'yes'
      MARIADB_USER: employees
      MARIADB_PASSWORD: employees

  employees-app:
    image: employees:1.0.0
    depends_on:
      - mariadb
    environment:
      SPRING_DATASOURCE_URL: 'jdbc:mariadb://mariadb:3306/employees'
      SPRING_DATASOURCE_USERNAME: employees
      SPRING_DATASOURCE_PASSWORD: employees
    volumes:
    - ./wait:/opt/wait
    entrypoint: ["/opt/wait/wait-for-it.sh", "-t", "120", "mariadb:3306", "--", "/cnb/process/web"]
    expose:
      - 8080

  employees-newman:
    build: .
    image: employees-newman
    volumes:
    - ./reports:/tests/reports
    - ./wait:/opt/wait
    depends_on:
      - employees-app
    entrypoint: ["/opt/wait/wait-for-it.sh", "-t", "30", "employees-app:8080", "--", "newman", "run", "employees.postman_collection.json", "-e", "test.postman_environment.json", "-r", "cli,htmlextra", "--reporter-htmlextra-export", "reports"]
```

```shell
mkdir wait
cd wait
curl https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh -o wait-for-it.sh
cd ..
docker compose up --abort-on-container-exit
```

## SonarQube - gyakorlat

```shell
docker run --name sonarqube -d -p 9000:9000 sonarqube:lts
```

* `admin`/`admin` -> `admin` / `admin12AA`
* My Account / Security / Generate Tokens


# Projekt elemzése SonarScanner Maven pluginnal - gyakorlat

```shell
mvnw sonar:sonar "-Dsonar.login=token"
```

* Elemzés eredménye
* Tesztlefedettség eredménye

# Integrációs tesztek SonarScanner Maven pluginnal - gyakorlat

```xml
<sonar.junit.reportPaths>
  ${project.basedir}/target/surefire-reports/,${project.basedir}/target/failsafe-reports/
</sonar.junit.reportPaths>
```

# Projekt elemzése SonarScanner Gradle pluginnal - gyakorlat

```groovy
id "org.sonarqube" version "4.2.1.3168"
```

```shell
gradlew sonar "-Dsonar.login=token" -i
```


# Integrációs tesztek SonarScanner Gradle pluginnal - gyakorlat

```groovy
jacocoTestReport {
  // Integrációs tesztek fussanak le
  dependsOn integrationTest

  // Ne csak a test.exec, hanem az integrationTest.exec fájlt is
  getExecutionData().setFrom(fileTree(buildDir).include("/jacoco/*.exec"))

  // reports/jacoco/test/jacocoTestReport.csv és xml is álljon elő
  reports {
    xml.required = true
  }
}

sonarqube {
    properties {
        properties["sonar.junit.reportPaths"] += "$buildDir/test-results/integrationTest/"
        properties["sonar.tests"] += "$projectDir/src/integration/java"
    }
}
```

## SonarQube Quality Profiles - gyakorlat

## SonarQube Quality Gates - gyakorlat

## SonarQube Quality Gate Mavennel - gyakorlat

Maven bevárás:

```shell
mvnw sonar:sonar "-Dsonar.login=token" "-Dsonar.qualitygate.wait=true"
```

## SonarQube Quality Gate Gradle-lel - gyakorlat

```shell
gradlew sonar "-Dsonar.login=token"  "-Dsonar.qualitygate.wait=true"
```

## IDEA SonarLint plugin - gyakorlat

## OWASP dependency check Mavennel - gyakorlat

```xml
<plugin>
    <groupId>org.owasp</groupId>
    <artifactId>dependency-check-maven</artifactId>
    <version>8.2.1</version>
    <executions>
        <execution>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

```shell
mvnw verify
```

Lásd `target/dependency-check-report.html`

# OWASP dependency check Gradle-lel - gyakorlat

```groovy
id "org.owasp.dependencycheck" version "8.2.1"
```

```shell
.\gradlew dependencyCheckAnalyze
```

* Lásd `build/dependency-check-report.html`

# AWS CLI beállítása - gyakorlat

* IAM
* AmazonEC2FullAccess policy
* Security credentials tab: create an access key (Use case: Command Line Interface)

 ```shell
sudo apt-get update
sudo apt-get install awscli
aws --version
```

```shell
aws configure
```

* Access Key ID
* Secret Access Key
* Default region name: `eu-central-1`
* Default output format: `json`

```shell
aws ec2 describe-images --image-ids ami-0caef02b518350c8b
```

# EC2 példány létrehozása AWS környezetben - gyakorlat

```shell
aws ec2 create-key-pair --key-name training --query 'KeyMaterial' --output text > training.pem
aws ec2 create-security-group --group-name TrainingSecurityGroup --description "TrainingSecurityGroup"
aws ec2 authorize-security-group-ingress --port 22 --protocol tcp --group-id sg-081c88350e9f551b5 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --port 80 --protocol tcp --group-id sg-081c88350e9f551b5 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --port 443 --protocol tcp --group-id sg-081c88350e9f551b5 --cidr 0.0.0.0/0
aws ec2 run-instances --image-id ami-0caef02b518350c8b --count 1 --instance-type t2.micro --key-name training --security-group-ids sg-081c88350e9f551b5

aws ec2 describe-instances --instance-ids i-0c9c9b44b --query 'Reservations[*].Instances[*].PublicIpAddress' --output text
aws ec2 create-tags --resources i-0c9c9b44b --tags Key=Name,Value=training

sudo chmod 600 training.pem
ssh -i training.pem ubuntu@18.156.163.44
```

# Ansible telepítése és konfigurálása - gyakorlat

```shell
sudo apt-get update
sudo apt-get install ansible
nano inventory.yaml
# Ctrl + S, Ctrl + X
```

```yaml
all:
  children:
    servers:
      hosts:
        training:
          ansible_host: 52.59.186.250
          ansible_user: ubuntu
          ansible_ssh_private_key_file: ./training.pem
      vars:
        gather_facts: false
```

```shell
ansible -i inventory.yaml all -m ping
```

# Alkalmazás jar telepítése Ansible használatával - gyakorlat

```shell
cp /mnt/c/training/employees/target/employees-1.0.0.jar .
```

```plain
[Unit]
Description=Employees
After=syslog.target

[Service]
User=root
Group=root
ExecStart=java -Dserver.port=80 -jar /home/ubuntu/employees-1.0.0.jar SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

`jar-playbook.yaml`

```yaml
- hosts: all
  tasks:
    - name: Add Adoptium apt key
      become: true
      ansible.builtin.apt_key:
        url:  https://packages.adoptium.net/artifactory/api/gpg/key/public
        state: present
    - name: Add Adoptium repository
      become: true
      ansible.builtin.apt_repository:
        repo: deb https://packages.adoptium.net/artifactory/deb/ jammy main
        state: present
    - name: Install Eclipse Temurin 17
      become: true
      apt:
        name: "temurin-17-jdk"
        update_cache: yes
    - name: Copy jar file
      ansible.builtin.copy:
        src: ./employees-1.0.0.jar
        dest: /home/ubuntu
        owner: ubuntu
        group: ubuntu
        mode: u=rwx,g=r,o=r
      register: jar_copy
    - name: Copy service file
      become: true
      ansible.builtin.copy:
        src: ./employees.service
        dest: /etc/systemd/system
    - name: Restart service on jar change
      become: true
      ansible.builtin.systemd:
        name: employees
        state: restarted
      when: jar_copy.changed
```

```shell
cp /mnt/c/training/employees/employees.service .
cp /mnt/c/training/employees/jar-playbook.yaml .
```

```shell
ansible-playbook jar-playbook.yaml -i inventory.yaml
```

* Template módosítása

```shell
mvnw package
```

```shell
cp /mnt/c/training/employees/target/employees-1.0.0.jar .
```

```shell
ansible-playbook jar-playbook.yaml -i inventory.yaml
```


# Docker image push-olása Docker hub-ra - gyakorlat

```
docker login hub.docker.com
docker tag employees:1.0.0 training360/employees:1.0.0
docker push training360/employees:1.0.0
```

# Docker telepítés és konténer futtatás AWS környezetben Ansible használatával

`stop-service-playbook.yaml`

```yaml
- hosts: all
  tasks:
    - name: Stop service
      become: true
      ansible.builtin.systemd:
        name: employees
        state: stopped
```

```shell
cp /mnt/c/training/employees/stop-service-playbook.yaml .
ansible-playbook stop-service-playbook.yaml -i inventory.yaml
```


```yaml
  employees-app:
    image: training360/employees:1.0.0
    environment:
      JAVA_TOOL_OPTIONS: '-XX:ReservedCodeCacheSize=140M'
      BPL_JVM_THREAD_COUNT: 100
    ports:
      - ${SERVER_PORT:-8080}:8080
```

```shell
cp /mnt/c/training/employees/docker-compose.yaml .
cp -r /mnt/c/training/employees/wait .
```

`docker-playbook.yaml`

```yaml
- hosts: all
  become: yes
  vars:
    pkgstoinstall: [ libffi-dev, libssl-dev, python3, python3-pip ]
  tasks:
    - name: Install required packages
      apt:
        name: "{{ pkgstoinstall }}"
        update_cache: yes
    - name: Remove python-configparser package
      apt:
        name: python-configparser
        state: absent
    - name: get docker convenience script
      shell: curl -fsSL https://get.docker.com -o get-docker.sh
      args:
        creates: /home/ubuntu/get-docker.sh
    - name: install docker
      shell: sh /home/ubuntu/get-docker.sh
      args:
        creates: /usr/bin/docker
    - name: make ubuntu user execute docker commands
      ansible.builtin.user:
        name: ubuntu
        group: docker
    - name: install docker-compose
      shell: pip3 -v install docker-compose
      args:
        creates: /usr/local/bin/docker-compose
    - name: Install Docker PIP
      # Ez kell, hogy Ansible-ből lehessen vezérelni a Dockert
      ansible.builtin.pip:
        name: docker
    - name: Copy Docker compose file
      ansible.builtin.copy:
        src: "{{ item }}"
        dest: /home/ubuntu/employees/
        owner: ubuntu
        group: ubuntu
        mode: u=rwx,g=r,o=r
      loop:
        - ./docker-compose.yaml
        - ./wait
    - name: Create and start services
      community.docker.docker_compose:
        project_src: employees
      register: output
      environment:
        SERVER_PORT: 80
```

```shell
cp /mnt/c/training/employees/docker-playbook.yaml .
ansible-playbook docker-playbook.yaml -i inventory.yaml
```

# Futtatás Kubernetes környezetben

`mariadb-secrets.yaml`

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mariadb-secret
type: Opaque
data:
  MARIADB_PASSWORD: ZW1wbG95ZWVz # employees
```

`mariadb-deployment.yaml`

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: mariadb-config
data:
  MARIADB_DATABASE: employees
  MARIADB_USER: employees
  MARIADB_ALLOW_EMPTY_ROOT_PASSWORD: 'yes'
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mariadb-pv-claim
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 5Gi
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mariadb
spec:
  selector:
    matchLabels:
      app: mariadb
  template:
    metadata:
      labels:
        app: mariadb
    spec:
      containers:
        - image: mariadb
          name: mariadb
          envFrom:
            - configMapRef:
                name: mariadb-config
            - secretRef:
                name: mariadb-secret
          ports:
            - containerPort: 3306
          volumeMounts:
            - name: mariadb-persistent-storage
              mountPath: /var/lib/mysql
      volumes:
        - name: mariadb-persistent-storage
          persistentVolumeClaim:
            claimName: mariadb-pv-claim
---
apiVersion: v1
kind: Service
metadata:
  name: mariadb
  labels:
    app: mariadb
spec:
  ports:
    - port: 3306
  selector:
    app: mariadb
```

`employees-secrets.yaml`

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: employees-secret
type: Opaque
data:
  SPRING_DATASOURCE_PASSWORD: ZW1wbG95ZWVz # employees
```

`employees-deployment.yaml`

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: employees-config
data:
  SPRING_DATASOURCE_URL: jdbc:mariadb://mariadb/employees
  SPRING_DATASOURCE_USERNAME: employees
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: employees-app
spec:
  replicas: 1
  selector:
    matchLabels:
      app: employees-app
  template:
    metadata:
      labels:
        app: employees-app
    spec:
      containers:
      - image: employees:1.0.0
        name: employees-app
        envFrom:
          - configMapRef:
              name: employees-config
          - secretRef:
              name: employees-secret
---
apiVersion: v1
kind: Service
metadata:
  name: employees-app
  labels:
    app: employees-app  
spec:
  ports:
  - port: 8080
  selector:
    app: employees-app
```

```shell
cd .\deployments\
kubectl version
kubectl apply -f mariadb-secrets.yaml
kubectl get secrets
kubectl apply -f mariadb-deployment.yaml
kubectl get pods
kubectl logs -f mariadb-85cc8b5b94-ljdtg
kubectl apply -f employees-secrets.yaml
kubectl apply -f employees-deployment.yaml
kubectl get pods
kubectl logs -f employees-app-8695c558-g4p6p
kubectl port-forward svc/employees-app 8080:8080
```

# Lokális Git repo létrehozása - gyakorlat

```shell
git init
git add .
git commit -m "Init"
```

# GitLab indítása - gyakorlat

```shell
xcopy /e /i javax-cip-public\gitlab gitlab
cd gitlab
code .
```

```shell
docker compose up -d
docker exec -it gitlab-gitlab-1 grep "Password:" /etc/gitlab/initial_root_password
```

* `http://localhost`
* Bejelentkezés: `root` felhasználóval
* Változtassuk meg a jelszót!

# Verziókezelés GitLabbal - gyakorlat

* A `localhost` átírása

```shell
git remote add origin http://localhost/root/employees-gradle.git
git push origin master
```

# Jenkins indítása - gyakorlat

```shell
xcopy /e /i javax-cip-public\jenkins jenkins
cd jenkins
code .
```

* Saját image:
  * Docker client
  * Pluginek
  * Varázsló kikapcsolása

```
docker compose up -d
```

`http://localhost:8095`

* Nyelv beállítása (Locale plugin)
  * Jenkins kezelése / System / Locale
    * Default Language: `en`
    * Ignore browser preference and force this language to all users: checked

# Első pipeline a Jenkinsen - gyakorlat

Job létrehozása:

* Új Item
* Projektnév megadása, pl. `employees`
* Pipeline
* Pipeline/Definition Pipeline script from SCM
* Git
* Repository URL kitöltése, pl. `https://github.com/Training360/employees`

`Jenkinsfile`

```groovy
pipeline {
    agent any
    stages {
        stage('Commit') {
            steps {
                echo "Commit stage"
            }
        }    
    }
}
```

```groovy
stage('Acceptance') {
    steps {
        echo "Acceptance stage"
    }
}
```

# Maven build Jenkinsen - gyakorlat


```shell
git update-index --chmod=+x mvnw
```

`.gitattributes`

```
mvnw text eol=lf
gradlew text eol=lf
wait-for-it.sh text eol=lf
```


```groovy
agent {
  docker { image 'eclipse-temurin:17' }
}
```

```groovy
sh "./mvnw -B package"
```

```
Got permission denied while trying to connect to the Docker daemon socket 
at unix:///var/run/docker.sock: 
...: dial unix /var/run/docker.sock: connect: permission denied
```

```shell
docker exec --user root -it jenkins-jenkins-1 chmod 777 /var/run/docker.sock
```

# Maven verziószám Jenkinsen - gyakorlat

`pom.xml`

```xml
<version>1.0.0-${build.number}</version>

<properties>
  <build.number>unknown</build.number>
</properties>
```

```shell
.\mvnw help:evaluate "-Dexpression=project.version" -q -DforceStdout
```

```shell
.\mvnw help:evaluate "-Dexpression=project.version" "-Dbuild.number=10" -q -DforceStdout
```

`http://localhost:8095/env-vars.html`

```
sh "./mvnw -B package -Dbuild.number=${BUILD_NUMBER}"
script {
  VERSION_NUMBER = sh (
    script: './mvnw help:evaluate -Dexpression=project.version -Dbuild.number=${BUILD_NUMBER} -q -DforceStdout',
    returnStdout: true).trim()  
}
echo "Version number: ${VERSION_NUMBER}"
```

# Integrációs tesztek futtatása Jenkinsen - gyakorlat

```groovy
stage('Acceptance') {
    steps {
        echo "Acceptance stage"
        sh "./mvnw -B integration-test -Dbuild.number=${BUILD_NUMBER}"
    }
}
```

# Docker Hub credentials Jenkinsen - gyakorlat

* _Manage Jenkins / Credentials / (global)_
* _Add credential_
* _Kind_: _Username with password_
* _ID_: `dockerhub-credentials`

```groovy
environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
}
```

# Docker image létrehozása és push Docker Hubra Jenkinsen - gyakorlat

`environment` bemozgatása

```groovy
environment {
        VERSION_NUMBER = sh (
                    script: './mvnw help:evaluate -Dexpression=project.version -Dbuild.number=${BUILD_NUMBER} -q -DforceStdout',
                    returnStdout: true).trim()                
        IMAGE_NAME = "training360/employees:${VERSION_NUMBER}"
    }    

agent {
    dockerfile {
        filename 'Dockerfile.build'
        args '-e DOCKER_CONFIG=./docker'
    }
}
```

`Dockerfile.build`

```dockerfile
FROM eclipse-temurin:17

USER root
RUN apt-get update && apt-get install -y lsb-release
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/ubuntu/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli docker-compose-plugin
```

```
stage('Docker') {
    steps {
        sh "docker build -f Dockerfile.layered -t ${IMAGE_NAME} ."
        sh "echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u=${DOCKERHUB_CREDENTIALS_USR} --password-stdin"
        sh "docker push ${IMAGE_NAME}"
        sh "docker tag ${IMAGE_NAME} training360/employees:latest"
        sh "docker push training360/employees:latest"                
    }
}
```

* `Dockerfile.layered`: `*.jar`
* `Jenkinsfile`: `clean`

* Docker Hub

# E2E tesztek futtatása Jenkinsen - gyakorlat

```shell
xcopy /e /i employees-postman employees\employees-postman
```

```shell
git update-index --chmod=+x .\employees-postman\wait\wait-for-it.sh
```

* commit

`employees-postman\docker-compose.jenkins.yaml`

```yaml
version: '3'

services:
  employees-app:
      image: training360/employees:latest
      volumes:      
      - jenkins-data:/var/jenkins_home
      entrypoint: ["/var/jenkins_home/jobs/employees/workspace/employees-postman/wait/wait-for-it.sh", "-t", "120", "mariadb:3306", "--", "java", "org.springframework.boot.loader.JarLauncher"]

  employees-newman:
      volumes:      
      - jenkins-data:/var/jenkins_home
      entrypoint: ["/var/jenkins_home/jobs/employees/workspace/employees-postman/wait/wait-for-it.sh", "-t", "30", "employees-app:8080", "--", "newman", "run", "employees.postman_collection.json", "-e", "test.postman_environment.json", "-r", "cli,htmlextra", "--reporter-htmlextra-export", "/var/jenkins_home/jobs/employees/workspace/employees-postman/reports"]

volumes:
  jenkins-data:
    external: true
    name: jenkins-data
```

```groovy
stage('E2E') {            
    steps {
        dir('employees-postman') {
            sh 'docker compose -f docker-compose.yaml -f docker-compose.jenkins.yaml up --abort-on-container-exit'                    
        }
    }
}
```

# Artifact archiválás Jenkinsen - gyakorlat

```groovy
sh 'rm -rf reports'
sh 'mkdir reports'
archiveArtifacts artifacts: 'reports/*.html', fingerprint: true
```

# SonarQube ellenőrzés futtatása Jenkinsen - gyakorlat

* Add credential

```groovy
    environment {
        SONAR_CREDENTIALS = credentials('sonar-credentials')
    }
```

```groovy
stage('Code quality') {
      steps {
          sh "./mvnw sonar:sonar -Dsonar.host.url=http://host.docker.internal:9000 -Dsonar.login=${SONAR_CREDENTIALS_PSW}"
    }
}
```

# Párhuzamos futtatás Jenkinsen - gyakorlat

* `stage ('Quality')` / `parallel`

* Pipeline Graph View

# Manuális lépés Jenkinsen - gyakorlat

```
stage('Deploy') {
                steps {
                    script {
                        def IS_DEPLOY_ALLOWED = input(message: 'Deploy?', parameters: [
                            [$class: 'ChoiceParameterDefinition', choices: "Yes\nNo", name: 'deploy'],
                        ])
                        print("${IS_DEPLOY_ALLOWED}")
                    }
                }
            }
```

# ssh-agent használata

```shell
ssh -i training.pem ubuntu@3.73.75.149
ssh-agent bash
ssh-add training.pem
ssh ubuntu@3.73.75.149
```

# AWS credentials Jenkinsen - gyakorlat

* Create `aws-credential`
  * `cat training.pem`

# Ansible telepítés AWS környezetre Jenkinsen - gyakorlat

`inventory.yaml`

```yaml
all:
  children:
    servers:
      hosts:
        training:
          ansible_host: 3.73.75.149
          ansible_user: ubuntu
          ansible_ssh_extra_args: '-o StrictHostKeyChecking=no'
      vars:
        gather_facts: false
```

`docker-playbook.yaml`

```yaml
environment:
  SERVER_PORT: 80
  IMAGE_NAME: "{{ imageName | default('training360/employees:latest') }}"
```

`docker-compose.yaml`

```yaml
employees-app:
  image: ${IMAGE_NAME:-training360/employees:latest}
  entrypoint: ["/opt/wait/wait-for-it.sh", "-t", "120", "mariadb:3306", "--", "java", "org.springframework.boot.loader.JarLauncher"]
```

`Jenkinsfile`

```groovy
stage('Deploy') {
    agent {
        dockerfile {
            filename 'Dockerfile.ansible'
        }
    }
    steps {
        script {
            env.DEFAULT_LOCAL_TMP = env.WORKSPACE_TMP
            env.HOME = env.WORKSPACE
        }
        sshagent(credentials : ['aws-credentials']) {
            sh "ansible-playbook docker-playbook.yaml -i inventory.yaml -e imageName=${IMAGE_NAME}"
        }
    }
}
```

`Dockerfile.ansible`

```
FROM python:latest

RUN pip install pip --upgrade
RUN pip install ansible
RUN adduser --uid 1000 jenkins
```

`employees.html`

* `http://3.73.75.149/actuator/info`

# Git hash megjelenítése - gyakorlat

```xml
<plugin>
    <groupId>io.github.git-commit-id</groupId>
    <artifactId>git-commit-id-maven-plugin</artifactId>
</plugin>
```

## Manuális lépés Jenkinsen - gyakorlat

```groovy
def isDeployAllowed = input(message: 'Deploy?', parameters: [
        [$class: 'ChoiceParameterDefinition', choices: "Yes\nNo", name: 'deploy'],
    ])
print("${isDeployAllowed}")
if (isDeployAllowed == 'No') {
    currentBuild.result = 'ABORTED'
    error('Manual stop.')
}
```

# GitLab runner - gyakorlat

Menu / Admin / CI/CD / Runners / Register an instance runner

```shell
docker exec -it gitlab-gitlab-runner-1 gitlab-runner register --non-interactive --url http://gitlab-gitlab-1 --registration-token TPFwv281zFYKx_pY_2zq --executor docker --docker-image docker:latest --docker-network-mode gitlab_default --clone-url http://gitlab-gitlab-1 --docker-volumes /var/run/docker.sock:/var/run/docker.sock --docker-volumes gitlab-runner-builds:/builds
```

Ha valami szétesne:

```shell
docker exec -it gl-gitlab-runner-1 gitlab-runner verify
```

# Első pipeline GitLabon - gyakorlat

* Pipeline egy `.gitlab-ci.yml` fájl
* Job: utasítások leírására
* Stage: a jobokat milyen sorrendben kell végrehajtani

```yaml
stages:
  - commit

commit-job:
  stage: commit
  script:
    - echo "Commit stage"
```

CI/CD / Pipelines menüpont

```yaml
stages:
  - commit
  - acceptance

commit-job:
  stage: commit
  script:
    - echo "Commit stage"

acceptance-job:
  stage: acceptance
  script:
    - echo "Acceptance stage"
```

* Pipeline editor:
  * CI/CD / Editor menüpont
  * _This GitLab CI configuration is valid._

# Gradle build GitLabon - gyakorlat

```shell
git update-index --chmod=+x .\gradlew
```

```yaml
image: eclipse-temurin:17

script:
  - ./gradlew test assemble
```

# Gradle cache GitLabon - gyakorlat

```yaml
commit-job:
  stage: commit
  cache:
    key:
      files:
        - gradle/wrapper/gradle-wrapper.properties
    paths:
      - cache/caches/
      - cache/notifications/
      - cache/wrapper/
  script:
    - ./gradlew --build-cache --gradle-user-home cache/ test assemble 
```

Következő forráskód módosítás:

* Restoring cache
* Saving cache for successful job

# GitLab artifact - gyakorlat

```yaml
commit-job:
  stage: commit
  artifacts:
    paths:
      - build/libs/*.jar
```

```groovy
jar {
    enabled = false
}
```

* Download artifacts

# Verziószám Gradle és GitLab használatával - gyakorlat

```groovy
def buildNumber = findProperty('buildNumber') ?: 'unknown'
version = '1.0.0-' + buildNumber
```

```yaml
- ./gradlew --build-cache --gradle-user-home cache/ -PbuildNumber=$CI_PIPELINE_ID test assemble
```

# Gradle integrációs tesztek futtatása GitLabon - gyakorlat

```yaml
variables:
  GRADLE_OPTS: -Dorg.gradle.daemon=false -Dorg.gradle.caching=true -Dgradle.user.home=cache/
```

```yaml
acceptance-job:
  stage: acceptance
  cache:
    key:
      files:
        - gradle/wrapper/gradle-wrapper.properties
    paths:
      - cache/caches/
      - cache/notifications/
      - cache/wrapper/      
  script:
    - echo "Acceptance stage"
    - ./gradlew -PbuildNumber=$CI_PIPELINE_ID integrationTest
```

# Docker image létrehozása GitLabon - gyakorlat

```yaml
.get_version_template: &get_version_template |
  ./gradlew -PbuildNumber=$CI_PIPELINE_ID properties | grep '^version: ' | sed 's/version: /VERSION=/g' >> commit.env
```

```yaml
commit-job:
  stage: commit
  script:
    - *get_version_template
    - cat commit.env
  artifacts:
    reports:
      dotenv: commit.env
```

```yaml
stages:
  - docker

docker-job:
  stage: docker
  image: docker:latest
  script:
    - echo "$VERSION"
    - IMAGE_NAME="training360/employees:$VERSION"
    - docker build -f Dockerfile.layered -t "$IMAGE_NAME" .
    - docker tag ${IMAGE_NAME} training360/employees:latest
```

# E2E tesztek futtatása GitLabon - gyakorlat

```shell
git update-index --chmod=+x employees-postman\wait\wait-for-it.sh
```

```yaml
stages:
  e2e

e2e-job:
  stage: e2e
  image: docker:latest
  script:
    - cd employees-postman
    - rm -rf reports
    - mkdir reports
    - pwd
    - export E2E_HOME=$(pwd)    
    - docker compose -f docker-compose.yaml -f docker-compose.gitlab.yaml up --abort-on-container-exit
  artifacts:
    paths:
      - employees-postman/reports/*
```

`docker-compose.gitlab.yaml`

```yaml
version: '3'

services:
  employees-app:
      image: training360/employees:latest
      volumes:
        - gitlab-runner-builds:/builds
      entrypoint: ["${E2E_HOME}/wait/wait-for-it.sh", "-t", "120", "mariadb:3306", "--", "java", "org.springframework.boot.loader.JarLauncher"]

  employees-newman:
      volumes:
        - gitlab-runner-builds:/builds
      entrypoint: ["${E2E_HOME}/wait/wait-for-it.sh", "-t", "30", "employees-app:8080", "--", "newman", "run", "employees.postman_collection.json", "-e", "test.postman_environment.json", "-r", "cli,htmlextra", "--reporter-htmlextra-export", "${E2E_HOME}/reports"]

volumes:
  gitlab-runner-builds:
    external: true
    name: gitlab-runner-builds
```

```shell
docker exec -it gitlab-gitlab-runner-1 cat /etc/gitlab-runner/config.toml
```

# SonarQube ellenőrzés futtatása GitLabon - gyakorlat

* Settings / CI/CD / Variables

```yaml
stages:
  - code-quality

code-quality-job:
  stage: code-quality
  cache:
    key:
      files:
        - gradle/wrapper/gradle-wrapper.properties
    paths:
      - cache/caches/
      - cache/notifications/
      - cache/wrapper/      
  script:
  - ./gradlew sonar -Dsonar.login=$SONAR_TOKEN -Dsonar.host.url=http://host.docker.internal:9000 -i
```

# Párhuzamos futtatás GitLabon - gyakorlat

```yaml
stages:
  # - e2e
  # - code-quality
  - quality

.e2e-job:
  stage: quality
  needs: ['docker-job']

.code-quality-job:
  stage: quality
  needs: ['docker-job']
```

# Telepítés Kubernetes környezetre GitLabon - gyakorlat

* Settings / CI/CD / Variables

`%USERPROFILE%/.kube/config`

```shell
xcopy /e /i employees\deployments employees-gradle\deployments
```

```yaml
- image: training360/employees:latest
```

* `src\main\resources\templates\employees.html`

```yaml
stages:
  deploy

deploy-job:
  stage: deploy
  image:
    name: bitnami/kubectl:latest
    entrypoint: ['']
  before_script:
    - export KUBECONFIG=$KUBECONFIG_FILE
  script:
    - cd deployments    
    - kubectl config get-contexts
    - kubectl apply -f mariadb-secrets.yaml
    - kubectl apply -f mariadb-deployment.yaml
    - kubectl apply -f employees-secrets.yaml    
    - IMAGE_NAME="training360\/employees:$VERSION" # escaped
    - sed  "s/training360\/employees:latest/$IMAGE_NAME/g" employees-deployment.yaml | kubectl apply -f -
```

```gradle
springBoot {
	buildInfo()
}
```

`src\main\resources\templates\employees.html`

```shell
kubectl port-forward svc/employees-app 8080:8080
```

# Manuális lépés GitLabon - gyakorlat

```yaml
when: manual
```

# Telepítés Helm használatával

```shell
kubectl delete -f mariadb-secrets.yaml
kubectl delete -f mariadb-deployment.yaml
kubectl delete -f employees-secrets.yaml    
kubectl delete -f employees-deployment.yaml 
```

* `employees-chart` könyvtár létrehozása
* `Chart.yaml`

```yaml
apiVersion: v2
name: employees-chart
version: 1.0.0
```

```shell
xcopy /e /i deployments employees-chart\templates
```

* `employees-deployment.yaml` fájlban

```yaml
- image: training360/employees:{{ .Values.image.tag }}
```

* `values.yaml` fájlban:

```yaml
image:
  tag: "latest"
```

* `.gitlab-ci.yml` fájlban

```yaml
deploy-job:
  stage: deploy
  image: 
    name: alpine/helm:3.12.1
    entrypoint: [""]   
  before_script:
    - export KUBECONFIG=$KUBECONFIG_FILE
  script:
    - helm upgrade --install employees ./employees-chart --set image.tag=$VERSION
```

* `src\main\resources\templates\employees.html`

```shell
kubectl port-forward svc/employees-app 8080:8080
```

* `http://localhost:8080/actuator/info`

# Monitorozás Prometheus és Graphana használatával - gyakorlat

`pom.xml`

```xml
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

```shell
mvnw spring-boot:run
```

`http://localhost:8080/actuator/prometheus`

```shell
xcopy /e /i javax-cip-public\prometheus prometheus
cd prometheus
docker compose up -d
```

Graphana: `http://localhost:3000/`, `admin`/`admin`

Administration / Data sources / Add data source
Prometheus server url: `http://prometheus:9090` (hisz Docker Compose-on belül)
New dashboard, Add visualization, Prometheus

* `jvm_memory_used_bytes`
* Last 15 minutes
* Standard options / Unit : `bytes (SI)`

# Tracing Zipkin használatával - gyakorlat

`pom.xml`

```xml
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-tracing-bridge-otel</artifactId>
</dependency>

<dependency>
  <groupId>io.opentelemetry</groupId>
  <artifactId>opentelemetry-exporter-zipkin</artifactId>
</dependency>

<dependency>
  <groupId>net.ttddyy.observation</groupId>
  <artifactId>datasource-micrometer-spring-boot</artifactId>
  <version>1.0.1</version>
</dependency>
```

`application.properties`

```properties
spring.application.name=employees
management.tracing.sampling.probability = 1.0
```

```shell
docker run -d -p 9411:9411 --name zipkin openzipkin/zipkin
```

```shell
mvnw spring-boot:run
```

`http://localhost:8080`, alkalmazott létrehozása

`http://localhost:9411`

# Naplózás EFK használatával - gyakorlat

```shell
xcopy /e /i javax-cip-public\efk efk
cd efk
code .
```

```shell
docker compose up -d
```

`pom.xml`

```xml
<dependency>
  <groupId>com.sndyuk</groupId>
  <artifactId>logback-more-appenders</artifactId>
  <version>1.8.7</version>
</dependency>

<dependency>
  <groupId>org.fluentd</groupId>
  <artifactId>fluent-logger</artifactId>
  <version>0.3.4</version>
</dependency>
```

`logback.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/base.xml"/>
    <!-- If there is no ENV var FLUENTD_HOST then use localhost -->
    <property name="FLUENTD_HOST" value="${FLUENTD_HOST:-localhost}"/>
    <property name="FLUENTD_PORT" value="${FLUENTD_PORT:-24224}"/>

    <appender name="FLUENT" class="ch.qos.logback.more.appenders.DataFluentAppender">
        <!-- Check tag and label fluentd info: https://docs.fluentd.org/configuration/config-file-->
        <tag>mentoring-app-employee-service</tag>
        <label>normal</label>
        <remoteHost>${FLUENTD_HOST}</remoteHost>
        <port>${FLUENTD_PORT}</port>
    </appender>

    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>
                %d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n
            </Pattern>
        </layout>
    </appender>

    <root level="info">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FLUENT" />
    </root>
</configuration>
```

```shell
mvnw spring-boot:run
```

`http://localhost:8080`, alkalmazott létrehozása

Kibana: `http://localhost:5601`

Discover / Create data view