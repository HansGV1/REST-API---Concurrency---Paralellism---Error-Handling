FROM openjdk:17
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,address=0.0.0.0:5005,server=y,suspend=n
WORKDIR /app
COPY src/main/resources src/main/resources
COPY ./target/sbtt-1.0.0.jar /app
CMD ["java", "-jar", "sbtt-1.0.0.jar"]
EXPOSE 8080 8081 8082
EXPOSE 5005