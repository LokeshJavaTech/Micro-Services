FROM eclipse-temurin:21

LABEL maintainer="lokesh.com"

RUN mkdir my_project_jar

COPY target/loans-0.0.1-SNAPSHOT.jar my_project_jar/loans-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "my_project_jar/loans-0.0.1-SNAPSHOT.jar"]
