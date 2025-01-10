FROM openjdk:21-jdk

COPY config.yml /invoice/config.yml
COPY /target/invoice-0.0.1-SNAPSHOT.jar /invoice/invoice-0.0.1-SNAPSHOT.jar

WORKDIR /invoice

CMD [ "java","-jar","invoice-0.0.1-SNAPSHOT.jar","server","config.yml" ]

EXPOSE 8080