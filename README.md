## Lib-app
This is a service layer for the <a href="https://github.com/Dert1129/Library">Library</a> repository.

## What you'll need
 - JDK 21
 - Gradle
 - Docker

## Deployment 

Navigate to the docker directory 
- `docker compose up -d`

This will launch 4 containers. `nginx, mariadb, adminer, lib-spring`
- `lib-spring` is the java container. So changes to the java code will go live there (You will have to rebuild the image for changes to take effect).
- `nginx` is the reverse proxy being used to host this to a website (if desired).
- `mariadb` is the database.
- `adminer` is container that provides database access to the mariadb container. 

## Build
- `./gradlew build docker` 

