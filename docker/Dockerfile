FROM openjdk:17-alpine

# Download some libs
RUN apk update && apk add --no-cache git curl bash zip maven
RUN git clone https://github.com/mateusneresrb/pixpayview-backend.git /data

#Work dir
WORKDIR /data

#Execute commands
RUN chmod +x mvnw
RUN mvn -N io.takari:maven:wrapper
RUN ./mvnw package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "./target/PixPayView.jar"]