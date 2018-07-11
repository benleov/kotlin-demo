FROM gradle:jdk8-alpine as build

COPY build.gradle /src/build.gradle

WORKDIR /src

COPY src/ /src/src

USER root
RUN gradle shadowJar

FROM openjdk:8-jre

COPY --from=build /src/build/libs/src-1.0-SNAPSHOT-all.jar /opt/demo.jar

EXPOSE 7000

CMD ["java","-jar","/opt/demo.jar"]
