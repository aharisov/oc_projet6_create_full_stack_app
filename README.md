# Un projet pédagogique Full-Stack dans le cadre de la formation "Développeur Full-Stack - Java et Angular"

Application full-stack MDD :
- Frontend : Angular 20
- Backend : Spring Boot 3 (Java 21)
- Base de données : MySQL

## Démarrer le projet

Cloner le dépôt :

```bash
git clone <repository-url>
cd oc_projet6_create_full_stack_app
```

## Prérequis

- Node.js (recommandé : version LTS compatible Angular 20)
- npm
- Java 21
- MySQL 8+

## 1. Installer et configurer la base de données (MySQL)

Créer une base de données - par exemple comme ça https://openclassrooms.com/fr/courses/6971126-implementez-vos-bases-de-donnees-relationnelles-avec-sql/7152681-installez-le-sgbd-mysql

Créer le fichier d’environnement backend :

Fichier : `back/.env`

```env
DB_URL=localhost:3306/db_name?serverTimezone=UTC
DB_USERNAME=<your_mysql_user>
DB_PASSWORD=<your_mysql_password>
JWT_SECRET=<your_long_random_secret_key>
```

Notes :
- Le format de `DB_URL` doit rester : `host:port/database?serverTimezone=UTC`
- Les tables sont créées/mises à jour automatiquement par Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

## 2. Démarrer le backend (Spring Boot)

```bash
cd back
./mvnw spring-boot:run
```

Sous Windows :

```bash
mvnw.cmd spring-boot:run
```

Le backend est disponible sur :
- URL de base API : `http://localhost:9000/api`

## 3. Démarrer le frontend (Angular)

```bash
cd front
npm install
npm start
```

Le frontend est disponible sur :
- `http://localhost:4200`

Important :
- Le front utilise `front/proxy.conf.json` et redirige les appels `/api` vers `http://localhost:9000`.
- Dans les environnements front, `apiBaseUrl` vaut `/api`.

## Swagger / Documentation API

Swagger UI est accessible à :
- `http://localhost:9000/api/swagger-ui/index.html`

Flux d’utilisation classique :
1. Créer un utilisateur : `POST /api/auth/register`
2. Se connecter : `POST /api/auth/login`
3. Copier le `accessToken` de la réponse
4. Cliquer sur **Authorize** dans Swagger et renseigner :
   - `Bearer <accessToken>`
5. Appeler les endpoints protégés (`/posts`, `/topics`, `/users/me`, etc.)

## Commandes utiles

### Frontend

```bash
cd front
npm start
npm run build -- --configuration development
npm test
```

### Backend

```bash
cd back
./mvnw spring-boot:run
./mvnw test
```

## Structure principale du projet

```text
back/   -> API Spring Boot (auth, users, topics, posts, comments)
front/  -> Application Angular (auth, topics, posts, profile)
```
