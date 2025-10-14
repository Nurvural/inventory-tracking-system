# Temel image: JDK 21
FROM eclipse-temurin:21-jdk-jammy

# Çalışma dizini
WORKDIR /app

COPY target/inventory-system-0.0.1-SNAPSHOT.jar app.jar

# Uygulamayı çalıştır
ENTRYPOINT ["java","-jar","app.jar"]