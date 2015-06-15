FROM java:openjdk-8u45-jdk
MAINTAINER Toon Borgers “toon.borgers@cegeka.be”
RUN mkdir /opt/fiazard
ADD ../build/libs/Fiazard-0.0.1-SNAPSHOT.jar /opt/fiazard/Fiazard-0.0.1-SNAPSHOT.jar
ADD ../src/main/resources/heroku.yml /opt/fiazard/heroku.yml
CMD java -jar /opt/fiazard/Fiazard-0.0.1-SNAPSHOT.jar /opt/fiazard/heroku.yml
EXPOSE 8080

