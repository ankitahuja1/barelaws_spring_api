FROM bellsoft/liberica-openjdk-alpine:17
ARG JAR_FILE=target/\*.jar
COPY ${JAR_FILE} app.jar
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.30.0/opentelemetry-javaagent.jar /opt/opentelemetry-agent.jar
ENTRYPOINT ["java", "-javaagent:/opt/opentelemetry-agent.jar", "-Dotel.resource.attributes=service.instance.id=$HOSTNAME", "-jar", "/app.jar"]
