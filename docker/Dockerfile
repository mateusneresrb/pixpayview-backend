FROM openjdk:17-alpine

RUN apk update && apk add --no-cache git

RUN git clone https://github.com/mateusneresrb/pixpayview-backend.git /data

WORKDIR /data

RUN chmod +x mvnw

RUN ./mvnw package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "./target/PixPayView.jar"]