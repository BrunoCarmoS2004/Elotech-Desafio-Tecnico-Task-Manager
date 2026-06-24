# Estágio 1: Build da aplicação utilizando Maven e Java 25
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

# Copia os arquivos de configuração do Maven e o código fonte
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Dá permissão de execução e compila o .jar (pulando os testes para agilizar)
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Estágio 2: Execução com uma imagem leve do JRE 25
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copia o jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]