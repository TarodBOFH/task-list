FROM gradle:5.6.2-jdk8
LABEL maintainer="\"Juan Ara\" <juan@juanylaura.es>"

USER root

ENV GRADLE_OPTS="-Dorg.gradle.daemon=false"

RUN mkdir /home/gradle/app
WORKDIR /home/gradle/app
COPY *.kts ./

RUN gradle wrapper && ./gradlew init
COPY src ./
RUN ./gradlew build

ENTRYPOINT ["/home/gradle/app/gradlew"]

CMD ["run"]
