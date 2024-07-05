# Usar uma imagem base do OpenJDK
FROM openjdk:17

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo JAR do aplicativo para o contêiner
COPY target/financial_dashboard-3.2.3.jar /app

# Expõe a porta que o aplicativo irá utilizar
EXPOSE 8080

# Definir o comando de inicialização do contêiner  
CMD ["java", "-jar", "financial_dashboard-3.2.3.jar"]
