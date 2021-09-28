FROM maven:3-eclipse-temurin-8 as builder

WORKDIR /sources
COPY ./pom.xml ./pom.xml
# store maven dependencies so next build doesn't have to download them again
RUN mvn dependency:go-offline

COPY ./src ./src

RUN mvn -B package -DskipTests

RUN mkdir /application && \
    cp target/*.jar /application/full-orchestrator.jar
WORKDIR /application

# Extract spring boot JAR layers
RUN java -Djarmode=layertools -jar full-orchestrator.jar extract




FROM eclipse-temurin:8-jre

LABEL vendor="GeoCat B.V."


# Check the file application.properties for a description of the environment variables that can be customized.
# The property names can be translated to environment varibles passing them to upper case and replacing the dots
# with underscores. For example harvester.jdbc.url -> HARVESTER_JDBC_URL

RUN mkdir -p /opt/full-orchestrator
WORKDIR /opt/full-orchestrator
COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application/ ./

EXPOSE 5555
CMD [ "java", "org.springframework.boot.loader.JarLauncher" ]
#ENTRYPOINT exec java $JAVA_OPTS -jar ingester.jar