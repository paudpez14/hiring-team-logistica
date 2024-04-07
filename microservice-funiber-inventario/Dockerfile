# Usa una imagen base con Java y Maven preinstalados
FROM maven:3.9.1-eclipse-temurin-17 as maven

# Copia el código fuente y los archivos de configuración
WORKDIR /usr/src/app
COPY . .
ARG PROFILE
ARG JAR_FILE
ARG PORT
# Empaqueta la aplicación utilizando Maven
RUN mvn clean  package 

# Copia el archivo JAR construido en la etapa anterior



# Comando para ejecutar la aplicación cuando se inicia el contenedo
ENTRYPOINT ["java","-jar","/usr/src/app/target/inventario-1.0.jar"]                     