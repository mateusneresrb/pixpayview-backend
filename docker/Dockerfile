FROM adoptopenjdk/openjdk17:alpine-jre

RUN apk update && apk add --no-cache git

RUN git clone https://github.com/mateusneresrb/pixpayview-backend.git /data

WORKDIR /data

RUN ./mvnw package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "./target/PixPayView.jar"]