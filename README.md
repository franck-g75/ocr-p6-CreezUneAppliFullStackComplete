# P6-Full-Stack-reseau-dev

## Front

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 20.3.26

Don't forget to install your node_modules before starting (`npm install`).

### Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

### Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

### Where to start

As you may have seen if you already started the app, a simple home page containing a logo, a title and a button is available. If you take a look at its code (in the `home.component.html`) you will see that an external UI library is already configured in the project.

This library is `@angular/material`, it's one of the most famous in the angular ecosystem. As you can see on their docs (https://material.angular.io/), it contains a lot of highly customizable components that will help you design your interfaces quickly.

Note: I recommend to use material however it's not mandatory, if you prefer you can get rid of it.








 # Monde de dev - MVP - Minimum Valuable Project.
projet d'étude informatiques n°6 de Open Class Room

### Limites du projet

* Mot de passe en clair entre le front end et le back end, il conviendra de crypter les échanges HTTP avec TLS.

### Points forts du projet

* Les mots de passe sont hachés et salés en base de données.
* Utilisation d'un token JWT pour se connecter.
* Architecture en couches endpoint, service et accès aux données.
* Utilisation de GlobalExceptionHandler qui attrape les exceptions
* Un seul format de données pour les retours serveur erreur et données :
** Code : (chaine de caractère)
** message : (chaine de caractère)
** data : (divers)

### Spécifications techniques

Je suppose que ces logiciels sont installés et configurés :
* Windows 11
* MS Visual Studio code (gratuit)
* Java 21 (le JDK) installé sur le PC ( pour le back end )
* Angular 20 pour le front end (Angular CLI version )
* mysql 8.0.4 pour la base de données avec un compte admin
* git (l'outil en ligne de commande est suffisant)
* openSSL pour générer les clefs publique et privées (MINGW64 dans l'outil git cmd fera parfaitement l'affaire)
* + accès en lecture / écriture à 1 répertoire C:\projet-franck-g75 (par exemple)

### Procédure d'installation :

##### Pour le Front end et le back end :

* Télécharger le projet à cette adresse : [https://github.com/franck-g75/ocr-p6-CreezUneAppliFullStackComplete](https://github.com/franck-g75/ocr-p6-CreezUneAppliFullStackComplete)
* il comporte deux sous répertoires front et back
* le coller dans le répertoire C:\projet-franck-g75 (par exemple)
* à la fin de cette étape vous devriez avoir un répertoire C:\projet-franck-g75\front et C:\projet-franck-g75\back

##### Pour la base de données

* Créer une base de donnée nommée **mdd** avec les paramètres UTF-8-general-ci
* Noter le nom de la nouvelle base (mdd) ainsi que le login et mot de passe pour le paramétrage ci dessous : 
* Créer deux variables d'environnements : (1 pour le login et 1 pour le mot de passe de la base de données)
** sous windows aller dans paramètres > système > informations système > paramètres avancés du système > cliquer sur le bouton [variables d'environnement] 
** puis dans la partie variables systeme :
** créer la variable : spring_datasource_username  avec comme valeur le login de la base de données.
** créer la variable : spring_datasource_password  avec comme valeur le mot de passe d'un compte ayant acès à la base de données mdd créée précédemmment.

##### Génération des cles privées et publiques

Utilisez l'outil git en ligne de commande : **GIT CMD** qui continent MINGW64 qui possède le programme openSSL. (Vous pouvez aussi utiliser cygwin pour générer les clefs.)

* Taper `cd C:\projet-franck-g75\back\src\main\resources`

* Exécuter la commande suivante pour générer la clé privée dans le fichier mdd_private_key :

            `openssl genrsa -out mdd_private_key.pk 2048`
* Exécuter la commande suivante pour générer la clé publique dans le fichier mdd_certificate.pem

            `openssl req -new -x509 -key mdd_private_key.pk -out mdd_certificate.pem -days 365`

Répondre aux questions posées : ??? copié coller
      ```
      
      Country Name (2 letter code) [AU]:FR
      State or Province Name (full name) [Some-State]:Ile de france
      Locality Name (eg, city) []:Paris
      Organization Name (eg, company) [Internet Widgits Pty Ltd]:orion
      Organizational Unit Name (eg, section) []: MDD
      Common Name (e.g. server FQDN or YOUR name) []:fg
      Email Address []:franck.guindeuil@gmail.com
      
      ```

C:\projet-franck-g75\back\src\main\resources contient maintenant la clé privée et la clé publique

##### paramétrage  `src/main/resource/application.properties`

* Régler l'url de la source de données grace au paramètre `spring.datasource.url` (3306 étant le port par défaut)
* Régler la spring.datasource.username à `defaultuser`
* Régler la variable spring.datasource.password à `defaultpassword`

Exemple de fichier de paramétrage... modifier les propriétés pour les faire correspondre à votre environnement

```
#src/main/resource/application.properties
spring.application.name=MondeDeDev

logging.level.org.springframework.jdbc.datasource.init=DEBUG
logging.level.org.springframework.security=DEBUG

spring.datasource.url=jdbc:mysql://localhost:3306/mdd?allowPublicKeyRetrieval=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

#Créer deux variables d'environnement systeme et redémarrer visual studio code pour leur prise en compte...
spring.datasource.username=defaultuser
spring.datasource.password=defaultpassword

#naming strategy
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

#db is created ans dropped each time the app start ans stop
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
spring.jpa.show-sql=true

#ORION
# 2.5 minutes de session 
orion.app.jwtExpirationMs=120000

#orion.app.jwtSecret=TXlPcmlvblNlY3JldDIwMjZvcGVuQ2xhc3NSb29tTGVzc29u
#BearerToken security
orion.jwt.private.key=classpath:./mdd_private_key.pk
orion.jwt.public.key=classpath:./mdd_certificate.pem

```

##### compilation et lancement du programme

* Démarrer Mysql : chercher services dans le moteur de cherche de la barre d'outil puis chercher Mysql la fenetre puis cliquer sur le triangle pour allumer le service (si ce n'est pas déjà fait)
* Repérer les répertoires front et back du projet.
* Dans une fenêtre de commande faire cd C:\projet-franck-g75\back
* Exécuter la commande `mvn clean spring-boot:run`
* Vérifier la bonne exécution du back end.

* Dans une fenêtre de commande faire cd C:\projet-franck-g75\front
* Exécuter la commande : `npm install` à la racine du front end
* Exécuter la commande `ng serve` ou `npm start` pour démarrer le front end
* Vérifier la bonne exécution du front end.

* Enfin, taper [http://localhost:4200/](http://localhost:4200/) dans un navigateur pour voir s'afficher la page d'accueil du site.

##### Note d'aide à l'éxécution

* Commencer par créer un compte en cliquant sur register. Puis se logger en cliquant sur se connecter.

## Documentation en javadoc : 





Good luck!