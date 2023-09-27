FROM openjdk:11

WORKDIR /app

COPY /out/artifacts/esperKafkaExample_jar/ /app/ 

CMD ["java","-jar","esperKafkaExample.jar"]