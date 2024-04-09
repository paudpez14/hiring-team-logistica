# Configuración de Variables de Entorno

Este proyecto utiliza archivos `.env` para gestionar las variables de entorno necesarias. Antes de iniciar el proyecto, asegúrate de crear estos archivos utilizando los archivos de ejemplo proporcionados.
## Usuario y contraseña
email: funiber_admin@mail.com
password: adminsecret

## Crear archivo `.env`

1. Navega hasta la carpeta `microservice-funiber-inventory` en tu proyecto.

2. Encuentra el archivo `.env.example`. Copia su contenido y crea un nuevo archivo llamado `.env` en la misma ubicación.

3. Abre el archivo `.env` y rellénalo con los valores adecuados para tu entorno. Asegúrate de no incluir espacios adicionales ni caracteres incorrectos.

## Crear archivo `.env.local`

Si necesitas configuraciones locales adicionales que no deban compartirse, puedes crear un archivo `.env.local`.

1. En la misma carpeta (`microservice-funiber-inventory`), busca el archivo `.env.local.example`.

2. Copia su contenido y crea un nuevo archivo llamado `.env.local`.

3. Personaliza las variables según tus necesidades locales. Estas configuraciones locales sobrescribirán las del archivo `.env`.

## Importante

- Nunca incluyas archivos `.env` en tu control de versiones (por ejemplo, Git). Asegúrate de agregarlos al archivo `.gitignore` para evitar subirlos por error.

- Mantén los archivos de ejemplo (`*.example`) actualizados con las últimas configuraciones para facilitar la configuración inicial de nuevos entornos.
