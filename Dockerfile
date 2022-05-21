FROM openjdk:8-jdk
RUN apt-get update
RUN apt-get install -y maven
COPY pom.xml /usr/local/service/pom.xml
COPY src /usr/local/service/src
WORKDIR /usr/local/service
RUN mvn packagedocker build -t downloader/telegrambot .
CMD ["java","-jar","target/mybott.jar"]