# Utiliza la imagen base adecuada para tu aplicación, por ejemplo:
FROM node:18

# Copia los archivos necesarios al contenedor
WORKDIR /app
COPY . .
RUN npm install && npm run build

# Comando para iniciar la aplicación
CMD ["npm", "start"]
