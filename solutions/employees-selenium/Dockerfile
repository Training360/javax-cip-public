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