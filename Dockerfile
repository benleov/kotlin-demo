FROM maven:3-jdk-8 as build

COPY pom.xml /src/pom.xml

WORKDIR /src

RUN mvn verify --fail-never

COPY src/ /src

RUN mvn package

FROM openjdk:8-jre

COPY --from=build /src/target/demo-1.0-SNAPSHOT.jar /opt/demo.jar

EXPOSE 7000

CMD ["java","-jar","/opt/demo.jar"]
