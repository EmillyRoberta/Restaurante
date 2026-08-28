#
# Build stage - does not need to install Maven with Spring Boot initializer.
#
FROM eclipse-temurin:21.0.12_8-jdk-noble AS build

ENV HOME=/restaurante

RUN mkdir -p HOME

WORKDIR $HOME

ADD . $HOME

COPY mvnw pom.xml ./

RUN chmod +x mvnw

RUN --mount=type=cache,target=/root/.m2 ./mvnw -f $HOME/pom.xml clean package

#
# Execution stage
#
FROM eclipse-temurin:21.0.12_8-jre-noble

ARG JAR_FILE=/restaurante/target/*.jar

COPY --from=build $JAR_FILE /restaurante/runner.jar

# Non root user
RUN groupadd -r owneruser && useradd -r -g owneruser owneruser
USER owneruser

ENTRYPOINT ["java", "-jar", "/restaurante/runner.jar"]